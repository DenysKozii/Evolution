package evolution.controllers;

import evolution.dto.UserDto;
import evolution.jwt.JwtProvider;
import evolution.services.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserRestController {

    private UserServiceImpl userService;
    private JwtProvider jwtProvider;

    @GetMapping
    public UserDto profile(@AuthenticationPrincipal UserDto user) {
        return userService.loadUserByUsername(user.getUsername());
    }

    @PostMapping
    public ResponseEntity<String> googleLoginPost(@RequestBody String token) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpUriRequest request = new HttpGet("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token);
            HttpResponse response = client.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line;
            String username = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.contains("given_name")) {
                    username = line.substring(17, line.length() - 2);
                    userService.register(username);
                    break;
                }
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(jwtProvider.generateToken(username));
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }

}
