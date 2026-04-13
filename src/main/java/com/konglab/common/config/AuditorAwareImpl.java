package com.konglab.common.config;
import java.util.Optional;

import com.konglab.common.context.SessionUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * 현재 로그인 사용자 ID를 반환하는 클래스
 *
 * 지금은 로그인 기능이 없기 때문에
 * 하드코딩으로 사용자 ID를 반환한다.
 *
 * 나중에 Spring Security 붙이면 여기만 수정하면 된다.
 *
 * 그외에 BasicEntity에 있는 시스템에서 자동으로 저장할 데이터들은 여기서 자동으로 주입한다.
 */
@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {
    public final SessionUserProvider sessionUserProvider;

    @Override
    public Optional<Long> getCurrentAuditor() {
        // TODO: 나중에 세션 / SecurityContext에서 가져오기
        return Optional.of(sessionUserProvider.getCurrentUserId()); // 임시 로그인 사용자 ID
    }
}
