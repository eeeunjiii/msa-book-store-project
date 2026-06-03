package com.example.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    String key();

    long waitTime() default 5; // 락 획득 대기 시간 5초
    long leaseTime() default 10; // 락 점유 시간
}
