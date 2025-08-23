package co.com.pragma.r2dbc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {
    
    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    /**private final UserEntity UserEntity = UserEntity.builder()
            .build();

    private final User User = User.builder()
            .build();

    private final User otherUser = User.builder()
            .build();

    @Test
    void shouldFindUserById() {

        when(mapper.map(UserEntity, User.class)).thenReturn(User);

        when(repository.findById("1")).thenReturn(Mono.just(UserEntity));

        Mono<User> result = repositoryAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(t -> t.getId().equals("1") && t.getTitle().equals("Tarea 1"))
                .verifyComplete();
    }

    @Test
    void shouldFindAllUser() {
        when(mapper.map(UserEntity, User.class)).thenReturn(User);
        when(repository.findAll()).thenReturn(Flux.just(UserEntity));

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(User)
                .verifyComplete();
    }

    @Test
    void shouldSaveUser() {
        when(mapper.map(UserEntity, User.class)).thenReturn(User);
        when(mapper.map(User, UserEntity.class)).thenReturn(UserEntity);
        when(repository.save(UserEntity)).thenReturn(Mono.just(UserEntity));

        Mono<User> result = repositoryAdapter.save(User);

        StepVerifier.create(result)
                .expectNext(User)
                .verifyComplete();
    }**/

}
