package evolution.jwt;


import evolution.dto.UserDto;
import evolution.services.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@AllArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private JwtProvider jwtProvider;
    private UserServiceImpl userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if ((token != null) && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            UserDto user = userService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(BEARER)) {
            return bearer.substring(BEARER.length());
        }
        return null;
    }
}
