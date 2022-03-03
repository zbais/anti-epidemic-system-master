package com.xgp.questionbrushingsystem.model;

public class Error {

    private Integer id;

    private Integer uId;

    private Integer lId;

    private String lastErrorAnswer;

    private Integer errorcount;

    private String remarks;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getlId() {
        return lId;
    }

    public void setlId(Integer lId) {
        this.lId = lId;
    }

    public String getLastErrorAnswer() {
        return lastErrorAnswer;
    }

    public void setLastErrorAnswer(String lastErrorAnswer) {
        this.lastErrorAnswer = lastErrorAnswer == null ? null : lastErrorAnswer.trim();
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
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Error() {
    }



    public Error(Integer uId, Integer lId, String lastErrorAnswer) {
        this.uId = uId;
        this.lId = lId;
        this.lastErrorAnswer = lastErrorAnswer;
    }

    @Override
    public String toString() {
        return "Error{" +
                "id=" + id +
                ", uId=" + uId +
                ", lId=" + lId +
                ", lastErrorAnswer='" + lastErrorAnswer + '\'' +
                ", errorcount=" + errorcount +
                ", remarks='" + remarks + '\'' +
                ", flag=" + flag +
                '}';
    }
}