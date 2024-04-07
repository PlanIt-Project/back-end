package com.sideProject.PlanIT.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.PlanIT.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String accept = request.getHeader("Accept");
        log.info(accept);

        ApiResponse<?> error = ApiResponse.error(401," Authentication information not found.");

        String result = mapper.writeValueAsString(error);

        response.setStatus(401);
        response.getWriter().write(result);
    }
}
