package com.dzb.service.Impl;

import com.dzb.constant.CodeType;
import com.dzb.constant.RoleConstant;
import com.dzb.mapper.UserMapper;
import com.dzb.model.User;
import com.dzb.service.UserService;
import com.dzb.utils.DataMap;
import com.dzb.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Random;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 14:38
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User findUserByPhone(String phone) {
        return userMapper.findUserByPhone(phone);
    }

    @Override
    public void updatePasswordByPhone(String phone,String password) {
        userMapper.updatePassword(phone,password);
        //修改完密码后，注销当前用户
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public int findUseridByPhone(String phone) {
        return 0;
    }

    @Override
    public User findUsernameByPhone(String phone) {
        return userMapper.findUsernameByPhone(phone);
    }

    @Override
    public String getAvatarUrlById(int id) {
        return userMapper.getAvatarUrl(id);
    }

    @Override
    public String findUsernameById(int id) {
        return userMapper.findUsernameById(id);
    }

    @Override
    public void updateAvatarUrlById(String avatarUrl, int id) {
        userMapper.updateAvatarImgUrlById(id,avatarUrl);
    }

    @Override
    public DataMap insert(User user) {

        user.setUsername(user.getUsername().trim().replaceAll("", StringUtil.BLANK));
        String username = user.getUsername();

        if (username.length() > StringUtil.USERNAME_MAX_LENGTH || StringUtil.BLANK.equals(username)){
            return DataMap.fail(CodeType.USERNAME_FORMAT_ERROR);
        }

        if (userIsExist(user.getPhone())){
            return DataMap.fail(CodeType.PHONE_EXIST);
        }

        Random r = new Random();
        user.setAvatar("https://mybobbucket.oss-cn-beijing.aliyuncs.com/myweb/userAvatar/"+r.nextInt(13)+".jpg");

        userMapper.save(user);
        int userid = userMapper.findUserIdByPhone(user.getPhone());
        insertRole(userid, RoleConstant.ROLE_USER);

        return DataMap.success();
    }

    @Override
    public void findPhoneByUsername(String username) {
        userMapper.findPhoneByUsername(username);
    }

    @Override
    public int findIdByUsername(String username) {
        return userMapper.findIdByUsername(username);
    }

    @Override
    public void updateRecentLogin(String username, int recentLogin) {
        String phone = userMapper.findPhoneByUsername(username);
        userMapper.updateRecentLogin(phone,recentLogin);
    }

    @Override
    public boolean usernameIsExist(String username) {
        User user = userMapper.findUsernameByUsername(username);
        return user != null;
    }

    @Override
    public boolean isSuperAdmin(String phone) {
        int userid = userMapper.findUserIdByPhone(phone);
        List<Object> roleids = userMapper.findRoleIdByUserId(userid);

        for (Object i : roleids){
            if ((int) i == 4){
                return true;
            }
        }
        return false;
    }

    @Override
    public DataMap getAvatarUrl(int id) {
        String avatarUrl = userMapper.getAvatarUrl(id);
        return DataMap.success().setData(avatarUrl);
    }

    @Override
    public DataMap savePersonalInfo(User user, String username) {
        user.setUsername(user.getUsername().trim().replaceAll("",StringUtil.BLANK));
        String newName = user.getUsername();

        if (newName.length() > StringUtil.USERNAME_MAX_LENGTH){
            return DataMap.fail(CodeType.USERNAME_FORMAT_ERROR);
        } else if (StringUtil.BLANK.equals(newName)){
            return DataMap.fail(CodeType.USERNAME_BLANK);
        }

        int status;
        if (!newName.equals(username)){
            if (usernameIsExist(newName)){
                return DataMap.fail(CodeType.USERNAME_EXIST);
            }
            status = CodeType.HAS_MODIFY_USERNAME.getCode();
            SecurityContextHolder.getContext().setAuthentication(null);
        } else {
            status = CodeType.NOT_MODIFY_USERNAME.getCode();
        }
        userMapper.savePersonInfo(user,username);
        return DataMap.success(status);
    }

    @Override
    public DataMap getUserPersonalInfo(String username) {
        User user = userMapper.getUserPersonalByUsername(username);
        return DataMap.success().setData(user);
    }

    @Override
    public int countUserNum() {
        return userMapper.countUserNum();
    }

    /**
     * 查看用户是否存在，存在则无法插入
     * @param phone
     * @return
     */
    public boolean userIsExist(String phone){
        User user = userMapper.findUserByPhone(phone);
        return user != null;
    }

    /**
     * 增加用户角色权限
     * @param userid
     * @param roleid
     */
    private void insertRole(int userid,int roleid){
        userMapper.saveRole(userid,roleid);
    }
}
