package com.dzb.config;

import com.dzb.service.security.CustomUserServiceImpl;
import com.dzb.utils.MD5Util;
import com.dzb.utils.SHAUtil;
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
                .antMatchers("/login","/register")
                .permitAll()
                .antMatchers("/editor","/user","/","/calligraphy","/score").hasAnyRole("USER")
                .antMatchers("/teacher").hasAnyRole("ADMIN")
                .antMatchers("/admin").hasAnyRole("SUPERADMIN")
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/")
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");

        http.csrf().disable();
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
                SHAUtil md5Util = new SHAUtil();
                @Override
                public String encode(CharSequence rawPassword) {
                    return md5Util.SHA256Encrypt((String) rawPassword);
                }

                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return encodedPassword.equals(encode(rawPassword));
                }
            });
    }


}
