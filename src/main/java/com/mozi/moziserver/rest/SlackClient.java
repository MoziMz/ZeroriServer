package com.mozi.moziserver.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slackClient", url = "${slack.webhook.blocked-info}")
public interface SlackClient {
    @PostMapping
    void sendMessage(@RequestBody String payload);
}
