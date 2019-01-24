package com.xiaohes.common.bean;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author by lei
 * @date 2019-1-24 13:32
 */
public class Role implements GrantedAuthority {
    Long id;
    String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
