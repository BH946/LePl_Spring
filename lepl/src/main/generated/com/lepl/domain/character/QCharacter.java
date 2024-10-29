package com.lepl.domain.character;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCharacter is a Querydsl query type for Character
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCharacter extends EntityPathBase<Character> {

    private static final long serialVersionUID = -72869306L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCharacter character = new QCharacter("character");

    public final ListPath<CharacterItem, QCharacterItem> characterItems = this.<CharacterItem, QCharacterItem>createList("characterItems", CharacterItem.class, QCharacterItem.class, PathInits.DIRECT2);

    public final QExp exp;

    public final ListPath<Follow, QFollow> follows = this.<Follow, QFollow>createList("follows", Follow.class, QFollow.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> money = createNumber("money", Long.class);

    public final ListPath<Notification, QNotification> notifications = this.<Notification, QNotification>createList("notifications", Notification.class, QNotification.class, PathInits.DIRECT2);

    public QCharacter(String variable) {
        this(Character.class, forVariable(variable), INITS);
    }

    public QCharacter(Path<? extends Character> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCharacter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCharacter(PathMetadata metadata, PathInits inits) {
        this(Character.class, metadata, inits);
    }

    public QCharacter(Class<? extends Character> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.exp = inits.isInitialized("exp") ? new QExp(forProperty("exp")) : null;
    }

}

