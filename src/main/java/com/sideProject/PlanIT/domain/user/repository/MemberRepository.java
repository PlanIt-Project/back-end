package com.sideProject.PlanIT.domain.user.repository;

import com.sideProject.PlanIT.domain.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.role = 'MEMBER'")
    List<Member> findAllMembers();

    @Query("SELECT m FROM Member m WHERE m.role = 'TRAINER'")
    List<Member> findAllEmployees();
}
