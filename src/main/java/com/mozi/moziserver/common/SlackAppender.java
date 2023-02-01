package com.mozi.moziserver.common;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private WebClient webClient;
    @Setter
    private String webhookUrl;
    @Setter
    private String channelName;
    @Setter
    private String emoji = ":ghost:";
    @Setter
    private String username = "slack-appender-bot";
    @Setter
    private final String errorMsgHeaderStr = ":fire: ERROR :fire:";
    @Setter
    private final String warnMsgHeaderStr = ":warning: WARN :warning:";

    @Override
    public void start() {
        final ObjectMapper objectMapper = new ObjectMapper();

        final ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                }).build();

        final HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10000L, TimeUnit.MILLISECONDS)));

        webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        String msg = "";

        switch (eventObject.getLevel().toString()) {
            case "ERROR":
                msg += errorMsgHeaderStr;
                break;
            case "WARN":
                msg += warnMsgHeaderStr;
                break;
            default:
                return;
        }

        if (eventObject.getThrowableProxy() != null) {

            msg += ("\n" + eventObject.getThrowableProxy().getClassName() + " : " + eventObject.getThrowableProxy().getMessage());

            final StackTraceElementProxy[] arr = eventObject.getThrowableProxy().getStackTraceElementProxyArray();
            if (arr != null) {
                msg += "\n" + Stream.of(Arrays.copyOfRange(arr, 0, Math.min(3, arr.length)))
                        .map(Objects::toString)
                        .collect(Collectors.joining("\n"));
            }
        } else {
            msg += "\n" + eventObject.getMessage();
        }

        sendSlack(msg);
    }

    private void sendSlack(final String msg) {
        final long startTime = System.currentTimeMillis();
        webClient
                .method(HttpMethod.POST)
                .uri(webhookUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("payload", "{\"channel\": \"" + channelName + "\", \"username\": \"" + username + "\", \"text\": \"" + msg + "\", \"icon_emoji\": \"" + emoji + "\"}"))
                .retrieve()
                .bodyToMono(String.class)
                .onErrorContinue(onErrorContinueFunc(startTime))
                .subscribe(result -> {
                });
    }

    private BiConsumer<Throwable, Object> onErrorContinueFunc(final long startTime) {
        return (t, o) -> {
//            long duration = System.currentTimeMillis() - startTime;
//            Integer statusCode = null;
//            String errorMsg = t.getMessage();
//            if (t instanceof WebClientResponseException) {
//                statusCode = ((WebClientResponseException) t).getRawStatusCode();
//                errorMsg = ((WebClientResponseException) t).getResponseBodyAsString();
//            }
//
//            try {
//                final StackTraceElement[] arr = t.getStackTrace();
//                String stackTraceMsg = "";
//                if (arr != null) {
//                    stackTraceMsg = "\n" + Stream.of(Arrays.copyOfRange(arr, 0, Math.min(3, arr.length)))
//                            .map(Objects::toString)
//                            .collect(Collectors.joining("\n"));
//                }
//                sendSlack(errorMsgHeaderStr
//                        + "\n" + t.getClass().getName() + " : " + errorMsg
//                        + "\n statusCode : " + statusCode + " duration : " + duration
//                        + stackTraceMsg);
//            } catch (Exception ignored) {
//            }
        };
    }
}
