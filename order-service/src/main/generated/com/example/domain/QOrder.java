package com.example.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -1000394283L;

    public static final QOrder order = new QOrder("order1");

    public final StringPath customer = createString("customer");

    public final EnumPath<com.example.constant.DeliveryStatus> deliveryStatus = createEnum("deliveryStatus", com.example.constant.DeliveryStatus.class);

    public final StringPath destination = createString("destination");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final EnumPath<com.example.constant.Payment> payment = createEnum("payment", com.example.constant.Payment.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

