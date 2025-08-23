package co.com.pragma.api.handler;

import co.com.pragma.model.User;
import co.com.pragma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class)
                .flatMap(userUseCase::updateUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                //.contentType(MediaType.APPLICATION_JSON)
                //.contentType(MediaType.APPLICATION_NDJSON)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return userUseCase.getUserById(Long.valueOf(id))
                .flatMap(User -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(User))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenDeleteUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return userUseCase.deleteUser(Long.valueOf(id))
                .then(ServerResponse.noContent().build());
    }
}
