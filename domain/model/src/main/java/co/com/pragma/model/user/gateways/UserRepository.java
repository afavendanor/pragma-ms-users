package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository {

    Mono<User> save(User user);

    Mono<User> findByEmail(String email);

    Flux<User> findAllByEmails(List<String> emails);

}
