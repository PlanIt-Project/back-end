package com.sideProject.PlanIT.domain.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.PlanIT.common.security.JwtAccessDeniedHandler;
import com.sideProject.PlanIT.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.config.SecurityConfig;
import com.sideProject.PlanIT.domain.reservation.dto.reqeust.ChangeReservationRequestDto;
import com.sideProject.PlanIT.domain.reservation.dto.reqeust.ReservationRequestDto;
import com.sideProject.PlanIT.domain.reservation.service.ReservationService;
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
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(
        controllers = ReservationController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class, // 추가
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@Import({
        SecurityConfig.class,
        JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class})
@ActiveProfiles("test")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private JwtUtil jwtUtil;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("예약을 생성한다.")
    @Test
    void changeReservation() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.TRAINER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ChangeReservationRequestDto requestDto = new ChangeReservationRequestDto(
                LocalDate.of(2024,4,24),
                List.of(LocalTime.of(10,0),LocalTime.of(11,0))
        );

        //when//then
        mockMvc.perform(
                        put("/reservation/change")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("예약을 생성시 예약 날짜가 주어지지 않으면 예외가 발생한다.")
    @Test
    void changeReservationWithoutReservationDate() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.TRAINER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ChangeReservationRequestDto requestDto = new ChangeReservationRequestDto(
                null,
                List.of(LocalTime.of(10,0),LocalTime.of(11,0))
        );

        //when//then
        mockMvc.perform(
                        put("/reservation/change")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("예약 날짜가 주어지지 않았습니다."));
    }

    @DisplayName("회원이 예약 가능하다")
    @Test
    void reservation() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ReservationRequestDto requestDto = new ReservationRequestDto(1L);

        //when//then
        mockMvc.perform(
                        post("/reservation/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("회원이 예약 가능하다")
    @Test
    void reservationWithProgramId() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ReservationRequestDto requestDto = new ReservationRequestDto(null);

        //when//then
        mockMvc.perform(
                        post("/reservation/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("프로그램이 주어지지 않았습니다."));
    }

    @DisplayName("트레이너의 예약을 조회할 수 있다.")
    @Test
    void findReservationByTrainer() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.TRAINER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/reservation")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("회원의 예약을 조회할 수 있다.")
    @Test
    void findReservationByUser() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/reservation")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("트레이너의 예약을 트레이너 ID로 조회할 수 있다.")
    @Test
    void findTrainerReservation() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.MEMBER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        //when//then
        mockMvc.perform(
                        get("/reservation/trainer/1")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("예약을 취소할 수 있다.")
    @Test
    void cancelReservation() throws Exception {
        //given
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(MemberRole.TRAINER.toString()));
        Authentication auth = new UsernamePasswordAuthenticationToken(1L, null, authorities);

        ChangeReservationRequestDto requestDto = new ChangeReservationRequestDto(
                null,
                List.of(LocalTime.of(10,0),LocalTime.of(11,0))
        );

        //when//then
        mockMvc.perform(
                        delete("/reservation/1")
                                .with(authentication(auth))
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}