package com.dzb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : zhengbo.du
 * @date : 2022/3/7 23:04
 */
@Data
@NoArgsConstructor
public class CalligraphyLikeRecord {

    private int id;

    private long calligraphyId;

    private int likeId;

    private int likeDate;

    /**
     * 该条点赞是否已读
     */
    private int isRead = 1;

    private CalligraphyLikeRecord(int calligraphyId,int likeId,int likeDate){
        this.calligraphyId = calligraphyId;
        this.likeId = likeId;
        this.likeDate = likeDate;
    }


}
