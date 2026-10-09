package com.healthcareapp.healthcareapp.security;

import com.healthcareapp.healthcareapp.models.Permission;
import com.healthcareapp.healthcareapp.models.Roles;
import com.healthcareapp.healthcareapp.models.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Roles role;

    private UserPrincipal(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public static UserPrincipal from(User user) {
        return new UserPrincipal(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities =
                new HashSet<>();

        // ROLE
        authorities.add(
                new SimpleGrantedAuthority(
                        "ROLE_" + role.getName()
                )
        );

        // PERMISSIONS
        for (Permission permission :
                role.getPermissions()) {

            authorities.add(
                    new SimpleGrantedAuthority(
                            permission.getName()
                    )
            );
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
