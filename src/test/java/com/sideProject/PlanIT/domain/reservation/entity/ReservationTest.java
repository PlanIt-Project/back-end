package com.sideProject.PlanIT.domain.reservation.entity;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Profile("dev")
class ReservationTest {

    private Product initProduct(String name, Period period, int number, ProductType type) {
        return Product.builder()
                .name(name)
                .period(period)
                .number(number)
                .price(30000)
                .type(type)
                .build();
    }

    private Member initMember(String name, MemberRole role) {
        return Member.builder()
                .name(name)
                .email(name + "test.com")
                .password("test123")
                .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                .phone_number("010-0000-0000")
                .role(role)
                .build();


    }

    private Employee initTrainer(String name) {
        Member member = initMember(name, MemberRole.TRAINER);

        return Employee.builder()
                .member(member)
                .build();
    }
    @DisplayName("예약")
    @Test
    void reservationTest(){
        //given
        Member member = initMember("test",MemberRole.MEMBER);
        Employee trainer = initTrainer("trainer");
        Program program = Program.builder()
                .build();

        LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
        Reservation reservation = Reservation.builder()
                .reservedTime(reservationTime)
                .employee(trainer)
                .status(ReservationStatus.POSSIBLE)
                .classTime(LocalTime.of(1,0))
                .build();

        LocalDateTime reservationTime1 = LocalDateTime.of(2021, 1, 10, 9, 49, 59);
        //when
        reservation.reservation(program,member,reservationTime1);

        //then
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(reservation.getMember()).isEqualTo(member);
    }

    @DisplayName("예약 가능 시간이 지나서 예약을 하면 예외가 발생한다")
    @Test
    void reservationTest2(){
        //given
        Member member = initMember("test",MemberRole.MEMBER);
        Employee trainer = initTrainer("trainer");
        Program program = Program.builder()
                .build();

        LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
        Reservation reservation = Reservation.builder()
                .reservedTime(reservationTime)
                .employee(trainer)
                .status(ReservationStatus.POSSIBLE)
                .classTime(LocalTime.of(1,0))
                .build();

        LocalDateTime reservationTime1 = LocalDateTime.of(2021, 1, 10, 9, 50, 50);
        //when

        //then
        assertThatThrownBy(() -> reservation.reservation(program,member,reservationTime1))
                .isInstanceOf(CustomException.class)
                .hasMessage("예약 null은 예약 가능 시간이 지났습니다.");
    }

    @DisplayName("예약 되어있으면 예외가 발생한다.")
    @Test
    void reservationTest3(){
        //given
        Member member = initMember("test",MemberRole.MEMBER);
        Employee trainer = initTrainer("trainer");
        Program program = Program.builder()
                .build();

        LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
        Reservation reservation = Reservation.builder()
                .reservedTime(reservationTime)
                .employee(trainer)
                .status(ReservationStatus.RESERVED)
                .classTime(LocalTime.of(1,0))
                .build();

        LocalDateTime reservationTime1 = LocalDateTime.of(2021, 1, 10, 9, 50, 50);
        //when

        //then
        assertThatThrownBy(() -> reservation.reservation(program,member,reservationTime1))
                .isInstanceOf(CustomException.class)
                .hasMessage("예약 null은 예약할 수 없습니다.");
    }

    @DisplayName("예약을 취소할 수 있다.")
    @Test
    void cancelTes1(){
        //given
        Member member = initMember("test",MemberRole.MEMBER);
        Employee trainer = initTrainer("trainer");
        Program program = Program.builder()
                .build();

        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .reservedTime(now)
                .employee(trainer)
                .status(ReservationStatus.RESERVED)
                .classTime(LocalTime.of(1,0))
                .build();

        LocalDateTime reservationTime = now.minusMinutes(10);
        //when
        reservation.cancel(reservationTime);

        //then
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.POSSIBLE);
        assertThat(reservation.getMember()).isEqualTo(null);
    }

    @DisplayName("예약 취소 시간이 예약 시간 10분 이내이면 예외가 발생한다.")
    @Test
    void cancelTest2(){
        //given
        Member member = initMember("test",MemberRole.MEMBER);
        Employee trainer = initTrainer("trainer");
        Program program = Program.builder()
                .build();

        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .reservedTime(now)
                .employee(trainer)
                .status(ReservationStatus.RESERVED)
                .classTime(LocalTime.of(1,0))
                .build();

        LocalDateTime reservationTime = now.minusMinutes(9);
        //when//then

        assertThatThrownBy(() -> reservation.cancel(reservationTime))
                .isInstanceOf(CustomException.class)
                .hasMessage("예약 null은 예약 취소시간이 지났습니다.");
    }

}