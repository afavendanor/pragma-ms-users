package co.com.pragma.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static co.com.pragma.api.security.config.TokenJwtConfig.*;

public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);

        setAuthenticationSuccessHandler(this::onAuthenticationSuccess);

        setAuthenticationFailureHandler((exchange, ex) -> {
            exchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getExchange().getResponse().setComplete();
        });
    }

    private Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        var response = webFilterExchange.getExchange().getResponse();

        try {
            String username = authentication.getName();

            String token = Jwts.builder()
                    .subject(username)
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plusSeconds(3600)))
                    .claim("authorities", authentication.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList())
                    .signWith(SECRET_KEY)
                    .compact();

            Map<String, String> body = Map.of(
                    "token", PREFIX_TOKEN + token,
                    "username", username
            );

            byte[] responseBytes = objectMapper.writeValueAsBytes(body);

            response.setStatusCode(HttpStatus.OK);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBytes)));

        } catch (Exception e) {
            byte[] responseBytes = "{\"error\":\"Cannot create token\"}".getBytes(StandardCharsets.UTF_8);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBytes)));
        }
    }
}
