package com.lepl.domain.task;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTaskStatus is a Querydsl query type for TaskStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTaskStatus extends EntityPathBase<TaskStatus> {

    private static final long serialVersionUID = 477170370L;

    public static final QTaskStatus taskStatus = new QTaskStatus("taskStatus");

    public final BooleanPath completedStatus = createBoolean("completedStatus");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath timerOnOff = createBoolean("timerOnOff");

    public QTaskStatus(String variable) {
        super(TaskStatus.class, forVariable(variable));
    }

    public QTaskStatus(Path<? extends TaskStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTaskStatus(PathMetadata metadata) {
        super(TaskStatus.class, metadata);
    }

}

