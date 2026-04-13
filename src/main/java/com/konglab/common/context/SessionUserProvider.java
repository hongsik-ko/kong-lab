package com.konglab.common.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 현재 로그인 사용자 정보를 제공하는 컴포넌트
 *
 * 지금은 로그인 기능이 없어서 하드코딩으로 사용자 정보를 반환한다.
 * 나중에 실제 로그인 기능이 붙으면 이 클래스 내부 구현만 바꾸면 된다.
 */
@Component // Spring bean injection
public class SessionUserProvider {
    /**
     * 현재 로그인 사용자 정보 반환
     */
    public SessionUserContext getCurrentUser() {
        Map<String, Object> userInfoMap = new HashMap<>();

        userInfoMap.put("role", "ADMIN");
        userInfoMap.put("department", "KONG_LAB");

        return SessionUserContext.builder()
                .loginUserId(1L)
                .userName("konglab-admin")
                .userInfoMap(userInfoMap)
                .build();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getLoginUserId();
    }
}
