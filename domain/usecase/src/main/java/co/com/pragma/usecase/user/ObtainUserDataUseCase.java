package co.com.pragma.usecase.user;

import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.model.error.RoleNotFoundException;
import co.com.pragma.model.error.UserNotFoundException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ObtainUserDataUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<User> getUserByEmail(String username) {
        return userRepository.findByEmail(username)
                .switchIfEmpty(Mono.error(new UserNotFoundException(ResponseCode.MSUS006)))
                .flatMap(user -> roleRepository.findById(user.getRole().getId())
                        .switchIfEmpty(Mono.error(new RoleNotFoundException(ResponseCode.MSUS005)))
                        .map(role -> {
                            user.setRole(role);
                            return user;
                        }));
    }

    public Flux<User> getAllByEmails(List<String> emails) {
        return userRepository.findAllByEmails(emails);
    }
}
