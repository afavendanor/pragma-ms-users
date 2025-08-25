package co.com.pragma.usecase.user;

import co.com.pragma.model.User;
import co.com.pragma.model.error.CustomException;
import co.com.pragma.model.util.ResponseCode;
import gateways.UserGateway;
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
class UserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void createUser_OK() {
        // Arrange
        User user = new User();
        user.setIdentification("user-123");
        when(userGateway.save(any(User.class)))
                .thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(userUseCase.saveUser(user))
                .expectNextMatches(resultado -> resultado.equals(user))
                .verifyComplete();

        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void createUser_Error() {
        // Arrange
        User user = new User();
        user.setIdentification("user-123");
        when(userGateway.save(any(User.class)))
                .thenReturn(Mono.error(new CustomException(ResponseCode.MSUS000, "Error guardando usuario")));

        // Act & Assert
        StepVerifier.create(userUseCase.saveUser(user))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(CustomException.class, error);
                    assertEquals(ResponseCode.MSUS000, ((CustomException) error).getResponseCode());
                })
                .verify();
    }

}
