package backend.security.controllers.user;

import backend.security.SecurityApplication;
import backend.security.models.user.Role;
import backend.security.models.user.User;
import backend.security.models.user.UserEditRequest;
import backend.security.repositories.user.UserRepository;
import backend.security.services.auth.AuthenticationService;
import backend.security.services.auth.JwtService;
import backend.security.services.user.UserDetailsServiceImplementation;
import backend.security.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserInfoController.class)
@ContextConfiguration(classes = {SecurityApplication.class})
@AutoConfigureMockMvc(addFilters = true)
@ActiveProfiles("test")
class UserInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

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

    UserEditRequest userEditRequest = UserEditRequest.builder()
            .surname("Update")
            .givenName("Testy")
            .email("updated@gmx.com")
            .build();

    @Test
    @WithMockUser(authorities = {"USER", }, username = "TestUser")
    void authenticatedUserInfo() throws Exception {
        when(userDetailsService.loadUserByUsername("TestUser"))
                .thenReturn(testUser);
        mockMvc.perform(get("/api/user/authenticated")
                        .contentType("application/json")
                )
                .andDo(print())
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


    @Test
    @WithMockUser(username = "TestUser", authorities = {"USER", })
    void editUserInfo() throws Exception {
        when(userDetailsService.loadUserByUsername("TestUser"))
                .thenReturn(testUser);
        testUser.setEmail(userEditRequest.getEmail());
        testUser.setSurname(userEditRequest.getSurname());
        testUser.setGivenName(userEditRequest.getGivenName());
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userDetailsService.editUserInformation(userEditRequest, testUser))
                .thenReturn(testUser);
        MockHttpServletRequestBuilder request = post(
                "/api/user/edit")
                .contentType("application/json")
                .with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(
                        userEditRequest)
                );
        mockMvc.perform(request)
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("givenName")
                        .value(
                                "Testy"
                        )
                )
                .andExpect(jsonPath("surname")
                        .value(
                                "Update"
                        )
                )
                .andExpect(jsonPath("email")
                        .value(
                                "updated@gmx.com"
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
