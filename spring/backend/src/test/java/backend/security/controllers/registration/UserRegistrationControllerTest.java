package backend.security.controllers.registration;

import backend.security.SecurityApplication;
import backend.security.exceptions.auth.PasswordConfirmationFailureException;
import backend.security.models.registration.RegisterRequest;
import backend.security.models.registration.RegistrationResponse;
import backend.security.models.user.Role;
import backend.security.models.user.User;
import backend.security.repositories.user.UserRepository;
import backend.security.services.auth.JwtService;
import backend.security.services.registration.UserRegistrationService;
import backend.security.services.user.UserDetailsServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import backend.security.utils.TestUtil;

import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@WebMvcTest(UserRegistrationController.class)
@ContextConfiguration(classes = {SecurityApplication.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsServiceImplementation userDetailsService;

    @MockBean
    UserRegistrationService userRegistrationService;

    @MockBean
    UserRepository userRepository;

    User testAdmin = User.builder()
            .givenName("Test")
            .surname("Admin")
            .username("Test Admin")
            .email("test@gmx.com")
            .password("testpassword")
            .role(Role.ADMIN)
            .build();

    RegisterRequest testAdminRegistrationRequest = RegisterRequest.builder()
            .givenName("Test")
            .surname("Admin")
            .username("Test Admin")
            .email("test@gmx.com")
            .password("testpassword")
            .passwordConfirmation("testpassword")
            .build();

    RegisterRequest testAdminRegistrationRequestPasswordError = RegisterRequest.builder()
            .givenName("Test")
            .surname("Admin")
            .username("Test Admin")
            .email("test@gmx.com")
            .password("testpassword")
            .passwordConfirmation("testpassword!!")
            .build();

    RegistrationResponse testAdminRegistrationResponse = RegistrationResponse
            .builder()
            .message("Account successfully created for admin: " +
                    testAdmin.getUsername())
            .build();

    User testUser = User.builder()
            .givenName("Test")
            .surname("User")
            .username("Test User")
            .email("test@gmx.com")
            .password("testpassword")
            .role(Role.USER)
            .build();

    RegisterRequest testUserRegistrationRequest = RegisterRequest.builder()
            .givenName("Test")
            .surname("User")
            .username("Test User")
            .email("test@gmx.com")
            .password("testpassword")
            .passwordConfirmation("testpassword")
            .build();

    RegisterRequest testUserRegistrationRequestPasswordError = RegisterRequest.builder()
            .givenName("Test")
            .surname("User")
            .username("Test User")
            .email("test@gmx.com")
            .password("testpassword")
            .passwordConfirmation("testpassword!!")
            .build();

    RegistrationResponse testUserRegistrationResponse = RegistrationResponse
            .builder()
            .message("Account successfully created for user: " +
                    testUser.getUsername())
            .build();

    @Test
    void registerUser() throws Exception {
        when(userRegistrationService.register(testUserRegistrationRequest))
                .thenReturn(testUserRegistrationResponse);
        mockMvc.perform(post("/api/register/user")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(testUserRegistrationRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "Account successfully created for user: Test User")
                );

    }

    @Test
    void registerUserPasswordConfirmationFailure() throws Exception {
        when(userRegistrationService.register(any(RegisterRequest.class)))
                .thenThrow(new PasswordConfirmationFailureException(
                        "The passwords do not match. Please try again.")
                );
        mockMvc.perform(post("/api/register/user")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(
                                testUserRegistrationRequestPasswordError)
                        )
                )
                //.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "The passwords do not match. Please try again.")
                );

    }

    @Test
    void registerUserDataIntegrityFailure() throws Exception {
        when(userRegistrationService.register(any(RegisterRequest.class)))
                .thenThrow(new DataIntegrityViolationException(
                        "A user with that username already exists. Please try again")
                );
        mockMvc.perform(post("/api/register/user")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(
                                testUserRegistrationRequest)
                        )
                )
                //.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "A user with that username already exists. Please try again")
                );

    }

    @Test
    void registerAdmin() throws Exception {
        when(userRegistrationService.registerAdmin(testAdminRegistrationRequest))
                .thenReturn(testAdminRegistrationResponse);
        mockMvc.perform(post("/api/register/admin")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(
                                testAdminRegistrationRequest)
                        )
                )
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "Account successfully created for admin: Test Admin")
                );
    }

    @Test
    void registerAdminDataIntegrityFailure() throws Exception {
        when(userRegistrationService.registerAdmin(any(RegisterRequest.class)))
                .thenThrow(new DataIntegrityViolationException(
                        "A user with that username already exists. Please try again")
                );
        mockMvc.perform(post("/api/register/admin")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(
                                testAdminRegistrationRequest)
                        )
                )
                //.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "A user with that username already exists. Please try again")
                );

    }

    @Test
    void registerAdminPasswordConfirmationFailure() throws Exception {
        when(userRegistrationService.registerAdmin(any(RegisterRequest.class)))
                .thenThrow(new PasswordConfirmationFailureException(
                        "The passwords do not match. Please try again.")
                );
        mockMvc.perform(post("/api/register/admin")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(
                                testAdminRegistrationRequestPasswordError)
                        )
                )
                //.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message")
                        .value(
                               "The passwords do not match. Please try again.")
                );

    }
}
