package co.com.pragma.usecase.user;

import co.com.pragma.model.User;
import gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserGateway userGateway;

    public Mono<User> saveUser(User user) {
        return userGateway.save(user);
    }

}
