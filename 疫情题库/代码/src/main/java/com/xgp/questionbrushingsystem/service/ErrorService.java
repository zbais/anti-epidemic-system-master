package com.xgp.questionbrushingsystem.service;

import com.xgp.questionbrushingsystem.model.Error;
import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.tool.PageBean;
import com.xgp.questionbrushingsystem.model.tool.QuestionAllmessage;
import com.xgp.questionbrushingsystem.service.base.BaseService;

import java.util.List;

public interface ErrorService extends BaseService<Error> {
    /**
     * 根据两个id查询是否有改错题
     * @param getuId
     * @param getlId
     * @return
     */
    int selectByTwoId(Integer getuId, Integer getlId);

    /**
     * 更新错误题数
     * @param getuId
     * @param getlId
     */
    void updateErrorcount(Integer getuId, Integer getlId);

    /**
     * 到数据库中修改备注
     * @param uid
     * @param lid
     * @param remarks
     */
    void addRemarks(Integer uid, Integer lid, String remarks);

    List<Lib> randLib(int i,Integer uid);

    List<QuestionAllmessage> selectTwoAllMessage(Integer getuId);

    List<QuestionAllmessage> selectAllMessage();

    PageBean<QuestionAllmessage> findErrorsSpaceByPage(String currentPage, String rows, Integer uid,int flag);

    void updateFlag(Integer uid, Integer lid, Integer flag);
}
