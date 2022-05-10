package com.dzb.model;

import lombok.Data;

/**
 * @author : zhengbo.du
 * @date : 2022/3/6 23:01
 * describe: 书法
 */
@Data
public class Calligraphy {

    private int id;

    /**
     * 用户名
     */
    private int username;

    /**
     * 预处理照片
     */
    private String preUrl;

    /**
     * 上传时间
     */
    private int uploadTime;

    /**
     * 已处理图片
     */
    private String afterUrl;

    /**
     * 处理时间
     */
    private int disposeTime;

    /**
     * 评分内容
     */
    private String remarkContent;

    /**
     * 分数
     */
    private int score;

    /**
     * 分数评定标准
     */
    private String scoreDetail;

    /**
     * 描述
     */
    private String descri;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类
     */
    private String category;

    /**
     * 点赞
     */
    private int likes;

    /**
     * 上一篇书法
     */
    private int lastCallId;

    /**
     * 下一篇书法
     */
    private int nextCallId;
}
