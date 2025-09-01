package co.com.pragma.usecase.user;

import co.com.pragma.model.error.DuplicateEntryException;
import co.com.pragma.model.error.InternalErrorException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void createUser_OK() {
        // Arrange
        User user = new User();
        user.setIdentification("user-123");
        user.setEmail("user@test.com");
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(registerUserUseCase.saveUser(user))
                .expectNextMatches(resultado -> resultado.equals(user))
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_EmailExist() {
        // Arrange
        User user = new User();
        user.setIdentification("user-123");
        user.setEmail("user@test.com");
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(registerUserUseCase.saveUser(user))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(DuplicateEntryException.class, error);
                    assertEquals(ResponseCode.MSUS003.getMessage(), error.getMessage());
                })
                .verify();

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_Error() {
        // Arrange
        User user = new User();
        user.setIdentification("user-123");
        user.setEmail("user@test.com");
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.error(new InternalErrorException(ResponseCode.MSUS000, "Error guardando usuario")));

        // Act & Assert
        StepVerifier.create(registerUserUseCase.saveUser(user))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(InternalErrorException.class, error);
                    assertEquals(ResponseCode.MSUS000.getMessage(), error.getMessage());
                })
                .verify();

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

}
