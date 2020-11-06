package com.play.robot.bean;

public class LoginBean {

    int userId;
    int deptId;//部门id
    String loginName;
    String password;
    String userName;

    public int getUserId() {
        return userId;
    }


    public int getDeptId() {
        return deptId;
    }


    public String getLoginName() {
        return loginName;
    }


    public String getPassword() {
        return password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }
}
