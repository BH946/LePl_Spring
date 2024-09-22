package com.lepl.api.character;

import static com.lepl.util.Messages.SESSION_NAME_LOGIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.FollowService;
import com.lepl.Service.character.NotificationService;
import com.lepl.Service.member.MemberService;
import com.lepl.domain.character.Character;
import com.lepl.domain.character.Exp;
import com.lepl.domain.character.Follow;
import com.lepl.domain.member.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = FollowApiController.class)
class FollowApiControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockBean // 가짜 객체
  FollowService followService;
  @MockBean
  MemberService memberService;
  @MockBean
  CharacterService characterService;
  @MockBean
  NotificationService notificationService;
  static MockHttpSession s; // 전역
  static final Long MEMBER_ID = 1L;
  static final Long MEMBER_ID2 = 2L;
  static final Long FOLLOW_ID = 1L;
  static final Long CHARACTER_ID = 1L;
  static final Long CHARACTER_ID2 = 2L;

  @BeforeEach
  public void 로그인_세션() {
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(SESSION_NAME_LOGIN, MEMBER_ID); // 회원 인증 인터셉터 통과 + @Login memberId
    s = session;
  }

  /**
   * create, delete, findFollowing, findFollower
   */

  @Test
  public void 팔로우_생성() throws Exception {
    // given
    Map<String, Long> map = new HashMap<>();
    map.put("followingId", MEMBER_ID2); //to: MEMBER_ID2
    ObjectMapper obj = new ObjectMapper();
    String content = obj.writeValueAsString(map);

//    Member member = Member.createMember("123456789", "팔로우 사용자");
    Member member = mock(Member.class);
    when(member.getId()).thenReturn(MEMBER_ID);
    when(member.getUid()).thenReturn("123456789");
    when(member.getNickname()).thenReturn("팔로우 사용자");
//    Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(),
//        new ArrayList<>(), new ArrayList<>());
//    Character findCharacter = Character.createCharacter(Exp.createExp(0L, 0L, 1L),
//        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    Character character = mock(Character.class);
    Character findCharacter = mock(Character.class);
    // when
    when(memberService.findOne(MEMBER_ID)).thenReturn(member);
    when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
    when(characterService.findOne(MEMBER_ID2)).thenReturn(
        findCharacter); //상대방 캐릭터에 알림 추가목적 (MEMBER_ID2)
    mockMvc.perform(
            post("/api/v1/follow/new")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content)
        )
        .andExpect(status().isCreated())
        .andDo(print());

    // then
    verify(memberService).findOne(MEMBER_ID);
    verify(characterService).findCharacterWithMember(MEMBER_ID);
    verify(characterService).findOne(MEMBER_ID2);
    verify(followService).join(any());
    verify(notificationService).join(any());
  }

  @Test
  public void 팔로우_삭제() throws Exception {
    // given
    Map<String, Long> map = new HashMap<>();
    map.put("followId", FOLLOW_ID);
    ObjectMapper obj = new ObjectMapper();
    String content = obj.writeValueAsString(map);
//    Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(),
//        new ArrayList<>(), new ArrayList<>());
//        character.setId(CHARACTER_ID);
//    Character character2 = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(),
//        new ArrayList<>(), new ArrayList<>());
//        character2.setId(CHARACTER_ID2);
//    Follow follow = Follow.createFollow(
//        Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(), new ArrayList<>(),
//            new ArrayList<>()), 1L);
//    follow.setId(FOLLOW_ID);
    Character character = mock(Character.class);
    Character character2 = mock(Character.class);
    Follow follow = mock(Follow.class);
    when(character.getId()).thenReturn(CHARACTER_ID);
    when(character2.getId()).thenReturn(CHARACTER_ID2);
    when(follow.getId()).thenReturn(FOLLOW_ID);
    //follow.setCharacter(character);
    when(follow.getCharacter()).thenReturn(character);

    // when
    when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
    when(followService.findOne(FOLLOW_ID)).thenReturn(follow);
    mockMvc.perform(
            post("/api/v1/follow/delete")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content)
        )
        .andExpect(status().isOk())
        .andDo(print());
    when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character2); //권한 없는 사용자
    mockMvc.perform(
            post("/api/v1/follow/delete")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content)
        )
        .andExpect(status().isUnauthorized())
        .andDo(print());

    // then
    verify(followService).remove(any());
  }

  @Test
  public void 팔로잉_조회() throws Exception {
    // given
//    Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(),
//        new ArrayList<>(), new ArrayList<>());
//        character.setId(1L);
    Character character = mock(Character.class);
    when(character.getId()).thenReturn(CHARACTER_ID);
//    Character.createCharacter(Exp.createExp(0L, 0L, 1L)
    Exp exp1 = Exp.createExp(0L, 0L, 1L);
    Exp exp2 = Exp.createExp(0L, 0L, 2L);
    Exp exp3 = Exp.createExp(0L, 0L, 3L);
    Character character1 = mock(Character.class);
    Character character2 = mock(Character.class);
    Character character3 = mock(Character.class);
    when(character1.getId()).thenReturn(10L);
    when(character2.getId()).thenReturn(11L);
    when(character3.getId()).thenReturn(12L);
    when(character1.getExp()).thenReturn(exp1);
    when(character2.getExp()).thenReturn(exp2);
    when(character3.getExp()).thenReturn(exp3);

    List<Follow> follows = new ArrayList<>();
    List<Follow> follows2 = new ArrayList<>();
    Follow f1 = Follow.createFollow(character1, CHARACTER_ID);
    Follow f2 = Follow.createFollow(character2, CHARACTER_ID);
    Follow f3 = Follow.createFollow(character3, CHARACTER_ID);
    follows.add(f1);
    follows.add(f2);
    follows.add(f3);

    // when
    when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
    when(followService.findAllWithFollowing(CHARACTER_ID)).thenReturn(follows);
    mockMvc.perform(
            get("/api/v1/follow/ing/all")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        .andExpect(status().isOk())
        .andDo(print());
    when(followService.findAllWithFollowing(CHARACTER_ID)).thenReturn(follows2); //빈 컨텐츠
    mockMvc.perform(
            get("/api/v1/follow/ing/all")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    // then
    verify(characterService, times(2)).findCharacterWithMember(MEMBER_ID);
    verify(followService, times(2)).findAllWithFollowing(CHARACTER_ID);
  }

  @Test
  public void 팔로워_조회() throws Exception {
    // given
//    Character character = Character.createCharacter(Exp.createExp(0L, 0L, 1L), new ArrayList<>(),
//        new ArrayList<>(), new ArrayList<>());
//        character.setId(1L);
    Character character = mock(Character.class);
    when(character.getId()).thenReturn(CHARACTER_ID);
//    Character.createCharacter(Exp.createExp(0L, 0L, 1L)
    Exp exp1 = Exp.createExp(0L, 0L, 1L);
    Exp exp2 = Exp.createExp(0L, 0L, 2L);
    Exp exp3 = Exp.createExp(0L, 0L, 3L);
    Character character1 = mock(Character.class);
    Character character2 = mock(Character.class);
    Character character3 = mock(Character.class);
    when(character1.getId()).thenReturn(10L);
    when(character2.getId()).thenReturn(11L);
    when(character3.getId()).thenReturn(12L);
    when(character1.getExp()).thenReturn(exp1);
    when(character2.getExp()).thenReturn(exp2);
    when(character3.getExp()).thenReturn(exp3);
    List<Follow> follows = new ArrayList<>();
    List<Follow> follows2 = new ArrayList<>();
    Follow f1 = Follow.createFollow(character1, CHARACTER_ID);
    Follow f2 = Follow.createFollow(character2, CHARACTER_ID);
    Follow f3 = Follow.createFollow(character3, CHARACTER_ID);
    follows.add(f1);
    follows.add(f2);
    follows.add(f3);

    // when
    when(characterService.findCharacterWithMember(MEMBER_ID)).thenReturn(character);
    when(followService.findAllWithFollower(CHARACTER_ID)).thenReturn(follows);
    mockMvc.perform(
            get("/api/v1/follow/er/all")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        .andExpect(status().isOk())
        .andDo(print());
    when(followService.findAllWithFollower(CHARACTER_ID)).thenReturn(follows2);
    mockMvc.perform(
            get("/api/v1/follow/er/all")
                .session(s)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        .andExpect(status().isNoContent())
        .andDo(print());

    // then
    verify(characterService, times(2)).findCharacterWithMember(MEMBER_ID);
    verify(followService, times(2)).findAllWithFollower(CHARACTER_ID);
  }

}