package com.xgp.questionbrushingsystem.mapper;

import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.LibExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
//@Repository
public interface LibMapper {

    int countByExample(LibExample example);

    int deleteByExample(LibExample example);

    /**
     * 根据主键删除
     * @param lId
     * @return
     */
    int deleteByPrimaryKey(Integer lId);

    /**
     * 增加一条记录
     * @param record
     * @return
     */
    int insert(Lib record);

    /**
     * 有选择的增加
     * @param record
     * @return
     */
    int insertSelective(Lib record);

    List<Lib> selectByExample(LibExample example);

    /**
     * 根据主键查询
     * @param lId
     * @return
     */
    Lib selectByPrimaryKey(Integer lId);

    int updateByExampleSelective(@Param("record") Lib record, @Param("example") LibExample example);

    int updateByExample(@Param("record") Lib record, @Param("example") LibExample example);

    int updateByPrimaryKeySelective(Lib record);

    /**
     * 根据主键更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKey(Lib record);

    /**
     * 从题库中随机查询num条信息
     * @param num
     * @return
     */
    @Select("SELECT * FROM lib ORDER BY RAND() LIMIT #{num}")
    List<Lib> randLib(int num);

    /**
     * 顺序查询数据
     * @param lastindex
     * @param num
     * @return
     */
    @Select("SELECT * FROM lib LIMIT #{lastindex},#{num}")
    List<Lib> orderLib(@Param("lastindex") int lastindex,@Param("num") int num);

    @Select("SELECT COUNT(*) FROM lib")
    int countLib();

    @Select("SELECT * FROM lib WHERE question LIKE CONCAT('%',#{sealib},'%') LIMIT 0,10")
    List<Lib> searchLib(@Param("sealib") String sealib);
}