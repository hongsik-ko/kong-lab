package com.konglab.common.config;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * 현재 로그인 사용자 ID를 반환하는 클래스
 *
 * 지금은 로그인 기능이 없기 때문에
 * 하드코딩으로 사용자 ID를 반환한다.
 *
 * 나중에 Spring Security 붙이면 여기만 수정하면 된다.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    public final Long defaultId = 1L;
    @Override
    public Optional<Long> getCurrentAuditor() {
        // TODO: 나중에 세션 / SecurityContext에서 가져오기
        return Optional.of(defaultId); // 임시 로그인 사용자 ID
    }
}
