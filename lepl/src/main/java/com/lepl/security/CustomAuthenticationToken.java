//package com.lepl.security;
//
//import java.util.Collection;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.SpringSecurityCoreVersion;
//import org.springframework.util.Assert;
//
//public class CustomAuthenticationToken extends AbstractAuthenticationToken {
//  private static final Long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
//  private final Object principal; //사용자 식별 정보(ex: username, id)
//  private Object credentials; //사용자 인증 정보(ex: password, token)
//
//  //인증 전 생성자
//  public CustomAuthenticationToken(Object principal, Object credentials) {
//    super(null); //인증전이라 role X
//    this.principal = principal;
//    this.credentials = credentials;
//    setAuthenticated(false); //인증되지 않은 상태로 설정 (오버라이딩한 자식 메소드임)
//  }
//  //인증 후 생성자
//  public CustomAuthenticationToken(Object principal, Object credentials,
//      Collection<? extends GrantedAuthority> authorities) {
//    super(authorities); //role 등록 -> 이 플젝은 role 미사용이긴 함!
//    this.principal = principal;
//    this.credentials = credentials;
//    super.setAuthenticated(true); //인증된 상태로 설정 (부모)
//  }
//
//  @Override
//  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//    //애초에 true 로 설정할 일이 없어서 조건문 추가 위해 Override
//    Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
//    super.setAuthenticated(false);
//  }
//  @Override
//  public void eraseCredentials() {
//    super.eraseCredentials(); //인증 정보 지우기
//    this.credentials = null;
//  }
//
//
//  @Override
//  public Object getCredentials() {
//    return this.credentials;
//  }
//
//  @Override
//  public Object getPrincipal() {
//    return this.principal;
//  }
//}
