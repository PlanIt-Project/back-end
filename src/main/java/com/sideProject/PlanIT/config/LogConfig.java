package com.sideProject.PlanIT.config;

import com.sideProject.PlanIT.common.loging.LogTraceAspect;
import com.sideProject.PlanIT.common.loging.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
