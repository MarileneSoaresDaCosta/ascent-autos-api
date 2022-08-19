package com.example.autos;

import com.example.autos.security.JwtProperties;
import com.example.autos.security.TokenHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
@TestPropertySource(locations= "classpath:application-test.properties")
public class HelloControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtProperties jwtProperties;

    TokenHelper tokenHelper;

    @BeforeEach
    void setUp(){
        tokenHelper = new TokenHelper(jwtProperties);
    }

    @Test
    void sayHello(){
    }

    @WithMockUser(username= "TEST-USER", roles = "USER")
    @Test
    void getTokenInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sayHiToAdminByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                .header("Authorization",
                        tokenHelper.getToken("TEST-ADMIN-USER",
                                Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
