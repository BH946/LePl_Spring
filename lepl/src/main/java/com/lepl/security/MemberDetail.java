package com.lepl.security;

import com.lepl.domain.member.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class MemberDetail implements UserDetails {
  private Member member;

  public MemberDetail(Member member) {
    this.member = member;
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // 특별한 권한 시스템을 사용하지 않을경우 -> role
    return Collections.EMPTY_LIST;
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return member.getUid();
  }

  // 계정 만료여부 제공
  // 특별히 사용을 안할시 항상 true를 반환하도록 처리
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  // 계정 비활성화 여부 제공
  // 특별히 사용 안할시 항상 true를 반환하도록 처리
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  // 계정 인증 정보를 항상 저장할지에 대한 여부
  // true 처리할시 모든 인증정보를 만료시키지 않기에 주의해야한다.
  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }
  // 계정의 활성화 여부
  // 딱히 사용안할시 항상 true를 반환하도록 처리
  @Override
  public boolean isEnabled() {
    return true;
  }
}
