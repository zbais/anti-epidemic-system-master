package com.xgp.questionbrushingsystem.mapper;

import com.xgp.questionbrushingsystem.model.Error;
import com.xgp.questionbrushingsystem.model.ErrorExample;
import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.tool.QuestionAllmessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
//@Repository
public interface ErrorMapper {

    int countByExample(ErrorExample example);
    int deleteByExample(ErrorExample example);
    int deleteByPrimaryKey(Integer id);
    int insert(Error record);
    int insertSelective(Error record);
    List<Error> selectByExample(ErrorExample example);
//    按主键选择
    Error selectByPrimaryKey(Integer id);
    int updateByExampleSelective(@Param("record") Error record, @Param("example") ErrorExample example);
    int updateByExample(@Param("record") Error record, @Param("example") ErrorExample example);
    //按主要关键字选择更新
    int updateByPrimaryKeySelective(Error record);
    //按主键更新
    int updateByPrimaryKey(Error record);

    /**
     * 根据两个id查询错题
     * @param uid
     * @param lid
     * @return
     */
    @Select("SELECT COUNT(*) FROM error WHERE u_id = #{uid} AND l_id = #{lid}")
    int selectByTwoId(@Param("uid") Integer uid,@Param("lid") Integer lid);

    @Update("UPDATE error SET errorcount = errorcount + 1 WHERE u_id = #{uid} AND l_id = #{lid}")
    void updateErrorcount(@Param("uid") Integer uid,@Param("lid") Integer lid);

    @Update("UPDATE error SET remarks = #{remarks} WHERE u_id = #{uid} AND l_id = #{lid}")
    void addRemarks(@Param("uid") Integer uid,@Param("lid") Integer lid,@Param("remarks") String remarks);

    @Select("SELECT l.* FROM lib l,error e WHERE l.l_id = e.l_id AND e.u_id = #{uid} ORDER BY RAND() LIMIT #{num}")
    List<Lib> randLib(@Param("num") int num,@Param("uid") Integer uid);

    @Select("SELECT l.*,e.last_error_answer,e.errorcount,e.remarks,e.flag FROM lib l,error e WHERE l.l_id = e.l_id AND e.u_id = #{uid}")
    List<QuestionAllmessage> electTwoAllMessage(Integer uid);

    @Select("SELECT l.*,e.last_error_answer,e.errorcount,e.remarks,e.flag,u.username FROM lib l,error e,USER u WHERE l.l_id = e.l_id AND e.u_id = u.u_id")
    List<QuestionAllmessage> selectAllMessage();

    @Select("SELECT COUNT(*) FROM ERROR WHERE u_id = #{uid} AND flag = #{flag}")
    int selectSpaceCount(@Param("uid") Integer uid,@Param("flag") Integer flag);

    @Select("SELECT l.*,e.last_error_answer,e.errorcount,e.remarks,e.flag FROM lib l,error e WHERE l.l_id = e.l_id AND e.u_id = #{uid} AND e.flag = #{flag} ORDER BY e.errorcount DESC LIMIT #{start},#{rows}")
    List<QuestionAllmessage> selectErrorSpaceMsg(@Param("start") int start,@Param("rows") int rows,@Param("uid") Integer uid,@Param("flag") int flag);

    @Update("UPDATE error SET flag = #{flag} WHERE u_id = #{uid} AND l_id = #{lid}")
    void updateFlag(@Param("uid") Integer uid, @Param("lid") Integer lid, @Param("flag") Integer flag);

//    SELECT l.* FROM lib l,error e WHERE l.l_id = e.l_id AND e.u_id = 3 ORDER BY RAND() LIMIT 10
}