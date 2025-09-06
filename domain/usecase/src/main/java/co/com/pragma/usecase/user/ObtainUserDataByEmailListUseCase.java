package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class ObtainUserDataByEmailListUseCase {

    private final UserRepository userRepository;

    public Flux<User> getAllByEmails(List<String> emails) {
        return userRepository.findAllByEmails(emails);
    }

}
