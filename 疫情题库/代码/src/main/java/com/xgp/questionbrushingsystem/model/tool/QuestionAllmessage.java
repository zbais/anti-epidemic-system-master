package com.xgp.questionbrushingsystem.model.tool;

public class QuestionAllmessage {
    private Integer uId;
    private String username;
    private Integer lId;
    private String type;
    private String question;
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;
    private Integer difficulty;
    private String lastErrorAnswer;
    private Integer errorcount;
    private String remarks;
    private Integer flag;

    public QuestionAllmessage() {
    }

    public QuestionAllmessage(Integer lId, String type, String question, String a, String b, String c, String d, String answer, Integer difficulty, String lastErrorAnswer, Integer errorcount, String remarks, Integer flag) {
        this.lId = lId;
        this.type = type;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
        this.difficulty = difficulty;
        this.lastErrorAnswer = lastErrorAnswer;
        this.errorcount = errorcount;
        this.remarks = remarks;
        this.flag = flag;
    }

    public QuestionAllmessage(Integer uId, String username, Integer lId, String type, String question, String a, String b, String c, String d, String answer, Integer difficulty, String lastErrorAnswer, Integer errorcount, String remarks, Integer flag) {
        this.uId = uId;
        this.username = username;
        this.lId = lId;
        this.type = type;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
        this.difficulty = difficulty;
        this.lastErrorAnswer = lastErrorAnswer;
        this.errorcount = errorcount;
        this.remarks = remarks;
        this.flag = flag;
    }

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
        this.username = username;
    }

    public Integer getlId() {
        return lId;
    }

    public void setlId(Integer lId) {
        this.lId = lId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getLastErrorAnswer() {
        return lastErrorAnswer;
    }

    public void setLastErrorAnswer(String lastErrorAnswer) {
        this.lastErrorAnswer = lastErrorAnswer;
    }

    public Integer getErrorcount() {
        return errorcount;
    }

    public void setErrorcount(Integer errorcount) {
        this.errorcount = errorcount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "QuestionAllmessage{" +
                "uId=" + uId +
                ", username='" + username + '\'' +
                ", lId=" + lId +
                ", type='" + type + '\'' +
                ", question='" + question + '\'' +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", d='" + d + '\'' +
                ", answer='" + answer + '\'' +
                ", difficulty=" + difficulty +
                ", lastErrorAnswer='" + lastErrorAnswer + '\'' +
                ", errorcount=" + errorcount +
                ", remarks='" + remarks + '\'' +
                ", flag=" + flag +
                '}';
    }
}
