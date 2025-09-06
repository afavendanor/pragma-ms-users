package co.com.pragma.api.handler;

import co.com.pragma.api.dto.GenericResponseDTO;
import co.com.pragma.api.dto.RequestLoginDTO;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.model.error.UnhauthorizedException;
import co.com.pragma.model.error.UserNotFoundException;
import co.com.pragma.usecase.user.ObtainUserDataByEmailUseCase;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static co.com.pragma.api.security.config.TokenJwtConfig.PREFIX_TOKEN;
import static co.com.pragma.api.security.config.TokenJwtConfig.SECRET_KEY;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private static final Logger log = Loggers.getLogger(AuthHandler.class.getName());

    private final ObtainUserDataByEmailUseCase obtainUserDataByEmailUseCase;
    private final PasswordEncoder passwordEncoder;

    public Mono<GenericResponseDTO<Map<String, Object>>> login(RequestLoginDTO requestLoginDTO) {
        ErrorHandler<Map<String, Object>> errorHandler = new ErrorHandler<>();

        return errorHandler.addErrors(
                obtainUserDataByEmailUseCase.getUserByEmail(requestLoginDTO.getEmail())
                        .switchIfEmpty(Mono.error(new UserNotFoundException(ResponseCode.MSUS006)))
                        .flatMap(user -> {
                            log.debug("Procesando login para usuario {}", requestLoginDTO.getEmail());

                            if (!passwordEncoder.matches(requestLoginDTO.getPassword(), user.getPassword())) {
                                return Mono.error(new UnhauthorizedException(ResponseCode.MSUS007));
                            }

                            if (!requestLoginDTO.getEmail().equals(user.getEmail())) {
                                return Mono.error(new UserNotFoundException(ResponseCode.MSUS006));
                            }

                            String roleName = user.getRole().getName();
                            if (!roleName.startsWith("ROLE_")) {
                                roleName = "ROLE_" + roleName;
                            }

                            String token = Jwts.builder()
                                    .claim("authorities", Arrays.asList(roleName, "ID_" + user.getIdentification()))
                                    .subject(user.getEmail())
                                    .issuedAt(Date.from(Instant.now()))
                                    .expiration(Date.from(Instant.now().plusSeconds(10800)))
                                    .signWith(SECRET_KEY)
                                    .compact();

                            Map<String, Object> responseData = Map.of(
                                    "token", PREFIX_TOKEN + token,
                                    "username", user.getEmail()
                            );

                            return Mono.just(responseData);
                        })
                        .map(responseData -> new GenericResponseDTO<>(HttpStatus.OK, ResponseCode.MSUS001, responseData))
                        .doOnSuccess(response ->
                                log.debug("Login exitoso para usuario {}", requestLoginDTO.getEmail())
                        ),
                "login"
        );
    }
}
