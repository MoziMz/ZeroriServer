package com.mozi.moziserver.adminService;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.mappedenum.ConfirmStateType;
import com.mozi.moziserver.repository.ConfirmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminConfirmService {

    private final ConfirmRepository confirmRepository;

    public Confirm getById(Long seq) {

        return confirmRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.CONFIRM_NOT_EXISTS::getResponseException);
    }

    public List<Confirm> getConfirmList(
            Long userSeq,
            List<ConfirmStateType> confirmSateList,
            Integer pageNumber,
            Integer pageSize
    ) {

        return confirmRepository.findAllByUserSeqAndState(userSeq, confirmSateList, pageNumber, pageSize);
    }

    public void updateConfirmState(Long seq, ConfirmStateType confirmSateType) {

        Confirm confirm = getById(seq);
        confirm.setState(confirmSateType);
        confirmRepository.save(confirm);
    }

    public void deleteConfirm(Long seq) {

        Confirm confirm = getById(seq);
        confirm.setState(ConfirmStateType.DELETED);
        confirmRepository.save(confirm);
    }
}
