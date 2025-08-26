package co.com.pragma.persistence;

import co.com.pragma.model.user.User;
import co.com.pragma.model.error.CustomException;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.r2dbc.UserReactiveRepository;
import co.com.pragma.r2dbc.UserRepositoryAdapter;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {
    
    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setup() {
       userEntity = new UserEntity();
       userEntity.setId(1L);
       userEntity.setEmail("test@test.com");

       user = new User();
       user.setIdentification("id-123");
       user.setEmail("test@test.com");
    }

    @Test
    void shouldFindUserById() {

        when(mapper.map(userEntity, User.class)).thenReturn(user);

        when(repository.findById(anyLong()))
                .thenReturn(Mono.just(userEntity));

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(t -> t.getIdentification().equals("id-123") && t.getEmail().equals("test@test.com"))
                .verifyComplete();
    }

    @Test
    void shouldFindAllUser() {
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        when(repository.findAll()).thenReturn(Flux.just(userEntity));

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldDeleteById() {
        when(repository.deleteById(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.deleteById(1L))
                .verifyComplete();
    }

    @Test
    void shouldSaveUser() {
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldSaveUser_error() {
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(any())).
                thenReturn(Mono.error(new RuntimeException("Error en base de datos")));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(CustomException.class, error);
                    assertEquals(ResponseCode.MSUS000, ((CustomException) error).getResponseCode());
                })
                .verify();
    }

    @Test
    void shouldSaveUser_errorDuplicado() {
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(any())).
                thenReturn(Mono.error(new DataIntegrityViolationException("Error, dato duplicado")));

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(CustomException.class, error);
                    assertEquals(ResponseCode.MSUS004, ((CustomException) error).getResponseCode());
                })
                .verify();
    }

}
