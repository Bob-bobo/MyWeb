package com.dzb.service.Impl;

import com.dzb.constant.CodeType;
import com.dzb.mapper.CalligraphyMapper;
import com.dzb.model.Calligraphy;
import com.dzb.service.CalligraphyService;
import com.dzb.utils.DataMap;
import com.dzb.utils.FileUtil;
import com.dzb.utils.StringUtil;
import com.dzb.utils.TimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhengbo.du
 * @date : 2022/3/8 22:20
 */
@Service
@Slf4j
public class CalligraphyServiceImpl implements CalligraphyService {

    @Autowired
    private CalligraphyService calligraphyService;
    @Autowired
    private CalligraphyMapper calligraphyMapper;

    @Override
    public String uploadImage(File file) {
        String fileUrl;
        try{
            FileUtil fileUtil = new FileUtil();

            String subCatalog = "callimage/"+new TimeUtil().getFormatDateForThree();
            fileUrl = fileUtil.uploadFile(file,subCatalog);
        }catch (Exception e){
            log.error("upload image error");
            return null;
        }
        return fileUrl;
    }

    @Override
    public DataMap insertCalligraphy(Calligraphy calligraphy) {
        Map<String,Object> dataMap = new HashMap<>(4);

        if (StringUtil.BLANK.equals(calligraphy.getPreUrl())){
            calligraphy.setPreUrl(calligraphy.getAfterUrl());
        }

        Calligraphy endCall = calligraphyMapper.findEndCalligraphy();
        //设置该篇文章的上一篇为插入前最后一篇的id
        if (endCall != null){
            calligraphy.setLastCallId(endCall.getId());
        }
        TimeUtil timeUtil = new TimeUtil();
        calligraphy.setDisposeTime((int) timeUtil.getLongTime());
        //初始化点赞数
        calligraphy.setLikes(0);
        calligraphyMapper.save(calligraphy);
        //设置上一篇书法的下一篇id
        if (endCall != null){
            calligraphyService.updateCalligraphyLastOrNext("next_call_id",calligraphy.getId(),endCall.getId());
        }

        dataMap.put("calligraphyTitle",calligraphy.getTitle());
        dataMap.put("dispose_time",calligraphy.getDisposeTime());
        dataMap.put("category",calligraphy.getCategory());
        return DataMap.success().setData(dataMap);
    }

    @Override
    public DataMap getCalligraphy(int id, String username) {
        Calligraphy calligraphy = calligraphyMapper.findCalligraphyById(id);

        if (calligraphy != null){
            Map<String,Object> datamap = new HashMap<>(32);
            Calligraphy lastCall = calligraphyMapper.findCalligraphyById(calligraphy.getLastCallId());
            Calligraphy nextCall = calligraphyMapper.findCalligraphyById(calligraphy.getNextCallId());
            datamap.put("id",calligraphy.getId());
            datamap.put("username",calligraphy.getUsername());
            datamap.put("title",calligraphy.getTitle());
            datamap.put("upload_time",calligraphy.getUploadTime());
            datamap.put("dispose_time",calligraphy.getDisposeTime());
            datamap.put("remark_content",calligraphy.getRemarkContent());
            datamap.put("score",calligraphy.getScore());
            datamap.put("score_detail",calligraphy.getScoreDetail());
            datamap.put("describe",calligraphy.getDescri());
            datamap.put("likes",calligraphy.getLikes());
            if (username == null){
                datamap.put("isLiked",0);
            }else {
                //该用户是否点赞该篇文章，这是一个空白留着
                datamap.put("isLiked",1);
            }
            if (lastCall != null){
                datamap.put("lastStatus",200);
                datamap.put("lastTitle",lastCall.getTitle());
                datamap.put("lastCategory",lastCall.getCategory());
            }else {
                datamap.put("lastStatus",500);
                datamap.put("lastInfo","无");
            }
            if (nextCall != null){
                datamap.put("nextStatus",200);
                datamap.put("nextTitle",nextCall.getTitle());
                datamap.put("nextCategory",nextCall.getCategory());
            }else {
                datamap.put("nextStatus",500);
                datamap.put("nextInfo","无");
            }
            return DataMap.success().setData(datamap);
        }
        return DataMap.fail(CodeType.CALLIGRAPHY_NOT_EXIST);
    }

    @Override
    public Map<String, String> findCalligraphyTitlAndCategoryById(int id) {
        return null;
    }

    @Override
    public DataMap findAllCalligraphies(int rows, int pagaNum) {
        return null;
    }

    @Override
    public void updateCalligraphyLastOrNext(String lastOrNext, int lastCallId, int nextCallId) {

    }

    @Override
    public DataMap updateLikeByCalligraphyId(int calligraphyId) {
        return null;
    }

    @Override
    public DataMap findCalligraphiesByCategory(String category, int rows, int pageNum) {
        return null;
    }

    @Override
    public DataMap findCalligraphyManagement(int rows, int pageNum) {
        return null;
    }

    @Override
    public Calligraphy findCalligraphyById(int id) {
        return null;
    }

    @Override
    public int countCalligraphyByCategory(String category) {
        return 0;
    }

    @Override
    public int countCalligraphy() {
        return 0;
    }

    @Override
    public DataMap deleteCalligraphy(int id) {
        return null;
    }
}
