//package exolution.config;
//
//import exolution.entity.Role;
//import exolution.entity.User;
//import exolution.repositories.UserRepository;
//import exolution.services.AbilityService;
//import exolution.services.AuthorizationService;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@AllArgsConstructor
//@EnableWebSecurity
//@EnableOAuth2Sso
//public class SecurityGoogleConfiguration extends WebSecurityConfigurerAdapter {
//
//    private final AuthorizationService userService;
//    private final AbilityService abilityService;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new CustomUserDetailsService(userService);
//    }
//
//    @Bean
//    protected PrincipalExtractor principalExtractor(UserRepository userRepository) {
//        return map -> {
//            Long id = Long.valueOf((String) map.get("sub"));
//            return userRepository.findById(id).orElseGet(()->{
//                User user = new User();
//                user.setId(id);
//                user.setRole(Role.USER);
//                user.setUsername((String) map.get("name"));
//                user.setRating(0);
//                user.setCoins(100);
//                user.setCrystals(10);
//                user.setAvailableAbilities(abilityService.getStartList());
//                userRepository.save(user);
//                return user;
//            });
//        };
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/api/user").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable();
////        http.oauth2Client();
////        http.oauth2Login();
//
//    }
//}
