package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.util.JwtTokenProvider;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.common.util.RedisUtil;
import com.sideProject.PlanIT.domain.user.controller.enums.MemberSearchOption;
import com.sideProject.PlanIT.domain.user.dto.employee.request.TrainerRequestDto;
import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberChangePasswordRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignInRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.MemberSignUpRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ActiveProfiles("prod")
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RedisUtil redisUtil;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        redisUtil.deleteAll();
    }

    private Member initMember(String name, String password, MemberRole memberRole) {
        Member member = Member.builder()
                .name(name)
                .email(name + "@naver.com")
                .password(password)
                .birth(LocalDate.parse("2000-01-01", DateTimeFormatter.ISO_DATE))
                .phone_number("010-0000-0000")
                .address("서울")
                .role(memberRole)
                .build();

        return memberRepository.save(member);
    }

    @Nested
    @DisplayName("signUpTest")
    class signUpTest {

        @DisplayName("모든 정보를 받아 회원 가입을 한다.")
        @Test
        void signUp() {
            // given
            // when
            // then
            // given
            MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto.builder()
                    .email("test1@naver.com")
                    .password("test1234")
                    .name("test")
                    .phone_number("123456789")
                    .birth(LocalDate.of(2024, 03, 25))
                    .address("서울")
                    .build();

            // when
            Member member = memberService.signUp(memberSignUpRequestDto);

            // then
            assertThat(member).isNotNull();
            assertThat(member.getEmail()).isEqualTo("test1@naver.com");
            assertThat(member.getName()).isEqualTo("test");
            assertThat(member.getPhone_number()).isEqualTo("123456789");
            assertThat(member.getBirth()).isEqualTo("2024-03-25");
            assertThat(member.getAddress()).isEqualTo("서울");
            assertThat(passwordEncoder.matches("test1234", member.getPassword())).isTrue();
        }

        @DisplayName("이메일이 이미 존재하면 오류가 발생한다")
        @Test
        void signUp2() {
            // given
            MemberSignUpRequestDto memberSignUpRequestDto1 = MemberSignUpRequestDto.builder()
                    .email("test1@naver.com")
                    .password("test1234")
                    .name("test1")
                    .build();

            MemberSignUpRequestDto memberSignUpRequestDto2 = MemberSignUpRequestDto.builder()
                    .email("test1@naver.com")
                    .password("test5678")
                    .name("test2")
                    .build();

            memberService.signUp(memberSignUpRequestDto1);

            // when, then
            assertThatThrownBy(() -> memberService.signUp(memberSignUpRequestDto2))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("이미 존재하는 이메일입니다.");
        }

        @DisplayName("비밀번호를 입력하지 않으면 오류가 발생한다")
        @Test
        void signUp3() {
            // given
            MemberSignUpRequestDto memberSignUpRequestDto1 = MemberSignUpRequestDto.builder()
                    .email("test1@naver.com")
                    .name("test1")
                    .build();

            // when, then
            assertThatThrownBy(() -> memberService.signUp(memberSignUpRequestDto1))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("비밀번호를 입력해주세요.");
        }
    }

    @Nested
    @DisplayName("signInTest")
    class signInTest {

        @DisplayName("로그인을 하고 토큰을 받는다.")
        @Test
        void signIn() {
            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            MemberSignInRequestDto memberSignInRequestDto = MemberSignInRequestDto.builder()
                    .email("test1@naver.com")
                    .password("test1234")
                    .build();

            // when
            JwtResponseDto jwtResponseDto = memberService.memberValidation(memberSignInRequestDto);

            // then
            assertThat(jwtResponseDto.getAccessToken()).isNotNull();
            assertThat(jwtResponseDto.getRefreshToken()).isNotNull();
            assertThat(jwtUtil.getMemberIdFromToken(jwtResponseDto.getAccessToken())).isEqualTo(memberRepository.findByEmail("test1@naver.com").get().getId());
            assertThat(jwtUtil.getMemberRoleFromToken(jwtResponseDto.getAccessToken())).isEqualTo(memberRepository.findByEmail("test1@naver.com").get().getRole().toString());
            assertThat(redisUtil.getData(jwtResponseDto.getRefreshToken())).isEqualTo(memberRepository.findByEmail("test1@naver.com").get().getId().toString());
        }

        @DisplayName("이메일이 없으면 오류가 발생한다")
        @Test
        void signIn2() {
            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberSignInRequestDto memberSignInRequestDto = MemberSignInRequestDto.builder()
                    .email("test2@naver.com")
                    .password("test1234")
                    .build();

            // then
            assertThatThrownBy(() -> memberService.memberValidation(memberSignInRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("회원을 찾을 수 없습니다");
        }

        @DisplayName("비밀번호가 틀리면 오류가 발생한다")
        @Test
        void signIn3() {
            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberSignInRequestDto memberSignInRequestDto = MemberSignInRequestDto.builder()
                    .email("test1@naver.com")
                    .password("test12345")
                    .build();

            // then
            assertThatThrownBy(() -> memberService.memberValidation(memberSignInRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("비밀번호가 틀렸습니다.");
        }
    }

    @Nested
    @DisplayName("deleteMemberTest")
    class deleteMemberTest {

        @DisplayName("member_id로 member를 삭제한다")
        @Test
        void deleteMember() {

            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            String result = memberService.deleteMember(memberRepository.findByEmail("test1@naver.com").get().getId());

            // then
            assertThat(memberRepository.findByEmail("test1").isPresent()).isFalse();
            assertThat(result).isEqualTo("삭제 완료");
        }
    }

    @Nested
    @DisplayName("editMemberTest")
    class editMemberTest {

        @DisplayName("사용자 정보를 수정한다")
        @Test
        void editMember() {

            // given
            memberRepository.save(Member.builder()
                            .email("test1@naver.com")
                            .password("test1234")
                            .name("test1")
                            .phone_number("010-0000-0000")
                            .birth(LocalDate.of(2024, 12, 31))
                            .address("서울")
                            .role(MemberRole.MEMBER)
                            .build());
            MemberEditRequestDto memberEditRequestDto = MemberEditRequestDto.builder()
                    .name("test2")
                    .phone_number("010-1234-5678")
                    .birth(LocalDate.of(2001, 1, 1))
                    .address("경기")
                    .build();

            // when
            String result = memberService.editMember(memberRepository.findByEmail("test1@naver.com").get().getId(), memberEditRequestDto);

            // then
            Member member = memberRepository.findByEmail("test1@naver.com").get();
            assertThat(member.getName()).isEqualTo("test2");
            assertThat(member.getPhone_number()).isEqualTo("010-1234-5678");
            assertThat(member.getBirth()).isEqualTo("2001-01-01");
            assertThat(member.getAddress()).isEqualTo("경기");
            assertThat(result).isEqualTo("수정 완료");
        }
    }

    @Nested
    @DisplayName("changePasswordTest")
    class changePasswordTest {

        @DisplayName("비밀번호를 변경한다")
        @Test
        void changePassword() {

            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberChangePasswordRequestDto memberChangePasswordRequestDto = MemberChangePasswordRequestDto.builder()
                    .currentPassword("test1234")
                    .newPassword("test5678")
                    .newPasswordCheck("test5678")
                    .build();
            String result = memberService.changePassword(memberRepository.findByEmail("test1@naver.com").get().getId(), memberChangePasswordRequestDto);

            // then
            assertThat(passwordEncoder.matches("test5678", memberRepository.findByEmail("test1@naver.com").get().getPassword())).isTrue();
            assertThat(result).isEqualTo("비밀번호 변경 완료");
        }

        @DisplayName("현재 비밀번호가 틀리면 오류가 발생한다")
        @Test
        void changePassword2() {

            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberChangePasswordRequestDto memberChangePasswordRequestDto = MemberChangePasswordRequestDto.builder()
                    .currentPassword("test5678")
                    .newPassword("test5678")
                    .newPasswordCheck("test5678")
                    .build();

            // then
            assertThatThrownBy(() -> memberService.changePassword(memberRepository.findByEmail("test1@naver.com").get().getId(), memberChangePasswordRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("비밀번호가 틀렸습니다.");
        }

        @DisplayName("변경할 비밀번호와 변경할 비밀번호 재입력이 다르면 오류가 발생한다")
        @Test
        void changePassword3() {

            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberChangePasswordRequestDto memberChangePasswordRequestDto = MemberChangePasswordRequestDto.builder()
                    .currentPassword("test1234")
                    .newPassword("test5678")
                    .newPasswordCheck("test0000")
                    .build();

            // then
            assertThatThrownBy(() -> memberService.changePassword(memberRepository.findByEmail("test1@naver.com").get().getId(), memberChangePasswordRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("비밀번호가 틀렸습니다.");
        }

        @DisplayName("기존 비밀번호와 변경할 비밀번호가 같으면 오류가 발생한다")
        @Test
        void changePassword4() {

            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberChangePasswordRequestDto memberChangePasswordRequestDto = MemberChangePasswordRequestDto.builder()
                    .currentPassword("test1234")
                    .newPassword("test1234")
                    .newPasswordCheck("test1234")
                    .build();

            // then
            assertThatThrownBy(() -> memberService.changePassword(memberRepository.findByEmail("test1@naver.com").get().getId(), memberChangePasswordRequestDto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("변경 비밀번호가 같습니다.");
        }
    }

    @Nested
    @DisplayName("findMemberTest")
    class findMemberTest {

        @DisplayName("member_id로 멤버를 조회한다")
        @Test
        void findMember() {

            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);

            // when
            MemberResponseDto memberResponseDto = memberService.findMember(memberRepository.findByEmail("test1@naver.com").get().getId());

            // then
            assertThat(memberResponseDto.getName()).isEqualTo("test1");
            assertThat(memberResponseDto.getAddress()).isEqualTo("서울");
            assertThat(memberResponseDto.getBirth()).isEqualTo("2000-01-01");
            assertThat(memberResponseDto.getPhone_number()).isEqualTo("010-0000-0000");
            assertThat(memberResponseDto.getRole()).isEqualTo(MemberRole.MEMBER);
        }

        @DisplayName("member_id로 조회한 멤버가 트레이너면 트레이너 정보와 함께 조회한다")
        @Test
        void findMemberTrainer() {

            // given
            memberRepository.save(Member.builder()
                            .email("test1@naver.com")
                            .password("test1234")
                            .name("test1")
                            .birth(LocalDate.of(2000, 1, 1))
                            .address("서울")
                            .phone_number("010-0000-0000")
                            .role(MemberRole.TRAINER)
                    .build());
            employeeRepository.save(Employee.builder()
                            .member(memberRepository.findByEmail("test1@naver.com").get())
                            .career("P1Y2M3D")
                            .trainerMessage("test trainer message")
                    .build());

            // when
            MemberResponseDto memberResponseDto = memberService.findMember(memberRepository.findByEmail("test1@naver.com").get().getId());

            // then
            assertThat(memberResponseDto.getEmail()).isEqualTo("test1@naver.com");
            assertThat(memberResponseDto.getName()).isEqualTo("test1");
            assertThat(memberResponseDto.getAddress()).isEqualTo("서울");
            assertThat(memberResponseDto.getPhone_number()).isEqualTo("010-0000-0000");
            assertThat(memberResponseDto.getBirth()).isEqualTo("2000-01-01");
            assertThat(memberResponseDto.getRole()).isEqualTo(MemberRole.TRAINER);
            assertThat(memberResponseDto.getTrainerInfo().getCareer()).isEqualTo("P1Y2M3D");
            assertThat(memberResponseDto.getTrainerInfo().getTrainerMessage()).isEqualTo("test trainer message");
        }

    }

    @Nested
    @DisplayName("signOutTest")
    class signOutTest {

        @DisplayName("로그아웃을 하고 refresh token을 삭제한다")
        @Test
        void signOut() {

            // given
            Member member = initMember("test1", "test1234", MemberRole.MEMBER);
            String refreshToken = jwtTokenProvider.createRefreshToken(member);

            // when
            String result = memberService.signOut(memberRepository.findByEmail("test1@naver.com").get().getId());

            // then
            assertThat(redisUtil.isExist(refreshToken)).isFalse();
            assertThat(result).isEqualTo("로그아웃 성공");
        }
    }

    @Nested
    @DisplayName("findTest")
    class findTest {

        @DisplayName("모든 사용자를 조회한다.")
        @Test
        void find() {

            // given
            initMember("test1", "test1234", MemberRole.MEMBER);
            initMember("test2", "test1234", MemberRole.MEMBER);
            initMember("test3", "test1234", MemberRole.MEMBER);
            initMember("test4", "test1234", MemberRole.TRAINER);
            initMember("test5", "test1234", MemberRole.TRAINER);

            // when
            Pageable pageable = PageRequest.of(1, 3);
            Page<MemberResponseDto> result = memberService.find(MemberSearchOption.ALL, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(5);
            assertThat(result.getTotalPages()).isEqualTo(2);
            assertThat(result.getContent().size()).isEqualTo(2);
        }

        @DisplayName("모든 회원를 조회한다.")
        @Test
        void find2() {

            // given
            initMember("test1", "test1234", MemberRole.MEMBER);
            initMember("test2", "test1234", MemberRole.MEMBER);
            initMember("test3", "test1234", MemberRole.MEMBER);
            initMember("test4", "test1234", MemberRole.TRAINER);
            initMember("test5", "test1234", MemberRole.TRAINER);

            // when
            Pageable pageable = PageRequest.of(0, 3);
            Page<MemberResponseDto> result = memberService.find(MemberSearchOption.MEMBER, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(3);
        }

        @DisplayName("모든 트레이너를 조회한다.")
        @Test
        void find3() {

            // given
            initMember("test1", "test1234", MemberRole.MEMBER);
            initMember("test2", "test1234", MemberRole.MEMBER);
            initMember("test3", "test1234", MemberRole.MEMBER);
            initMember("test4", "test1234", MemberRole.TRAINER);
            initMember("test5", "test1234", MemberRole.TRAINER);

            // when
            Pageable pageable = PageRequest.of(0, 3);
            Page<MemberResponseDto> result = memberService.find(MemberSearchOption.TRAINER, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("findAllEmployeesTest")
    class findAllEmployeesTest {

        @DisplayName("전체 트레이너를 조회합니다.")
        @Test
        void findAllEmployees() {

            // given
            employeeRepository.save(Employee.builder()
                    .member(initMember("test1", "test1234", MemberRole.TRAINER))
                    .career("P1Y2M3D")
                    .trainerMessage("test trainer messsage")
                    .build());
            employeeRepository.save(Employee.builder()
                    .member(initMember("test2", "test1234", MemberRole.TRAINER))
                    .career("P1Y2M3D")
                    .trainerMessage("test trainer messsage")
                    .build());

            // when
            Pageable pageable = PageRequest.of(0, 2);
            Page<TrainerResponseDto> result = memberService.findAllEmployees(pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("grantEmployeeTest")
    class grantEmployeeTest {

        @DisplayName("회원에게 트레이너 권한을 부여한다")
        @Test
        void grantEmployee() {
            // given
            initMember("test1", passwordEncoder.encode("test1234"), MemberRole.MEMBER);
            TrainerRequestDto trainerRequestDto = TrainerRequestDto.builder()
                    .career("P1Y2M3D")
                    .trainerMessage("test trainer message")
                    .build();

            // when
            memberService.grantEmployeeAuth(memberRepository.findByEmail("test1@naver.com").get().getId(), trainerRequestDto);

            // then
            assertThat(memberRepository.findByEmail("test1@naver.com").get().getRole()).isEqualTo(MemberRole.TRAINER);
        }
    }
}
