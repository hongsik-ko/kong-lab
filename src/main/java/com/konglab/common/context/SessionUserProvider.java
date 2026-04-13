package com.konglab.common.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 현재 로그인 사용자 정보 제공
 *
 * 지금은 하드코딩
 * 나중에 세션 / JWT로 교체 예정
 */
@Component
public class SessionUserProvider {
    public SessionUserContext getCurrentUser() {
        return SessionUserContext.builder()
                .loginUserId(1L)
                .userName("testUserz")
                .userInfoMap(new HashMap<>())
                .build();
    }
}
