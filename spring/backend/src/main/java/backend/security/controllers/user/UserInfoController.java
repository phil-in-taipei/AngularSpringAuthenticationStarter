package backend.security.controllers.user;

import backend.security.exceptions.auth.RefreshTokenExpiredException;
import backend.security.exceptions.user.SaveEditedUserException;
import backend.security.exceptions.user.UserNotFoundException;
import backend.security.models.auth.TokenRefreshRequest;
import backend.security.models.errors.ApiError;
import backend.security.models.user.User;
import backend.security.models.user.UserEditRequest;
import backend.security.services.user.UserDetailsServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController {


    @Autowired
    UserDetailsServiceImplementation userService;

    @GetMapping("/test")
    public ResponseEntity<String> authenticatedAdminTestRoute() {
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(
                "{\"message\": \"Response from authenticated endpoint successful\"}",
                httpHeaders, HttpStatus.OK
        );
    }

    @GetMapping("/authenticated")
    public ResponseEntity<Object> authenticatedUserInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(userService.loadUserByUsername(userDetails.getUsername()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Object> editUserInfo(
            @RequestBody UserEditRequest userEditRequest,
            Authentication authentication
    ) {
        System.out.println("...Calling the edit controller method....");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            User user = userService.loadUserByUsername(userDetails.getUsername());
            return ResponseEntity.ok(
                    userService.editUserInformation(userEditRequest, user)
            );
        } catch (UsernameNotFoundException | SaveEditedUserException e) {
            return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
        }
    }
}
