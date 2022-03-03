package com.xgp.questionbrushingsystem.service;

import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.service.base.BaseService;

public interface UserService extends BaseService<User> {
    /**
     * 根据用户名是否已经使用
     * @param username
     * @return
     */
    int selectByUsername(String username);

    /**
     * 查找改邮箱是否注册
     * @param email
     * @return
     */
    int selectByEmail(String email);

    /**
     * 根据用户名和密码查询该用户是否存在
     * @param username
     * @param password
     * @return
     */
    int login(String username, String password);

    /**
     * 通过邮箱修改用户名和密码
     * @param email
     * @param username
     * @param password
     * @return
     */
    int updateByEmail(String email, String username, String password);
}
