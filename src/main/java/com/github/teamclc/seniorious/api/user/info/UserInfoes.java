package com.github.teamclc.seniorious.api.user.info;

import com.github.teamclc.seniorious.api.data.property.PropertyKey;
import com.github.teamclc.seniorious.api.data.property.dummy.DummyPropertyKey;
import com.github.teamclc.seniorious.api.user.Genders;
import com.github.teamclc.seniorious.api.user.UserStatus;

import java.time.Instant;

public interface UserInfoes {
    PropertyKey<String> ADDRESS = new DummyPropertyKey<>(String.class, "user_info:address");
    PropertyKey<Byte> AGE = new DummyPropertyKey<>(Byte.class, "user_info:age");
    PropertyKey<String> AREA = new DummyPropertyKey<>(String.class, "user_info:area");
    PropertyKey<Instant> BIRTHDAY = new DummyPropertyKey<>(Instant.class, "user_info:birthday");
    PropertyKey<String> EMAIL = new DummyPropertyKey<>(String.class, "user_info:email");
    PropertyKey<String> ENGLISH_NAME = new DummyPropertyKey<>(String.class, "user_info:english_name");
    PropertyKey<Genders> GENDER = new DummyPropertyKey<>(Genders.class, "user_info:gender");
    PropertyKey<Short> HEADER = new DummyPropertyKey<>(Short.class, "user_info:header");
    PropertyKey<Short> LEVEL = new DummyPropertyKey<>(Short.class, "user_info:level");
    PropertyKey<Byte> QQ_AGE = new DummyPropertyKey<>(Byte.class, "user_info:qq_age");
    PropertyKey<UserStatus> STATUS = new DummyPropertyKey<>(UserStatus.class, "user_info:status");
    PropertyKey<String> HOMETOWN = new DummyPropertyKey<>(String.class, "user_info:hometown");
    PropertyKey<String> HOME_PAGE = new DummyPropertyKey<>(String.class, "user_info:home_page");
    PropertyKey<String> BIO = new DummyPropertyKey<>(String.class, "user_info:bio");
    PropertyKey<String> MOBILE = new DummyPropertyKey<>(String.class, "user_info:mobile");
    PropertyKey<String> NAME = new DummyPropertyKey<>(String.class, "user_info:name");
    PropertyKey<String> NICK = new DummyPropertyKey<>(String.class, "user_info:nick");
    PropertyKey<String> REMARK = new DummyPropertyKey<>(String.class, "user_info:remark");
    PropertyKey<String> SCHOOL = new DummyPropertyKey<>(String.class, "user_info:school");

    /**
     * In Chinese we call it "生肖"
     */
    PropertyKey<String> SHENG_XIAO = new DummyPropertyKey<>(String.class, "user_info:sheng_xiao");

    PropertyKey<String> WORK = new DummyPropertyKey<>(String.class, "user_info:work");
    PropertyKey<String> CONSTELLATION = new DummyPropertyKey<>(String.class, "user_info:constellation");
    PropertyKey<String> ZIP_CODE = new DummyPropertyKey<>(String.class, "user_info:zip_code");

    PropertyKey<Tag> TAG = new DummyPropertyKey<>(Tag.class, "user_info:tag"); // FIXME
    PropertyKey<Signature> SIGNATURE = new DummyPropertyKey<>(Signature.class, "user_info:signature");
}
