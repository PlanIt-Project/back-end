package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.common.util.RedisUtil;
import com.sideProject.PlanIT.domain.user.dto.member.request.EmailValidationRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    private final RedisUtil redisUtil;

    private int authNumber;

    public String validEmail(EmailValidationRequestDto emailValidationRequestDto) {
        if (!emailValidationRequestDto.getValidCode().equals(redisUtil.getData(emailValidationRequestDto.getEmail()))) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_AUTH);
        }
        redisUtil.deleteData(emailValidationRequestDto.getEmail());
        return "인증 완료";
    }

    public String joinEmail(String email) {
        makeRandomNumber();
        String setFrom = "planitvalidation@gmail.com";
        String toMail = email;
        String title = "[PlanIT] 회원 가입 인증 이메일 입니다.";
        String content =
                "PlanIT에 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "인증 번호는 [" + authNumber + "]입니다." +
                        "<br>" +
                        "인증번호를 입력해주세요"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        redisUtil.setMailValidation(email, String.valueOf(authNumber));
        return "이메일 전송 완료";
    }

    private void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }

}
