package co.com.pragma.usecase.user;

import co.com.pragma.model.error.DuplicateEntryException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.model.error.ResponseCode;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.<User>error(new DuplicateEntryException(ResponseCode.MSUS003)))
                .switchIfEmpty(Mono.defer(() -> userRepository.save(user)));
    }



}
