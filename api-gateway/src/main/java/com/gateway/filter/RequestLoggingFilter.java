package com.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();
        log.info("Incoming Request {}:{}",
                method,
                path
                );
        return chain.filter(exchange)
                .doFinally(signalType -> {

                    long executionTime = System.currentTimeMillis()-startTime;

                    Integer code = exchange.getResponse().getStatusCode()!=null?
                            exchange.getResponse().getStatusCode().value():null;

                    log.info("Request Completed {} {} | Status {} | Time {}ms",
                            method,
                            path,
                            code,
                            executionTime
                            );
                });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
