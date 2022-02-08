package evolution.controllers;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import evolution.dto.UserDto;
import evolution.jwt.JwtProvider;
import evolution.services.UserService;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users")
public class UserRestController {

    private final        UserService userService;
    private final        JwtProvider jwtProvider;
    private static final String      GOOGLE_API = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
    private static final String      GIVEN_NAME = "given_name";
    private static final String      EMAIL = "email";

    @GetMapping
    public UserDto profile(@AuthenticationPrincipal UserDto user) {
        return userService.profile(user);
    }

    @GetMapping("friends")
    public List<UserDto> friends(@AuthenticationPrincipal UserDto user) {
        return userService.getFriends(user);
    }

    @PostMapping("googleLogin")
    public String googleLogin(@RequestBody String token) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpUriRequest request = new HttpGet(GOOGLE_API + token);
            HttpResponse response = client.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            String username = "";
            String email = "";
            boolean filled = false;
            while ((line = bufReader.readLine()) != null) {
                if (line.contains(GIVEN_NAME)) {
                    username = line.substring(17, line.length() - 2);
                }
                if (line.contains(EMAIL) && !filled) {
                    email = line.substring(12, line.length() - 2);
                    filled = true;
                }
            }
            userService.register(email, username);
            return jwtProvider.generateToken(email);
        }
    }

    @PostMapping("login")
    public String login(@RequestParam String email, @RequestParam String username) {
        userService.register(email, username);
        return jwtProvider.generateToken(email);
    }

}
