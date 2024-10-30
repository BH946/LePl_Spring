//package com.lepl.security;
//
//import com.lepl.Repository.member.v3.MemberRepository;
//import com.lepl.domain.member.Member;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class MemberDetailService implements UserDetailsService {
//  private final MemberRepository repository;
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    Member member = repository.findByUid(username);
//    if(member == null)
//      throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
//    return new MemberDetail(member);
//  }
//  /**
//   * 위에서 비밀번호는 사용안하냐고 물을수 있다.
//   * 보통 Database에 비밀번호를 암호화 해서 저장하므로 Database에서 해당 정보를 찾을때 ID값과 암호화된 비밀번호를
//   * 비교해가면서 찾는것보다 먼저 ID값으로 먼저 찾고 비밀번호를 복호화해서 비교하는게 더 빠르고 정확하다.
//   * 때문에 대부분 회원가입할때 ID 중복을 확인하는 이유가 ID값으로 찾았을때 여러개의 계정정보가 검색되면 어떤 계정으로 인증을 해야할지 알수없기 때문.
//   */
//}
