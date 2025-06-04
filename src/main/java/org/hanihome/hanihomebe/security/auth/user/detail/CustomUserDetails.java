package org.hanihome.hanihomebe.security.auth.user.detail;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//사용자 정보 관리하는 구현체 => SecurityContext에 등록됨
//지금은 간단히 userId, role을 담을 것임. 추후에 확정 예정
public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String role;

    public CustomUserDetails(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }


    //사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId); // 나중에 email 등으로 확장 가능
    }


    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() { return true; }

    //계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() { return true; }

    // 인증 정보 만료 엽
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    //계정 활성화 여부
    @Override
    public boolean isEnabled() { return true; }


    public String getRole() {
        return role;
    }


}
