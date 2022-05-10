package com.dzb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author: zhengbo.du
 * @Date: 2022/3/6 20:03
 * Describe: 定制错误页面
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    /**
     * WebMvcConfigurerAdaoter是spring的一中配置方式
     * 其中常见的方法如：
     * addInterceptors拦截器，拦截过滤路径规则
     * addCorsMapping跨域问题
     * addViewControllers跳转指定页面，无需使用controller控制类去控制跳转，如registry.addViewController("/toLogin").setViewName("login")
     * resourceViewResolver视图解析器，配置请求视图映射
     * configureMessageConverters信息转换器，配置fastJson返回json转换
     * addResourceHandlers静态资源，指定静态资源的位置
     * @param registry
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源映射
        registry.addResourceHandler("/article/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/template/**").addResourceLocations("classpath:/static/");
    }

}
