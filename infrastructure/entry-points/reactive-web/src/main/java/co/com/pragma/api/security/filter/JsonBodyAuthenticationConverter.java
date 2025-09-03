package co.com.pragma.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class JsonBodyAuthenticationConverter implements ServerAuthenticationConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return exchange.getRequest().getBody()
                .next()
                .flatMap(dataBuffer -> {
                    try {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        Map<String, String> body = mapper.readValue(bytes, Map.class);
                        String username = body.get("username");
                        String password = body.get("password");
                        if (username == null || password == null) {
                            return Mono.empty();
                        }
                        return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                });
    }
}
