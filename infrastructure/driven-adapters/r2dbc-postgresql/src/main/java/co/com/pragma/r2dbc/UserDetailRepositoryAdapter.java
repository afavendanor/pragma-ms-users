package co.com.pragma.r2dbc;

import co.com.pragma.model.error.InternalErrorException;
import co.com.pragma.model.error.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class UserDetailRepositoryAdapter implements ReactiveUserDetailsService {

    private final UserReactiveRepository userReactiveRepository;
    private final RoleReactiveRepository roleReactiveRepository;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userReactiveRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        new UsernameNotFoundException("User " + email + " not found")))
                .flatMap(user ->
                        roleReactiveRepository.findById(user.getRolId())
                                .switchIfEmpty(Mono.error(new InternalErrorException(ResponseCode.MSUS005)))
                                .map(role -> {
                                    SimpleGrantedAuthority roleAuthority =
                                            new SimpleGrantedAuthority(role.getName().startsWith("ROLE_")
                                                    ? role.getName()
                                                    : "ROLE_" + role.getName());

                                    SimpleGrantedAuthority idAuthority =
                                            new SimpleGrantedAuthority("ID_" + user.getIdentification());

                                    return Arrays.asList(roleAuthority, idAuthority);
                                })
                                .map(authorities ->
                                        User.withUsername(user.getEmail())
                                                .password(user.getPassword())
                                                .authorities(authorities)
                                                .build()
                                )
                );
    }

}
