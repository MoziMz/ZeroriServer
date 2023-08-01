package com.mozi.moziserver.adminService;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.ChallengeExplanation;
import com.mozi.moziserver.model.ChallengeExplanationContent;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeCreate;
import com.mozi.moziserver.model.adminReq.AdminReqChallengeUpdate;
import com.mozi.moziserver.model.adminReq.AdminReqCurrentTagList;
import com.mozi.moziserver.model.adminReq.AdminReqCurrentThemeList;
import com.mozi.moziserver.model.entity.*;
import com.mozi.moziserver.model.mappedenum.ChallengeStateType;
import com.mozi.moziserver.model.mappedenum.ChallengeTagType;
import com.mozi.moziserver.repository.*;
import com.mozi.moziserver.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminChallengeService {

    private final S3ImageService s3ImageService;

    private final ChallengeRepository challengeRepository;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final ChallengeThemeRepository challengeThemeRepository;
    private final ChallengeStatisticsRepository challengeStatisticsRepository;
    private final TagRepository tagRepository;
    private final ChallengeTagRepository challengeTagRepository;
    private final CurrentTagListRepository currentTagListRepository;
    private final CurrentThemeListRepository currentThemeListRepository;
    private final TopicRepository topicRepository;
    private final CurrentTopicListRepository currentTopicListRepository;
    private final ChallengeTopicRepository challengeTopicRepository;

    private final PlatformTransactionManager transactionManager;

    // -------------------- -------------------- Challenge -------------------- -------------------- //
    public Challenge getChallenge(Long seq) {

        Challenge challenge = challengeRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        ChallengeRecord challengeRecord = challengeRecordRepository.findByChallenge(challenge);
        challenge.setChallengeRecord(challengeRecord);

        return challenge;
    }

    private List<Challenge> getChallengeListBySeq(List<Long> challengeSeqList) {

        return challengeRepository.findAllBySeqIn(challengeSeqList);
    }

    public List<Challenge> getChallengeListByThemeAndTagAndName(Long themeSeq, ChallengeTagType challengeTagType, Long topicSeq, String keyword, Integer pageNumber, Integer pageSize) {

        if (themeSeq != null) {
            getChallengeTheme(themeSeq.intValue());
        }

        Long tagSeq = null;
        if (challengeTagType != null) {
            tagSeq = getTagByName(challengeTagType.getName()).getSeq();
        }


        return challengeRepository.findAllByThemeAndTagAndTopicAndName(themeSeq, tagSeq, topicSeq, keyword, pageNumber, pageSize);
    }

    @Transactional
    public void createChallenge(AdminReqChallengeCreate req) {

        final Challenge challenge = Challenge.builder()
                .name(req.getName())
                .description(req.getDescription())
                .recommendedCnt(req.getRecommendedCnt())
                .mainTag(req.getMainTag())
                .themeSeq(req.getThemeSeq())
                .point(req.getPoint())
                .state(ChallengeStateType.ACTIVE)
                .build();

        try {
            challengeRepository.save(challenge);

            final ChallengeRecord challengeRecord = ChallengeRecord.builder()
                    .challenge(challenge)
                    .build();

            challengeRecordRepository.save(challengeRecord);

        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }
    }

    public void createChallengeExplanation(Long challengeSeq, String title, List<String> contentList) {

        Challenge challenge = challengeRepository.getById(challengeSeq);

        createChallengeTag(challenge);

        List<ChallengeExplanationContent> challengeExplanationContentList = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++) {
            challengeExplanationContentList.add(
                    ChallengeExplanationContent.builder()
                            .turn(i + 1)
                            .content(contentList.get(i))
                            .build()
            );
        }

        ChallengeExplanation challengeExplanation = ChallengeExplanation.builder()
                .title(title)
                .contents(challengeExplanationContentList)
                .build();

        challenge.setExplanation(challengeExplanation);
        challengeRepository.save(challenge);
    }

    @Transactional
    public void updateChallenge(Long seq, AdminReqChallengeUpdate req, String title, List<String> contentList) {

        final Challenge challenge = getChallenge(seq);

        if (StringUtils.hasText(req.getName())) {
            challenge.setName(req.getName());
        }

        if (StringUtils.hasText(req.getDescription())) {
            challenge.setDescription(req.getDescription());
        }

        if (req.getRecommendedCnt() != null) {
            challenge.setRecommendedCnt(req.getRecommendedCnt());
        }

        if (req.getMainTag() != null) {
            updateChallengeTag(challenge, req.getMainTag());

            challenge.setMainTag(req.getMainTag());
        }

        if (req.getThemeSeq() != null) {
            challenge.setThemeSeq(req.getThemeSeq());
        }

        if (req.getPoint() != null) {
            challenge.setPoint(req.getPoint());
        }

        if (StringUtils.hasText(title) || (contentList != null && !contentList.isEmpty())) {
            ChallengeExplanation challengeExplanation = updateChallengeExplanation(challenge, title, contentList);
            challenge.setExplanation(challengeExplanation);

        }

        try {
            challengeRepository.save(challenge);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public ChallengeExplanation updateChallengeExplanation(Challenge challenge, String title, List<String> contentList) {

        List<ChallengeExplanationContent> challengeExplanationContentList = new ArrayList<>();
        if (contentList != null && !contentList.isEmpty()) {
            for (int i = 0; i < contentList.size(); i++) {
                challengeExplanationContentList.add(
                        ChallengeExplanationContent.builder()
                                .turn(i + 1)
                                .content(contentList.get(i))
                                .build()
                );
            }
        }

        ChallengeExplanation challengeExplanation = challenge.getExplanation();
        if (StringUtils.hasText(title)) {
            challengeExplanation.setTitle(title);
        }

        if (contentList != null && !contentList.isEmpty()) {
            challengeExplanation.setContents(challengeExplanationContentList);
        }

        return challengeExplanation;
    }

    public void deleteChallenge(Long seq) {

        Challenge challenge = getChallenge(seq);

        challenge.setState(ChallengeStateType.DELETED);

        challengeRepository.save(challenge);
    }

    // -------------------- -------------------- Tag -------------------- -------------------- //
    public Tag getTagBySeq(Long seq) {

        Tag tag = tagRepository.findById(seq).
                orElseThrow(ResponseError.NotFound.TAG_NOT_EXISTS::getResponseException);

        return tag;
    }

    public Tag getTagByName(String name) {

        Tag tag = tagRepository.findByName(name)
                .orElseThrow(ResponseError.NotFound.TAG_NOT_EXISTS::getResponseException);

        return tag;
    }

    public List<Tag> getTagList(String name) {

        if (StringUtils.hasText(name)) {
            return tagRepository.findAll();
        }
        return tagRepository.findAllByNameContaining(name);
    }

    public void createTag(String name) {
        Optional<Tag> optionalTag = tagRepository.findByName(name);

        if (optionalTag.isPresent()) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException("already created tag");
        }

        Tag tag = Tag.builder().name(name).build();

        try {
            tagRepository.save(tag);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public void updateTag(Long seq, String name) {

        final Tag tag = getTagBySeq(seq);

        tag.setName(name);

        tagRepository.save(tag);
    }

    public void deleteTag(Long seq) {

        Tag tag = getTagBySeq(seq);

        Optional<CurrentTagList> currentTagList = currentTagListRepository.findByTag(tag);

        withTransaction(() -> {
            try {

                if (currentTagList.isPresent()) {
                    deleteCurrentTagList(currentTagList.get().getSeq());
                }

                tagRepository.delete(tag);

            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
            }

        });

    }

    // -------------------- -------------------- ChallengeTheme -------------------- -------------------- //
    public List<ChallengeTheme> getChallengeThemeList(String name) {

        if (StringUtils.hasText(name)) {
            return challengeThemeRepository.findAll();
        }
        return challengeThemeRepository.findAllByNameContaining(name);
    }

    public void createChallengeTheme(String name, String color, String inactiveColor) {

        Optional<ChallengeTheme> optionalChallengeTheme = challengeThemeRepository.findByName(name);

        if (optionalChallengeTheme.isPresent()) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException("already created challengeTheme");
        }

        ChallengeTheme challengeTheme = ChallengeTheme.builder()
                .name(name)
                .color(color)
                .inactiveColor(inactiveColor)
                .build();

        withTransaction(() -> {
            try {
                challengeThemeRepository.save(challengeTheme);
            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
            }
        });
    }

    public void updateChallengeTheme(Integer seq, String name, String color, String inactiveColor) {

        if (StringUtils.hasText(name) && challengeThemeRepository.findByName(name).isPresent()) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException("already created challengeTheme");
        }

        final ChallengeTheme challengeTheme = getChallengeTheme(seq);

        if (StringUtils.hasText(name)) {
            challengeTheme.setName(name);
        }

        if (StringUtils.hasText(color)) {
            challengeTheme.setColor(color);
        }

        if (StringUtils.hasText(inactiveColor)) {
            challengeTheme.setInactiveColor(inactiveColor);
        }

        try {
            challengeThemeRepository.save(challengeTheme);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public void deleteChallengeTheme(Integer seq) {

        ChallengeTheme challengeTheme = getChallengeTheme(seq);

        Optional<CurrentThemeList> currentThemeList = currentThemeListRepository.findByChallengeTheme(challengeTheme);

        withTransaction(() -> {
            try {

                if (currentThemeList.isPresent()) {
                    deleteCurrentThemeList(currentThemeList.get().getSeq());
                }

                challengeThemeRepository.delete(challengeTheme);

            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
            }

        });

        challengeThemeRepository.delete(challengeTheme);
    }

    // -------------------- -------------------- CurrentTagList -------------------- -------------------- //
    public List<CurrentTagList> getAllCurrentTagList() {

        return currentTagListRepository.findAll();
    }

    public void createCurrentTagList(Integer turn, Long tagSeq) {

        final Tag tag = tagRepository.findById(tagSeq).
                orElseThrow(ResponseError.NotFound.TAG_NOT_EXISTS::getResponseException);

        if (currentTagListRepository.findByTag(tag).isPresent()) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }

        Integer lastTurn = getAllCurrentTagList().stream().max(Comparator.comparing(currentTagList -> currentTagList.getTurn())).get().getTurn();

        if (turn != lastTurn + 1) {
            throw ResponseError.BadRequest.INVALID_CURRENT_TAG_LIST.getResponseException("invalid turn of CurrentTagList");
        }

        CurrentTagList currentTagList = CurrentTagList.builder()
                .turn(turn)
                .tag(tag)
                .build();

        try {
            currentTagListRepository.save(currentTagList);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public void updateAllCurrentTagList(List<AdminReqCurrentTagList> reqList) {

        List<CurrentTagList> currentTagLists = new ArrayList<>();

        for (AdminReqCurrentTagList req : reqList) {
            Tag tag = getTagBySeq(req.getTagSeq());
            if (tag != null) {
                CurrentTagList currentTag = currentTagListRepository.findById(req.getSeq())
                        .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

                currentTag.setTurn(req.getTurn());
                currentTag.setTag(tag);

                currentTagLists.add(currentTag);
            }
        }

        boolean duplicated = currentTagLists.stream()
                .map(CurrentTagList::getTurn)
                .distinct()
                .count() != currentTagLists.size();

        if (duplicated == true) {
            throw ResponseError.BadRequest.INVALID_CURRENT_TAG_LIST.getResponseException();
        }

        Set<Integer> set = new HashSet<>();
        for (int i = 1; i <= currentTagLists.size(); i++) {
            set.add(i);
        }

        Long cnt = 0L;
        for (CurrentTagList c : currentTagLists) {
            if (set.contains(c.getTurn())) {
                cnt += 1;
            }
        }

        if (cnt != set.size()) {
            throw ResponseError.BadRequest.INVALID_CURRENT_TAG_LIST.getResponseException();
        }

        try {
            currentTagListRepository.saveAll(currentTagLists);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public void deleteCurrentTagList(Long seq) {

        final CurrentTagList currentTagList = currentTagListRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

        List<CurrentTagList> currentTagLists = getAllCurrentTagList();

        withTransaction(() -> {
            try {
                currentTagLists.removeIf(c -> c.equals(currentTagList));

                currentTagListRepository.delete(currentTagList);

                currentTagLists.forEach(c -> {
                    if (c.getTurn() > currentTagList.getTurn()) {
                        c.setTurn(c.getTurn() - 1);
                    }
                });

                currentTagListRepository.saveAll(currentTagLists);
            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
            }
        });
    }

    // -------------------- -------------------- CurrentThemeList -------------------- -------------------- //
    public List<CurrentThemeList> getAllCurrentThemeList() {

        return currentThemeListRepository.findAll();
    }

    public void createCurrentThemeList(Integer turn, Integer challengeThemeSeq) {

        final ChallengeTheme challengeTheme = challengeThemeRepository.findById(challengeThemeSeq).
                orElseThrow(ResponseError.NotFound.CHALLENGE_THEME_NOT_EXISTS::getResponseException);

        if (currentThemeListRepository.findByChallengeTheme(challengeTheme).isPresent()) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }

        Integer lastTurn = getAllCurrentThemeList().stream().max(Comparator.comparing(currentThemeList -> currentThemeList.getTurn())).get().getTurn();

        if (turn != lastTurn + 1) {
            throw ResponseError.BadRequest.INVALID_CURRENT_THEME_LIST.getResponseException("invalid turn of CurrentThemeList");
        }

        final CurrentThemeList currentThemeList = CurrentThemeList.builder()
                .turn(turn)
                .challengeTheme(challengeTheme)
                .build();

        try {
            currentThemeListRepository.save(currentThemeList);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public void updateAllCurrentThemeList(List<AdminReqCurrentThemeList> reqList) {
        List<CurrentThemeList> currentThemeLists = new ArrayList<>();

        for (AdminReqCurrentThemeList req : reqList) {
            ChallengeTheme challengeTheme = getChallengeTheme(req.getChallengeThemeSeq());
            if (challengeTheme != null) {
                CurrentThemeList currentTheme = currentThemeListRepository.findById(req.getSeq())
                        .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

                currentTheme.setTurn(req.getTurn());
                currentTheme.setChallengeTheme(challengeTheme);

                currentThemeLists.add(currentTheme);
            }
        }

        boolean duplicated = currentThemeLists.stream()
                .map(CurrentThemeList::getTurn)
                .distinct()
                .count() != currentThemeLists.size();

        if (duplicated == true) {
            throw ResponseError.BadRequest.INVALID_CURRENT_THEME_LIST.getResponseException();
        }

        Set<Integer> set = new HashSet<>();
        for (int i = 1; i <= currentThemeLists.size(); i++) {
            set.add(i);
        }

        Long cnt = 0L;
        for (CurrentThemeList c : currentThemeLists) {
            if (set.contains(c.getTurn())) {
                cnt += 1;
            }
        }

        if (cnt != set.size()) {
            throw ResponseError.BadRequest.INVALID_CURRENT_THEME_LIST.getResponseException();
        }

        try {
            currentThemeListRepository.saveAll(currentThemeLists);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }
    }

    public void deleteCurrentThemeList(Long seq) {

        final CurrentThemeList currentThemeList = currentThemeListRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

        List<CurrentThemeList> currentThemeLists = getAllCurrentThemeList();

        withTransaction(() -> {
            try {
                currentThemeLists.removeIf(c -> c.equals(currentThemeList));

                currentThemeListRepository.delete(currentThemeList);

                currentThemeLists.forEach(c -> {
                    if (c.getTurn() > currentThemeList.getTurn()) {
                        c.setTurn(c.getTurn() - 1);
                    }
                });

                currentThemeListRepository.saveAll(currentThemeLists);

            } catch (Exception e) {
                throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
            }
        });

    }

    // -------------------- -------------------- ChallengeTag -------------------- -------------------- //
    public void createChallengeTag(Challenge challenge) {

        Tag tag = getTagByName(challenge.getMainTag().getName());

        ChallengeTag challengeTag = ChallengeTag.builder()
                .tag(tag)
                .challenge(challenge)
                .turn(1)
                .build();

        challengeTagRepository.save(challengeTag);
    }

    public void updateChallengeTag(Challenge challenge, ChallengeTagType challengeTagType) {

        Tag beforeTag = getTagByName(challenge.getMainTag().getName());

        Tag newTag = getTagByName(challengeTagType.getName());

        ChallengeTag challengeTag = challengeTagRepository.findByChallengeAndTag(challenge, beforeTag)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TAG_NOT_EXISTS::getResponseException);

        challengeTag.setTag(newTag);

        challengeTagRepository.save(challengeTag);
    }

    // -------------------- -------------------- ChallengeTheme -------------------- -------------------- //
    public ChallengeTheme getChallengeTheme(Integer seq) {

        ChallengeTheme challengeTheme = challengeThemeRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_THEME_NOT_EXISTS::getResponseException);

        return challengeTheme;
    }

    // -------------------- -------------------- Challenge Topic -------------------- -------------------- //
    public List<Topic> getTopicList() {

        return topicRepository.findAll();
    }

    public List<Topic> getCurrentTopicList() {

        return topicRepository.findAllOrderByCurrentTopicTurn();
    }

    private List<ChallengeTopic> getChallengeTopicByChallenge(List<Challenge> challengeList) {

        return challengeTopicRepository.findByChallengeIn(challengeList);
    }

    @Transactional
    public void createTopicOnly(
            String title,
            String subTitle,
            MultipartFile image
    ) {

        createTopic(title, subTitle, image);
    }

    @Transactional
    public void createTopicAndCurrentTopic(
            String title,
            String subTitle,
            MultipartFile image
    ) {

        Topic topic = createTopic(title, subTitle, image);

        createCurrentTopic(topic);
    }

    private Topic createTopic(
            String title,
            String subTitle,
            MultipartFile image
    ) {

        String imgUrl;
        try {
            imgUrl = s3ImageService.uploadFile(image, "topic");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        Topic topic = Topic.builder()
                .title(title)
                .subTitle(subTitle)
                .imgUrl(imgUrl)
                .build();

        try {
            topicRepository.save(topic);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_TOPIC_TITLE.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        return topic;
    }

    private void createCurrentTopic(Topic topic) {

        int nextTurn = currentTopicListRepository.findAll().size() + 1;
        CurrentTopicList currentTopicList = CurrentTopicList.builder()
                .turn(nextTurn)
                .topic(topic)
                .build();
        currentTopicListRepository.save(currentTopicList);
    }

    @Transactional
    public void createChallengeTopicByTopic(Long topicSeq, List<Long> challengeSeqList) {

        Topic topic = topicRepository.findById(topicSeq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TOPIC_NOT_EXISTS::getResponseException);

        List<Challenge> existChallengeList = getChallengeListBySeq(challengeSeqList);
        if (challengeSeqList.size() != existChallengeList.size()) {
            String notFoundedChallengeSeqListString = challengeSeqList.stream()
                    .filter(seq -> existChallengeList.stream().noneMatch(challenge -> challenge.getSeq().equals(seq)))
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            throw ResponseError.NotFound.CHALLENGE_NOT_EXISTS.getResponseException(notFoundedChallengeSeqListString);
        }

        List<ChallengeTopic> challengeTopicList = existChallengeList.stream()
                .map(challenge -> ChallengeTopic.builder().topic(topic).challenge(challenge).build())
                .collect(Collectors.toList());

        try {
            challengeTopicRepository.saveAll(challengeTopicList);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_CHALLENGE_SEQ.getResponseException();

            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    @Transactional
    public void updateTopic(
            Long seq,
            String title,
            String subTitle,
            MultipartFile image
    ) {

        Topic topic = topicRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TOPIC_NOT_EXISTS::getResponseException);

        if (StringUtils.hasLength(title)) {
            topic.setTitle(title);
        }

        if (StringUtils.hasLength(subTitle)) {
            topic.setTitle(subTitle);
        }

        String imgUrl;
        if (image != null) {
            try {
                imgUrl = s3ImageService.uploadFile(image, "animalItem");
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            topic.setImgUrl(imgUrl);
        }

        try {
            topicRepository.save(topic);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_TOPIC_TITLE.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    @Transactional
    public void updateAllCurrentTopicList(List<Long> topicSeqList) {

        List<CurrentTopicList> currentTopicList = new ArrayList<>();
        for (int i = 0; i < topicSeqList.size(); i++) {
            Topic topic = topicRepository.findById(topicSeqList.get(i))
                    .orElseThrow(ResponseError.NotFound.CHALLENGE_TOPIC_NOT_EXISTS::getResponseException);

            CurrentTopicList currentTopic = CurrentTopicList.builder()
                    .turn(i + 1)
                    .topic(topic)
                    .build();
            currentTopicList.add(currentTopic);
        }

        currentTopicListRepository.deleteAllInBatch();
        currentTopicListRepository.saveAll(currentTopicList);
    }

    @Transactional
    public void deleteTopic(Long topicSeq) {

        Topic topic = topicRepository.findById(topicSeq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TOPIC_NOT_EXISTS::getResponseException);

        deleteCurrentTopicByTopic(topic);

        deleteChallengeTopicByTopic(topic);

        topicRepository.delete(topic);
    }

    @Transactional
    public void deleteChallengeTopicByTopicAndChallenge(Long topicSeq, Long challengeSeq) {

        Topic topic = topicRepository.findById(topicSeq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TOPIC_NOT_EXISTS::getResponseException);

        Challenge challenge = challengeRepository.findById(challengeSeq)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_NOT_EXISTS::getResponseException);

        ChallengeTopic challengeTopic = challengeTopicRepository.findByTopicAndChallenge(topic, challenge)
                .orElseThrow(ResponseError.NotFound.CHALLENGE_TOPIC_NOT_EXISTS::getResponseException);

        try {
            challengeTopicRepository.delete(challengeTopic);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_DELETED.getResponseException(); // for duplicate exception
        }
    }

    private void deleteCurrentTopicByTopic(Topic topic) {

        Optional<CurrentTopicList> optionalCurrentTopic = currentTopicListRepository.findByTopic(topic);

        if (optionalCurrentTopic.isEmpty()) {
            return;
        }

        CurrentTopicList currentTopic = optionalCurrentTopic.get();
        int turn = currentTopic.getTurn();

        currentTopicListRepository.delete(currentTopic);

        List<CurrentTopicList> currentTopicList = currentTopicListRepository.findByTurnGreaterThan(turn);
        for (CurrentTopicList nextTopic : currentTopicList) {
            nextTopic.setTurn(turn);
            turn++;
        }
    }

    private void deleteChallengeTopicByTopic(Topic topic) {

        challengeTopicRepository.deleteByTopic(topic);
    }

    // -------------------- -------------------- ChallengeStatistics -------------------- -------------------- //
    public List<ChallengeStatistics> getChallengeStatisticsListByPeriod(
            Challenge challenge,
            Integer startYear,
            Integer startMonth
    ) {

        LocalDate today = LocalDate.now();

        List<ChallengeStatistics> curChallengeStatisticsList = challengeStatisticsRepository.findAllByPeriod(
                challenge.getSeq(),
                startYear,
                startMonth,
                today.getYear(),
                today.getMonthValue()
        );

        if (curChallengeStatisticsList == null) {
            curChallengeStatisticsList = new ArrayList<>();
        }

        int size = curChallengeStatisticsList.size();
        //월이 없는 달은 통계를 0으로 채운다
        for (int i = 1; i <= today.getMonthValue(); i++) {
            boolean exists = false;
            for (int j = 0; j < size; j++) {
                if (curChallengeStatisticsList.get(j).getMonth() == i) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                curChallengeStatisticsList.add(ChallengeStatistics.builder()
                        .challenge(challenge)
                        .year(startYear)
                        .month(i)
                        .playerFirstTryingCnt(0)
                        .playerConfirmCnt(0)
                        .build());
            }
        }
        curChallengeStatisticsList = curChallengeStatisticsList.stream()
                .sorted(Comparator.comparing(ChallengeStatistics::getYear)
                        .thenComparing(ChallengeStatistics::getMonth))
                .collect(Collectors.toList());

        return curChallengeStatisticsList;
    }

    private void withTransaction(Runnable runnable) {

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}