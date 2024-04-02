package com.sideProject.PlanIT.domain.reservation.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.repository.ProgramRepository;
import com.sideProject.PlanIT.domain.reservation.controller.ENUM.ReservationFindOption;
import com.sideProject.PlanIT.domain.reservation.dto.response.ReservationResponseDto;
import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.reservation.entity.Reservation;
import com.sideProject.PlanIT.domain.reservation.repository.ReservationRepository;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final ProgramRepository programRepository;

    @Override
    @Transactional
    public String changeAvailability(LocalDate reservedDate,List<LocalTime> reservedTimes, Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException(userId + "는 존재하지 않는 유저입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );
        Employee employee = employeeRepository.findByMemberId(member.getId()).orElseThrow(() ->
                new CustomException(member.getId() + "은 직원이 아닙니다.", ErrorCode.NO_AUTHORITY)
        );

        List<LocalDateTime> reservedDateTimes = createLocalDateTimes(reservedDate, reservedTimes);

        List<Reservation> existingReservations
                = reservationRepository.findByEmployeeAndReservedTimeIn(employee, reservedDateTimes);


        // 기존 예약 삭제
        List<Reservation> reservedReservations = existingReservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.POSSIBLE)
                .toList();
        reservedReservations.forEach(reservationRepository::delete);

        // 새 예약 추가 (기존 예약이 없는 reservedTimes에 대해서만)
        reservedDateTimes.forEach(reservedDateTime -> {
            boolean exists = existingReservations.stream()
                    .anyMatch(reservation -> reservation.getReservedTime().equals(reservedDateTime));
            if (!exists) {
                Reservation newReservation = Reservation.builder()
                        .reservedTime(reservedDateTime)
                        .status(ReservationStatus.POSSIBLE)
                        .employee(employee)
                        .classTime(LocalTime.of(1,0))
                        .build();
                reservationRepository.save(newReservation);
            }
        });

        return "ok";
    }

    public static List<LocalDateTime> createLocalDateTimes(LocalDate date, List<LocalTime> times) {
        // Stream을 사용하여 각 LocalTime 요소에 대해 LocalDate와 결합
        return times.stream()
                .map(time -> date.atTime(time))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String reservation(Long reservationId, Long userId, Long programId, LocalDateTime now) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException(userId+"은 존재하지 않는 유저입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );
        Program program = programRepository.findById(programId).orElseThrow(() ->
                new CustomException(programId+ "는 존재하지 않는 수업입니다.", ErrorCode.PROGRAM_NOT_FOUND)
        );
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
                new CustomException(reservationId + "는 존재하지 않는 예약입니다.", ErrorCode.RESERVATION_NOT_FOUND)
        );

        //프로그램 상태 변경
        program.reservation();

        if(!Objects.equals(program.getEmployee().getId(), reservation.getEmployee().getId())) {
            throw new CustomException("유저 " + userId + "은 해당 트레이너에 예약할 수 없습니다.", ErrorCode.NOT_YOUR_TRAINER);
        }

        //프로그램 예약
        reservation.reservation(program,member,now);

        programRepository.save(program);
        reservationRepository.save(reservation);

        return "ok";
    }


    @Override
    public Map<LocalDate, List<ReservationResponseDto>> findReservationForWeekByMember(
            LocalDate date,
            Long userId,
            ReservationFindOption option
    ) {
        LocalDateTime startOfWeek = calStartOfWeek(date);
        LocalDateTime endOfWeek = calEndOfWeek(date);

        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new CustomException("존재하지 않는 유저입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );

        List<Reservation> reservations;
        //트레이너이면
        if(member.getRole() == MemberRole.TRAINER) {
            Employee employee = employeeRepository.findByMemberId(member.getId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 트레이너입니다.", ErrorCode.MEMBER_NOT_FOUND)
            );
            reservations = findReservationByEmployee(employee,startOfWeek,endOfWeek,option);
        } else {
            reservations = findReservationByMember(member,startOfWeek,endOfWeek,option);
        }


        return reservations.stream()
                .map(ReservationResponseDto::of)
                .collect(Collectors.groupingBy(response -> response.getReservationTime().toLocalDate()));
    }

    private List<Reservation> findReservationByEmployee(
            Employee employee,
            LocalDateTime startTime,
            LocalDateTime endTime,
            ReservationFindOption option
    ) {
        if(option == ReservationFindOption.ALL) {
            return reservationRepository.findByEmployeeAndDateTimeBetween(employee,startTime,endTime);
        } else if (option == ReservationFindOption.RESERVED) {
            return reservationRepository.findByEmployeeAndStatusAndDateTimeBetween(employee,ReservationStatus.RESERVED,startTime,endTime);
        } else if (option == ReservationFindOption.POSSIBLE) {
            return reservationRepository.findByEmployeeAndStatusAndDateTimeBetween(employee,ReservationStatus.POSSIBLE,startTime,endTime);
        } else {
            return reservationRepository.findByEmployeeAndStatusAndDateTimeBetween(employee,ReservationStatus.FINISHED,startTime,endTime);
        }
    }

    private List<Reservation> findReservationByMember(
            Member member,
            LocalDateTime startTime,
            LocalDateTime endTime,
            ReservationFindOption option
    ) {
        if(option == ReservationFindOption.ALL) {
            return reservationRepository.findByMemberAndDateTimeBetween(member,startTime,endTime);
        } else if (option == ReservationFindOption.RESERVED) {
            return reservationRepository.findByMemberAndStatusAndDateTimeBetween(member,ReservationStatus.RESERVED,startTime,endTime);
        } else if (option == ReservationFindOption.POSSIBLE) {
            return reservationRepository.findByMemberAndStatusAndDateTimeBetween(member,ReservationStatus.POSSIBLE,startTime,endTime);
        } else {
            return reservationRepository.findByMemberAndStatusAndDateTimeBetween(member,ReservationStatus.FINISHED,startTime,endTime);
        }
    }

    @Override
    public List<ReservationResponseDto> findReservationForDayByEmployee(LocalDate date, Long employeeId) {
        LocalDateTime startOfWeek = calStartOfDay(date);
        LocalDateTime endOfWeek = calEndOfDay(date);

        List<Reservation> reservations;
        //트레이너이면
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new CustomException("존재하지 않는 트레이너입니다.", ErrorCode.MEMBER_NOT_FOUND)
        );
        reservations = reservationRepository.findByEmployeeAndDateTimeBetween(employee,startOfWeek,endOfWeek);

        return reservations.stream()
                .map(ReservationResponseDto::of)
                .toList();
    }

    //그 주의 월요일 00:00:00
    private LocalDateTime calStartOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
    }

    //그 주의 일요일 23:59:59
    private LocalDateTime calEndOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
    }

    //그 주의 월요일 00:00:00
    private LocalDateTime calStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    //그 주의 일요일 23:59:59
    private LocalDateTime calEndOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    @Override
    @Transactional
    public String cancel(Long userId, Long reservationId, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->
            new CustomException("예약 " + reservationId + "은 없는 예약입니다.",ErrorCode.RESERVATION_NOT_FOUND)
        );

        if(reservation.getStatus() != ReservationStatus.RESERVED || !Objects.equals(reservation.getMember().getId(), userId)) {
            throw new CustomException("예약 " + reservationId + "은 취소할 수 없습니다.",ErrorCode.RESERVATION_NOT_FOUND);
        }

        //프로그램 횟수 추가
        Program program = reservation.getProgram();
        program.cancelReservation();
        programRepository.save(program);

        //예약 취소
        reservation.cancel(now);
        reservationRepository.save(reservation);

        return "ok";
    }
}
