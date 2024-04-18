package com.sideProject.PlanIT.domain.controller_test_module;

import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomSecurityContextFactory.class)
public @interface WithMockCustomMember {
    String first() default "name";

    MemberRole second() default MemberRole.MEMBER;
}