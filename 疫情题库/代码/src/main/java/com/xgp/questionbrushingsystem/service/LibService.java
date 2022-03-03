package com.xgp.questionbrushingsystem.service;

import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.service.base.BaseService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LibService extends BaseService<Lib> {

    /**
     * 从题库中随机查询10条信息
     * @param num
     * @return
     */
    List<Lib> randLib(int num);

    /**
     * 顺序查询数据
     * @param lastindex
     * @param num
     * @return
     */
    List<Lib> orderLib(int lastindex, int num);

    /**
     * 查询题目总数
     * @return
     */
    int countLib();

    List<Lib> searchLib(String sealib);
}
