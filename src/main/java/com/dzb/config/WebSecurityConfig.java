package com.dzb.config;

import com.dzb.service.security.CustomUserServiceImpl;
import com.dzb.utils.MD5Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

//import com.dzb.service.security.CustomUserServiceImpl;

/**
 * @author: zhengbo.du
 * @Date: 2022/3/5 18:45
 * Describe: SpringSecurity配置
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean
    UserDetailsService customUserService(){
        return new CustomUserServiceImpl();
    }

    /**
     * 配置的是认证信息
     * AuthenticationManagerBuilder这个类是AuthenticationManager的建造者
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService())
            //启动MD5加密
            .passwordEncoder(new PasswordEncoder() {
                MD5Util md5Util = new MD5Util();
                @Override
                public String encode(CharSequence rawPassword) {
                    return md5Util.encode((String) rawPassword);
                }

                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return encodedPassword.equals(encode(rawPassword));
                }
            });
    }

    /**
     * 配置Security认证策略，每个模块配置使用and结尾
     * 其中authorizeRequests配置路径拦截，表明路径访问所对应的权限，角色，认证信息
     * formLogin对应表单认证的相关配置
     * logout对应了注销相关的配置
     * httpBasic可以配置basic登录
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/index","/aboutme","/archives","/friendlylink","/tags","/update","/categories","/test","/","/calligraphy")
                .permitAll()
                .antMatchers("/editor","/user").hasAnyRole("USER")
                .antMatchers("/ali","/mylove").hasAnyRole("ADMIN")
                .antMatchers("/superadmin","/myheart","/today","/yesterday").hasAnyRole("SUPERADMIN")
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/")
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");

        http.csrf().disable();
    }
}
