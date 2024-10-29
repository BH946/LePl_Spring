package com.lepl.domain.character;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCharacterItem is a Querydsl query type for CharacterItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCharacterItem extends EntityPathBase<CharacterItem> {

    private static final long serialVersionUID = 1510504057L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCharacterItem characterItem = new QCharacterItem("characterItem");

    public final QCharacter character;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final BooleanPath wearingStatus = createBoolean("wearingStatus");

    public QCharacterItem(String variable) {
        this(CharacterItem.class, forVariable(variable), INITS);
    }

    public QCharacterItem(Path<? extends CharacterItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCharacterItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCharacterItem(PathMetadata metadata, PathInits inits) {
        this(CharacterItem.class, metadata, inits);
    }

    public QCharacterItem(Class<? extends CharacterItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.character = inits.isInitialized("character") ? new QCharacter(forProperty("character"), inits.get("character")) : null;
    }

}

