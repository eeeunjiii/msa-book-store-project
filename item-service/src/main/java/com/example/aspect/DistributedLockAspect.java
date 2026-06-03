package com.example.aspect;

import com.example.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser=new SpelExpressionParser();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey=parseKey(joinPoint, distributedLock.key());
        long waitTime= distributedLock.waitTime();
        long leaseTime= distributedLock.leaseTime();

        RLock rLock= redissonClient.getLock(lockKey);
        boolean lockAcquired=false;

        try {
            lockAcquired=rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (!lockAcquired) {
                log.warn("[RedissonLock] 락 획득 실패 - lockKey: {}", lockKey);
                throw new IllegalStateException("락 획득 실패 - lockKey: "+lockKey);
            }
            log.info("[RedissonLock] 락 획득 성공 - lockKey: {}", lockKey);
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (lockAcquired&&rLock.isHeldByCurrentThread()) {
                try {
                    rLock.unlock();
                    log.info("[RedissonLock] 락 해제 완료 - lockKey: {}", lockKey);
                } catch (IllegalStateException e) {
                    log.warn("[RedissonLock] 이미 해제된 락 또는 스레드 불일치 - lockKey: {}", lockKey, e);
                }
            }
        }
    }

    // SpEL 표현식을 기반으로 락 키 생성
    private String parseKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        EvaluationContext context=new StandardEvaluationContext();
        Object[] args= joinPoint.getArgs();

        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        String[] parameterNames= signature.getParameterNames();

        for (int i=0;i<parameterNames.length;i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }
}
