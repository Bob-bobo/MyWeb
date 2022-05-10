package com.dzb.python;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author : zhengbo.du
 * @date : 2022/4/6 23:42
 */
public class test {
    public static void main(String[] args) {
        String temp = "杭";
        char[] chars = temp.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            returnStr.append("\\u").append(Integer.toString(chars[i], 16));
        }

        String path = "/root/imgStore/f8efebbe-de6a-4f86-9838-e2d5626305c46.png";

        String pa = "/root/template/"+returnStr.toString().substring(2)+".png";
        try {
            String[] args2 = new String[]{"python", "/root/calligraphy.py", path, pa};
            Process proc = Runtime.getRuntime().exec(args2);
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            String res = "";
            while ((line = in.readLine()) != null){
                res += line;
            }
            in.close();
            proc.waitFor();

            if (res == null || res == " " || res == ""){
                System.out.println("-----404------");
                return;
            }
            res = res.substring(1,res.length()-1);
            String[] splits = res.split(",");
            HashMap<Integer,String> hash = new HashMap<>();
            for (int i = 0; i < splits.length; i++) {
                String mid = splits[i].trim();
                String[] stow = mid.split(":");
                hash.put(i,stow[1].trim());
            }

            for (Integer i:hash.keySet()){
                Float f = Float.parseFloat(hash.get(i).substring(0,3))/2;
                if (i >=3 && i < 7){
                    continue;
                }
                System.out.println(hash.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, String> changeValue(String res) {
        if (res == ""){
            return null;
        }
        res = res.substring(1,res.length()-1);
        String[] splits = res.split(",");
        HashMap<Integer,String> hash = new HashMap<>();
        for (int i = 0; i < splits.length; i++) {
            String mid = splits[i].trim();
            String[] stow = mid.split(":");
            hash.put(i,stow[1].trim());
        }
        return hash;
    }
    public static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            returnStr.append("\\u").append(Integer.toString(chars[i], 16));
        }
        return returnStr.toString();
    }
}
