package gateways;

import co.com.pragma.model.User;
import reactor.core.publisher.Mono;

public interface UserGateway {

    Mono<User> save(User user);

}
