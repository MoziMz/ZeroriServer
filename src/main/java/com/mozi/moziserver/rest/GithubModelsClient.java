package com.mozi.moziserver.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mozi.moziserver.config.GithubModelsConfig;
import com.mozi.moziserver.model.req.ReqConfirmCheck;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "githubModelsClient", url = "${github.models.url}",
        configuration = GithubModelsConfig.class)
public interface GithubModelsClient {
    @PostMapping("/inference/chat/completions")
    ResConfirmCheck verifyConfirmForChallengeName(
            @RequestBody ReqConfirmCheck reqChallgeCheck
    );

    @Getter
    @Setter
    class ResConfirmCheck {
        private List<Choice> choices;

        public boolean isAllContentN() {
            return choices != null && choices.stream().allMatch(Choice::isContentN);
        }
    }

    @Getter
    @Setter
    class Choice {
        @JsonProperty("finish_reason")
        private String finishReason;
        private int index;
        private Message message;

        public boolean isContentN() {
            return "N".equals(message.getContent());
        }
    }

    @Getter
    @Setter
    class Message {
        private String content;
        private String role;
    }
}
