package backend.security.controllers.user;

import backend.security.SecurityApplication;
import backend.security.models.user.Role;
import backend.security.models.user.User;
import backend.security.repositories.user.UserRepository;
import backend.security.services.auth.AuthenticationService;
import backend.security.services.auth.JwtService;
import backend.security.services.user.UserDetailsServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserInfoController.class)
@ContextConfiguration(classes = {SecurityApplication.class})
@AutoConfigureMockMvc(addFilters = true)
@ActiveProfiles("test")
class UserInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsServiceImplementation userDetailsService;

    @MockBean
    UserRepository userRepository;

    User testUser = User.builder()
            .givenName("Test")
            .surname("User")
            .username("TestUser")
            .email("test@gmx.com")
            .password("testpassword")
            .role(Role.USER)
            .build();


    @Test
    @WithMockUser(authorities = {"USER", }, username = "TestUser")
    void authenticatedUserInfo() throws Exception {
        when(userDetailsService.loadUserByUsername("TestUser"))
                .thenReturn(testUser);
        mockMvc.perform(get("/api/user/authenticated")
                        .contentType("application/json")
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("givenName")
                        .value(
                                "Test"
                        )
                )
                .andExpect(jsonPath("surname")
                        .value(
                                "User"
                        )
                )
                .andExpect(jsonPath("email")
                        .value(
                                "test@gmx.com"
                        )
                )
                .andExpect(jsonPath("role")
                        .value(
                                "USER"
                        )
                )
                .andExpect(jsonPath("username")
                        .value(
                                "TestUser"
                        )
                );
    }
}
