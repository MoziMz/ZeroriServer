package com.mozi.moziserver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class SecurityBasedTest {

    @Autowired
    MockMvc mvc;

    @Test
    void 인증된_사용자가_아니면_UNAUTHORIZED_STATUS() throws Exception{
        mvc
            .perform(get("/api/v1/users/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@naver.com",roles = "USER")
    void 인증된_사용자이면_OK_STATUS() throws Exception{
        mvc
            .perform(get("/api/v1/boards"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@naver.com", roles = "ADMIN")
    void admin_ROLE로_user_API_요청하면_FORBIDDEN_STATUS() throws Exception{
        mvc
            .perform(get("/api/v1/boards"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@naver.com", roles = "ADMIN")
    void admin_ROLE로_admin_API_요청하면_OK_STATUS() throws Exception{
        mvc
            .perform(get("/api/admin/boards"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@naver.com", roles = "USER")
    void user_ROLE로_admin_API_요청하면_FORBIDDEN_STATUS() throws Exception{
        mvc
            .perform(get("/api/admin/boards"))
            .andExpect(status().isForbidden());
    }
}
