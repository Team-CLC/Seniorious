package com.github.teamclc.seniorious.api.user;

import com.github.teamclc.seniorious.api.data.property.PropertyKey;
import com.github.teamclc.seniorious.api.data.property.dummy.DummyPropertyKey;
import com.github.teamclc.seniorious.api.group.GroupPrivileges;

import java.time.Instant;

/**
 * Properties modifier for an user.
 */
public interface UserProperties {
    /**
     * Chat score for user.
     */
    PropertyKey<Integer> SCORE = new DummyPropertyKey<>(Integer.class, "user:score");

    PropertyKey<Instant> JOIN_TIME = new DummyPropertyKey<>(Instant.class, "user:join_time");

    PropertyKey<GroupPrivileges> PRIVILEGE = new DummyPropertyKey<>(GroupPrivileges.class, "user:group_privilege");

    PropertyKey<String> NICK = new DummyPropertyKey<>(String.class, "user:nick");

    /**
     * We call it "称号" in Chinese.
     */
    PropertyKey<String> LEVEL_TITLE = new DummyPropertyKey<>(String.class, "user:level_title");
}
