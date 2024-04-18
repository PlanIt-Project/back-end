package com.sideProject.PlanIT.domain.controller_test_module;

import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.enums.Gender;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;
import java.util.Collections;

public class WithMockCustomSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Member member = new Member(annotation.first(),"test@test.com","test","01000000000",null,"",annotation.second(), Gender.MALE);
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString()));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getId(), null, authorities);
        context.setAuthentication(authenticationToken);

        return context;
    }
}
