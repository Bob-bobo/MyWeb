package com.dzb.python;

import java.util.HashMap;

/**
 * @author : zhengbo.du
 * @date : 2022/4/7 23:21
 */
public class adsta {
    public static void main(String[] args) {
        String s = "{0: 6.836155844472252, 1: 8.56217235993629, 2: 7.773538981325711, 11: 0.18351910374492977, " +
                "12: 0.8146910647471538, 13: 6523.965623811271, 21: 0.12654544141777269, 22: 0.906108469897189, " +
                "23: 1993.578652831775, 31: 0.2021252656359473, 32: 0.8719453431308695, 33: 3284.0914677144187, " +
                "41: 0.10378375793308445, 42: 1344.6789973147647, 51: 0.532595750015671, 52: 0.5907995832790728, " +
                "53: 0.5618894601542417, 54: 0.6048658271246786, 61: 7.635175740167501, 62: 7.519487966834731}";
        s = s.substring(1,s.length()-1);
        String[] splits = s.split(",");
        HashMap<Integer,String> hash = new HashMap<>();
        for (int i = 0; i < splits.length; i++) {
            String mid = splits[i].trim();
            String[] stow = mid.split(":");
            hash.put(i,stow[1].trim());
        }
        for (Integer i : hash.keySet()){
            System.out.println("hash"+i+"的值为："+hash.get(i));
        }
    }
}
