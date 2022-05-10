package com.dzb.model;

import com.dzb.utils.TimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 0:12
 */
@Data
@NoArgsConstructor
public class User {

    private int id;

    private String phone;

    private String username;

    private String password;

    private String trueName;

    private String birthday;

    private String email;

    private String personal;

    private String avatar;

    private int recentLogin;

    private List<Role> role;

    public User(String phone,String username,String password){
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    public User(String phone,String username,String password,String trueName,String birthday,
                String email,String avatar){
        TimeUtil time = new TimeUtil();
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.trueName = trueName;
        this.birthday = birthday;
        this.email = email;
        this.avatar = avatar;
        this.recentLogin = (int)time.getLongTime();
    }

}
