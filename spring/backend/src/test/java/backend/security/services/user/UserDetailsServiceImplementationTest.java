package backend.security.services.user;

import backend.security.SecurityApplication;
import backend.security.exceptions.user.SaveEditedUserException;
import backend.security.models.user.Role;
import backend.security.models.user.User;
import backend.security.models.user.UserEditRequest;
import backend.security.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes= SecurityApplication.class)
@ActiveProfiles("test")
class UserDetailsServiceImplementationTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImplementation userService;

    User testUser = User.builder()
            .givenName("Test")
            .surname("User")
            .username("Test User")
            .email("test@gmx.com")
            .password("testpassword")
            .role(Role.USER)
            .build();

    UserEditRequest userEditRequest = UserEditRequest.builder()
            .surname("Update")
            .givenName("Testy")
            .email("updated@gmx.com")
            .build();

    @Test
    void loadUserByUsername() throws UsernameNotFoundException {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.ofNullable(testUser));
        assertThat(userService.loadUserByUsername("Test User"))
                .isEqualTo(testUser);
    }

    @Test
    void editUserInformation() throws SaveEditedUserException {
        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);
        User editedUser = userService.editUserInformation(userEditRequest,  testUser);
        assertEquals(editedUser.getGivenName(), userEditRequest.getGivenName());
        assertEquals(editedUser.getSurname(), userEditRequest.getSurname());
        assertEquals(editedUser.getEmail(), userEditRequest.getEmail());
    }

    @Test
    public void testLoadUserByUsernameFailureBehavior()
            throws UsernameNotFoundException {
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("Test User");
        });
    }
}
