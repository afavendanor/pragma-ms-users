package co.com.pragma.api.handler;

import co.com.pragma.api.dto.RequestLoginDTO;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.model.user.Role;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.user.ObtainUserDataByEmailUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthHandlerTest {

    @Mock
    private ObtainUserDataByEmailUseCase obtainUserDataByEmailUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthHandler authService;

    private RequestLoginDTO loginDTO;
    private User user;

    @BeforeEach
    void setup() {
        loginDTO = new RequestLoginDTO();
        loginDTO.setEmail("user@test.com");
        loginDTO.setPassword("encodedPass");

        Role role = new Role();
        role.setName("ADMIN");

        user = new User();
        user.setEmail("user@test.com");
        user.setPassword("encodedPass");
        user.setIdentification("123456");
        user.setRole(role);
    }

    @Test
    void login_shouldReturnTokenWhenCredentialsValid() {
        when(obtainUserDataByEmailUseCase.getUserByEmail(anyString()))
                .thenReturn(Mono.just(user));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        StepVerifier.create(authService.login(loginDTO))
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK.value(), response.getResponseCode());
                    assertEquals(ResponseCode.MSUS001.getMessage(), response.getResponseMessage());

                    Map<String, Object> data = response.getData();
                    assertTrue(data.containsKey("token"));
                    assertEquals("user@test.com", data.get("username"));
                })
                .verifyComplete();
    }

    @Test
    void login_shouldErrorWhenUserNotFound() {
        when(obtainUserDataByEmailUseCase.getUserByEmail(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(authService.login(loginDTO))
                .expectNextMatches(respuesta ->
                        respuesta.getResponseCode().equals(HttpStatus.NOT_FOUND.value()) &&
                                respuesta.getResponseMessage().equals(ResponseCode.MSUS006.getMessage())
                )
                .verifyComplete();
    }

    @Test
    void login_shouldErrorWhenPasswordInvalid() {
        when(obtainUserDataByEmailUseCase.getUserByEmail("user@test.com"))
                .thenReturn(Mono.just(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        StepVerifier.create(authService.login(loginDTO))
                .expectNextMatches(respuesta ->
                        respuesta.getResponseCode().equals(HttpStatus.UNAUTHORIZED.value()) &&
                                respuesta.getResponseMessage().equals(ResponseCode.MSUS007.getMessage())
                )
                .verifyComplete();
    }

    @Test
    void login_shouldErrorWhenEmailDifferent() {
        user.setEmail("other@test.com");

        when(obtainUserDataByEmailUseCase.getUserByEmail("user@test.com"))
                .thenReturn(Mono.just(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        StepVerifier.create(authService.login(loginDTO))
                .expectNextMatches(respuesta ->
                        respuesta.getResponseCode().equals(HttpStatus.NOT_FOUND.value()) &&
                                respuesta.getResponseMessage().equals(ResponseCode.MSUS006.getMessage())
                )
                .verifyComplete();
    }

    @Test
    void login_shouldPrefixRoleIfMissing() {
        user.getRole().setName("ADMIN");
        when(obtainUserDataByEmailUseCase.getUserByEmail(anyString()))
                .thenReturn(Mono.just(user));
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        StepVerifier.create(authService.login(loginDTO))
                .assertNext(response -> {
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }



}
