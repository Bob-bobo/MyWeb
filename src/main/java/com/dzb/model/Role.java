package com.dzb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : zhengbo.du
 * @date : 2022/3/5 0:20
 */
@Data
@NoArgsConstructor
public class Role {

    private int id;

    private String role;

    private String describe;

    public Role(int id,String role,String describe){
        this.id = id;
        this.role = role;
        this.describe = describe;
    }
}
