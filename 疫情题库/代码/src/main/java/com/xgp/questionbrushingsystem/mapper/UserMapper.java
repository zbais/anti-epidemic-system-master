package com.xgp.questionbrushingsystem.mapper;


import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.model.UserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.List;
@Mapper
//@Repository
public interface UserMapper {

    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer uId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer uId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查找user表中该用户名条数
     * @param username
     * @return
     */
    @Select("SELECT COUNT(*) FROM USER WHERE username = #{username}")
    int selectByUsername(String username);

    /**
     * 查找user表中该email条数
     * @param email
     * @return
     */
    @Select("SELECT COUNT(*) FROM USER WHERE email = #{email}")
    int selectByEmail(String email);

    /**
     * 根据用户名和密码，放回id
     * @param username
     * @param password
     * @return
     *
     * 注意
        1,当只有一个参数时，dao接口的方法中可不用添加注解。Mybatis可成功匹配参数。
        2,当大于一个参数时，dao接口的方法中需要使用注解@Param(“XXX”)为Mybatis指定参数名称。
     */
    @Select("SELECT u_id FROM USER WHERE username = #{username} AND password = #{password}")
    int login(@Param("username")String username,@Param("password")String password);

    /**
     *
     * 查询该用户是否存在
     * @param username
     * @param password
     * @return
     */
    @Select("SELECT COUNT(*) FROM USER WHERE username = #{username} AND password = #{password}")
    int selectByUaernameAndPassword(@Param("username")String username,@Param("password")String password);

    /**
     * 通过邮箱修改用户名和密码
     * @param email
     * @param username
     * @param password
     * @return
     */
    @Update("UPDATE USER SET username=#{username},PASSWORD=#{password} WHERE email=#{email}")
    int updateByEmail(@Param("email") String email,@Param("username") String username,@Param("password") String password);
}