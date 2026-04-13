package com.konglab.common.controller;

import com.konglab.common.context.SessionUserContext;
import com.konglab.common.context.SessionUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 임시 로그인 사용자 확인용 테스트 API
 */
@RestController // REST API 컨트롤러
@RequiredArgsConstructor // final 필드 생성자 주입
@RequestMapping("/api/test/")
public class SessionTestController {
    private final SessionUserProvider sessionUserProvider;

    /**
     * 현재 로그인 사용자 정보 조회
     */
    @GetMapping("session-user") // GET 요청 매핑
    public SessionUserContext getSessionUser() {
        return sessionUserProvider.getCurrentUser();
    }

}
 