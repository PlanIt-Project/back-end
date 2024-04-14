package com.sideProject.PlanIT.domain.program.controller;

import com.sideProject.PlanIT.common.security.JwtAccessDeniedHandler;
import com.sideProject.PlanIT.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.config.SecurityConfig;
import com.sideProject.PlanIT.domain.controller_test_module.WithMockCustomMember;
import com.sideProject.PlanIT.domain.program.service.ProgramService;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(
        controllers = ProgramAdminController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@Import({
        SecurityConfig.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class})
@ActiveProfiles("test")
class ProgramAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProgramService programService;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    WebApplicationContext wac;


    @DisplayName("모든 프로그램을 조회한다.")
    @Test
    @WithMockCustomMember(first="name",second = MemberRole.ADMIN)
    void findProgram() throws Exception {
        //given

        //when//then
        mockMvc.perform(
                get("/admin/program")
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}