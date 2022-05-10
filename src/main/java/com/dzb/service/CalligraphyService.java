package com.dzb.service;

import com.dzb.model.Calligraphy;
import com.dzb.utils.DataMap;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Map;

/**
 * @author : zhengbo.du
 * @date : 2022/3/8 10:06
 * describe: 书法表的业务操作
 */
public interface CalligraphyService {

    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadImage(File file);

    /**
     * 保存书法
     * @param calligraphy
     * @return  status:200 --成功  500 --失败
     */
    DataMap insertCalligraphy(Calligraphy calligraphy);

    /**
     * 通过id和用户名查询书法
     * @param id
     * @param username
     * @return
     */
    DataMap getCalligraphy(int id,String username);

    /**
     * 通过id获取书法表的标题和分类
     * @param id
     * @return
     */
    Map<String,String> findCalligraphyTitlAndCategoryById(int id);

    /**
     * 分页获取所有的书法表
     * @param rows
     * @param pagaNum
     * @return
     */
    DataMap findAllCalligraphies(int rows,int pagaNum);

    /**
     * 通过书法id更新它的上下篇文案
     * @param lastOrNext
     * @param lastCallId
     * @param nextCallId
     */
    void updateCalligraphyLastOrNext(String lastOrNext,int lastCallId,int nextCallId);

    /**
     * 点赞更新
     * @param calligraphyId
     * @return
     */
    DataMap updateLikeByCalligraphyId(int calligraphyId);

    /**
     * 分页获取该分类下的书法篇章
     * @param category
     * @param rows
     * @param pageNum
     * @return
     */
    DataMap findCalligraphiesByCategory(String category,int rows,int pageNum);

    /**
     * 分页进行文章管理
     * @param rows
     * @param pageNum
     * @return
     */
    DataMap findCalligraphyManagement(int rows,int pageNum);

    /**
     *
     * @param id
     * @return
     */
    Calligraphy findCalligraphyById(int id);

    /**
     * 计算该分类下的书法总数
     * @param category
     * @return
     */
    int countCalligraphyByCategory(String category);

    /**
     * 计算所有书法总数
     * @return
     */
    int countCalligraphy();

    /**
     * 通过id删除书法篇
     * @param id
     * @return
     */
    @Transactional
    DataMap deleteCalligraphy(int id);
}
