package com.sideProject.PlanIT.domain.program.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponse;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import com.sideProject.PlanIT.domain.program.repository.ProgramRepository;
import com.sideProject.PlanIT.domain.program.repository.RegistrationRepository;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequest;
import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus.EXPIRED;
import static com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus.IN_PROGRESS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class ProgramServiceTest {

    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProgramService programService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RegistrationRepository registrationRepository;
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
    @AfterEach
    void tearDown() {
        programRepository.deleteAllInBatch();
        registrationRepository.deleteAllInBatch();
        employeeRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    private Product initProduct(String name, String period, int number, ProductType type) {
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

    @Nested
    @DisplayName("modifyProgramTest")
    class modifyProgramTest {
        @DisplayName("등록된 프로그램을 수정한다.")
        @Test
        void modifyProgram(){
            //given
            Product product = initProduct("pt 30회권", "30",0,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            Program beforeProgram = programRepository.save(program);

            //변경할 멤버들
            Employee trainer2 = initTrainer("employee2@test.com");
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            //when
            ProgramModifyRequest request = ProgramModifyRequest.builder()
                    .employId(trainer2.getId())
                    .memberId(member2.getId())
                    .startTime("2023-03-06")
                    .endTime("2023-04-06")
                    .build();

            String result = programService.modify(beforeProgram.getId(), request);
            Program afterProgram = programRepository.findById(beforeProgram.getId()).orElseThrow();

            //then
            assertThat(result).isEqualTo("OK");
            //변경된 직원 확인
            assertThat(afterProgram.getEmployee().getId()).isEqualTo(trainer2.getId());
            //변경된 회원 확인
            assertThat(afterProgram.getMember().getId()).isEqualTo(member2.getId());
            assertThat(afterProgram)
                    .extracting("startAt","endAt")
                    .contains(
                            LocalDate.parse("2023-03-06", DateTimeFormatter.ISO_DATE),
                            LocalDate.parse("2023-04-06", DateTimeFormatter.ISO_DATE)
                    );
        }


        @DisplayName("실패 : 등록되지 않은 프로그램을 수정할 때 예외가 발생한다")
        @Test
        void modifyProgram2(){
            //given //when
            ProgramModifyRequest request = ProgramModifyRequest.builder()
                    .employId(1L)
                    .memberId(1L)
                    .startTime("2023-03-06")
                    .endTime("2023-04-06")
                    .build();


            //then
            assertThatThrownBy(() -> programService.modify(1L, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("등록되지 않은 프로그램입니다.");
        }

        @DisplayName("실패 : 프로그램을 수정할 때 존재하지 않는 회원으로 수정하면 예외가 발생한다")
        @Test
        void modifyProgram3(){
            //given //when
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);
            log.info("memberId = {}",member.getId());

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            Program result = programRepository.save(program);

            ProgramModifyRequest request = ProgramModifyRequest.builder()
                    .employId(1L)
                    .memberId(member.getId()+1)
                    .startTime("2023-03-06")
                    .endTime("2023-04-06")
                    .build();


            //then
            assertThatThrownBy(() -> programService.modify(result.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("존재하지 않는 회원입니다.");
        }

        @DisplayName("실패 : 프로그램을 수정할 때 존재하지 않는 트레이너로 수정하면 예외가 발생한다")
        @Test
        void modifyProgram4(){
            //given //when
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);
            log.info("memberId = {}",member.getId());

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            Program result = programRepository.save(program);;

            ProgramModifyRequest request = ProgramModifyRequest.builder()
                    .employId(trainer.getId()+1)
                    .memberId(member.getId())
                    .startTime("2023-03-06")
                    .endTime("2023-04-06")
                    .build();


            //then
            assertThatThrownBy(() -> programService.modify(result.getId(), request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("존재하지 않는 직원입니다.");
        }
    }

    @Nested
    @DisplayName("refundProgramTest")
    class refundProgramTest {
        @DisplayName("등록한 프로그램을 환불한다")
        @Test
        void refundProgram(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            Program beforeProgram = programRepository.save(program);

            //when
            String result = programService.refund(beforeProgram.getId(),localDateTime);
            Program afterProgram = programRepository.findById(beforeProgram.getId()).orElseThrow();
            Registration afterRegistration = registrationRepository.findById(beforeProgram.getRegistration().getId()).orElseThrow();

            //then
            assertThat(result).isEqualTo("OK");
            assertThat(afterProgram.getStatus()).isEqualTo(ProgramStatus.REFUND);
            assertThat(afterRegistration.getStatus()).isEqualTo(RegistrationStatus.REFUND);
            assertThat(afterRegistration.getRefundAt()).isEqualTo(localDateTime);
        }

        //환불 요청
        @DisplayName("실패 : 이미 환불 된 프로그램을 환불하면 예외가 발생한다.")
        @Test
        void refundProgramAlreadyRefunded(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.REFUND)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .status(ProgramStatus.REFUND)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            Program beforeProgram = programRepository.save(program);


            //then
            assertThatThrownBy(() -> programService.refund(beforeProgram.getId(), localDateTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 환불된 프로그램입니다.");
        }

        @DisplayName("실패 : 등록되지 않은 프로그램을 환불시 예외가 발생한다.")
        @Test
        void refundProgram2(){
            LocalDateTime localDateTime = LocalDateTime.now();
            //then
            assertThatThrownBy(() -> programService.refund(1L,localDateTime))
                    .isInstanceOf(CustomException.class).hasMessage("등록되지 않은 프로그램입니다.");
        }
    }

    @Nested
    @DisplayName("approveProgramTest")
    class approveProgramTest {
        @DisplayName("등록 요청된 프로그램을 트레이너와 매칭하여 등록한다.")
        @Test
        void approveProgram(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "30",30,ProductType.PT);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1" ,MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            long result = programService.approve(saveRegistration.getId(),trainer.getId(),localDateTime);
            Program program = programRepository.findById(result).orElseThrow();

            //then
            assertThat(program.getRegistration().getId()).isEqualTo(saveRegistration.getId());
            assertThat(program.getStartAt()).isEqualTo(saveRegistration.getRegistrationAt().toLocalDate()); //등록일 확인
            assertThat(program.getEmployee().getId()).isEqualTo(trainer.getId()); //등록된 트레이너 확인
        }

        @DisplayName("30일 회원권을 등록하면 등록 날짜에서 30일 후 까지의 프로그램을 생성한다")
        @Test
        void approveProgramBy30DaysProgram(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("30일권", "30",0,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            long result = programService.approve(saveRegistration.getId(),trainer.getId(),localDateTime);
            Program program = programRepository.findById(result).orElseThrow();

            //then
            assertThat(program.getRegistration().getId()).isEqualTo(saveRegistration.getId());
            assertThat(program.getEmployee()).isNull();
            assertThat(program.getStartAt()).isEqualTo(saveRegistration.getRegistrationAt().toLocalDate()); //등록일 확인
            assertThat(program.getEndAt()).isEqualTo(saveRegistration.getRegistrationAt().toLocalDate().plusDays(30)); //등록일 확인
            assertThat(program.getRemainedNumber()).isEqualTo(0); //등록일 확인
        }

        @DisplayName("30회 pt권을 등록하면 남은 횟수가 30회이다")
        @Test
        void approveProgramBy30NumberPtProgram(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "0",30,ProductType.PT);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            long result = programService.approve(saveRegistration.getId(),trainer.getId(),localDateTime);
            Program program = programRepository.findById(result).orElseThrow();

            log.info("program endTime = {}",program.getEndAt());
            //then
            assertThat(program.getRegistration().getId()).isEqualTo(saveRegistration.getId());
            assertThat(program.getStartAt()).isEqualTo(saveRegistration.getRegistrationAt().toLocalDate()); //등록일 확인
            assertThat(program.getEndAt()).isNull(); //등록일 확인
            assertThat(program.getRemainedNumber()).isEqualTo(30); //등록일 확인
        }

        @DisplayName("실패 : 요청한 등록으로 이미 프로그램이 등록되어 있으면 예외가 발생한다")
        @Test
        void ApproveProgram1(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "30",30,ProductType.PT);
            Employee trainer = initTrainer("employee1@test.com");
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);

            Program program = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration)
                    .product(saveRegistration.getProduct())
                    .member(saveRegistration.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            //when //then
            assertThatThrownBy(() -> programService.approve(saveRegistration.getId(),trainer.getId(),localDateTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 등록된 프로그램입니다.");
        }

        @DisplayName("실패 : 존재하지 않는 트레이너로 등록시 예외가 발생한다")
        @Test
        void approveProgram3(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "30",30,ProductType.PT);
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);


            //then
            assertThatThrownBy(() -> programService.approve(saveRegistration.getId(),1L,localDateTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("존재하지 않는 직원입니다.");
        }

        @DisplayName("실패 : pt권에서 트레이너를 선택하지 않고 등록시 예외가 발생한다.")
        @Test
        void approveProgram4(){
            //given
            LocalDateTime localDateTime = LocalDateTime.now();

            Product product = initProduct("pt 30회권", "30",30,ProductType.PT);
            Member member = initMember("tester1",MemberRole.MEMBER);

            Registration registration = Registration.builder()
                    .product(product)
                    .member(member)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(localDateTime)
                    .build();
            Registration saveRegistration = registrationRepository.save(registration);


            //then
            assertThatThrownBy(() -> programService.approve(saveRegistration.getId(),null,localDateTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("파라미터 값을 확인해주세요.");
        }
    }

    @Nested
    @DisplayName("findProgramTest")
    class findProgramTest {
        @DisplayName("어드민 유저는 진행 중인 모든 프로그램을 조회가능하다")
        @Test
        void findAllInProcessProgram(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");
            Employee trainer2 = initTrainer("employee2");
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Member member3 = Member.builder()
                    .name("admin")
                    .email("admin@admin.com")
                    .password("test123")
                    .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .phone_number("010-0000-0000")
                    .role(MemberRole.ADMIN)
                    .build();
            Member admin = memberRepository.save(member3);

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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration2 = registrationRepository.save(registration2);

            Program program2 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration2)
                    .product(saveRegistration2.getProduct())
                    .member(saveRegistration2.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-03-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer2)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when
            List<ProgramResponse> results1 = programService.find(admin.getId(), ProgramSearchStatus.ALL);

            //then
            assertThat(results1.size()).isEqualTo(3);
            assertThat(results1).extracting("member","employee","startAt","endAt","status")
                    .contains(tuple("tester1","employee1","2000-01-01","2000-02-01",IN_PROGRESS),
                            tuple("tester1","employee1","2000-01-01","2000-03-01",IN_PROGRESS),
                            tuple("tester2","employee2","2000-01-01","2000-04-01",IN_PROGRESS)
                    );
        }

        @DisplayName("어드민 유저는 모든 프로그램을 조회가능한다")
        @Test
        void findAllProgram(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");
            Employee trainer2 = initTrainer("employee2");
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Member member3 = Member.builder()
                    .name("admin")
                    .email("admin@admin.com")
                    .password("test123")
                    .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .phone_number("010-0000-0000")
                    .role(MemberRole.ADMIN)
                    .build();
            Member admin = memberRepository.save(member3);

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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration2 = registrationRepository.save(registration2);

            Program program2 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration2)
                    .product(saveRegistration2.getProduct())
                    .member(saveRegistration2.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-03-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer2)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(EXPIRED)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when
            List<ProgramResponse> results1 = programService.find(admin.getId(), ProgramSearchStatus.ALL);

            //then
            assertThat(results1.size()).isEqualTo(3);
            assertThat(results1).extracting("member","employee","startAt","endAt","status")
                    .contains(tuple("tester1","employee1","2000-01-01","2000-02-01",IN_PROGRESS),
                            tuple("tester1","employee1","2000-01-01","2000-03-01",IN_PROGRESS),
                            tuple("tester2","employee2","2000-01-01","2000-04-01",EXPIRED)
                    );
        }

        @DisplayName("실패 : 프로그램이 존재하지 않으면 조회시 예외가 발생한다")
        @Test
        void findAllProgramNoExistAllProgram(){
            //given

            Member member3 = Member.builder()
                    .name("admin")
                    .email("admin@admin.com")
                    .password("test123")
                    .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .phone_number("010-0000-0000")
                    .role(MemberRole.ADMIN)
                    .build();
            Member admin = memberRepository.save(member3);


            //when

            //then
            assertThatThrownBy(() -> programService.find(admin.getId(), ProgramSearchStatus.ALL))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("프로그램을 찾을 수 없습니다");
        }

        @DisplayName("실패 : 진행중인 프로그램이 존재하지 않으면 조회시 예외가 발생한다")
        @Test
        void findInProgressProgramNoExistInProgressProgram(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer2 = initTrainer("employee2");
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Member member3 = Member.builder()
                    .name("admin")
                    .email("admin@admin.com")
                    .password("test123")
                    .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .phone_number("010-0000-0000")
                    .role(MemberRole.ADMIN)
                    .build();
            Member admin = memberRepository.save(member3);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer2)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(EXPIRED)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when

            //then
            assertThatThrownBy(() -> programService.find(admin.getId(), ProgramSearchStatus.VALID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("프로그램을 찾을 수 없습니다");
        }

        @DisplayName("실패 : 진행중인 모든 프로그램을 조회할 때 등록되지 않은 유저면 예외가 발생한다.")
        @Test
        void findInProgressProgramNoRegisterMember(){
            //given
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            //when

            //then
            assertThatThrownBy(() -> programService.find(member2.getId() + 1, ProgramSearchStatus.VALID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("존재하지 않는 회원입니다.");
        }

        @DisplayName("실패 : 진행중인 모든 프로그램을 조회할 때 어드민이 아니면 예외가 발생한다")
        @Test
        void findInProgressProgramNotAdmin(){
            //given
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            //when

            //then
            assertThatThrownBy(() -> programService.find(member2.getId(), ProgramSearchStatus.VALID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("findProgramByUserTest")
    class findProgramByUserTest {
        @DisplayName("회원의 진행중인 프로그램을 조회할 수 있다.")
        @Test
        void findInProgressProgramByUser(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1@test.com");
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration2 = registrationRepository.save(registration2);

            Program program2 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration2)
                    .product(saveRegistration2.getProduct())
                    .member(saveRegistration2.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-03-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when
            List<ProgramResponse> results1 = programService.findByUser(member1.getId(), ProgramSearchStatus.INVALID);
            List<ProgramResponse> results2 = programService.findByUser(member2.getId(), ProgramSearchStatus.INVALID);

            //then
            assertThat(results1.size()).isEqualTo(2);
            assertThat(results1).extracting("member","startAt","endAt","status")
                    .contains(tuple("tester1","2000-01-01","2000-02-01",IN_PROGRESS),
                            tuple("tester1","2000-01-01","2000-03-01",IN_PROGRESS)
                    );
            assertThat(results2.size()).isEqualTo(1);
            assertThat(results2).extracting("member","startAt","endAt","status")
                    .contains(tuple("tester2","2000-01-01","2000-04-01",IN_PROGRESS));
        }

        @DisplayName("트레이너의 진행중인 프로그램을 조회할 수 있다.")
        @Test
        void findInProgressProgramByEmployee(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");
            Employee trainer2 = initTrainer("employee2");
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration2 = registrationRepository.save(registration2);

            Program program2 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration2)
                    .product(saveRegistration2.getProduct())
                    .member(saveRegistration2.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-03-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer2)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when
            List<ProgramResponse> results1 = programService.findByUser(trainer.getMember().getId(), ProgramSearchStatus.INVALID);
            List<ProgramResponse> results2 = programService.findByUser(trainer2.getMember().getId(), ProgramSearchStatus.INVALID);

            //then
            assertThat(results1.size()).isEqualTo(2);
            assertThat(results1).extracting("member","startAt","endAt","status")
                    .contains(tuple("tester1","2000-01-01","2000-02-01",IN_PROGRESS),
                            tuple("tester1","2000-01-01","2000-03-01",IN_PROGRESS)
                    );
            assertThat(results2.size()).isEqualTo(1);
            assertThat(results2).extracting("member","startAt","endAt","status")
                    .contains(tuple("tester2","2000-01-01","2000-04-01",IN_PROGRESS));
        }



        @DisplayName("유저의 진행중인 모든 프로그램을 조회할 수 있다.")
        @Test
        void findAllProgramByUser(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");
            Employee trainer2 = initTrainer("employee2");
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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration2 = registrationRepository.save(registration2);

            Program program2 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration2)
                    .product(saveRegistration2.getProduct())
                    .member(saveRegistration2.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-03-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer2)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(EXPIRED)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when
            List<ProgramResponse> results1 = programService.findByUser(member1.getId(), ProgramSearchStatus.ALL);

            //then
            assertThat(results1.size()).isEqualTo(3);
            assertThat(results1).extracting("member","employee","startAt","endAt","status")
                    .contains(tuple("tester1","employee1","2000-01-01","2000-02-01",IN_PROGRESS),
                            tuple("tester1","employee1","2000-01-01","2000-03-01",IN_PROGRESS),
                            tuple("tester1","employee2","2000-01-01","2000-04-01",EXPIRED)
                    );
        }

        @DisplayName("트레이너의 진행중인 모든 프로그램을 조회할 수 있다.")
        @Test
        void findAllProgramByEmployee(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

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
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-02-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration2 = registrationRepository.save(registration2);

            Program program2 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration2)
                    .product(saveRegistration2.getProduct())
                    .member(saveRegistration2.getMember())
                    .status(IN_PROGRESS)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-03-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(EXPIRED)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when
            List<ProgramResponse> results1 = programService.findByUser(trainer.getMember().getId(), ProgramSearchStatus.ALL);

            //then
            assertThat(results1.size()).isEqualTo(3);
            assertThat(results1).extracting("member","employee","startAt","endAt","status")
                    .contains(tuple("tester1","employee1","2000-01-01","2000-02-01",IN_PROGRESS),
                            tuple("tester1","employee1","2000-01-01","2000-03-01",IN_PROGRESS),
                            tuple("tester2","employee1","2000-01-01","2000-04-01",EXPIRED)
                    );
        }

        @DisplayName("실패 : 트레이너가 관리했던 프로그램이 없으면 조회시 예외가 발생한다.")
        @Test
        void findByEmployeeNoExistProgram(){
            //given
            Employee trainer = initTrainer("employee1");

            //when

            //then
            assertThatThrownBy(() -> programService.findByUser(trainer.getMember().getId(), ProgramSearchStatus.ALL))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("프로그램을 찾을 수 없습니다.");
        }

        @DisplayName("실패 : 트레이너가 관리했던 프로그램이 없으면 조회시 예외가 발생한다.")
        @Test
        void findByEmployeeNoExistInProgressProgram(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");

            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(EXPIRED)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when

            //then
            assertThatThrownBy(() -> programService.findByUser(trainer.getMember().getId(), ProgramSearchStatus.VALID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("프로그램을 찾을 수 없습니다.");
        }

        @DisplayName("실패 : 유저가 진행했던 없으면 조회시 예외가 발생한다.")
        @Test
        void findByMemberNoExistProgram(){
            //given
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            //when

            //then
            assertThatThrownBy(() -> programService.findByUser(member1.getId(), ProgramSearchStatus.ALL))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("프로그램을 찾을 수 없습니다.");
        }

        @DisplayName("실패 : 유저가 진행중인 프로그램이 없으면 조회시 예외가 발생한다.")
        @Test
        void findByMemberNoExistInProgressProgram(){
            //given
            Product product = initProduct("pt 30회권", "30",30,ProductType.MEMBERSHIP);
            Employee trainer = initTrainer("employee1");
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            Registration saveRegistration3 = registrationRepository.save(registration3);

            Program program3 = Program.builder()
                    .employee(trainer)
                    .registration(saveRegistration3)
                    .product(saveRegistration3.getProduct())
                    .member(saveRegistration3.getMember())
                    .status(EXPIRED)
                    .startAt(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                    .endAt(LocalDate.parse("2000-04-01", DateTimeFormatter.ISO_DATE))
                    .build();
            programRepository.save(program3);

            //when

            //then
            assertThatThrownBy(() -> programService.findByUser(member2.getId(), ProgramSearchStatus.VALID))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("프로그램을 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("findRegistrationByUserTest")
    class FindRegistrationByUserTest {

        @DisplayName("유저의 모든 registration을 조회 가능하다.")
        @Test
        void FindAllRegistrationByUser(){
            //given
            Product product = initProduct("pt 30회권", "0",30,ProductType.MEMBERSHIP);
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Registration registration1= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(10000)
                    .status(RegistrationStatus.DECLINED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration1);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(20000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration3);

            Registration registration4= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.REFUND)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration4);
            Registration registration5= Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration5);

            //when
            List<RegistrationResponse> result1 = programService.findRegistrationsByUser(member1.getId(), RegistrationSearchStatus.ALL);
            List<RegistrationResponse> result2 = programService.findRegistrationsByUser(member2.getId(), RegistrationSearchStatus.ALL);

            //then
            assertThat(result1).hasSize(4);
            assertThat(result1).extracting("member","memberId","totalPrice","discount","status")
                    .contains(tuple("tester1",member1.getId(),10000,0,RegistrationStatus.DECLINED),
                            tuple("tester1",member1.getId(),20000,0,RegistrationStatus.ACCEPTED),
                            tuple("tester1",member1.getId(),30000,0,RegistrationStatus.PENDING),
                            tuple("tester1",member1.getId(),40000,0,RegistrationStatus.REFUND)
                    );
            assertThat(result2).hasSize(1);
            assertThat(result2).extracting("member","memberId","totalPrice","discount","status")
                    .contains(tuple("tester2",member2.getId(),40000,0,RegistrationStatus.PENDING)
                    );
        }
        @DisplayName("유저의 승인대기 중 인 registration을 조회 가능하다.")
        @Test
        void findReadyRegistrationByUser(){
            //given
            Product product = initProduct("pt 30회권", "0",30,ProductType.MEMBERSHIP);
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);

            Registration registration1= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(10000)
                    .status(RegistrationStatus.DECLINED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration1);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(20000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration3);

            Registration registration4= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration4);
            Registration registration5= Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration5);

            //when
            List<RegistrationResponse> result1 = programService.findRegistrationsByUser(member1.getId(), RegistrationSearchStatus.READY);
            List<RegistrationResponse> result2 = programService.findRegistrationsByUser(member2.getId(), RegistrationSearchStatus.READY);

            //then
            assertThat(result1).hasSize(2);
            assertThat(result1).extracting("member","memberId","totalPrice","discount","status")
                    .contains(
                            tuple("tester1",member1.getId(),30000,0,RegistrationStatus.PENDING),
                            tuple("tester1",member1.getId(),40000,0,RegistrationStatus.PENDING)
                    );
            assertThat(result2).hasSize(1);
            assertThat(result2).extracting("member","memberId","totalPrice","discount","status")
                    .contains(tuple("tester2",member2.getId(),40000,0,RegistrationStatus.PENDING)
                    );
        }

        @DisplayName("실패 : registration이 없을때 조회를 하면 예외가 발생한다.")
        @Test
        void findRegistrationNoExist(){
            //given
            Member member1 = initMember("tester1",MemberRole.MEMBER);

            //when //then
            assertThatThrownBy(() -> programService.findRegistrationsByUser(member1.getId(), RegistrationSearchStatus.ALL))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("조건을 만족하는 Registration이 없습니다.");
        }

        @DisplayName("실패 : 유저의 준비 중인 registration을 조회할 때 없으면 예외가 발생한다.")
        @Test
        void findReadyRegistrationByUserAtUnknownReadyRegistration(){
            //given
            Product product = initProduct("pt 30회권", "0",30,ProductType.MEMBERSHIP);
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Registration registration1= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(10000)
                    .status(RegistrationStatus.DECLINED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration1);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(20000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration2);

            //when //then
            assertThatThrownBy(() -> programService.findRegistrationsByUser(member1.getId(), RegistrationSearchStatus.READY))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("조건을 만족하는 Registration이 없습니다.");
        }

        @DisplayName("실패 : 존재하지 않는 회원")
        @Test
        void findRegistrationByUserAtUnknownUser(){
            //given
            Member user = initMember("tester2",MemberRole.MEMBER);

            //when //then
            assertThatThrownBy(() -> programService.findRegistrationsByUser(user.getId() + 1,RegistrationSearchStatus.READY))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("존재하지 않는 회원입니다.");
        }

    }

    @Nested
    @DisplayName("findRegistrationTest")
    class findRegistrationTest {
        @DisplayName("어드민은 모든 registration을 조회 가능하다")
        @Test
        void adminIsFindAllRegistration(){
            //given
            Product product = initProduct("pt 30회권", "0",30,ProductType.MEMBERSHIP);
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);
            Member admin = initMember("admin",MemberRole.ADMIN);

            Registration registration1= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(10000)
                    .status(RegistrationStatus.DECLINED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration1);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(20000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration3);

            Registration registration4= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.REFUND)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration4);
            Registration registration5= Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration5);

            //when
            List<RegistrationResponse> result1 = programService.findRegistrations(admin.getId(), RegistrationSearchStatus.ALL);

            //then
            assertThat(result1).hasSize(5);
            assertThat(result1).extracting("member","memberId","totalPrice","discount","status")
                    .contains(
                            tuple("tester1",member1.getId(),10000,0,RegistrationStatus.DECLINED),
                            tuple("tester1",member1.getId(),20000,0,RegistrationStatus.ACCEPTED),
                            tuple("tester1",member1.getId(),30000,0,RegistrationStatus.PENDING),
                            tuple("tester1",member1.getId(),40000,0,RegistrationStatus.REFUND),
                            tuple("tester2",member2.getId(),40000,0,RegistrationStatus.PENDING)
                    );
        }

        @DisplayName("어드민은 승인대기 중 인 모든 registration을 조회 가능하다.")
        @Test
        void adminIsFindReadyRegistration(){
            //given
            Product product = initProduct("pt 30회권", "0",30,ProductType.MEMBERSHIP);
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member member2 = initMember("tester2",MemberRole.MEMBER);
            Member admin = initMember("admin",MemberRole.ADMIN);

            Registration registration1= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(10000)
                    .status(RegistrationStatus.DECLINED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration1);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(20000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration2);

            Registration registration3 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(30000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration3);

            Registration registration4= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration4);
            Registration registration5= Registration.builder()
                    .product(product)
                    .member(member2)
                    .discount(0)
                    .totalPrice(40000)
                    .status(RegistrationStatus.PENDING)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration5);

            //when
            List<RegistrationResponse> result1 = programService.findRegistrations(admin.getId(), RegistrationSearchStatus.READY);

            //then
            assertThat(result1).hasSize(3);
            assertThat(result1).extracting("member","memberId","totalPrice","discount","status")
                    .contains(
                            tuple("tester1",member1.getId(),30000,0,RegistrationStatus.PENDING),
                            tuple("tester1",member1.getId(),40000,0,RegistrationStatus.PENDING),
                            tuple("tester2",member2.getId(),40000,0,RegistrationStatus.PENDING)
                    );
        }

        @DisplayName("실패 : 어드민이 조회할 때 조건에 맞는 registration이 없으면 예외가 발생한다,.")
        @Test
        void adminFindRegistrationFail(){
            //given
            Product product = initProduct("pt 30회권", "0",30,ProductType.MEMBERSHIP);
            Member member1 = initMember("tester1",MemberRole.MEMBER);
            Member admin = initMember("tester2",MemberRole.MEMBER);
            Registration registration1= Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(10000)
                    .status(RegistrationStatus.DECLINED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration1);

            Registration registration2 = Registration.builder()
                    .product(product)
                    .member(member1)
                    .discount(0)
                    .totalPrice(20000)
                    .status(RegistrationStatus.ACCEPTED)
                    .paymentAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .registrationAt(LocalDateTime.parse("2000-01-01 00:00", DATE_TIME_FORMATTER))
                    .refundAt(null)
                    .build();
            registrationRepository.save(registration2);

            //when //then
            assertThatThrownBy(() -> programService.findRegistrations(admin.getId(),RegistrationSearchStatus.READY))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("조건을 만족하는 Registration이 없습니다.");
        }

        @DisplayName("실패 : registration이 존재하지 않으면 조회시 예외가 발생한다.")
        @Test
        void adminFindRegistrationFail2(){
            //given
            Member admin = initMember("tester1",MemberRole.MEMBER);

            //when //then
            assertThatThrownBy(() -> programService.findRegistrations(admin.getId(), RegistrationSearchStatus.READY))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("조건을 만족하는 Registration이 없습니다.");
        }
    }

}