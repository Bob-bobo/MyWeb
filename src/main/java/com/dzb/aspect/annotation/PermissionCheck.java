package com.dzb.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author : zhengbo.du
 * @date : 2022/3/4 18:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCheck {

    String value();
}
