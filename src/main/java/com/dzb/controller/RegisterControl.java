package com.dzb.controller;

import com.dzb.aspect.PrincipalAspect;
import com.dzb.constant.CodeType;
import com.dzb.model.User;
import com.dzb.redis.StringRedisServiceImpl;
import com.dzb.service.UserService;
import com.dzb.utils.DataMap;
import com.dzb.utils.JsonResult;
import com.dzb.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author : zhengbo.du
 * @date : 2022/3/6 10:08
 */
@RestController
@Slf4j
public class RegisterControl {

    @Autowired
    UserService userService;
    @Autowired
    StringRedisServiceImpl stringRedisService;

    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String register(User user,
                           HttpServletRequest request){
        try{
//            String authCode = request.getParameter("authCode");
//
//            String trueMsgCode = (String) stringRedisService.get(user.getPhone());
//
//            //判断手机号是否正确
//            if (trueMsgCode == null){
//                return JsonResult.fail(CodeType.PHONE_ERROR).toJSON();
//            }
//            //判断验证码是否正确
//            if (!authCode.equals(trueMsgCode)){
//                return JsonResult.fail(CodeType.AUTH_CODE_ERROR).toJSON();
//            }
            //判断用户名是否存在
            if (userService.usernameIsExist(user.getUsername()) || user.getUsername().equals(PrincipalAspect.ANONYMOUS_USER)){
                return JsonResult.fail(CodeType.USERNAME_EXIST).toJSON();
            }
            //注册时对密码进行MD5加密
            MD5Util md5Util = new MD5Util();
            user.setPassword(md5Util.encode(user.getPassword()));

            //注册结果
            DataMap data = userService.insert(user);
//            if (0 == data.getCode()){
//                stringRedisService.remove(user.getPhone());
//            }
            return JsonResult.build(data).toJSON();
        }catch (Exception e){
            log.error("User [{}] register exception",user,e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
