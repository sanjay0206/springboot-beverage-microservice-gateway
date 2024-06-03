package com.infybuzz.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class JwtAuthGatewayFilter implements GlobalFilter {
    Logger logger = LoggerFactory.getLogger(JwtAuthGatewayFilter.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RouteValidator validator;

    @Value("${token.validation.url}")
    String tokenValidationUrl;

    private boolean isInternalRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().containsKey("X-Internal-Request");
    }

    public String getAuthHeader(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return Optional
                .ofNullable(headers.getFirst(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new RuntimeException("Missing authorization header"));
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
        String token = Optional.ofNullable(authHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElseThrow(() -> new RuntimeException("Invalid authorization header format"));

        logger.info("Making REST call to validate JWT token: " + token);
        logger.info("Token valid: " +  restTemplate.getForObject(tokenValidationUrl + token, Boolean.class));

        // Continue with the filter chain
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() ->
                        logger.info("Post Filter Response Code: " + exchange.getResponse().getStatusCode())));
    }
}
