package com.sideProject.PlanIT.domain.reservation.repository;

import com.sideProject.PlanIT.domain.reservation.entity.ENUM.ReservationStatus;
import com.sideProject.PlanIT.domain.reservation.entity.Reservation;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.employee = :employee AND r.reservedTime IN :reservedTimes")
    List<Reservation> findByEmployeeAndReservedTimeIn(@Param("employee") Employee employee, @Param("reservedTimes") List<LocalDateTime> reservedTimes);
    @Query("SELECT r FROM Reservation r WHERE r.employee = :employee AND r.reservedTime IN :reservedTimes AND r.status = :status")
    List<Reservation> findByEmployeeAndReservedTimeInAndStatus(@Param("employee") Employee employee,
                                                               @Param("reservedTimes") List<LocalDateTime> reservedTimes,
                                                               @Param("status") ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.member = :member AND r.reservedTime BETWEEN :startDateTime AND :endDateTime")
    List<Reservation> findByMemberAndDateTimeBetween(@Param("member") Member member, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT r FROM Reservation r WHERE r.employee = :employee AND r.reservedTime BETWEEN :startDateTime AND :endDateTime")
    List<Reservation> findByEmployeeAndDateTimeBetween(@Param("employee") Employee employee, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
}
