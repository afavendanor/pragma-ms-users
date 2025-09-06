package co.com.pragma.r2dbc;

import co.com.pragma.model.user.Role;
import co.com.pragma.r2dbc.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.function.Function;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleReactiveRepositoryAdapterTest {
    
    @InjectMocks
    RoleRepositoryAdapter repositoryAdapter;

    @Mock
    RoleReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    private Function<RoleEntity, Role> toEntityFn;

    private RoleEntity roleEntity;
    private Role role;

    @BeforeEach
    void setup() {
       roleEntity = new RoleEntity();
       roleEntity.setId(1L);
       roleEntity.setName("ADMIN");

       role = new Role();
       role.setId(1L);
       role.setName("ADMIN");
    }

    @Test
    void shouldFindRoleById() {

        when(repository.findById(anyLong()))
                .thenReturn(Mono.just(roleEntity));
        when(mapper.map(any(RoleEntity.class), eq(Role.class)))
                .thenReturn(role);

        Mono<Role> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(t -> t.getId().equals(1L) && t.getName().equals("ADMIN"))
                .verifyComplete();
    }


}
