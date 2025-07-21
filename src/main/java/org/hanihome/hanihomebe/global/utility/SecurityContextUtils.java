package org.hanihome.hanihomebe.global.utility;

import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityContextUtils {

    public static Optional<Long> getHttpRequesterId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Optional.empty();
        }
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return Optional.of(userDetails.getUserId());
    }
}
