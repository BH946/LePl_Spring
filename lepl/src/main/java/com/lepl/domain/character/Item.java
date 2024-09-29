package com.lepl.domain.character;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Item {

  @Id
  @GeneratedValue
  @Column(name = "item_id")
  private Long id;

  private String type;
  private String name;

  private int price;
  private int purchase_quantity;

  private LocalDateTime start_time;
  private LocalDateTime end_time;

  /*
   * 생성 편의 메서드
   * */
  public static Item createItem(String type, String name, int price, int purchase_quantity,
      LocalDateTime start_time, LocalDateTime end_time) {
    Item item = new Item();
    item.type = type;
    item.name = name;
    item.price = price;
    item.purchase_quantity = purchase_quantity;
    item.start_time = start_time;
    item.end_time = end_time;
    return item;
  }
}
