package com.management.accommodation.auth.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.management.accommodation.auth.entity.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),
    GIRL_WARDEN(
            Set.of(
                    GIRL_WARDEN_CREATE,
                    GIRL_WARDEN_READ,
                    GIRL_WARDEN_UPDATE,
                    GIRL_WARDEN_DELETE
            )
    ),
    BOY_WARDEN(
            Set.of(
                    BOY_WARDEN_CREATE,
                    BOY_WARDEN_READ,
                    BOY_WARDEN_UPDATE,
                    BOY_WARDEN_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                        .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }

}
