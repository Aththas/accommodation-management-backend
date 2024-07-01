package com.management.accommodation.auth.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    BOY_WARDEN_CREATE("boy_warden:create"),
    BOY_WARDEN_READ("boy_warden:read"),
    BOY_WARDEN_UPDATE("boy_warden:update"),
    BOY_WARDEN_DELETE("boy_warden:delete"),

    GIRL_WARDEN_CREATE("girl_warden:create"),
    GIRL_WARDEN_READ("girl_warden:read"),
    GIRL_WARDEN_UPDATE("girl_warden:update"),
    GIRL_WARDEN_DELETE("girl_warden:delete");

    @Getter
    private final String permission;
}
