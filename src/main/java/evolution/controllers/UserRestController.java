package evolution.controllers;

import evolution.dto.UserDto;
import evolution.jwt.JwtProvider;
import evolution.services.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserRestController {

    private UserServiceImpl userService;
    private JwtProvider jwtProvider;

    @Value("${googleapis}")
    private String googleapis;

    @GetMapping("profile")
    public UserDto profile(@AuthenticationPrincipal UserDto user) {
        return user;
    }

    @PostMapping
    public String googleLoginPost(@RequestBody String token) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpUriRequest request = new HttpGet(googleapis + token);
            HttpResponse response = client.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line;
            String username = "";
            String email = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.contains("given_name")) {
                    username = line.substring(17, line.length() - 2);
                }
                if (line.contains("email")) {
                    email = line.substring(12, line.length() - 2);
                }
            }
            userService.register(email, username);
            return jwtProvider.generateToken(token);
        }
    }

    @PostMapping("login")
    public String loginPost(@RequestBody String email, @RequestBody String username) {
        userService.register(email, username);
        return jwtProvider.generateToken(email);
    }

}
