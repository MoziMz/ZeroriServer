package com.mozi.moziserver.model.adminRes;

import com.mozi.moziserver.model.entity.Topic;
import lombok.Getter;

@Getter
public class AdminResCurrentTopicList {
    private final Integer turn;
    private final Topic topic;

    private AdminResCurrentTopicList(Integer turn, Topic topic) {
        this.turn = turn;
        this.topic = topic;
    }

    public static AdminResCurrentTopicList of(Integer turn, Topic topic) {
        return new AdminResCurrentTopicList(turn, topic);
    }
}