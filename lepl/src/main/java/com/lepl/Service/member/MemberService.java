package com.lepl.Service.member;

import com.lepl.Repository.member.MemberRepository;
import com.lepl.domain.character.Character;
import com.lepl.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기 모드로 기본으로 사용
@RequiredArgsConstructor // 생성자 주입 방식 사용
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional // 쓰기모드 필요해서 선언
    public Long join(Member member) {
        // 1. 중복 회원 검증(필수)
        validateDuplicateMember(member);
        // 2. 회원 저장
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByUid(member.getUid());
        if(findMember!=null){
            // IllegalStateException 예외를 호출
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 부분 회원 조회
     * id, uid 조회 둘다 구현
     */
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
    public Member findByUid(String uid) {
        return memberRepository.findByUid(uid);
    }

    @Cacheable(value = "members", key = "#pageId") // [캐시 없으면 저장] 조회
    public List<Member> findAllWithPage(int pageId) { return memberRepository.findAllWithPage(pageId); }
    // 캐시에 저장된 값 제거 (회원가입 로직에 추가. 업데이트 땐 일단 무시)
    @CacheEvict(value="members", allEntries = true)
    public void initCacheMembers(){}
}