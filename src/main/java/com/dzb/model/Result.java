package com.dzb.model;

import lombok.Data;

/**
 * @author : zhengbo.du
 * @date : 2022/3/4 18:15
 */
@Data
public class Result<T> {

    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //具体内容
    private T data;
}
