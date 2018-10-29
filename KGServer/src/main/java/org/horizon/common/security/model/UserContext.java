package org.horizon.common.security.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author taos 20180301
 *
 */
public class UserContext {
    private final String username;
    private final List<GrantedAuthority> authorities;
    private final Long id;
    private final String userface;
    private final String name;

    private UserContext(String username, List<GrantedAuthority> authorities, Long id, String userface, String name) {
        this.username = username;
        this.authorities = authorities;
        this.id = id;
        this.userface = userface;
        this.name = name;
    }
    
    public static UserContext create(String username, List<GrantedAuthority> authorities ,Long id, String userface, String name) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, authorities, id, userface, name);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }
}
