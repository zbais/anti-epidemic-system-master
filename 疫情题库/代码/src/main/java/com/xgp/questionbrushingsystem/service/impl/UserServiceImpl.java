package com.xgp.questionbrushingsystem.service.impl;

import com.xgp.questionbrushingsystem.mapper.UserMapper;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    protected UserMapper userMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        int flag = userMapper.deleteByPrimaryKey(id);
        return flag;
    }

    @Override
    public int insert(User user) {
        int flag = userMapper.insert(user);
        return flag;
    }

    @Override
    public int insertSelective(User user) {
        int flag = userMapper.insertSelective(user);
        return flag;
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public int updateByPrimaryKeySelective(User user) {
        int flag = userMapper.updateByPrimaryKeySelective(user);
        return flag;
    }

    @Override
    public int updateByPrimaryKey(User user) {
        int flag = userMapper.updateByPrimaryKey(user);
        return flag;
    }

    @Override
    public int selectByUsername(String username) {
        int flag = userMapper.selectByUsername(username);
        return flag;
    }

    @Override
    public int selectByEmail(String email) {
        int flag = userMapper.selectByEmail(email);
        return flag;
    }

    @Override
    public int login(String username, String password) {
        //1，先查询用户是否存在
        int flag = userMapper.selectByUaernameAndPassword(username,password);
        //2，再登陆
        if(flag == 1) {
            int id = userMapper.login(username,password);
            return id;
        }
        return flag;
    }

    @Override
    public int updateByEmail(String email, String username, String password) {
        int flag = userMapper.updateByEmail(email,username,password);
        return flag;
    }
}
