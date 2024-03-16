package com.sideProject.PlanIT.domain.program.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequest;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponse;
import com.sideProject.PlanIT.domain.program.dto.response.FindRegistrationResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 프로그램 관련 Service
 * 프로그램 조회, 환불, 수정, 승인 요청
 *
 * @author 김문진
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;
    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final RegistrationRepository registrationRepository;
    private final ProductRepository productRepository;
    @Override
    public RegistrationResponse registration(RegistrationRequest request, Long memberId, LocalDateTime now){
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
                new CustomException("상품을 찾을 수 없습니다.", ErrorCode.PRODUCT_NOT_FOUND)
        );

        Long trainerId = null;
        if(request.getTrainerId() != null) {
            trainerId = employeeRepository.findById(request.getTrainerId()).orElseThrow(() ->
                    new CustomException("직원을 찾을 수 없습니다", ErrorCode.EMPLOYEE_NOT_FOUND)
            ).getId();
        }
        //결제 로직
        RegistrationStatus status = RegistrationStatus.PENDING;

        Registration registrationEntity = Registration.builder()
                .registrationAt(request.getRegistrationAt().atStartOfDay())
                .paymentAt(now)
                .status(status)
                .discount(0)
                .trainerId(trainerId)
                .totalPrice(product.getPrice())
                .member(member)
                .product(product)
                .build();

        Registration resultRegistration = registrationRepository.save(registrationEntity);

        if(resultRegistration.getProduct().getType() == ProductType.MEMBERSHIP) {
            approve(resultRegistration.getId(),null,now);
            return RegistrationResponse.of(resultRegistration.getId(),"회원권 등록이 완료되었습니다.");
        }

        return RegistrationResponse.of(resultRegistration.getId(),"PT권 등록이 요청되었습니다.");
    }
    @Override
    public String refund(long programId, LocalDateTime localDateTime) {
        Program program = getProgramById(programId);

        //프로그램을 등록한 등록데이터로 환불
        refundRegistration(program.getRegistration().getId(), localDateTime);

        //이미 환불이 완료된 요청
        if(program.getStatus().equals(ProgramStatus.REFUND)) {
            throw new CustomException("이미 환불된 프로그램입니다.", ErrorCode.ALREADY_REFUNDED_PROGRAM);
        }

        program.changeToRefund();
        programRepository.save(program);

        return "OK";
    }

    private void refundRegistration(long registrationId, LocalDateTime localDateTime) {
        Registration registration = getRegistrationById(registrationId);

        //이미 등록이 환불 처리 된 경우
        if(registration.getStatus().equals(RegistrationStatus.REFUND)) {
            log.error("registrationID = {} , 이미 환불된 등록, 환불 무시",registration.getId());
            return;
        }
        //todo : 환불 정책에 따른 계산 로직 추가

        registration.changeToRefund(localDateTime);

        registrationRepository.save(registration);
    }

    private Registration getRegistrationById(long registrationId) {
        return registrationRepository.findById(registrationId).orElseThrow(() ->
                new CustomException("존재하지 않는 등록입니다.", ErrorCode.PROGRAM_NOT_FOUND)
        );
    }

    private Program getProgramById(long programId) {
        return programRepository.findById(programId)
                .orElseThrow(() -> new CustomException("등록되지 않은 프로그램입니다.", ErrorCode.PROGRAM_NOT_FOUND));
    }

    @Override
    public String modify(long programId, ProgramModifyRequest request) {
        Program program = getProgramById(programId);

        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );
        Employee trainer = employeeRepository.findById(request.getEmployId()).orElseThrow(() ->
                new CustomException("존재하지 않는 직원입니다.", ErrorCode.EMPLOYEE_NOT_FOUND)
        );

        program.updateProgram(
                LocalDate.parse(request.getStartTime(), DateTimeFormatter.ISO_DATE),
                LocalDate.parse(request.getEndTime(), DateTimeFormatter.ISO_DATE),
                member,
                trainer
        );

        //todo : 세이브 성능에 문제 가능성 있음. 참고 ) https://devs0n.tistory.com/113
        programRepository.save(program);

        return "OK";
    }


    @Override
    public Long approve(Long registrationId, Long trainerId, LocalDateTime localDateTime) {
        Registration registration = getRegistrationById(registrationId);

        Member member = memberRepository.findById(registration.getMember().getId()).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        Employee trainer = null;
        if(registration.getProduct().getType().equals(ProductType.PT)) {
            if(trainerId == null) {
                log.error("pt권인데 트레이너가 등록되지 않았습니다.");
                throw new CustomException("파라미터 값을 확인해주세요.", ErrorCode.INVALID_PARAMETER);
            }
            trainer = employeeRepository.findById(trainerId).orElseThrow(() ->
                    new CustomException("존재하지 않는 직원입니다.", ErrorCode.EMPLOYEE_NOT_FOUND)
            );
        }

        //이미 등록 여부 조회
        checkProgramExistenceByRegistration(registration.getId());

        //등록 승인
        registration.changeToAccepted();
        registrationRepository.save(registration);

        Product product = registration.getProduct();

        //회원궝 종료 일자 계산
        LocalDate startAt = registration.getRegistrationAt().toLocalDate();
        LocalDate endAt = null;
        if(Integer.parseInt(product.getPeriod()) > 0) {
            endAt = startAt.plusDays(Integer.parseInt(product.getPeriod()));
        }

        Program program = Program.builder()
                .remainedNumber(product.getNumber())
                .product(product)
                .employee(trainer)
                .member(member)
                .registration(registration)
                .status(ProgramStatus.IN_PROGRESS)
                .startAt(startAt)
                .endAt(endAt)
                .build();

        return programRepository.save(program).getId();
    }


    //프로그램 등록 여부 조회
    public void checkProgramExistenceByRegistration(Long registrationId) {
        Program program = programRepository.findSingleProgramByRegistrationId(registrationId);
        if(program != null) {
            throw new CustomException("이미 등록된 프로그램입니다.", ErrorCode.ALREADY_APPROVE_PROGRAM);
        }
    }

    /**
     * 프로그램을 조회하여 리스트를 반환하는 메서드(어드민 만 사용 가능)
     *
     * @param option 조회 옵션(ALL : 전부, VAILD : 유효한 프로그램, UNVAILD : 유효하지 않은 프로그램)
     * @return List<ProgramResponse>
     */
    @Override
    public List<ProgramResponse> find(long adminId, ProgramSearchStatus option) {
        Member admin = memberRepository.findById(adminId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        if(admin.getRole() != MemberRole.ADMIN) {
            throw new CustomException("권한이 없습니다.", ErrorCode.NO_AUTHORITY);
        }

        List<Program> programs = findProgram(option);

        if(programs.isEmpty()) {
            throw new CustomException("프로그램을 찾을 수 없습니다",ErrorCode.PROGRAM_NOT_FOUND);
        }

        return programs.stream().map(ProgramResponse::of).toList();
    }

    @Override
    public List<ProgramResponse> findByUser(long userId, ProgramSearchStatus option) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );
        //todo : 유저가 어드민 일 때 유저에 따라 조회하는거 구현
        List<Program> programs = null;
        if(member.getRole() == MemberRole.MEMBER) {
            programs = findProgramByUser(member, option);
        } else if(member.getRole() == MemberRole.TRAINER) {
            programs = findProgramByEmploy(member, option);
        }

        if(programs.isEmpty()) {
            throw new CustomException("프로그램을 찾을 수 없습니다.",ErrorCode.PROGRAM_NOT_FOUND);
        }

        return programs.stream().map(ProgramResponse::of).toList();
    }

    private List<Program> findProgramByEmploy(Member member, ProgramSearchStatus option) {
        log.info("findEmployeeByMemberId = {}" , member.getId());
        Employee employee = employeeRepository.findByMemberId(member.getId()).orElseThrow(()->
            new CustomException("존재하지 않는 직원입니다.", ErrorCode.EMPLOYEE_NOT_FOUND)
        );
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == ProgramSearchStatus.ALL) {
            return programRepository.findByEmployeeId(employee.getId());
        } else {
            return programRepository.findByEmployeeIdAndStatus(employee.getId(), ProgramStatus.IN_PROGRESS);
        }
    }
    private List<Program> findProgramByUser(Member member, ProgramSearchStatus option) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == ProgramSearchStatus.ALL) {
            return programRepository.findByMemberId(member.getId());
        } else {
            return programRepository.findByMemberIdAndStatus(member.getId(), ProgramStatus.IN_PROGRESS);
        }
    }

    private List<Program> findProgram(ProgramSearchStatus option) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == ProgramSearchStatus.ALL) {
            return programRepository.findAll();
        } else {
            return programRepository.findByStatus(ProgramStatus.IN_PROGRESS);
        }
    }

    //조건에 맞는 Registration list 조회
    @Override
    public List<FindRegistrationResponse> findRegistrations(long adminId, RegistrationSearchStatus option) {
        Member admin = memberRepository.findById(adminId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        if(admin.getRole() != MemberRole.ADMIN) {
            throw new CustomException("권한이 없습니다.", ErrorCode.NO_AUTHORITY);
        }

        List<Registration> registration = findRegistration(option);

        if(registration.isEmpty()) {
            throw new CustomException("조건을 만족하는 Registration이 없습니다.",ErrorCode.REGISTRATION_NOT_FOUND);
        }

        return registration.stream().map(FindRegistrationResponse::of).toList();
    }

    @Override
    public List<FindRegistrationResponse> findRegistrationsByUser(long userId, RegistrationSearchStatus option) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        List<Registration> registration = findRegistrationByUser(member, option);

        if(registration.isEmpty()) {
            throw new CustomException("조건을 만족하는 Registration이 없습니다.",ErrorCode.REGISTRATION_NOT_FOUND);
        }

        return registration.stream().map(FindRegistrationResponse::of).toList();
    }

    //리팩토링 여부 생각
    private List<Registration> findRegistrationByUser(Member member, RegistrationSearchStatus option) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == RegistrationSearchStatus.ALL) {
            return registrationRepository.findByMemberId(member.getId());
        } else {
            //PENDING인 상태 조회
            return registrationRepository.findByMemberIdAndStatus(member.getId(),RegistrationStatus.PENDING);
        }
    }

    private List<Registration> findRegistration(RegistrationSearchStatus option) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        log.info("{} = option", option);
        if(option == RegistrationSearchStatus.ALL) {
            return registrationRepository.findAll();
        } else {
            return registrationRepository.findByStatus(RegistrationStatus.PENDING);
        }
    }
}
