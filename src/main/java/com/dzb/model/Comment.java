package com.dzb.model;

import lombok.Data;

/**
 * @author : zhengbo.du
 * @date : 2022/3/7 23:22
 */
@Data
public class Comment {

    private int id;

    private long pId;

    private long calligraphyId;

    private int answererId;

    private int respondentId;

    private String commentDate;

    private int likes;

    private String commentContent;

    /**
     * 该条评论是否已读     1--未读  0--已读
     */
    private int isRead = 1;
}
