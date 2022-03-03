package com.xgp.questionbrushingsystem.service.base;

public interface BaseService<T> {
    /**
     * 根据主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 增加一条记录
     */
    int insert(T t);

    /**
     * 有选择的增加
     */
    int insertSelective(T t);

    /**
     * 根据主键查询
     */
    T selectByPrimaryKey(Integer id);

    /**
     * 根据主键有选择的更新记录
     */
    int updateByPrimaryKeySelective(T t);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(T t);
}
