package co.com.pragma.api.handler;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.UserDTO;
import co.com.pragma.api.mapper.UserApiRestMapper;
import co.com.pragma.model.error.InternalErrorException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.usecase.user.ObtainUserDataByEmailListUseCase;
import co.com.pragma.usecase.user.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private ObtainUserDataByEmailListUseCase obtainUserDataByEmailListUseCase;

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
        when(registerUserUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(userHandler.createUser(createUserDTO))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS001.getMessage(), respuesta.getResponseMessage());
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).createUserDTOToUser(any(CreateUserDTO.class));
        verify(registerUserUseCase, times(1)).saveUser(any(User.class));
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
        when(registerUserUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.error(new InternalErrorException(ResponseCode.MSUS000, "Fallo de prueba")));

        // Act & Assert
        StepVerifier.create(userHandler.createUser(createUserDTO))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS000.getMessage(), respuesta.getResponseMessage());
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).createUserDTOToUser(any(CreateUserDTO.class));
        verify(registerUserUseCase, times(1)).saveUser(any(User.class));
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
                .thenThrow(new InternalErrorException(ResponseCode.MSUS000, "Fallo de prueba"));

        // Act & Assert
        StepVerifier.create(userHandler.createUser(createUserDTO))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS000.getMessage(), respuesta.getResponseMessage());
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).createUserDTOToUser(any(CreateUserDTO.class));
        verify(registerUserUseCase, never()).saveUser(any(User.class));
    }

    @Test
    void listarUsuarioPorEmail_debeRetornarRespuestaExitosa() {
        // Arrange
        User user = new User();
        user.setEmail("mail@mail.com");

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Test");

        when(obtainUserDataByEmailListUseCase.getAllByEmails(anyList()))
                .thenReturn(Flux.just(user));
        when(userApiRestMapper.userTOUserDTO(any(User.class)))
                .thenReturn(userDTO);

        // Act & Assert
        StepVerifier.create(userHandler.getAllByEmails(List.of("mail.test.com")))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS001.getMessage(), respuesta.getResponseMessage());
                    assertEquals(1, respuesta.getData().size());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).userTOUserDTO(any(User.class));
        verify(obtainUserDataByEmailListUseCase, times(1)).getAllByEmails(anyList());
    }

    @Test
    void listarUsuarioPorEmail_deberiaRetornarError_cuandoFalla() {
        // Arrange
        when(obtainUserDataByEmailListUseCase.getAllByEmails(anyList()))
                .thenReturn(Flux.error(new InternalErrorException(ResponseCode.MSUS000, "Fallo de prueba")));

        // Act & Assert
        StepVerifier.create(userHandler.getAllByEmails(List.of("mail.test.com")))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS000.getMessage(), respuesta.getResponseMessage());
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, never()).userTOUserDTO(any(User.class));
        verify(obtainUserDataByEmailListUseCase, times(1)).getAllByEmails(anyList());
    }

    @Test
    void listarUsuarioPorEmail_deberiaRetornarError_cuandoFallaMapper() {
        // Arrange
        User user = new User();
        user.setEmail("mail@mail.com");


        when(obtainUserDataByEmailListUseCase.getAllByEmails(anyList()))
                .thenReturn(Flux.just(user));
        when(userApiRestMapper.userTOUserDTO(any(User.class)))
                .thenThrow(new InternalErrorException(ResponseCode.MSUS000, "Fallo de prueba"));

        // Act & Assert
        StepVerifier.create(userHandler.getAllByEmails(List.of("mail.test.com")))
                .assertNext(respuesta -> {
                    assertNotNull(respuesta);
                    assertEquals(ResponseCode.MSUS000.getMessage(), respuesta.getResponseMessage());
                    assertNull(respuesta.getData());
                })
                .verifyComplete();

        verify(userApiRestMapper, times(1)).userTOUserDTO(any(User.class));
        verify(obtainUserDataByEmailListUseCase, times(1)).getAllByEmails(anyList());
    }

}
