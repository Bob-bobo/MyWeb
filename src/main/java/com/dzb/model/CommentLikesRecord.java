package com.dzb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : zhengbo.du
 * @date : 2022/3/7 23:18
 */
@Data
@NoArgsConstructor
public class CommentLikesRecord {

    private int id;

    private long calligraphyId;

    /**
     * 评论的id
     */
    private int pId;

    /**
     * 点赞者id
     */
    private int likeId;

    /**
     * 点赞时间
     */
    private int likeDate;

    public CommentLikesRecord(long calligraphyId,int pId,int likeId,int likeDate){
        this.calligraphyId = calligraphyId;
        this.pId = pId;
        this.likeId = likeId;
        this.likeDate = likeDate;
    }
}
