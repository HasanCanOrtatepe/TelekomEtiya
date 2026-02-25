package com.etiya.etiyatelekom.security.model;

import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import com.etiya.etiyatelekom.common.enums.UserType;
import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Customer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class AuthenticatedUser implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final UserType userType;
    private final Collection<? extends GrantedAuthority> authorities;

    private AuthenticatedUser(Long id, String email, String password, UserType userType,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.authorities = authorities;
    }

    public static AuthenticatedUser fromCustomer(Customer customer) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_CUSTOMER")
        );
        return new AuthenticatedUser(
                customer.getId(),
                customer.getEmail(),
                customer.getPassword(),
                UserType.CUSTOMER,
                authorities
        );
    }

    public static AuthenticatedUser fromJwt(Long id, String email, UserType userType,
                                           Collection<? extends GrantedAuthority> authorities) {
        return new AuthenticatedUser(id, email, null, userType, authorities);
    }

    public static AuthenticatedUser fromAgent(Agent agent) {
        AgentRoleEnums role = agent.getRole() != null ? agent.getRole() : AgentRoleEnums.AGENT;
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
        return new AuthenticatedUser(
                agent.getId(),
                agent.getEmail(),
                agent.getPassword(),
                UserType.AGENT,
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
