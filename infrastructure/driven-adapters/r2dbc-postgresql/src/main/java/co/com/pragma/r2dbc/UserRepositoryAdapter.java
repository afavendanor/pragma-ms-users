package co.com.pragma.r2dbc;

import co.com.pragma.model.User;
import co.com.pragma.model.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
> implements UserRepository {
    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> save(User User) {
        return super.save(User);
    }
    @Override
    public Flux<User> findAll() {
        return super.findAll();
    }

    @Override
    public Mono<User> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

}
