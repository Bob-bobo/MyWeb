package com.dzb.mapper;

import com.dzb.model.Calligraphy;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : zhengbo.du
 * @date : 2022/3/7 23:36
 */
@Mapper
@Repository
public interface CalligraphyMapper {

    //保存书法鉴定表
    @Insert("insert into calligraphy_tab(username, pre_url, upload_time, after_url," +
            "dispose_time, remark_content, score, score_detail, descri,title,category,likes,last_call_id,next_call_id)" +
            "values (#{username},#{preUrl},#{uploadTime},#{afterUrl},#{disposeTime}) " +
            "#{remarkContent},#{score},#{scoreDetail},#{descri},#{title},#{category},#{likes},#{last_call_id},#{next_call_id}"  )
    void save(Calligraphy calligraphy);

    //更新书法，暂时不用
    @Update("")
    void updateCalligraphyById(Calligraphy calligraphy);

    //通过id找书法表
    @Select("select id,user_id,pre_url,upload_time,after_url,dispose_time,remark_content,score,score_detail,descri,title,category,likes from calligraphy_tab where id = #{id}")
    Calligraphy findCalligraphyById(int id);

    //通过用户找其书法表
    @Select("select * from calligraphy_tab where user_id = #{userId}")
    List<Calligraphy> findCalligraphiesByUserId(int userId);

    //通过id找书法表标题
    @Select("select title,category from calligraphy_tab where id = #{id}")
    Calligraphy findCalligraphyTitleById(int id);

    //找出所有书法表
    @Select("select username,pre_url,upload_time,after_url,dispose_time,remark_content,score,score_detail,descri,title,category,likes from calligraphy_tab")
    List<Calligraphy> findAllCalligraphies();

    //找出末尾的书法表
    @Select("select id from calligraphy order by id desc limit 1")
    Calligraphy findEndCalligraphy();

    //找出书法表的点赞数
    @Select("select IFNULL(max(likes),0) from calligraphy_Tab where id = #{calligraphyId}")
    int findLikesByCalligraphyId(@Param("calligraphyId") int calligraphyId);

    //通过分类找出同类书法表
    @Select("select username,pre_url,upload_time,after_url,dispose_time,remark_content,score,score_detail,descri,title,category,likes from calligraphy_tab where category = #{category}")
    List<Calligraphy> findCalligraphiesByCategory(@Param("category") String category);

    //默认管理书法表（通过降序排序来展示页面，最新在前面）
    @Select("select username,pre_url,upload_time,after_url,dispose_time,remark_content,category,likes from calligraphy order by id desc")
    List<Calligraphy> getCalligraphyManagement();

    //推荐管理书法表（通过点赞数的大小降序）
    @Select("select username,pre_url,upload_time,after_url,dispose_time,remark_content,category,likes from calligraphy order by likes desc")
    List<Calligraphy> getCalligraphyByLikesDesc();

    //该类型书法表总数
    @Select("select count(*) from calligraphy_tab where category = #{category}")
    int countCalligraphyByCategory(@Param("category") String category);

    //书法表的总个数
    @Select("select count(*) from calligraphy_tab")
    int countCalligraphy();

    //通过id找出上下篇
    @Select("select id,last_call_id,next_call_id from calligraphy_tab where id = #{id}")
    Calligraphy findLastAndNextCalligraphy(int id);

    @Update("update calligraphy set #{lastOrNext} = #{updateId} where id = #{id}")
    void updateLastOrNext(@Param("lastOrNext") String lastOrNext,@Param("updateId") int updateId,@Param("id") int id);

    @Delete("delete from calligraphy where id = #{id}")
    void deleteById(int id);
}
