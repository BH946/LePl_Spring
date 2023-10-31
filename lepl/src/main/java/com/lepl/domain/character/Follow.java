package com.lepl.domain.character;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Follow {
    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    private Long followerId; // from
    private Long followingId; // to

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id") // FK
    private Character character;

    /**
     * 생성 편의 메서드
     */
    public static Follow createFollow(Character character, Long followingId) {
        Follow follow = new Follow();
        follow.character = character;
        follow.followingId = followingId;
        follow.followerId = character.getId(); // 자기자신(from)
        return follow;
    }

    /**
     * setter
     */
    public void setCharacter(Character character) {
        this.character = character;
    }
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }
}