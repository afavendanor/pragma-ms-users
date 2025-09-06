package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class ObtainUserDataByEmailListUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ObtainUserDataByEmailListUseCase obtainUserDataByEmailListUseCase;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setIdentification("1234");
        user1.setEmail("one@email.com");

        user2 = new User();
        user2.setIdentification("2345");
        user2.setEmail("two@email.com");
    }

    @Test
    void shouldReturnUsers_whenEmailsExist() {
        List<String> emails = Arrays.asList("one@email.com", "two@email.com");

        when(userRepository.findAllByEmails(anyList()))
                .thenReturn(Flux.just(user1, user2));

        StepVerifier.create(obtainUserDataByEmailListUseCase.getAllByEmails(emails))
                .expectNext(user1)
                .expectNext(user2)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmpty_whenNoUsersFound() {
        List<String> emails = Arrays.asList("notfound@email.com");

        when(userRepository.findAllByEmails(anyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(obtainUserDataByEmailListUseCase.getAllByEmails(emails))
                .verifyComplete();
    }
}
