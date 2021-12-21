package com.cocodan.triplan.common;

import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.jwt.JwtAuthenticationToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(getUserId());
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            return null;
        }

        return ((JwtAuthentication) principal).getId();
    }
}
