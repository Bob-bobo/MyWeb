package com.dzb.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * @author : zhengbo.du
 * @date : 2022/3/6 9:57
 */
@Controller
public class BackControl {

    private static final String SLASH_SYMBOL = "/";

    /**
     * 跳转首页
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("lastUrl", (String) request.getSession().getAttribute("lastUrl"));
        return "calligraphyImg";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/admin")
    public String loginAdmin(){
        return "";
    }

    @PostMapping(value = "/score",  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String score(@RequestParam(value = "myImg",required = false)MultipartFile file,
                              @RequestParam(value = "temp",required = false)String temp,
                                     Model model) throws IOException {
        model.addAttribute("word",temp);

        char[] chars = temp.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            returnStr.append("\\u").append(Integer.toString(chars[i], 16));
        }

        String path = "/root/imgStore";
//        String path = "/Users/zhengbo.du/PythonFile/Calligraphy/imgStore";
        String originalFileName = file.getOriginalFilename();
        String newfileName = UUID.randomUUID()+originalFileName;
        File targetFile = new File(path,newfileName);
        file.transferTo(targetFile);
        path = path + "/" + newfileName;

        String pa = "/root/template/"+returnStr.toString().substring(2)+".png";
//        String pa = "/Users/zhengbo.du/PythonFile/Calligraphy/template/"+returnStr.substring(2)+".png";
        try {
            String[] args2 = new String[]{"/usr/bin/python3", "/root/calligraphy.py",path,pa};
//            String[] args2 = new String[]{"python3", "/Users/zhengbo.du/Desktop/MyWeb/src/main/java/com/dzb/python/calligraphy.py",path,pa};
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
                return "404";
            }
            res = res.substring(1,res.length()-1);
            String[] splits = res.split(",");
            HashMap<Integer,String> hash = new HashMap<>();
            for (int i = 0; i < splits.length; i++) {
                String mid = splits[i].trim();
                String[] stow = mid.split(":");
                hash.put(i,stow[1].trim());
            }
            System.out.println("hash1"+hash.get(0));
            System.out.println("hash2"+hash.get(1));
            System.out.println("hash3"+hash.get(2));
            Float[] scores = {Float.valueOf(hash.get(0).substring(0,3)), Float.valueOf(hash.get(1).substring(0,2)), Float.valueOf(hash.get(2).substring(0,2))};
            for (Integer i:hash.keySet()){
                Float f = Float.parseFloat(hash.get(i).substring(0,3))/2;
                if (i >=3 && i < 7){
                    model.addAttribute("value"+i,f*2);
                    continue;
                }
                model.addAttribute("value"+i,f);
            }

            String[] pingyu = {"字形", "框架", "骨架"};
            for (int i = 0; i < 3; i++) {
                model.addAttribute("remark"+i,comment(scores[i],pingyu[i],i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "score";
    }

    public static String comment(Float s, String pin, Integer i){
        String[] eights = {"画龙点睛","十分显著","非常棒","转折有力","心情舒畅"};
        String[] six = {"还算不错","可以再沉住气","离优秀差点点","进步极大","优化显著"};
        String[] fours = {"平平无奇","进步还是有","再加把劲而","多去思考","再多沉心练习"};
        String[] others = {"问题很多","有所期待","找出了问题","算是一次思考","自我意识提升"};
        Random rand = new Random();
        i = rand.nextInt(4);
        if (s > 8){
            return  "在"+pin+"方面……表现比较优秀……在字体整体中……起到了"+eights[i]+"的作用";
        }else if (s > 6){
            return  "在"+pin+"方面……达到了良好水平……放眼到整个汉字……总体感觉"+six[i];
        }else if (s > 4){
            return  "在"+pin+"方面……"+fours[i]+"……纵观整体还有很大发展空间";
        }else {
            return  "这次练习"+others[i]+"……还是有很多不足之处……例如"+pin+"有很大的提升空间……多多练习……不要灰心";
        }
    }
}
