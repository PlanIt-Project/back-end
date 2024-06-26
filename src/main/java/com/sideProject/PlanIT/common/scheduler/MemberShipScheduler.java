package com.sideProject.PlanIT.common.scheduler;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.repository.ProgramRepository;
import com.sideProject.PlanIT.domain.program.service.ProgramService;
import com.sideProject.PlanIT.domain.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberShipScheduler {
    private final ProgramRepository programRepository;
    private final EmailService emailService;
    private final ProgramService programService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void memberShipEndTimeEvent() {
        log.info("이메일 전송");
        LocalDate nowDate = LocalDate.now();

        LocalDate afterOneWeek = nowDate.plusWeeks(1);
        LocalDate afterOneMonth = nowDate.plusMonths(1);

        sendMailToOneWeekLeft(afterOneWeek);
        sendMailToOneMonthLeft(afterOneMonth);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void memberShipExpireEvent() {
        LocalDate toDay = LocalDate.now();
        log.info("{} 만료된 일정 처리",toDay.atStartOfDay());

        programService.expiredMemberShipProgram(toDay);
    }

    private void sendMailToOneWeekLeft(LocalDate afterOneWeek) {
        List<Program> oneWeekLefts = programRepository.findMembershipProgramsByEndAtAndProductType(afterOneWeek, ProductType.MEMBERSHIP);

        oneWeekLefts.forEach(program -> {
            log.info("program = {} 만료기간 일주일 남았습니다.", program.getId());
            emailService.memberShipEmail(program.getMember().getEmail(), "일주일");
        });
    }

    private void sendMailToOneMonthLeft(LocalDate afterOneMonth) {
        List<Program> oneWeekLefts = programRepository.findMembershipProgramsByEndAtAndProductType(afterOneMonth, ProductType.MEMBERSHIP);

        oneWeekLefts.forEach(program -> {
            log.info("program = {} 만료기간 한달 남았습니다.", program.getId());
            emailService.memberShipEmail(program.getMember().getEmail(), "한 달");
        });
    }



}
