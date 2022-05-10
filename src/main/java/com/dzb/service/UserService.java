package com.dzb.service;

import com.dzb.model.User;
import com.dzb.utils.DataMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 14:17
 */
public interface UserService {
    /**
     * 通过手机号查询注册的用户
     * @param phone
     * @return
     */
    User findUserByPhone(String phone);

    /**
     * 通过手机号修改密码
     * @param phone
     */
    void updatePasswordByPhone(String phone,String password);

    /**
     * 通过手机号查询用户id
     * @param phone
     * @return
     */
    int findUseridByPhone(String phone);

    /**
     * 通过手机号查询用户
     * @param phone
     * @return
     */
    User findUsernameByPhone(String phone);

    /**
     * 通过id查询头像
     * @param id
     * @return
     */
    String getAvatarUrlById(int id);
    /**
     * 通过id查询用户名
     * @param id
     * @return
     */
    String findUsernameById(int id);

    /**
     * 通过id更改头像
     * @param avatarUrl
     * @param id
     */
    @Transactional
    void updateAvatarUrlById(String avatarUrl,int id);

    /**
     * 注册用户，事务加成确保一致性
     * @param user
     * @return
     */
    @Transactional
    DataMap insert(User user);

    /**
     * 通过用户名获得手机号
     * @param username
     */
    void findPhoneByUsername(String username);

    /**
     * 通过用户名查询用户id
     * @param username
     * @return
     */
    int findIdByUsername(String username);

    /**
     * 更改最新登录时间
     * @param username
     * @param recentLogin
     */
    void updateRecentLogin(String username,int recentLogin);

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean usernameIsExist(String username);

    /**
     * 通过手机号判断是否为超级管理员
     * @param phone
     * @return
     */
    boolean isSuperAdmin(String phone);

    /**
     * 获取头像，上传头像后，验证是否存在并返回给前端
     * @param id
     * @return
     */
    DataMap getAvatarUrl(int id);

    /**
     * 保存用户个人信息
     * @param user
     * @param username
     * @return
     */
    DataMap savePersonalInfo(User user,String username);

    /**
     * 获取个人信息
     * @param username
     * @return
     */
    DataMap getUserPersonalInfo(String username);
    /**
     * 统计用户总量
     * @return
     */
    int countUserNum();
}
