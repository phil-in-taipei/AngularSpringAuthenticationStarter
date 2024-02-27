package backend.security.controllers.auth;

import backend.security.exceptions.auth.LoginFailureException;
import backend.security.exceptions.auth.RefreshTokenExpiredException;
import backend.security.exceptions.user.UserNotFoundException;
import backend.security.models.auth.*;
import backend.security.services.auth.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import backend.security.models.errors.ApiError;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshNewToken(
            @RequestBody TokenRefreshRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.authenticateRefreshToken(request));
        } catch (UserNotFoundException | RefreshTokenExpiredException e) {
            return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                    new ApiError("There was an error. Please try again")
            );
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> userLogin(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (UserNotFoundException | LoginFailureException e) {
            return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                    new ApiError("There was an error. Please try again")
            );
        }
    }

}
