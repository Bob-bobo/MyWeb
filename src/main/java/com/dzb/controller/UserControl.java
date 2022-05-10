package com.dzb.controller;

import com.dzb.aspect.annotation.PermissionCheck;
import com.dzb.constant.CodeType;
import com.dzb.service.UserService;
import com.dzb.utils.DataMap;
import com.dzb.utils.FileUtil;
import com.dzb.utils.JsonResult;
import com.dzb.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 15:53
 */
@RestController
@Slf4j
public class UserControl {

    @Autowired
    UserService userService;

    /**
     * 上传头像
     * @param request
     * @param authentication
     * @return
     */
    @PostMapping(value = "/uploadAvatar",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PermissionCheck(value = "ROLE_USER")
    public String uploadAvatar(HttpServletRequest request,
                               Authentication authentication){
        try{
            String username = authentication.getName();
            String img = request.getParameter("img");
            //获取上传文件的后缀名
            int index = img.indexOf(";base64,");
            String strFileExtendName = "." + img.substring(11,index);
            img = img.substring(index + 8);

            FileUtil fileUtil = new FileUtil();
            String filePath = this.getClass().getResource("/").getPath().substring(1) + "userImg/";
            TimeUtil timeUtil = new TimeUtil();
            File file = fileUtil.base64ToFile(filePath,img,timeUtil.getLongTime() + strFileExtendName);
            String url = fileUtil.uploadFile(file,"user/avatar/username");
            int userid = userService.findIdByUsername(username);
            userService.updateAvatarUrlById(url,userid);

            DataMap data = userService.getAvatarUrl(userid);
            return JsonResult.build(data).toJSON();
        }catch (Exception e){
            log.error("Upload head picture exception",e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }

    /**
     * 获取个人信息
     * @param authentication
     * @return
     */
    @PostMapping(value = "/getUserPersonalInfo",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PermissionCheck(value = "ROLE_USER")
    public String getUserPersonalInfo(Authentication authentication) {
        String username = authentication.getName();
        try {
            DataMap data = userService.getUserPersonalInfo(username);
            return JsonResult.build(data).toJSON();
        } catch (Exception e) {
            log.error("[{}] get user personal info exception", username, e);
        }
        return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
    }
}


