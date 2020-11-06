package com.play.robot.bean;

import android.text.TextUtils;

import com.play.robot.util.GsonUtil;
import com.play.robot.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LGQ
 * Time: 2018/8/24
 * Function:
 */

public class MySelfInfo {

    private MySelfInfo() {
    }

    private final static class HolderClass {
        private final static MySelfInfo INSTANCE = new MySelfInfo();
    }

    public static MySelfInfo getInstance() {
        return HolderClass.INSTANCE;
    }

    public void setData(LoginBean model) {

        SPUtils.getInstance().putInt(SPUtils.SP_USER_ID, model.getUserId());
        SPUtils.getInstance().putInt(SPUtils.SP_DEPT_ID, model.getDeptId());
        SPUtils.getInstance().putString(SPUtils.SP_LOGIN_NAME, model.getLoginName());
        SPUtils.getInstance().putString(SPUtils.SP_PASSWORD, model.getPassword());
        SPUtils.getInstance().putString(SPUtils.SP_USER_NAME, model.getUserName());
    }

    public int getUserId() {
        return SPUtils.getInstance().getInt(SPUtils.SP_USER_ID);
    }

    public int getDeptId() {
        return SPUtils.getInstance().getInt(SPUtils.SP_DEPT_ID);
    }

    public String getLoginName() {
        return SPUtils.getInstance().getString(SPUtils.SP_LOGIN_NAME);
    }

    public String getPassword() {
        return SPUtils.getInstance().getString(SPUtils.SP_PASSWORD);
    }

    public String getUserName() {
        return SPUtils.getInstance().getString(SPUtils.SP_USER_NAME);
    }

    //---------搜索 start
    public void setSearch(List<String> lists){
        if(lists == null) return;
        SPUtils.getInstance().putString(SPUtils.SP_SEARCH, GsonUtil.convertVO2String(lists));
    }

    public void addSearch(String str){
        List<String> results = new ArrayList<>();

        List<String> lists = getSearch();
        for (String item : lists){
            if(TextUtils.equals(str,item)){
                lists.remove(item);
                break;
            }
        }

        while (lists.size() > 8){
            lists.remove(lists.size() - 1);
        }

        results.add(str);
        results.addAll(lists);
        setSearch(results);
    }

    public List<String> getSearch(){
        if(TextUtils.isEmpty(SPUtils.getInstance().getString(SPUtils.SP_SEARCH)))
            return new ArrayList<>();
        return GsonUtil.convertJson2Array(SPUtils.getInstance().getString(SPUtils.SP_SEARCH));
    }

}
