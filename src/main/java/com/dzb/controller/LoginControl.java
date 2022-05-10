package com.dzb.controller;

import com.dzb.aspect.annotation.PermissionCheck;
import com.dzb.constant.CodeType;
import com.dzb.mapper.UserMapper;
import com.dzb.model.User;
import com.dzb.redis.StringRedisServiceImpl;
import com.dzb.service.UserService;
import com.dzb.utils.JsonResult;
import com.dzb.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 18:43
 * Describe: 登录控制
 */
@RestController
@Slf4j
public class LoginControl {

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    StringRedisServiceImpl stringRedisService;

    @PostMapping(value = "/changePassword",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String changePassword(@RequestParam("phone") String phone,
                                 @RequestParam("authCode") String authCode,
                                 @RequestParam("newPasswrod") String newPasswrod){
        try{
            String trueMsgCode = (String) stringRedisService.get(phone);

            //手机号是否是发送验证码的手机号
            if (trueMsgCode == null){
                return JsonResult.fail(CodeType.PHONE_ERROR).toJSON();
            }
            //验证码是否正确
            if (!authCode.equals(trueMsgCode)){
                return JsonResult.fail(CodeType.AUTH_CODE_ERROR).toJSON();
            }
            User user = userService.findUserByPhone(phone);
            if (user == null){
                return JsonResult.fail(CodeType.USERNAME_NOT_EXIST).toJSON();
            }
            MD5Util md5Util = new MD5Util();
            String md5Password = md5Util.encode(newPasswrod);
            userService.updatePasswordByPhone(phone,newPasswrod);

            //修改密码成功后删除redis中的验证码
            stringRedisService.remove(phone);

            return JsonResult.success().toJSON();
        }catch (Exception e){
            log.error("[{}] change password [{}] exception",phone,newPasswrod,e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}
