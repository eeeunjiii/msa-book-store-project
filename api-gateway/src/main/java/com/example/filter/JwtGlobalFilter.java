package com.example.filter;

import com.example.redis.ReactiveRedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String secret;
    private Key key;
    private final ReactiveRedisUtil reactiveRedisUtil;

    @PostConstruct
    public void init() {
        byte[] keyBytes= Decoders.BASE64.decode(secret);
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request=exchange.getRequest();
        String path=request.getURI().getPath();

        if (path.equals("/api/v1/users/join") || path.equals("/api/v1/users/login")) {
            return chain.filter(exchange);
        }

        HttpCookie cookie=request.getCookies().getFirst("accessToken");

        if (cookie!=null) {
            String token=cookie.getValue();

            return validate(token).flatMap(isValid -> {
                if (isValid) {
                    Claims claims=parseClaims(token);
                    String email=claims.get("email", String.class);
                    String role=claims.get("role", String.class);

                    ServerHttpRequest modifiedRequest=exchange.getRequest().mutate()
                            .headers(header -> header.remove("X-User-Email"))
                            .headers(header -> header.remove("X-User-Role"))
                            .header("X-User-Email", email)
                            .header("X-User-Role", role)
                            .build();

                    log.info("Successfully authenticated user: {}", email);
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                }
                return chain.filter(exchange);
            });
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Boolean> validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return reactiveRedisUtil.hasKeyBlackList(token)
                    .map(isBlackListed -> !isBlackListed);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("JWT 토큰이 만료되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims가 비어 있습니다.", e);
        }
        return Mono.just(false);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
