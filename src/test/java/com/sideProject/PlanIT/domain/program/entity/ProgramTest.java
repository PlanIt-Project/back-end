package com.sideProject.PlanIT.domain.program.entity;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


class ProgramTest {
    @DisplayName("프로그램 상태를 시작으로 바꾼다")
    @Test
    void changeToInProgressTest(){
        //given
        Program program = Program.builder()
                .remainedNumber(0)
                .status(ProgramStatus.NOT_STARTED)
                .build();
        program.changeToInProgress();
        //then
        assertThat(program.status).isEqualTo(ProgramStatus.IN_PROGRESS);
    }

    @DisplayName("프로그램 상태를 시작으로 바꾼다")
    @Test
    void changeToExpiredTest(){
        //given
        Program program = Program.builder()
                .remainedNumber(0)
                .status(ProgramStatus.IN_PROGRESS)
                .build();
        program.changeToExpired();
        //then
        assertThat(program.status).isEqualTo(ProgramStatus.EXPIRED);
    }

    @DisplayName("프로그램 상태를 시작으로 바꾼다")
    @Test
    void changeToRefundTest(){
        //given
        Program program = Program.builder()
                .remainedNumber(0)
                .status(ProgramStatus.IN_PROGRESS)
                .build();
        program.changeToRefund();
        //then
        assertThat(program.status).isEqualTo(ProgramStatus.REFUND);
    }

    @DisplayName("프로그램 상태를 일시정지 바꾼다")
    @Test
    void suspendProgram(){
        //given
        Program program = Program.builder()
                .remainedNumber(0)
                .status(ProgramStatus.IN_PROGRESS)
                .build();
        LocalDate now = LocalDate.now();
        program.suspendProgram(now);
        //then
        assertThat(program.status).isEqualTo(ProgramStatus.SUSPEND);
        assertThat(program.getSuspendAt()).isEqualTo(now);
    }

    @DisplayName("프로그램 상태를 재시작 상태로 바꾼다")
    @Test
    void resumeProgram(){
        //given
        Program program = Program.builder()
                .remainedNumber(0)
                .status(ProgramStatus.IN_PROGRESS)
                .build();
        LocalDate now = LocalDate.now();
        LocalDate endAt = LocalDate.now().plusDays(5);

        program.resumeProgram(now,endAt);
        //then
        assertThat(program.status).isEqualTo(ProgramStatus.IN_PROGRESS);
        assertThat(program.getResumeAt()).isEqualTo(now);
        assertThat(program.getEndAt()).isEqualTo(endAt);
    }

    @DisplayName("PT인 프로그램은 예약 가능하다")
    @Test
    void reservation1(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.PT)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(10)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        program.reservation();
        //then
        assertThat(program.getRemainedNumber()).isEqualTo(9);
    }

    @DisplayName("프로그램 남은횟수가 0이되면 만료된다")
    @Test
    void reservation2(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.PT)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(1)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        program.reservation();
        //then
        assertThat(program.getRemainedNumber()).isEqualTo(0);
        assertThat(program.getStatus()).isEqualTo(ProgramStatus.EXPIRED);
    }

    @DisplayName("PT인 프로그램은 예약 가능하다")
    @Test
    void reservation3(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.PT)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(0)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        //then
        assertThatThrownBy(() -> program.reservation())
                .isInstanceOf(CustomException.class)
                .hasMessage("null의 남은 횟수가 없습니다");
    }

    @DisplayName("PT가 아닌 프로그램은 예약을 할 수 없다")
    @Test
    void reservation4(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.MEMBERSHIP)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(0)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        //then
        assertThatThrownBy(() -> program.reservation())
                .isInstanceOf(CustomException.class)
                .hasMessage("program null 은 PT권이 아닙니다.");
    }


    @DisplayName("예약을 취소하면 잔여 횟수가 추가된다")
    @Test
    void cancelReservation1(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.PT)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(1)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        program.cancelReservation();
        //then
        assertThat(program.getRemainedNumber()).isEqualTo(2);
    }

    @DisplayName("PT가 아니면 예약 취소를 할 수 없다.")
    @Test
    void cancelReservation2(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.MEMBERSHIP)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(1)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        //then
        assertThatThrownBy(() -> program.cancelReservation())
                .isInstanceOf(CustomException.class)
                .hasMessage("null은 PT권이 아닙니다.");
    }

    @DisplayName("프로그램 횟수 이상으로 예약 취소가 불가능하다")
    @Test
    void cancelReservation3(){
        //given
        Product product = Product.builder()
                .number(10)
                .type(ProductType.PT)
                .build();

        Program program = Program.builder()
                .product(product)
                .remainedNumber(10)
                .status(ProgramStatus.IN_PROGRESS)
                .build();

        //then
        assertThatThrownBy(() -> program.cancelReservation())
                .isInstanceOf(CustomException.class)
                .hasMessage("null는 예약취소가 불가능합니다.");
    }

}