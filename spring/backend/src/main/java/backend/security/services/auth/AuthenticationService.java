package backend.security.services.auth;

import backend.security.exceptions.auth.LoginFailureException;
import backend.security.exceptions.auth.RefreshTokenExpiredException;
import backend.security.exceptions.user.UserNotFoundException;
import backend.security.logging.BatchLogger;
import backend.security.models.auth.AuthenticationRequest;
import backend.security.models.auth.AuthenticationResponse;
import backend.security.models.auth.TokenRefreshRequest;
import backend.security.repositories.user.UserRepository;
import backend.security.services.user.UserDetailsServiceImplementation;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsServiceImplementation userService;

    @BatchLogger
    public AuthenticationResponse authenticate(
            AuthenticationRequest request
    ) throws UserNotFoundException, AuthenticationException, LoginFailureException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            var user = userService.loadUserByUsername(request.getUsername());
            var jwtToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponse.builder()
                    .refresh(jwtRefreshToken)
                    .token(jwtToken)
                    .build();

        } catch (AuthenticationException e){
            throw new LoginFailureException(
                    "Login with the provided credentials failed. Please try again"
            );
        }

    }

    @BatchLogger
    public AuthenticationResponse authenticateRefreshToken(
            TokenRefreshRequest request
    ) throws UserNotFoundException, RefreshTokenExpiredException, ExpiredJwtException {
        var jwtRefreshToken = request.getRefresh();
        try {
            var username = jwtService.extractUsername(jwtRefreshToken);
            var user = userService.loadUserByUsername(username.substring(0, username.length() / 2));
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .refresh(jwtRefreshToken)
                    .token(jwtToken)
                    .build();
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException(
                    "The login session has expired. Please login again");
        } catch (UsernameNotFoundException e) {
        throw new UserNotFoundException("The user does not exist!");
    }
    }
}
