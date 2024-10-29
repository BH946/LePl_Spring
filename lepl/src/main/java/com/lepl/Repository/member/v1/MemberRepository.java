package com.lepl.Repository.member.v1;

import com.lepl.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * v1: 어댑터ver 두번쨰 예시 (비추) -> 레포지토리 단만 수정!!
 * Spring Data Jpa + 기존 JPA 레포 를 혼합 사용 테스트 해보기 위해 추가했다.
 * 이 인터페이스를 서비스 계층에서 사용하는 것.
 * 테스트용으로만 만들었으므로 서비스, 컨트롤러 부분은 건드리지 않겠다. 테스트 코드(레포 계층)에서 실행을 체크할 것
 */
//public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
//  //CRUD 자동 제공
//}
