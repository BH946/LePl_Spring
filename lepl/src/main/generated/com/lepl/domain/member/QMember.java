package com.lepl.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1184753200L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.lepl.domain.character.QCharacter character;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.lepl.domain.task.Lists, com.lepl.domain.task.QLists> lists = this.<com.lepl.domain.task.Lists, com.lepl.domain.task.QLists>createList("lists", com.lepl.domain.task.Lists.class, com.lepl.domain.task.QLists.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final QProfile profile;

    public final StringPath uid = createString("uid");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.character = inits.isInitialized("character") ? new com.lepl.domain.character.QCharacter(forProperty("character"), inits.get("character")) : null;
        this.profile = inits.isInitialized("profile") ? new QProfile(forProperty("profile")) : null;
    }

}

