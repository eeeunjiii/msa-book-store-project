package com.example.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -32447572L;

    public static final QItem item = new QItem("item");

    public final StringPath author = createString("author");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> publish_year = createNumber("publish_year", Integer.class);

    public final StringPath publisher = createString("publisher");

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final StringPath title = createString("title");

    public QItem(String variable) {
        super(Item.class, forVariable(variable));
    }

    public QItem(Path<? extends Item> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItem(PathMetadata metadata) {
        super(Item.class, metadata);
    }

}

