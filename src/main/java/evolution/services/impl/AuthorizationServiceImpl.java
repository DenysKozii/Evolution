package evolution.services.impl;

import evolution.entity.User;
import evolution.exception.EntityNotFoundException;
import evolution.repositories.UserRepository;
import evolution.services.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;

    @Override
    public void authorizeUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getId(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Override
    public User getProfileOfCurrent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.valueOf(principal instanceof UserDetails ?
                ((UserDetails)principal).getUsername() : principal.toString());
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username %s not found!", username))
        );

        return org.springframework.security.core.userdetails.User.withUsername(user.getId().toString())
                .password(user.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
                .build();
    }
}
