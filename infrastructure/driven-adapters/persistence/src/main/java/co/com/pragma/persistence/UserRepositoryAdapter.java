package co.com.pragma.persistence;

import co.com.pragma.model.User;
import co.com.pragma.model.error.CustomException;
import co.com.pragma.model.util.ResponseCode;
import co.com.pragma.persistence.entity.UserEntity;
import co.com.pragma.persistence.helper.ReactiveAdapterOperations;
import gateways.UserGateway;
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
> implements UserGateway {

    private static final Logger log = Loggers.getLogger(UserRepositoryAdapter.class.getName());

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user)
                .doOnError(e -> log.error("Error guardando usuario: {}", e.getMessage(), e))
                .onErrorMap(ex -> (ex instanceof DataIntegrityViolationException)
                        ? new CustomException(ResponseCode.MSUS003)
                        : new CustomException(ResponseCode.MSUS000));
    }


}
