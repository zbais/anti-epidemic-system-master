package com.xgp.questionbrushingsystem.tool;

public class CodeText {


    public static String MyMethod1(){//能随机一个有4个字符的字符串，其中 每个字符都是随机的数字或大小写字母(类似网站的验证)
        String str="";
        for(int i=0;i<4;i++){
            int n=(int) (Math.random()*3);
            char c=' ';
            if(n==0){
                c=(char)(Math.random()*10+48);//随机出0-9个数字
            }else if(n==1){
                c=(char)(Math.random()*26+97);//随机出小写字母
            }else{
                c=(char)(Math.random()*26+65);//随机出大写字母
            }
            str=c + str;
        }
        return str;
    }

}
