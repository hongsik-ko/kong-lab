package com.konglab.common.context;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
/**
 * 현재 로그인 사용자 정보 (런타임 전용)
 *
 * DB 저장 X
 * 서비스 레이어에서 사용
 */
@Builder
@Getter
public class SessionUserContext {

    // 현재 로그인한 사용자 ID
    private Long loginUserId;

    // 현재 로그인한 사용자 이름
    private String userName;

    /**
     * 확장용 유저 정보
     *  - 화면단과 합의한 캐시 유저 정보
     * (권한, 조직, 캐시 데이터 등)
     */
    private Map<String, Object> userInfoMap;
}
