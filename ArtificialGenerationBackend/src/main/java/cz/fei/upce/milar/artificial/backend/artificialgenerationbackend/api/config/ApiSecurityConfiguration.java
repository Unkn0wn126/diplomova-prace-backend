package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.config;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.filters.UserFilter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfiguration {

    private final ActiveClientsService activeClientsService;

    public ApiSecurityConfiguration(ActiveClientsService activeClientsService) {
        this.activeClientsService = activeClientsService;
    }

    @Bean
    public FilterRegistrationBean<UserFilter> userFilterFilterRegistration(UserFilter filter) {
        FilterRegistrationBean<UserFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers("/api/v1/hello**").permitAll()
                                .requestMatchers("/api/v1/**").permitAll()
                                .anyRequest().permitAll()
        ).addFilterBefore(new UserFilter(activeClientsService), AuthorizationFilter.class);

        return http.build();
    }
}
