package com.xgp.questionbrushingsystem.model;

import java.util.Date;

public class User {

    private Integer uId;        //用户id,注册时由数据库自动生成
    //前端页面的name属性，要与下列名字对应
    private String username;    //用户名
    private String password;    //密码
    private String phone;       //电话
    private String email;       //邮箱
    private Integer gender;     //性别
    //下面属性不需要从注册页面获取
    private Integer lastindex;  //顺序做题的题号
    private Integer allquestioncount;   //总做题数
    private Integer errorquestioncount; //错题数
    private Date lastlogintime;         //最后登陆时间


    public Integer getuId() {
        return uId;
    }


    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLastindex() {
        return lastindex;
    }

    public void setLastindex(Integer lastindex) {
        this.lastindex = lastindex;
    }

    public Integer getAllquestioncount() {
        return allquestioncount;
    }

    public void setAllquestioncount(Integer allquestioncount) {
        this.allquestioncount = allquestioncount;
    }

    public Integer getErrorquestioncount() {
        return errorquestioncount;
    }

    public void setErrorquestioncount(Integer errorquestioncount) {
        this.errorquestioncount = errorquestioncount;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public User() {
    }

    public User(String username, String password, String phone, String email, Integer gender) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId=" + uId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", lastindex=" + lastindex +
                ", allquestioncount=" + allquestioncount +
                ", errorquestioncount=" + errorquestioncount +
                ", lastlogintime=" + lastlogintime +
                '}';
    }
}