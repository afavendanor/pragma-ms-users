package co.com.pragma.api.handler;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.GenericResponseDTO;
import co.com.pragma.api.dto.UserDTO;
import co.com.pragma.api.mapper.UserApiRestMapper;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.usecase.user.ObtainUserDataByEmailListUseCase;
import co.com.pragma.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger log = Loggers.getLogger(UserHandler.class.getName());

    private final RegisterUserUseCase registerUserUseCase;
    private final ObtainUserDataByEmailListUseCase obtainUserDataByEmailListUseCase;
    private final UserApiRestMapper userApiRestMapper;

    public Mono<GenericResponseDTO<Object>> createUser(CreateUserDTO createUserDTO) {

        ErrorHandler<Object> errorHandler = new ErrorHandler<>();
        return errorHandler.addErrors(
                Mono.defer(() -> {
                    log.debug("Inicializar guardar usuario {}", createUserDTO.getAddress());
                    return registerUserUseCase.saveUser(
                                    userApiRestMapper.createUserDTOToUser(createUserDTO)
                            )
                            .thenReturn(new GenericResponseDTO<>(HttpStatus.CREATED, ResponseCode.MSUS001, null))
                            .doOnSuccess(response ->
                                    log.debug("Finalizar guardar usuario {}", createUserDTO.getAddress())
                            );
                }),
                "createUser"
        );
    }

    public Mono<GenericResponseDTO<List<UserDTO>>> getAllByEmails(List<String> emails) {

        ErrorHandler<List<UserDTO>> errorHandler = new ErrorHandler<>();
        return errorHandler.addErrors(
                Mono.defer(() -> {
                    log.debug("Inicializar lista de usuario por emails");
                    return obtainUserDataByEmailListUseCase.getAllByEmails(emails)
                            .map(userApiRestMapper::userTOUserDTO)
                            .collectList()
                            .map(list -> new GenericResponseDTO<>(HttpStatus.OK, ResponseCode.MSUS001, list))
                            .doOnSuccess(response ->
                                    log.debug("lista de usuario por emails")
                            );
                }),
                "getAllByEmails"
        );
    }
}
