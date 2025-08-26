package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.error.CustomException;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
> implements UserRepository {

    private static final Logger log = Loggers.getLogger(UserRepositoryAdapter.class.getName());

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user)
                .doOnError(e -> log.error("Error guardando usuario: {}", e.getMessage(), e))
                .onErrorMap(ex -> (ex instanceof DataIntegrityViolationException)
                        ? new CustomException(ResponseCode.MSUS004)
                        : new CustomException(ResponseCode.MSUS000));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        User userFilter = new User();
        userFilter.setEmail(email);
        return super.findByExample(userFilter)
                .next();
    }


}
