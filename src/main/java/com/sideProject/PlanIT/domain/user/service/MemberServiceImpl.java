package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.common.util.JwtTokenProvider;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponse;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberChangePasswordRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignInRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignUpRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.repository.EmployeeRepository;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Member signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        memberRepository.findByEmail(memberSignUpRequestDto.getEmail())
                .ifPresent( user1 -> {
                    throw new CustomException("이메일이 이미 존재합니다.", ErrorCode.ALREADY_EXIST_EMAIL);
                });

        String encryptedPassword = passwordEncoder.encode(memberSignUpRequestDto.getPassword());
        return memberRepository.save(Member.builder()
                        .email(memberSignUpRequestDto.getEmail())
                        .password(encryptedPassword)
                        .name(memberSignUpRequestDto.getName())
                        .phone_number(memberSignUpRequestDto.getPhone_number())
                        .birth(memberSignUpRequestDto.getBirth())
                        .address(memberSignUpRequestDto.getAddress())
                        .role(MemberRole.MEMBER)
                .build());
    }

    @Override
    public JwtResponseDto memberValidation(MemberSignInRequestDto memberSignInRequestDto) {
        Member member = memberRepository.findByEmail(memberSignInRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if(!passwordEncoder.matches(memberSignInRequestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(member))
                .refreshToken(jwtTokenProvider.createRefreshToken(member))
                .build();
    }

    @Override
    public String deleteMember(Long member_id) {
        memberRepository.deleteById(member_id);
        return "삭제 완료";
    }

    @Override
    public Member editMember(Long member_id, MemberEditRequestDto memberEditRequestDto) {
        Member memberToEdit = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("no extist id"));


        memberToEdit.update(memberEditRequestDto);
        return memberRepository.save(memberToEdit);
    }

    @Override
    public String changePassword(Long member_id, MemberChangePasswordRequestDto memberChangePasswordRequestDto) {
        Member memberToChangePassword = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("no exist id"));

        if (!passwordEncoder.matches(memberChangePasswordRequestDto.getCurrentPassword(), memberToChangePassword.getPassword())) {
            throw new CustomException("현재 비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        if (!memberChangePasswordRequestDto.getNewPassword().equals(memberChangePasswordRequestDto.getNewPasswordCheck())) {
            throw new CustomException("변경할 비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        String encryptedNewPassword = passwordEncoder.encode(memberChangePasswordRequestDto.getNewPassword());
        memberToChangePassword.changePassword(encryptedNewPassword);
        memberRepository.save(memberToChangePassword);

        return "비밀 번호 변경 완료";
    }

    @Override
    public MemberResponseDto findMember(Long member_id) {
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        //트레이너면 트레이너 정보 조회
        TrainerResponse trainerResponse = null;
        if(member.getRole().equals(MemberRole.TRAINER)) {
            Employee trainer = employeeRepository.findByMemberId(member.getId()).orElseThrow(() ->
                    new CustomException(ErrorCode.EMPLOYEE_NOT_FOUND));
            trainerResponse = TrainerResponse.of(trainer);
        }

        return MemberResponseDto.of(member,trainerResponse);
    }

    @Override
    public String signOut(Long member_id) {
        jwtTokenProvider.deleteRefreshToken(member_id);

        return "로그아웃 성공";
    }

    @Override
    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAllMembers();
        return members.stream()
                .map(Member::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainerResponseDto> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAllTrainers();
        return employees.stream()
                .map(Employee::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String grantEmployeeAuth(Long member_id, TrainerRequestDto trainerRequestDto) {
        Member memberToEmployee = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("no exist id"));
        memberToEmployee.grantEmployeeAuth();
        memberRepository.save(memberToEmployee);

        employeeRepository.save(Employee.builder()
                        .member(memberToEmployee)
                        .career(trainerRequestDto.getCareer())
                .build());

        return "직원 설정 완료";
    }
}
