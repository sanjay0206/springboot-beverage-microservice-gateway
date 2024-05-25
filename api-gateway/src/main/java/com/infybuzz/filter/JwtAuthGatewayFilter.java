package com.infybuzz.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthGatewayFilter implements GlobalFilter {
    Logger logger = LoggerFactory.getLogger(JwtAuthGatewayFilter.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RouteValidator validator;

    private static final String TOKEN_VALIDATION_URL = "http://localhost:9090/auth-service/api/auth/validate?token=";

    public String getAuthHeader(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new RuntimeException("Missing authorization header");
        }
        return headers.getFirst(HttpHeaders.AUTHORIZATION);
    }

    private boolean isInternalRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().containsKey("X-Internal-Request");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Inside JwtAuthenticationFilter");

        // Skip filter for internal requests
        if (isInternalRequest(exchange)) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();
        logger.info("Validating request for path: " + path);

        if (!validator.isSecured.test(exchange.getRequest())) {
            // Skip authentication for open API endpoints
            return chain.filter(exchange);
        }

        String authHeader = getAuthHeader(exchange);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        } else {
            throw new RuntimeException("Invalid authorization header format");
        }

        logger.info("Making REST call to validate JWT: " + authHeader);
        String tokenValidationEndpoint = TOKEN_VALIDATION_URL + authHeader;
        logger.info("Token valid: " +  restTemplate.getForObject(tokenValidationEndpoint, Boolean.class));

        // Continue with the filter chain
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() ->
                        logger.info("Post Filter Response Code: " + exchange.getResponse().getStatusCode())));
    }
}
