package com.cocodan.triplan.common;

import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.jwt.JwtAuthenticationToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (jwtAuthenticationToken == null) {
            return Optional.empty();
        }

        JwtAuthentication authentication = (JwtAuthentication) jwtAuthenticationToken.getPrincipal();
        return Optional.of(authentication.getId());
    }
}
