package evolution.config;

import evolution.entity.Role;
import evolution.entity.User;
import evolution.repositories.UserRepository;
import evolution.services.AbilityService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableOAuth2Sso
public class SecurityGoogleConfiguration extends WebSecurityConfigurerAdapter {

    private final AbilityService abilityService;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Bean
    protected PrincipalExtractor principalExtractor(UserRepository userRepository) {
        return map -> {
            String id =(String) map.get("sub");
            return userRepository.findById(id).orElseGet(()->{
                User user = new User();
                user.setId(id);
                user.setRole(Role.USER);
                user.setUsername((String) map.get("name"));
                user.setRating(0);
                user.setCoins(100);
                user.setCrystals(10);
                user.setAvailableAbilities(abilityService.getStartList());
                userRepository.save(user);
                return user;
            });
        };
    }

    @Order(1)
    @Configuration
    public static class HttpBasicAuthentication extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requestMatchers()
                    .antMatchers(HttpMethod.GET,
                            "/api-docs/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/v2/api-docs",
                            "/js/**",
                            "/css/**",
                            "/webjars/springfox-swagger-ui/**"
                    )
                    .and().httpBasic()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable();
        }
    }
}
