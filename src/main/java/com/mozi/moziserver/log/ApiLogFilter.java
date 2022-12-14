package com.mozi.moziserver.log;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.common.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mozi.moziserver.common.Constant.PW_FIELD_NAME;
import static com.mozi.moziserver.common.Constant.CURRENT_PW_FIELD_NAME;

@Slf4j
@RequiredArgsConstructor
public class ApiLogFilter extends OncePerRequestFilter {
    private final static List<Pattern> reqParamPwPatterns = Arrays.asList(
            Pattern.compile("(?<=\\\"" + PW_FIELD_NAME + "\\\":\\\")[\\S]+(?=\\\"\\,)"),
            Pattern.compile("(?<=\\\"" + PW_FIELD_NAME + "\\\":\\\")[\\S]+(?=\\\"\\})"),
            Pattern.compile("(?<=\\\"" + CURRENT_PW_FIELD_NAME + "\\\":\\\")[\\S]+(?=\\\"\\,)"),
            Pattern.compile("(?<=\\\"" + CURRENT_PW_FIELD_NAME + "\\\":\\\")[\\S]+(?=\\\"\\})")
    );

    private final ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

    private final String activeProfiles;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        MDC.clear();

        final LocalDateTime startDatetime = ZonedDateTime.now().withZoneSameInstant(koreaZoneId).toLocalDateTime();
        ApiLog.ApiLogBuilder apiLogBuilder = ApiLog.builder()
                .profiles(activeProfiles)
                .time(startDatetime);

        final String threadId = UUID.randomUUID().toString();
        MDC.put(Constant.MDC_KEY_THREAD_ID, threadId);
        apiLogBuilder.threadId(threadId);

        Transaction transaction = ElasticApm.currentTransaction();
        final String traceId = transaction.getTraceId();
        if (StringUtils.hasLength(traceId)) {
            MDC.put(Constant.MDC_KEY_THREAD_ID, traceId);
        }

//         TODO 로그인 구현 후 손보기
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth != null && auth.isAuthenticated()) {
//            MDC.put(Constant.MDC_KEY_USER_SEQ, ((User)auth.getPrincipal()).getSeq().toString());
//        }

        ContentCachingRequestWrapper wrappedRequest = request instanceof ContentCachingRequestWrapper
                ? (ContentCachingRequestWrapper) request
                : new ContentCachingRequestWrapper(request);


        // TODO url을 /api부터 시작하는 것에 대해 논의해보기 (/api가 아닌 것과 구분이 가능하다.)
        boolean cantWrap = !request.getRequestURI().startsWith("/api");

        ContentCachingResponseWrapper wrappedResponse = response instanceof ContentCachingResponseWrapper
                ? (ContentCachingResponseWrapper) response
                : new ContentCachingResponseWrapper(response);

        try {
            if (cantWrap)
                filterChain.doFilter(request, response);
            else
                filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            final LocalDateTime endDatetime = ZonedDateTime.now().withZoneSameInstant(koreaZoneId).toLocalDateTime();
            final long durationMillis = ChronoUnit.MILLIS.between(startDatetime, endDatetime);
            apiLogBuilder.duration(durationMillis);

            try {
                wrappedResponse.copyBodyToResponse();
            } catch (IOException ignored) {
            }

            setRequestLog(wrappedRequest, apiLogBuilder);
            setResponseLog(wrappedResponse, apiLogBuilder);

            try {
                log.info(objectMapper.writeValueAsString(apiLogBuilder.build()));
            } catch (Exception e) {
            }
        }
    }

    private void setRequestLog(ContentCachingRequestWrapper wrappedRequest, ApiLog.ApiLogBuilder apiLogBuilder) {
        String userIp = wrappedRequest.getHeader("X-Forwarded-For");
        if (userIp == null)
            userIp = wrappedRequest.getRemoteAddr();

        apiLogBuilder
                .userIp(userIp)
                .userAgent(wrappedRequest.getHeader(HttpHeaders.USER_AGENT))
                .method(wrappedRequest.getMethod())
                .path(wrappedRequest.getRequestURI())
                .patternPath((String) wrappedRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));

        HttpMethod method = HttpMethod.valueOf(wrappedRequest.getMethod());

        if (method == HttpMethod.GET) {
            apiLogBuilder.reqParam(wrappedRequest.getQueryString());
        } else if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            String reqParam = getMessagePayload(wrappedRequest);
            reqParam = hidePw(reqParam);
            apiLogBuilder.reqParam(reqParam);
        }
    }

    private void setResponseLog(ContentCachingResponseWrapper wrappedResponse, ApiLog.ApiLogBuilder apiLogBuilder) {

        apiLogBuilder.statusCode(wrappedResponse.getStatus());

        if (HttpStatus.valueOf(wrappedResponse.getStatus()).isError()) {
            apiLogBuilder.resBody(getMessagePayload(wrappedResponse));
        }

        apiLogBuilder.userSeq(
                Optional.ofNullable(MDC.get(Constant.MDC_KEY_USER_SEQ))
                        .filter(v -> !StringUtils.hasLength(v))
                        .map(Long::parseLong)
                        .orElse(null));
    }

    private String getMessagePayload(ContentCachingRequestWrapper wrappedRequest) {
        byte[] buf = wrappedRequest.getContentAsByteArray();
        if (buf.length <= 0)
            return null;
        try {
            return tripJsonString(
                    new String(buf, 0, Math.min(buf.length, 1000), wrappedRequest.getCharacterEncoding())
            );
        } catch (UnsupportedEncodingException ex) {
            return "[unknown]";
        }
    }

    private String getMessagePayload(ContentCachingResponseWrapper wrappedResponse) {
        byte[] buf = wrappedResponse.getContentAsByteArray();
        if (buf.length <= 0)
            return null;
        try {
            return new String(buf, 0, Math.min(buf.length, 1000), wrappedResponse.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            return "[unknown]";
        }
    }

    private String tripJsonString(String jsonString) {
        StringBuilder sb = new StringBuilder(jsonString);

        boolean prevBackslash = false;
        boolean openDoubleQuote = false;
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            if (openDoubleQuote) {
                if (!prevBackslash && ch == '"')
                    openDoubleQuote = false;
            } else if (ch == '"') {
                openDoubleQuote = true;
            } else if (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r') {
                sb.deleteCharAt(i--);
            }

            prevBackslash = ch == '\\';
        }

        return sb.toString();
    }

    private String hidePw(String reqParam) {
        if (!StringUtils.hasLength(reqParam) || !reqParam.contains("\"" + PW_FIELD_NAME + "\"") || !reqParam.contains("\"" + CURRENT_PW_FIELD_NAME + "\"")) {
            return reqParam;
        }

        for (final Pattern reqParamPwPattern : reqParamPwPatterns) {
            Matcher matcher = reqParamPwPattern.matcher(reqParam);
            if (!matcher.find()) {
                continue;
            }
            String pw = matcher.group();
            StringBuilder starPw = new StringBuilder();
            for (int i = 0; i < pw.length(); i++) {
                starPw.append('*');
            }
            return reqParam.substring(0, matcher.start()) + starPw + reqParam.substring(matcher.end());
        }

        return reqParam;
    }

    @Builder
    @Getter
    static class ApiLog {
        private final String profiles;
        @Builder.Default
        private final String logType = "API";
        private final LocalDateTime time;
        private final String threadId;
        private final Long duration;
        private final String userIp;
        private final String userAgent;
        private final Long userSeq;
        private final String method;
        private final String path;
        private final String patternPath;
        private final String reqParam;
        private final Integer statusCode;
        private final String resBody;
    }
}