package co.com.pragma.api.handler;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.mapper.UserApiRestMapper;
import co.com.pragma.model.User;
import co.com.pragma.model.error.CustomException;
import co.com.pragma.model.util.ResponseCode;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserApiRestMapper userApiRestMapper;

    @InjectMocks
    private UserHandler userHandler;

    @Test
    void guardarUsuario_debeRetornarRespuestaExitosa() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Test");
        createUserDTO.setEmail("email@mail.com");

        User user = new User();
        user.setEmail("mail@mail.com");

        when(userApiRestMapper.createUserDTOToUser(any(CreateUserDTO.class)))
                .thenReturn(user);
        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(userHandler.createUser(createUserDTO))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS001, ResponseCode.valueOf(respuesta.getResponseCode()));
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).createUserDTOToUser(any(CreateUserDTO.class));
        verify(userUseCase, times(1)).saveUser(any(User.class));
    }

    @Test
    void guardarUsuario_deberiaRetornarError_cuandoFalla() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Test");
        createUserDTO.setEmail("email@mail.com");

        User user = new User();
        user.setEmail("mail@mail.com");

        when(userApiRestMapper.createUserDTOToUser(any(CreateUserDTO.class)))
                .thenReturn(user);
        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.error(new CustomException(ResponseCode.MSUS000, "Fallo de prueba")));

        // Act & Assert
        StepVerifier.create(userHandler.createUser(createUserDTO))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS000, ResponseCode.valueOf(respuesta.getResponseCode()));
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).createUserDTOToUser(any(CreateUserDTO.class));
        verify(userUseCase, times(1)).saveUser(any(User.class));
    }

    @Test
    void guardarUsuario_deberiaRetornarError_cuandoFallaMapper() {
        // Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Test");
        createUserDTO.setEmail("email@mail.com");

        User user = new User();
        user.setEmail("mail@mail.com");

        when(userApiRestMapper.createUserDTOToUser(any(CreateUserDTO.class)))
                .thenThrow(new CustomException(ResponseCode.MSUS000, "Fallo de prueba"));

        // Act & Assert
        StepVerifier.create(userHandler.createUser(createUserDTO))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS000, ResponseCode.valueOf(respuesta.getResponseCode()));
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).createUserDTOToUser(any(CreateUserDTO.class));
        verify(userUseCase, never()).saveUser(any(User.class));
    }
}
