package evolution.services;

import evolution.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthorizationService {

    void authorizeUser(User user);

    User getProfileOfCurrent();

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
