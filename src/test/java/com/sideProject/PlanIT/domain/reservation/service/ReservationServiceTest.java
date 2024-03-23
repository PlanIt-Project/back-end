package com.sideProject.PlanIT.domain.reservation.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.repository.ProgramRepository;
import com.sideProject.PlanIT.domain.program.repository.RegistrationRepository;
import com.sideProject.PlanIT.domain.reservation.dto.response.ReservationResponse;
import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.reservation.entity.Reservation;
import com.sideProject.PlanIT.domain.reservation.repository.ReservationRepository;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class ReservationServiceTest {

    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationService reservationService;

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAllInBatch();
        programRepository.deleteAllInBatch();
        registrationRepository.deleteAllInBatch();
        employeeRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    private Product initProduct(String name, Period period, int number, ProductType type) {
        Product product = Product.builder()
                .name(name)
                .period(period)
                .number(number)
                .price(30000)
                .type(type)
                .build();
        return productRepository.save(product);
    }

    private Member initMember(String name, MemberRole role) {
        Member member = Member.builder()
                .name(name)
                .email(name + "test.com")
                .password("test123")
                .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                .phone_number("010-0000-0000")
                .role(role)
                .build();

        return memberRepository.save(member);
    }

    private Employee initTrainer(String name) {
        Member member = initMember(name, MemberRole.TRAINER);

        Employee employee = Employee.builder()
                .member(memberRepository.save(member))
                .build();

        return employeeRepository.save(employee);
    }

    //todo : 실패 케이스 처리해야함
    @Nested
    @DisplayName("addReservationTest")
    class AddReservationTest {
        @DisplayName("생성된 예약시간이 없으면 예약시간이 생성된다.")
        @Test
        void addReservation() {
            //given
            Employee trainer = initTrainer("trainer");

            LocalDateTime time1 = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
            LocalDateTime time2 = LocalDateTime.of(2021, 1, 1, 11, 0, 0);
            LocalDateTime time3 = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
            LocalDateTime time4 = LocalDateTime.of(2021, 1, 1, 13, 0, 0);

            List<LocalDateTime> times = List.of(time1, time2, time3, time4);
            //when
            String result = reservationService.changeAvailability(times, trainer.getId(),trainer.getMember().getId());
            List<Reservation> registrations = reservationRepository.findAll();
            //then
            assertThat(result).isEqualTo("ok");
            assertThat(registrations).hasSize(4);
            assertThat(registrations).extracting(
                    "reservedTime", "status"
            ).contains(
                    tuple(LocalDateTime.of(2021, 1, 1, 10, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 11, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 12, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 13, 0, 0), ReservationStatus.POSSIBLE)
            );

            assertThat(registrations)
                    .extracting((reservation) -> reservation.getEmployee().getId())
                    .containsExactly(trainer.getId(), trainer.getId(), trainer.getId(), trainer.getId());
        }

        @DisplayName("생성된 예약시간이 있으면 삭제하고 없으면 예약시간이 생성된다.")
        @Test
        void addReservation2() {
            //given
            Employee trainer = initTrainer("trainer");

            LocalDateTime time1 = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
            LocalDateTime time2 = LocalDateTime.of(2021, 1, 1, 11, 0, 0);
            LocalDateTime time3 = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
            LocalDateTime time4 = LocalDateTime.of(2021, 1, 1, 13, 0, 0);

            Reservation saveReservation = Reservation.builder()
                    .reservedTime(time1)
                    .employee(trainer)
                    .status(ReservationStatus.POSSIBLE)
                    .build();
            reservationRepository.save(saveReservation);

            List<LocalDateTime> times = List.of(time1, time2, time3, time4);
            //when
            String result = reservationService.changeAvailability(times, trainer.getId(),trainer.getMember().getId());
            List<Reservation> registrations = reservationRepository.findAll();
            //then
            assertThat(result).isEqualTo("ok");
            assertThat(registrations).hasSize(3);
            assertThat(registrations).extracting(
                    "reservedTime", "status"
            ).contains(
                    tuple(LocalDateTime.of(2021, 1, 1, 11, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 12, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 13, 0, 0), ReservationStatus.POSSIBLE)
            );

            assertThat(registrations)
                    .extracting((reservation) -> reservation.getEmployee().getId())
                    .containsExactly(trainer.getId(), trainer.getId(), trainer.getId());
        }

        @DisplayName("예약된 예약은 삭제되지 않는다")
        @Test
        void addReservation3() {
            //given
            Employee trainer = initTrainer("trainer");

            LocalDateTime time1 = LocalDateTime.of(2021, 1, 1, 10, 0, 0);
            LocalDateTime time2 = LocalDateTime.of(2021, 1, 1, 11, 0, 0);
            LocalDateTime time3 = LocalDateTime.of(2021, 1, 1, 12, 0, 0);
            LocalDateTime time4 = LocalDateTime.of(2021, 1, 1, 13, 0, 0);

            Reservation saveReservation = Reservation.builder()
                    .reservedTime(time1)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .build();
            reservationRepository.save(saveReservation);

            List<LocalDateTime> times = List.of(time1, time2, time3, time4);
            //when
            String result = reservationService.changeAvailability(times, trainer.getId(),trainer.getMember().getId());
            List<Reservation> registrations = reservationRepository.findAll();
            //then
            assertThat(result).isEqualTo("ok");
            assertThat(registrations).hasSize(4);
            assertThat(registrations).extracting(
                    "reservedTime", "status"
            ).contains(
                    tuple(LocalDateTime.of(2021, 1, 1, 10, 0, 0), ReservationStatus.RESERVED),
                    tuple(LocalDateTime.of(2021, 1, 1, 11, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 12, 0, 0), ReservationStatus.POSSIBLE),
                    tuple(LocalDateTime.of(2021, 1, 1, 13, 0, 0), ReservationStatus.POSSIBLE)
            );

            assertThat(registrations)
                    .extracting((reservation) -> reservation.getEmployee().getId())
                    .containsExactly(trainer.getId(), trainer.getId(), trainer.getId(), trainer.getId());
        }
    }

    @Nested
    @DisplayName("ReservationTest")
    class ReservationTest {

        @DisplayName("PT권을 등록한 회원은 지정된 트레이너에 예약 가능하다.")
        @Test
        void reservationTest(){
            //given
            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 10회", periodOfTenDays,10,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .remainedNumber(product.getNumber())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
            Reservation reservation = Reservation.builder()
                    .reservedTime(reservationTime)
                    .employee(trainer)
                    .status(ReservationStatus.POSSIBLE)
                    .classTime(LocalTime.of(1,0))
                    .build();
            reservationRepository.save(reservation);
            //when
            String result = reservationService.reservation(reservation.getId(), member1.getId(), program.getId());
            List<Reservation> reservation1 = reservationRepository.findAll();

            //then
            assertThat(result).isEqualTo("ok");
            assertThat(reservation1).hasSize(1);
            assertThat(reservation1).extracting(
                    "reservedTime", "status"
            ).contains(
                    tuple(reservationTime, ReservationStatus.RESERVED)
            );
            assertThat(reservation1.get(0).getMember().getId()).isEqualTo(member1.getId());
            assertThat(reservation1.get(0).getEmployee().getId()).isEqualTo(trainer.getId());
            assertThat(reservation1.get(0).getProgram().getId()).isEqualTo(program.getId());
        }

        @DisplayName("PT권으로 예약시 남은 횟수가 차감된다.")
        @Test
        void reservationTestMinusRemainNum(){
            //given
            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 10회", periodOfTenDays,10,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .remainedNumber(product.getNumber())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
            Reservation reservation = Reservation.builder()
                    .reservedTime(reservationTime)
                    .employee(trainer)
                    .status(ReservationStatus.POSSIBLE)
                    .classTime(LocalTime.of(1,0))
                    .build();
            reservationRepository.save(reservation);
            //when
            String result = reservationService.reservation(reservation.getId(), member1.getId(), program.getId());
            List<Reservation> reservation1 = reservationRepository.findAll();

            //then
            assertThat(result).isEqualTo("ok");
            assertThat(reservation1).hasSize(1);
            assertThat(reservation1).extracting(
                    "reservedTime", "status"
            ).contains(
                    tuple(reservationTime, ReservationStatus.RESERVED)
            );
            assertThat(reservation1.get(0).getMember().getId()).isEqualTo(member1.getId());
            assertThat(reservation1.get(0).getEmployee().getId()).isEqualTo(trainer.getId());
            assertThat(reservation1.get(0).getProgram().getId()).isEqualTo(program.getId());
            assertThat(reservation1.get(0).getProgram().getRemainedNumber()).isEqualTo(program.getRemainedNumber()-1);
        }

        @DisplayName("PT권을 등록한 회원은 지정된 트레이너가 아닌 사람에게 예약시 예외가 발생한다")
        @Test
        void reservationTest2(){
            //given
            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 10회", periodOfTenDays,10,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Employee trainer2 = initTrainer("trainer2");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .employee(trainer2)
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
            Reservation reservation = Reservation.builder()
                    .reservedTime(reservationTime)
                    .employee(trainer)
                    .status(ReservationStatus.POSSIBLE)
                    .classTime(LocalTime.of(1,0))
                    .build();
            reservationRepository.save(reservation);
            //when //then
            assertThatThrownBy(() -> reservationService.reservation(reservation.getId(), member1.getId(), program.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("유저 " + member1.getId()+ "은 해당 트레이너에 예약할 수 없습니다.");
        }

        @DisplayName("PT권을 등록하지 않은 회원은 예약시 예외가 발생한다")
        @Test
        void reservationTest3(){
            //given
            Period periodOfTenDays = Period.ofMonths(1);
            Product product = initProduct("30일 회원권", periodOfTenDays,0,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
            Reservation reservation = Reservation.builder()
                    .reservedTime(reservationTime)
                    .employee(trainer)
                    .status(ReservationStatus.POSSIBLE)
                    .classTime(LocalTime.of(1,0))
                    .build();
            reservationRepository.save(reservation);
            //when //then
            assertThatThrownBy(() -> reservationService.reservation(reservation.getId(), member1.getId(), program.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("program " + program.getId()+ " 은 PT권이 아닙니다.");
        }

        @DisplayName("이미 예약된 예약은 예약시 예외가 발생한다")
        @Test
        void reservationTest4(){
            //given
            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 30회권", periodOfTenDays,30,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .employee(trainer)
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            LocalDateTime reservationTime = LocalDateTime.of(2021, 1, 10, 10, 0, 0);
            Reservation reservation = Reservation.builder()
                    .reservedTime(reservationTime)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();
            reservation.reservation(program,member1);
            Reservation reservation1 = reservationRepository.save(reservation);

            //when //then
            assertThatThrownBy(() -> reservationService.reservation(reservation.getId(), member1.getId(), program.getId()))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("예약 " + reservation1.getId() + "은 예약할 수 없습니다.");
        }
    }

    //todo : 에러 처리 테스트 구현
    @Nested
    @DisplayName("FindReservationByMemberTest")
    class FindReservationTest {
        @DisplayName("회원의 이번주 예약을 확인할 수 있다.")
        @Test
        void findReservationByMemberTest(){
            //given

            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 30회권", periodOfTenDays,30,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2024-03-10 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-03-10 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .employee(trainer)
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2024-03-10", DateTimeFormatter.ISO_DATE))
                    .build();
            Program program1 = programRepository.save(program);

            LocalDateTime reservationTime1 = LocalDateTime.of(2024, 3, 18, 10, 0, 0);
            Reservation reservation1 = Reservation.builder()
                    .reservedTime(reservationTime1)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            LocalDateTime reservationTime2 = LocalDateTime.of(2024, 3, 19, 10, 0, 0);
            Reservation reservation2 = Reservation.builder()
                    .reservedTime(reservationTime2)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            LocalDateTime reservationTime3 = LocalDateTime.of(2024, 3, 17, 10, 0, 0);
            Reservation reservation3 = Reservation.builder()
                    .reservedTime(reservationTime3)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            reservation1.reservation(program1,member1);
            reservation2.reservation(program1,member1);
            reservation3.reservation(program1,member1);

            LocalDate today = LocalDate.of(2024, 3, 19);
            LocalDate today2 = LocalDate.of(2024, 3, 15);

            List<Reservation> reservations = List.of(reservation1,reservation2,reservation3);
            reservationRepository.saveAll(reservations);
            //when
            Map<LocalDate, List<ReservationResponse>>result1 = reservationService.findReservationForWeekByMember(today, member1.getId());
            Map<LocalDate, List<ReservationResponse>> result2 = reservationService.findReservationForWeekByMember(today2, member1.getId());


            //then
            assertThat(result1).hasSize(2);
            assertThat(result2).hasSize(1);
            assertThat(result1.get(LocalDate.of(2024, 3, 18)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime1, ReservationStatus.RESERVED
            );

            assertThat(result1.get(LocalDate.of(2024, 3, 19)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime2, ReservationStatus.RESERVED
            );

            assertThat(result2.get(LocalDate.of(2024, 3, 17)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime3, ReservationStatus.RESERVED
            );
        }

        @DisplayName("트레이너의 이번주 예약을 확인할 수 있다.")
        @Test
        void findReservationByEmployeeTest(){
            //given

            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 30회권", periodOfTenDays,30,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2024-03-10 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-03-10 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .employee(trainer)
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2024-03-10", DateTimeFormatter.ISO_DATE))
                    .build();
            Program program1 = programRepository.save(program);

            LocalDateTime reservationTime1 = LocalDateTime.of(2024, 3, 18, 10, 0, 0);
            Reservation reservation1 = Reservation.builder()
                    .reservedTime(reservationTime1)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            LocalDateTime reservationTime2 = LocalDateTime.of(2024, 3, 19, 10, 0, 0);
            Reservation reservation2 = Reservation.builder()
                    .reservedTime(reservationTime2)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            LocalDateTime reservationTime3 = LocalDateTime.of(2024, 3, 17, 10, 0, 0);
            Reservation reservation3 = Reservation.builder()
                    .reservedTime(reservationTime3)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            reservation1.reservation(program1,member1);
            reservation2.reservation(program1,member1);
            reservation3.reservation(program1,member1);

            LocalDate today = LocalDate.of(2024, 3, 19);
            LocalDate today2 = LocalDate.of(2024, 3, 15);

            List<Reservation> reservations = List.of(reservation1,reservation2,reservation3);
            List<Reservation> reservationList = reservationRepository.saveAll(reservations);
            //when
            Map<LocalDate, List<ReservationResponse>> result1 = reservationService.findReservationForWeekByMember(today, trainer.getMember().getId());
            Map<LocalDate, List<ReservationResponse>> result2 = reservationService.findReservationForWeekByMember(today2, trainer.getMember().getId());

            //then
            assertThat(result1).hasSize(2);
            assertThat(result2).hasSize(1);
            assertThat(result1.get(LocalDate.of(2024, 3, 18)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime1, ReservationStatus.RESERVED
            );

            assertThat(result1.get(LocalDate.of(2024, 3, 19)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime2, ReservationStatus.RESERVED
            );

            assertThat(result2.get(LocalDate.of(2024, 3, 17)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime3, ReservationStatus.RESERVED
            );
        }

        @DisplayName("트레이너의 이번주 예약을 확인할 수 있다.")
        @Test
        void findEmptyReservation(){
            //given
            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 30회권", periodOfTenDays,30,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2024-03-10 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-03-10 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .employee(trainer)
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2024-03-10", DateTimeFormatter.ISO_DATE))
                    .build();
            Program program1 = programRepository.save(program);

            LocalDateTime reservationTime1 = LocalDateTime.of(2024, 3, 18, 10, 0, 0);
            Reservation reservation1 = Reservation.builder()
                    .reservedTime(reservationTime1)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            LocalDateTime reservationTime2 = LocalDateTime.of(2024, 3, 19, 10, 0, 0);
            Reservation reservation2 = Reservation.builder()
                    .reservedTime(reservationTime2)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            reservation1.reservation(program1,member1);
            reservation2.reservation(program1,member1);

            LocalDate today = LocalDate.of(2024, 3, 19);
            LocalDate today2 = LocalDate.of(2024, 3, 15);

            List<Reservation> reservations = List.of(reservation1,reservation2);
            List<Reservation> reservationList = reservationRepository.saveAll(reservations);
            //when
            Map<LocalDate, List<ReservationResponse>> result1 = reservationService.findReservationForWeekByMember(today, trainer.getMember().getId());
            Map<LocalDate, List<ReservationResponse>> result2 = reservationService.findReservationForWeekByMember(today2, trainer.getMember().getId());


            //then
            assertThat(result1).hasSize(2);
            assertThat(result2).hasSize(0);
            assertThat(result1.get(LocalDate.of(2024, 3, 18)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime1, ReservationStatus.RESERVED
            );

            assertThat(result1.get(LocalDate.of(2024, 3, 19)).get(0)).extracting(
                    "programId","reservationTime", "status"
            ).containsExactly(
                    program1.getId(),reservationTime2, ReservationStatus.RESERVED
            );

        }
    }

    @Nested
    @DisplayName("CancelTest")
    class CancelTest {
        @DisplayName("")
        @Test
        void test(){
            //given
            Period periodOfTenDays = Period.ofMonths(0);
            Product product = initProduct("PT 30회권", periodOfTenDays,30,ProductType.PT);
            Employee trainer = initTrainer("trainer");
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2024-03-10 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-03-10 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .employee(trainer)
                    .remainedNumber(product.getNumber()-1)
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2024-03-10", DateTimeFormatter.ISO_DATE))
                    .build();
            Program program1 = programRepository.save(program);

            LocalDateTime reservationTime1 = LocalDateTime.of(2024, 3, 18, 10, 0, 0);
            Reservation reservation1 = Reservation.builder()
                    .reservedTime(reservationTime1)
                    .employee(trainer)
                    .status(ReservationStatus.RESERVED)
                    .classTime(LocalTime.of(1,0))
                    .build();

            reservation1.reservation(program1,member1);

            Reservation reservation = reservationRepository.save(reservation1);
            //when
            String result = reservationService.cancel(reservation.getMember().getId(),reservation.getId());
            Reservation resultReservation = reservationRepository.findById(reservation.getId()).orElseThrow();
            Program resultProgram = programRepository.findById(reservation.getProgram().getId()).orElseThrow();

            //then
            assertThat(result).isEqualTo("ok");
            assertThat(resultReservation).extracting(
                    "reservedTime", "status"
            ).containsExactly(
                    reservationTime1, ReservationStatus.POSSIBLE
            );
            assertThat(resultProgram.getRemainedNumber()).isEqualTo(product.getNumber());

        }
    }
}