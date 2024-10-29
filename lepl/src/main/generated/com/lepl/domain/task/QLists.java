package com.lepl.domain.task;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLists is a Querydsl query type for Lists
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLists extends EntityPathBase<Lists> {

    private static final long serialVersionUID = -756405302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLists lists = new QLists("lists");

    public final NumberPath<Long> curTime = createNumber("curTime", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> listsDate = createDate("listsDate", java.time.LocalDate.class);

    public final com.lepl.domain.member.QMember member;

    public final ListPath<Task, QTask> tasks = this.<Task, QTask>createList("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    public final NumberPath<Long> timerAllUseTime = createNumber("timerAllUseTime", Long.class);

    public QLists(String variable) {
        this(Lists.class, forVariable(variable), INITS);
    }

    public QLists(Path<? extends Lists> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLists(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLists(PathMetadata metadata, PathInits inits) {
        this(Lists.class, metadata, inits);
    }

    public QLists(Class<? extends Lists> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.lepl.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

