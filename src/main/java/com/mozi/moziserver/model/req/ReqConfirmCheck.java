package com.mozi.moziserver.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReqConfirmCheck {
    @JsonProperty("max_tokens")
    private int maxTokens;
    private String model;
    private List<Message> messages;

    @Builder
    @Getter
    public static class Message {
        String role;
        @JsonProperty("content")
        List<Content> contentList;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TextContent.class, name = "text"),
            @JsonSubTypes.Type(value = ImageContent.class, name = "image_url")
    })
    public static abstract class Content {
        String type;
    }

    @Builder
    @Getter
    public static class TextContent extends Content {
        String text;
    }

    @Builder
    @Getter
    public static class ImageContent extends Content {
        @JsonProperty("image_url")
        ImageUrl imageUrl;
    }

    @Builder
    @Getter
    public static class ImageUrl {
        String url;
    }

}
