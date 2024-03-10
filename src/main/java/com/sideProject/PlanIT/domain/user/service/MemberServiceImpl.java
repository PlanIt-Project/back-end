package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.user.dto.MemberDto;
import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Member signUp(MemberDto.MemberSignUpRequestDto memberSignUpRequestDto) {
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

    public void signIn() {
        //todo: 로그인 기능
    }

    @Override
    public String deleteMember(Long member_id) {
        memberRepository.deleteById(member_id);
        return "삭제 완료";
    }

    @Override
    public Member editMember(Long member_id, MemberDto.MemberEditRequestDto memberEditRequestDto) {
        Member memberToEdit = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("no extist id"));

        String newEmail = memberEditRequestDto.getEmail();

        if (!newEmail.equals(memberToEdit.getEmail())) {
            memberRepository.findByEmail(newEmail)
                    .ifPresent(user1 -> {
                        throw new CustomException("이메일이 이미 존재합니다.", ErrorCode.ALREADY_EXIST_EMAIL);
                    });
        }

        memberToEdit.update(memberEditRequestDto);
        return memberRepository.save(memberToEdit);
    }

    @Override
    public String changePassword(Long member_id, MemberDto.MemberChangePasswordRequestDto memberChangePasswordRequestDto) {
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
    public Member findMember(Long member_id) {
        return memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("no extist id"));
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAllMembers();
    }

    @Override
    public List<Member> findAllEmployees() {
        return memberRepository.findAllEmployees();
    }

    @Override
    public String grantEmployeeAuth(Long member_id) {
        Member memberToEmployee = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("no exist id"));
        memberToEmployee.grantEmployeeAuth();
        memberRepository.save(memberToEmployee);

        return "직원 설정 완료";
    }
}
