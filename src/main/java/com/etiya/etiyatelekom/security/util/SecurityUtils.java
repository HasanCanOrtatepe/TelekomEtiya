package com.etiya.etiyatelekom.security.util;

import com.etiya.etiyatelekom.common.enums.UserType;
import com.etiya.etiyatelekom.security.model.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null || !(auth.getPrincipal() instanceof AuthenticatedUser)) {
            return null;
        }
        return (AuthenticatedUser) auth.getPrincipal();
    }

    public static Long getCurrentUserId() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static boolean isCustomer() {
        AuthenticatedUser user = getCurrentUser();
        return user != null && user.getUserType() == UserType.CUSTOMER;
    }
}
