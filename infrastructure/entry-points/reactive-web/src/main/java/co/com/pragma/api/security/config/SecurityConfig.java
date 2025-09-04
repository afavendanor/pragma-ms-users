package co.com.pragma.api.security.config;

import co.com.pragma.api.security.filter.*;
import co.com.pragma.r2dbc.UserDetailRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableReactiveMethodSecurity  // Changed to reactive variant
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            UserDetailRepositoryAdapter uds,
            PasswordEncoder passwordEncoder) {
        var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(uds);
        authManager.setPasswordEncoder(passwordEncoder);
        return authManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager authenticationManager,
                                                         JsonBodyAuthenticationConverter jsonBodyAuthenticationConverter,
                                                         JwtValidationWebFilter jwtValidationWebFilter) {

        AuthenticationWebFilter loginFilter = new JwtAuthenticationWebFilter(authenticationManager);
        loginFilter.setServerAuthenticationConverter(jsonBodyAuthenticationConverter);
        loginFilter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login")
        );

        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/login").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                "/swagger-resources/**", "/webjars/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtValidationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(loginFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public JwtValidationWebFilter jwtValidationWebFilter() {
        return new JwtValidationWebFilter();
    }

    @Bean
    public JwtAuthenticationWebFilter jwtAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        return new JwtAuthenticationWebFilter(authenticationManager);
    }
}