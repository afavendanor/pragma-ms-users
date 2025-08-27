package co.com.pragma.api.handler;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.GenericResponseDTO;
import co.com.pragma.api.mapper.UserApiRestMapper;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger log = Loggers.getLogger(UserHandler.class.getName());

    private final RegisterUserUseCase registerUserUseCase;
    private final UserApiRestMapper userApiRestMapper;

    public Mono<GenericResponseDTO<Object>> createUser(CreateUserDTO createUserDTO) {

        ErrorHandler<Object> errorHandler = new ErrorHandler<>();
        return errorHandler.addErrors(
                Mono.defer(() -> {
                    log.debug("Inicializar guardar usuario {}", createUserDTO.getAddress());
                    return registerUserUseCase.saveUser(
                                    userApiRestMapper.createUserDTOToUser(createUserDTO)
                            )
                            .thenReturn(new GenericResponseDTO<>(ResponseCode.MSUS001, null))
                            .doOnSuccess(response ->
                                    log.debug("Finalizar guardar usuario {}", createUserDTO.getAddress())
                            );
                }),
                "createUser"
        );
    }
}
