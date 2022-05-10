package com.dzb.aspect;

import com.dzb.aspect.annotation.PermissionCheck;
import com.dzb.constant.CodeType;
import com.dzb.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author : zhengbo.du
 * @date : 2022/3/4 18:07
 */
@Aspect
@Component
@Slf4j
public class PrincipalAspect {

    public static final String ANONYMOUS_USER = "anonymousUser";

    @Pointcut("execution(public * com.dzb.controller..*(..))")
    public void login(){

    }

    @Around("login() && @annotation(permissionCheck)")
    public Object principalAround(ProceedingJoinPoint pjp, PermissionCheck permissionCheck) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loginName = auth.getName();
        if (loginName.equals(ANONYMOUS_USER)){
            return JsonResult.fail(CodeType.USER_NOT_LOGIN).toJSON();
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String value = permissionCheck.value();
        for (GrantedAuthority g : authorities){
            if (g.getAuthority().equals(value)){
                return pjp.proceed();
            }
        }
        log.error("[{}] has no access to the [{}] method ",loginName,pjp.getSignature().getName());
        return JsonResult.fail(CodeType.PERMISSION_VERIFY_FAIL).toJSON();
    }
}
