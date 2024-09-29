package com.lepl.Service.character;

import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Follow;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional // 쓰기모드 -> 서비스코드에 트랜잭션 유무 반드시 확인
@Slf4j
class FollowServiceTest {

  @Autowired
  CharacterService characterService;
  @Autowired
  FollowService followService;
  @Autowired
  ExpService expService;
  static Long followId; // 전역
  static Long characterId; // 전역
  static final Long CHARACTER_EX_ID = 15L;
  static final String MESSAGE = "이미 팔로우 요청을 하셨습니다.";

  /**
   * join(중복검증 포함), findOne, findAll, remove, findAllWithFollowing, findAllWithFollower
   */
  @Order(1)
  @Test
  @Rollback(value = false)
  public void 팔로우_저장과조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);
    Character character = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>());
    expService.join(exp);
    characterService.join(character); // id 기록위함
    Follow follow = Follow.createFollow(character,
        CHARACTER_EX_ID); // followerId 는 자동으로 자기자신 등록, followingId 는 임의로 10L인 캐릭터로 설정

    // when
    followService.join(follow);
    Follow findFollow = followService.findOne(follow.getId());
    List<Follow> follows = followService.findAll();

    // then
    Assertions.assertEquals(follow.getId(), findFollow.getId());
    for (Follow f : follows) {
      Assertions.assertInstanceOf(Follow.class, f);
    }
    followId = follow.getId();
    characterId = character.getId();
  }

  @Order(2)
  @Test
  @Rollback(value = false)
  public void 팔로잉_팔로워_조회() throws Exception {
    // given
    Exp exp = Exp.createExp(0L, 0L, 1L);
    Character c = Character.createCharacter(exp, new ArrayList<>(), new ArrayList<>(),
        new ArrayList<>()); // characterId 인 캐릭터를 팔로우 하는 캐릭터를 임의로 생성 목적
    expService.join(exp);
    c = characterService.join(c);
    Follow follow = Follow.createFollow(c, characterId);
    followService.join(follow);

    List<Follow> followers;
    List<Follow> followings;

    // when
    followers = followService.findAllWithFollower(characterId);
    followings = followService.findAllWithFollowing(characterId);

    // then
    for (Follow f : followers) {
      // followers 크기 1이라 가정
      Assertions.assertEquals(f.getCharacter().getId(), c.getId());
      log.info("나를 팔로워 하는 캐릭터 id : {}", f.getCharacter().getId()); // c.getId() 이어야 정상
    }
    for (Follow f : followings) {
      // followings 크기 1이라 가정
      Assertions.assertEquals(f.getFollowingId(), CHARACTER_EX_ID);
      log.info("내가 팔로잉 하는 캐릭터 id : {}", f.getFollowingId()); // CHARACTER_EX_ID 이어야 정상
    }

  }

  @Order(4)
  @Test
  public void 팔로우_제거() throws Exception {
    // given
    Follow findFollow = followService.findOne(followId);

    // when
    followService.remove(findFollow); // 영속성에서 이미 제거
    findFollow = followService.findOne(followId); // null 일 수 밖에

    // then
    Assertions.assertNull(findFollow);
  }

  @Order(3)
  @Test
  public void 중복검증_예외() throws Exception {
    // given
    Follow findFollow = followService.findOne(followId);

    // when
    // then
    Throwable exception = Assertions.assertThrows(IllegalStateException.class, () -> {
      followService.join(findFollow); // 중복검증 예외 발생
    });
    Assertions.assertEquals(MESSAGE, exception.getMessage());
    log.info("exception.getMessage() : {}", exception.getMessage());
  }


}