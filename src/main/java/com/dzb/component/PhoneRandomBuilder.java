package com.dzb.component;

import org.springframework.stereotype.Component;

/**
 * @author : zhengbo.du
 * @date : 18:52 2022/3/4
 */
@Component
public class PhoneRandomBuilder {
    public static String randomBuilder(){

        String result = "";
        for(int i=0;i<4;i++){
            result += Math.round(Math.random() * 9);
        }

        return result;

    }
}
