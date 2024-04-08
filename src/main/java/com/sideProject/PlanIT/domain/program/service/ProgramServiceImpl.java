package com.sideProject.PlanIT.domain.program.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import com.sideProject.PlanIT.domain.program.dto.request.RegistrationRequestDto;
import com.sideProject.PlanIT.domain.program.dto.response.ProgramResponseDto;
import com.sideProject.PlanIT.domain.program.dto.response.FindRegistrationResponseDto;
import com.sideProject.PlanIT.domain.program.dto.response.RegistrationResponseDto;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.enums.ProgramStatus;
import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationSearchStatus;
import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import com.sideProject.PlanIT.domain.program.repository.ProgramRepository;
import com.sideProject.PlanIT.domain.program.repository.RegistrationRepository;
import com.sideProject.PlanIT.domain.program.dto.request.ProgramModifyRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.EmployeeSemiResponseDto;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
    public RegistrationResponseDto registration(RegistrationRequestDto request, Long memberId, LocalDateTime now){
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(memberId + "는 존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
                new CustomException(request.getProductId() + "은 존재하지 않는 상품입니다.", ErrorCode.PRODUCT_NOT_FOUND)
        );

        Long trainerId = null;
        if(request.getTrainerId() != null) {
            trainerId = employeeRepository.findById(request.getTrainerId()).orElseThrow(() ->
                    new CustomException(request.getTrainerId() + "은 존재하지 않는 트레이너입니다.", ErrorCode.EMPLOYEE_NOT_FOUND)
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
            return RegistrationResponseDto.of(resultRegistration.getId(),"회원권 등록이 완료되었습니다.");
        }

        return RegistrationResponseDto.of(resultRegistration.getId(),"PT권 등록이 요청되었습니다.");
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
                new CustomException(registrationId + "은 존재하지 않는 등록입니다.", ErrorCode.PROGRAM_NOT_FOUND)
        );
    }

    private Program getProgramById(long programId) {
        return programRepository.findById(programId)
                .orElseThrow(() -> new CustomException("등록되지 않은 프로그램입니다.", ErrorCode.PROGRAM_NOT_FOUND));
    }

    @Override
    public String modify(long programId, ProgramModifyRequestDto request) {
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
    public Long suspendProgram(Long id, LocalDate now) {
        Program program = programRepository.findById(id).orElseThrow(() ->
                new CustomException("존재하지 않는 프로그램입니다.", ErrorCode.PROGRAM_NOT_FOUND)
        );

        if(program.getSuspendAt() != null || program.getProduct().getType().equals(ProductType.PT)) {
            throw new CustomException("정책상 정지 요청이 거부됩니다.", ErrorCode.SUSPEND_REQUEST_DENIED);
        }

        program.suspendProgram(now);
        Program result = programRepository.save(program);

        return result.getId();
    }

    @Override
    public Long resumeProgram(Long id, LocalDate now) {
        Program program = programRepository.findById(id).orElseThrow(() ->
                new CustomException("존재하지 않는 프로그램입니다.", ErrorCode.PROGRAM_NOT_FOUND)
        );

        if(program.getStatus() != ProgramStatus.SUSPEND) {
            throw new CustomException("프로그램이 정지 상태가 아닙니다.", ErrorCode.NOT_SUSPEND_PROGRAM);
        }

        Period periodBetween = Period.between(program.getSuspendAt(), now);
        LocalDate endAt = program.getEndAt().plus(periodBetween);

        program.resumeProgram(now,endAt);
        Program result = programRepository.save(program);

        return result.getId();
    }

    //프로그램이 이미 존재하면 종료일 다음날 기준으로 추가
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

        //회원권 종료 일자 계산
        LocalDate startAt = registration.getRegistrationAt().toLocalDate();
        LocalDate endAt = null;
        if(isPeriodGreaterThanZero(product.getPeriod())) {
            endAt = startAt.plus(product.getPeriod()).minusDays(1);
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

    private static boolean isPeriodGreaterThanZero(Period period) {
        // 전체 기간을 월로 변환하여 검사 (년과 월을 고려)
        if (period.toTotalMonths() > 0) {
            return true;
        }
        // 월이 0인 경우, 일수로 판단
        if (period.getDays() > 0) {
            return true;
        }
        return false;
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
     * @return List<ProgramResponseDto>
     */
    @Override
    public Page<ProgramResponseDto> find(long adminId, ProgramSearchStatus option, Pageable pageable) {
        Member admin = memberRepository.findById(adminId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        if(admin.getRole() != MemberRole.ADMIN) {
            throw new CustomException("권한이 없습니다.", ErrorCode.NO_AUTHORITY);
        }

        Page<Program> programs = findProgram(option,pageable);

        if(programs.isEmpty()) {
            throw new CustomException("프로그램을 찾을 수 없습니다",ErrorCode.PROGRAM_NOT_FOUND);
        }

        return programs.map(ProgramResponseDto::of);
    }

    @Override
    public Page<ProgramResponseDto> findByUser(long userId, ProgramSearchStatus option, Pageable pageable) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException(userId + "는 존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        Page<Program> programs = null;
        if(member.getRole() == MemberRole.MEMBER) {
            programs = findProgramByUser(member, option,pageable);
        } else if(member.getRole() == MemberRole.TRAINER) {
            programs = findProgramByEmploy(member, option,pageable);
        }

        if(programs == null || programs.isEmpty()) {
            throw new CustomException("프로그램을 찾을 수 없습니다.",ErrorCode.PROGRAM_NOT_FOUND);
        }

        return programs.map(ProgramResponseDto::of);
    }

    public ProgramResponseDto findByProgramId(long programId, long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException(userId + "은 존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        Program program = programRepository.findById(programId).orElseThrow(() ->
                new CustomException(programId + "은 존재하지 않는 프로그램입니다.", ErrorCode.PROGRAM_NOT_FOUND)
        );

        if(member.getRole().equals(MemberRole.TRAINER)) {
            validTrainerAccess(member,program);
        } else if(member.getRole().equals(MemberRole.MEMBER)){
            validMemberAccess(member,program);
        }

        return ProgramResponseDto.of(program);
    }

    private void validTrainerAccess(Member member, Program program) {
        Employee employee = employeeRepository.findByMemberId(member.getId()).orElseThrow(() ->
                new CustomException("존재하지 않는 직원입니다.", ErrorCode.EMPLOYEE_NOT_FOUND)
        );
        if(!Objects.equals(program.getEmployee().getId(), employee.getId())) {
            throw new CustomException(employee.getId()+ "은 " + program.getId() + "의 조회 권한이 없습니다.", ErrorCode.NO_AUTHORITY);
        }
    }

    private void validMemberAccess(Member member, Program program) {
        log.info(member.getId() + " " + program.getMember().getId());
        if(!Objects.equals(program.getMember().getId(), member.getId())) {
            throw new CustomException(member.getId()+ "은 " + program.getId() + "의 조회 권한이 없습니다.", ErrorCode.NO_AUTHORITY);
        }
    }

    private Page<Program> findProgramByEmploy(Member member, ProgramSearchStatus option,Pageable pageable) {
        log.info("findEmployeeByMemberId = {}" , member.getId());
        Employee employee = employeeRepository.findByMemberId(member.getId()).orElseThrow(()->
            new CustomException("존재하지 않는 직원입니다.", ErrorCode.EMPLOYEE_NOT_FOUND)
        );
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == ProgramSearchStatus.ALL) {
            return programRepository.findByEmployeeId(employee.getId(),pageable);
        } else {
            return programRepository.findByEmployeeIdAndStatus(employee.getId(), ProgramStatus.IN_PROGRESS,pageable);
        }
    }
    private Page<Program> findProgramByUser(Member member, ProgramSearchStatus option,Pageable pageable) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == ProgramSearchStatus.ALL) {
            return programRepository.findByMemberId(member.getId(),pageable);
        } else if (option == ProgramSearchStatus.VALID) {
            return programRepository.findByMemberIdAndStatusIn(member.getId(), ProgramStatus.forValid(),pageable);
        } else {
            return programRepository.findByMemberIdAndStatusIn(member.getId(), ProgramStatus.forUnValid(),pageable);
        }
    }

    private Page<Program> findProgram(ProgramSearchStatus option,Pageable pageable) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == ProgramSearchStatus.ALL) {
            return programRepository.findAll(pageable);
        } else if(option == ProgramSearchStatus.VALID) {
            return programRepository.findByStatusIn(ProgramStatus.forValid(),pageable);
        } else {
            return programRepository.findByStatusIn(ProgramStatus.forUnValid(), pageable);
        }
    }

    @Override
    public Page<FindRegistrationResponseDto> findRegistrationsByAdmin(long adminId, RegistrationSearchStatus option, Pageable pageable) {
        // 회원 검증 및 권한 확인
        validateMemberAndAuthority(adminId, MemberRole.ADMIN);
        // Registration 조회 및 변환
        return findAndConvertRegistrations(option, pageable, null);
    }

    @Override
    public Page<FindRegistrationResponseDto> findRegistrationsByUser(long userId, RegistrationSearchStatus option, Pageable pageable) {
        // 회원 검증
        Member member = validateMemberAndAuthority(userId, null);
        // Registration 조회 및 변환
        return findAndConvertRegistrations(option, pageable, member);
    }

    private Member validateMemberAndAuthority(long memberId, MemberRole requiredRole) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다.", ErrorCode.MEMBER_NOT_FOUND));
        if (requiredRole != null && member.getRole() != requiredRole) {
            throw new CustomException(memberId+"는 권한이 없습니다.", ErrorCode.NO_AUTHORITY);
        }
        return member;
    }

    private Page<FindRegistrationResponseDto> findAndConvertRegistrations(RegistrationSearchStatus option, Pageable pageable, Member member) {
        Page<Registration> registrations;
        if (member == null) {
            registrations = findRegistration(option, pageable);
        } else {
            registrations = findRegistrationByUser(member, option, pageable);
        }

        if (registrations == null || registrations.isEmpty()) {
            throw new CustomException("조건을 만족하는 Registration이 없습니다.", ErrorCode.REGISTRATION_NOT_FOUND);
        }

        return registrations.map(registration -> {
            FindRegistrationResponseDto dto = FindRegistrationResponseDto.of(registration);
            if (registration.getTrainerId() != null) {
                Optional<Employee> trainer = employeeRepository.findById(registration.getTrainerId());

                trainer.ifPresent(employee -> dto.setTrainer(new EmployeeSemiResponseDto(
                        employee.getId(),
                        employee.getMember().getName())
                ));
            }
            return dto;
        });
    }

    //todo : 리팩토링 여부 생각, INVALID 조회 추가
    private Page<Registration> findRegistrationByUser(Member member, RegistrationSearchStatus option, Pageable pageable) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        if(option == RegistrationSearchStatus.ALL) {
            return registrationRepository.findByMemberId(member.getId(), pageable);
        } else {
            //PENDING인 상태 조회
            return registrationRepository.findByMemberIdAndStatus(member.getId(),RegistrationStatus.PENDING, pageable);
        }
    }

    private Page<Registration> findRegistration(RegistrationSearchStatus option, Pageable pageable) {
        //ALL이면 모든 상태 조회, VALID이면 IN_PROCESS인 경우만 조회
        log.info("{} = option", option);
        if(option == RegistrationSearchStatus.ALL) {
            return registrationRepository.findAll(pageable);
        } else {
            return registrationRepository.findByStatus(RegistrationStatus.PENDING, pageable);
        }
    }

    @Override
    public String expiredMemberShipProgram(LocalDate now) {
        //만료 기간이 어제인 프로그램 조회
        List<Program> expirationDatePrograms = programRepository
                .findMembershipProgramsByEndAtAndProductType(
                        now.minusDays(1),
                        ProductType.MEMBERSHIP
                );

        for(Program expirationDateProgram : expirationDatePrograms) {
            expirationDateProgram.changeToExpired();

            programRepository.save(expirationDateProgram);
        }

        return "ok";
    }
}
