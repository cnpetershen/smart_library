package com.library.smart.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheKey {

    BOOK_LIST("smart:book:list"),
    BOOK_DETAIL("smart:book:detail:%s"),
    USER_INFO("smart:user:info:%s"),
    HOME_STAT("smart:home:stat"),
    AI_TREND("smart:ai:trend:%s"),
    AI_HOT_BOOKS("smart:ai:hot:%s"),
    AI_USER_PORTRAIT("smart:ai:portrait"),
    AI_PERSONAL_RECOMMEND("smart:ai:recommend:%s"),
    AI_REPORT("smart:ai:report:%s");

    private final String template;

    public String format(Object... args) {
        return String.format(template, args);
    }
}