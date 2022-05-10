package com.dzb.service.security;

import com.dzb.mapper.UserMapper;
import com.dzb.model.Role;
import com.dzb.model.User;
import com.dzb.service.UserService;
import com.dzb.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhengbo.du
 * @date : 2022/3/4 19:08
 */
public class CustomUserServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        User user = userMapper.getUsernameAndRolesByPhone(phone);

        if (user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        TimeUtil timeUtil = new TimeUtil();
        int recentlyLanded = (int) timeUtil.getLongTime();
        userService.updateRecentLogin(user.getUsername(),recentlyLanded);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getRole()){
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }
}
