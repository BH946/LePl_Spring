package com.lepl.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final MemberDetailService memberDetailService;
//  private final PasswordEncoder passwordEncoder; // 원래 여기서 password 복화해 해서 비교!

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();
    MemberDetail entity = (MemberDetail) memberDetailService.loadUserByUsername(username);
    return new CustomAuthenticationToken(entity, null, entity.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(CustomAuthenticationToken.class);
  }
}
