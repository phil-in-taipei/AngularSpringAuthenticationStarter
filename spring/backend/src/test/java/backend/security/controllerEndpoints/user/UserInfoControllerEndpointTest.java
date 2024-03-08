package backend.security.controllerEndpoints.user;

import backend.security.SecurityApplication;
import backend.security.models.user.UserEditRequest;
import backend.security.utils.TestUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Order;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = SecurityApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class UserInfoControllerEndpointTest {

    @Autowired
    MockMvc mockMvc;

    UserEditRequest userEditRequest = UserEditRequest.builder()
            .surname("Testy")
            .givenName("Update")
            .email("updated@gmx.com")
            .build();

    UserEditRequest userReturnToNormalRequest = UserEditRequest.builder()
            .surname("User")
            .givenName("Test")
            .email("test@gmx.com")
            .build();

    @Test
    @Order(1)
    @WithUserDetails("TestUser")
    void authenticatedUserInfo() throws Exception {
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

    @Test
    @Order(2)
    @WithUserDetails("TestUser")
    void editUserInfo() throws Exception {
        mockMvc.perform(patch("/api/user/edit")
                        .contentType("application/json")
                        .content(TestUtil.convertObjectToJsonBytes(
                                userEditRequest)
                        )
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("givenName")
                        .value(
                                "Update"
                        )
                )
                .andExpect(jsonPath("surname")
                        .value(
                                "Testy"
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
        mockMvc.perform(post("/api/user/edit")
                .contentType("application/json")
                .content(TestUtil.convertObjectToJsonBytes(
                        userReturnToNormalRequest)
                )
        );
    }
}
