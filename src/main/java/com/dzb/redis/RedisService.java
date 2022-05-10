package com.dzb.redis;

/**
 * @author: zhengbo.du
 * @Date: 2022/2/14 15:31
 * Describe:
 */
public interface RedisService {

    /**
     * 判断key是否存在
     */
    Boolean hasKey(String key);

}
