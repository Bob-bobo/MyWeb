package com.dzb.mapper;

import com.dzb.model.Role;
import com.dzb.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 0:04
 */
@Mapper
@Repository
public interface UserMapper {

    @Select("select * from user_tab where phone=#{phone}")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "phone", property = "role", javaType = List.class,
                    many = @Many(select = "com.dzb.mapper.UserMapper.getRoleNameByPhone"))
    })
    User getUsernameAndRolesByPhone(@Param("phone") String phone);

    @Select("select r.role from user_tab u left join user_role_tab sru on u.id = sru.userId" +
            " left join role_tab r on sru.roleId = r.id where phone = #{phone}")
    Role getRoleNameByPhone(String phone);

    @Select("select * from user_tab where phone = #{phone}")
    User findUserByPhone(@Param("phone") String phone);

    @Select("select username from user_tab where id = #{id}")
    String findUsernameById(int id);

    @Insert("insert into user_tab(phone,username,password) " +
            "values(#{phone},#{username},#{password})")
    void save(User user);

    @Select("select * from user_tab where phone = #{phone}")
    User findUsernameByPhone(@Param("phone") String phone);

    @Select("select * from user_tab where username = #{username}")
    User findUsernameByUsername(@Param("username") String username);

    @Insert("insert into user_role_tab(userid,roleid) values (#{userid},#{roleid})")
    void saveRole(@Param("userid") int userid,@Param("roleid") int roleid);

    @Select("select roleid from user_role_tab where userid = #{userid}")
    List<Object> findRoleIdByUserId(@Param("userid") int userid);

    @Select("select id from user_tab where phone = #{phone}")
    int findUserIdByPhone(@Param("phone") String phone);

    @Update("update user_tab set password = #{password} where phone = #{phone}")
    void updatePassword(@Param("phone") String phone,@Param("password") String password);

    @Select("select phone from user_tab where username = #{username}")
    String findPhoneByUsername(@Param("username") String username);

    @Select("select id from user_tab where username = #{username}")
    int findIdByUsername(@Param("username") String username);

    @Update("update user_tab set recent_login = #{recent_login} where phone = #{phone}")
    void updateRecentLogin(@Param("phone") String phone,@Param("recent_login")int recent_login);

    @Update("update user_tab set avatar = #{avatar} where id = #{id}")
    void updateAvatarImgUrlById(@Param("id") int id,@Param("avatar") String avatar);

    @Select("select avatar from user_tab where id = #{id}")
    String getAvatarUrl(@Param("id") int id);

    @Select("select personal from user_tab where username = #{username}")
    User getUserPersonalByUsername(@Param("username")String username);

    @Update("update user_tab set username = #{user.username},true_name = #{user.trueName},birthday = #{user.birthday}," +
            "email = #{user.email},personal = #{user.personal} where username = #{username}")
    void savePersonInfo(@Param("user") User user,@Param("username") String username);

    @Select("select count('id') from user_tab")
    int countUserNum();
}
