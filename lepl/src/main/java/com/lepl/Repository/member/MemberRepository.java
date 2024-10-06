package com.lepl.Repository.member;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Jpa + 기존 JPA 레포 를 혼합 사용 테스트 해보기 위해 추가했다.
 * 이 파일이 원래는 MemberRepository 이름을 가졌어야 하는 파일이다. -> 이 인터페이스를 서비스 계층에서 사용하는 것.
 * 테스트용으로만 만들었으므로 서비스, 컨트롤러 부분은 건드리지 않겠다. 테스트 코드(레포 계층)에서 실행을 체크할 것
 */
public interface SpringDataJpaTest extends JpaRepository, MemberRepositoryCustom {
  //CRUD 자동 제공
}
