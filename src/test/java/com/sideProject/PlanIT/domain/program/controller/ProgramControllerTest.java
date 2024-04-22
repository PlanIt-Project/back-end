package com.sideProject.PlanIT.domain.program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.PlanIT.common.security.JwtAccessDeniedHandler;
import com.sideProject.PlanIT.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.config.SecurityConfig;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ProgramController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@Import({
        SecurityConfig.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class})
@ActiveProfiles("test")
class ProgramControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProgramService programService;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    WebApplicationContext wac;

    @DisplayName("회원은 프로그램 신청 요청을 할 수 있다.")
    @Test
    void registration() throws Exception {
        //given
        Long productID = 1L;
        LocalDate registerDate = LocalDate.now();

        RegistrationRequestDto requestDto = new RegistrationRequestDto(
                productID,
                1L,
                registerDate
        );

        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when //then
        mockMvc.perform(
                post("/program/registration")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("회원은 프로그램 신청 요청할 때 상품ID는 필수다.")
    @Test
    void registrationWithoutProductId() throws Exception {
        //given
        LocalDate registerDate = LocalDate.now();

        RegistrationRequestDto requestDto = new RegistrationRequestDto(
                null,
                1L,
                registerDate
        );

        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when //then
        mockMvc.perform(
                        post("/program/registration")
                                .with(authentication(auth))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("상품 아이디가 없습니다"));

    }

    @DisplayName("회원은 프로그램 신청 요청할 때 등록 날짜는 필수다.")
    @Test
    void registrationWithoutRegistrationDate() throws Exception {
        //given
        Long productID = 1L;
        LocalDate registerDate = LocalDate.now();

        RegistrationRequestDto requestDto = new RegistrationRequestDto(
                productID,
                1L,
                null
        );

        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when //then
        mockMvc.perform(
                        post("/program/registration")
                                .with(authentication(auth))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("등록 날짜가 없습니다"))
        ;

    }

    @DisplayName("유저가 등록된 프로그램을 조회한다.")
    @Test
    void findProgram() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/program")
                                .with(authentication(auth))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @DisplayName("유저가 등록된 프로그램을 programID로 조회한다.")
    @Test
    void findProgramById() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/program/1")
                                .with(authentication(auth))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @DisplayName("유저가 등록을 조회 가능하다.")
    @Test
    void findRegistration() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/program/registration")
                                .with(authentication(auth))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }



}