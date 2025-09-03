package co.com.pragma.r2dbc;

import co.com.pragma.model.error.DuplicateEntryException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.error.ResponseCode;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.model.user.gateways.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.save(user)
                .doOnError(e -> log.error("Error guardando usuario: {}", e.getMessage(), e))
                .onErrorMap(ex -> (ex instanceof DataIntegrityViolationException)
                        ? new DuplicateEntryException(ResponseCode.MSUS004)
                        : new DuplicateEntryException(ResponseCode.MSUS000));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        User userFilter = new User();
        userFilter.setEmail(email);
        return super.findByExample(userFilter)
                .next();
    }


}
