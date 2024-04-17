package com.sideProject.PlanIT.domain.program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.PlanIT.common.security.JwtAccessDeniedHandler;
import com.sideProject.PlanIT.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.config.SecurityConfig;
import com.sideProject.PlanIT.domain.controller_test_module.WithMockCustomMember;
import com.sideProject.PlanIT.domain.program.dto.request.ApproveRequestDto;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequestDto;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequestDto;
import com.sideProject.PlanIT.domain.program.service.ProgramService;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;


import java.util.Collection;
import java.util.Collections;

import static javax.swing.UIManager.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("모든 프로그램을 조회한다.")
    @Test
    void findProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                get("/admin/program")
                        .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("유저 아이디로 유저의 프로그램을 조회한다.")
    @Test
    void findProgramByUser() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/admin/program/by-user/1")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @DisplayName("프로그램 ID로 프로그램을 조회한다.")
    @Test
    void findProgramByProgramId() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/admin/program/1")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @DisplayName("모든 등록요청을 조회 가능하다.")
    @Test
    void findReservation() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/admin/program/registration")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("프로그램을 환불 가능하다.")
    @Test
    void deleteProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        delete("/admin/program/1")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("유저 아이디로 유저의 등록요청을 조회 가능하다.")
    @Test
    void findReservationByUserId() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/admin/program/registration/user/1")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("프로그램을 수정 가능하다")
    @Test
    void putProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ProgramModifyRequestDto requestDto = new ProgramModifyRequestDto(
                "10:00",
                "13:00",
                1L,
                1L
        );

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/admin/program/1")
                                .with(authentication(auth))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))

                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("프로그램을 일시정지 가능하다")
    @Test
    void resumeProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ProgramModifyRequestDto requestDto = new ProgramModifyRequestDto(
                "10:00",
                "13:00",
                1L,
                1L
        );

        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/admin/program/1/resume")
                                .with(authentication(auth))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))

                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("프로그램을 일시정지 가능하다")
    @Test
    void suspendProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);


        //when//then
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/admin/program/1/suspend")
                                .with(authentication(auth))
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("프로그램을 승인 가능하다")
    @Test
    void approveProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ApproveRequestDto requestDto = new ApproveRequestDto(
                1L
        );

        //when//then
        mockMvc.perform(
                        post("/admin/program/approve/1")
                                .with(authentication(auth))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))

                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("모든 등록을 조회 가능하다.")
    @Test
    void findRegistration() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.ADMIN.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/admin/program/registration")
                                .with(authentication(auth)
                                )
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}