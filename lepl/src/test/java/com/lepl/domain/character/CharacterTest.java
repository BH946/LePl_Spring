package com.lepl.domain.character;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class CharacterTest {
    @Autowired
    EntityManager em;
    
    @Test
    @Transactional
    public void 캐릭터_경험치_관련() throws Exception {
        // given
        Exp exp = new Exp();
        exp.setExpAll(0l);
        exp.setExpValue(0l);
        List<CharacterItem> characterItems = new ArrayList<>();
        List<Friend> friends = new ArrayList<>();

        for(int i=0; i<2; i++) {
            CharacterItem characterItem = new CharacterItem();
            characterItem.setItemId(1l);
            characterItem.setWearingStatus(true);
            characterItems.add(characterItem);

            Friend friend = new Friend();
            friend.setFriendNickname("김철수"+i);
            friends.add(friend);
        }

        // when
        exp.updateExp(15l); // 경험치 15
        Character character = Character.createCharacter(exp, characterItems,friends);
        em.persist(character); // id 확인

        // then
        log.info("character.getId() : {}",character.getId());
        log.info("character.getExp().getExpAll() : {}",character.getExp().getExpAll());
        log.info("character.getExp().getExpValue() : {}",character.getExp().getExpValue());
        log.info("character.getExp().getLevel() : {}",character.getExp().getLevel());
        log.info("character.getCharacterItems().get(0).getItemId() : {}",character.getCharacterItems().get(0).getItemId());
        log.info("character.getFriends().get(0).getFriendNickname() : {}",character.getFriends().get(0).getFriendNickname());
    }

}