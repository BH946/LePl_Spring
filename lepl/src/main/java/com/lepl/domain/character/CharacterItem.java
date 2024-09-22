package com.lepl.domain.character;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CHARACTER_ITEM")
public class CharacterItem {

  @Id
  @GeneratedValue
  @Column(name = "character_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "character_id")
  private Character character;

  @NotNull
  private Long itemId; // 실제 아이템이 가지는 고유값(Null 불가)
  private Boolean wearingStatus; // 착용 유무 T/F

  /**
   * 생성 편의 메서드
   */
  public static CharacterItem createCharacterItem(Character character, Long itemId,
      Boolean wearingStatus) {
    CharacterItem characterItem = new CharacterItem();
    characterItem.character = character;
    characterItem.itemId = itemId;
    characterItem.wearingStatus = wearingStatus;
    return characterItem;
  }

  /**
   * 연관관계 편의 메서드
   */
  public void setCharacter(Character character) {
    this.character = character;
  }
}
