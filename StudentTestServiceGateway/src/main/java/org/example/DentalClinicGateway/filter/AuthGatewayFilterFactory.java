package org.example.DentalClinicGateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

enum Role {
    ADMIN, TEACHER, STUDENT, OTHER_SERVICE, ANONYMOUS
}

@Component
public class AuthGatewayFilterFactory implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;


    public AuthGatewayFilterFactory(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            LinkedHashSet<URI> path0 = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
            String path = path0.stream().findAny().get().getPath();

            if (path.startsWith("/auth/")) {
                return chain.filter(exchange);
            }

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw  new RuntimeException("Missing auth token");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            System.out.println("filter");

            return webClientBuilder.build()
                    .get()
                    .uri("http://auth/roles")
                    .header("Authorization", authHeader)
                    .retrieve().bodyToMono(String.class)
                    .map(authResponse -> {
                        ObjectMapper mapper = new ObjectMapper();
                        //List<Role> roles = new ArrayList<>();
                        HashMap<String, Object> userData;
                        try {
                            userData = mapper.readValue(authResponse, new TypeReference<>(){});
                            //roles = mapper.readValue(authResponse, new TypeReference<List<Role>>() {});
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        exchange.getRequest()
                                .mutate()
                                //.header("roles", roles.toString());
                                .header("roles", userData.get("roles").toString())
                                .header("user_id", userData.get("id").toString());
                        return exchange;
                    }).flatMap(chain::filter);
    }

    public static class Config {
        // ...
    }
}