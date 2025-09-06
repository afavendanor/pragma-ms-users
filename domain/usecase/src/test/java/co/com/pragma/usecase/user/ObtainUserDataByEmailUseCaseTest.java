package co.com.pragma.usecase.user;

import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.model.error.RoleNotFoundException;
import co.com.pragma.model.error.UserNotFoundException;
import co.com.pragma.model.user.Role;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtainUserDataByEmailUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ObtainUserDataByEmailUseCase obtainUserDataByEmailUseCase;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {

        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        user = new User();
        user.setEmail("test@email.com");
        user.setRole(role);
    }

    @Test
    void shouldReturnUserWithRole_whenUserAndRoleExist() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(user));
        when(roleRepository.findById(anyLong()))
                .thenReturn(Mono.just(role));

        StepVerifier.create(obtainUserDataByEmailUseCase.getUserByEmail("test@email.com"))
                .expectNextMatches(result ->
                        result.getEmail().equals("test@email.com") &&
                                result.getRole().getName().equals("ADMIN"))
                .verifyComplete();
    }

    @Test
    void shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(obtainUserDataByEmailUseCase.getUserByEmail("unknown@email.com"))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(UserNotFoundException.class, error);
                    assertEquals(ResponseCode.MSUS006.getMessage(), error.getMessage());
                })
                .verify();

    }

    @Test
    void shouldThrowRoleNotFoundException_whenRoleDoesNotExist() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Mono.just(user));
        when(roleRepository.findById(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(obtainUserDataByEmailUseCase.getUserByEmail("test@email.com"))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(RoleNotFoundException.class, error);
                    assertEquals(ResponseCode.MSUS005.getMessage(), error.getMessage());
                })
                .verify();

    }
}
