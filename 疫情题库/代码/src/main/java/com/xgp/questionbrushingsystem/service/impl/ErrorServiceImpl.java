package com.xgp.questionbrushingsystem.service.impl;

import com.xgp.questionbrushingsystem.mapper.ErrorMapper;
import com.xgp.questionbrushingsystem.model.Error;
import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.model.tool.PageBean;
import com.xgp.questionbrushingsystem.model.tool.QuestionAllmessage;
import com.xgp.questionbrushingsystem.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ErrorServiceImpl implements ErrorService {

    @Autowired
    protected ErrorMapper errorMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {

        return errorMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Error error) {

        return errorMapper.insert(error);
    }

    @Override
    public int insertSelective(Error error) {

        return errorMapper.insertSelective(error);
    }

    @Override
    public Error selectByPrimaryKey(Integer id) {

        return errorMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Error error) {

        return errorMapper.updateByPrimaryKeySelective(error);
    }

    @Override
    public int updateByPrimaryKey(Error error) {

        return errorMapper.updateByPrimaryKey(error);
    }

    @Override
    public int selectByTwoId(Integer getuId, Integer getlId) {
        return errorMapper.selectByTwoId(getuId,getlId);
    }

    @Override
    public void updateErrorcount(Integer getuId, Integer getlId) {
        errorMapper.updateErrorcount(getuId,getlId);
    }

    @Override
    public void addRemarks(Integer uid, Integer lid, String remarks) {
        errorMapper.addRemarks(uid,lid,remarks);
    }

    @Override
    public List<Lib> randLib(int i,Integer uid) {
        return errorMapper.randLib(i,uid);
    }

    @Override
    public List<QuestionAllmessage> selectTwoAllMessage(Integer getuId) {
        return errorMapper.electTwoAllMessage(getuId);
    }

    @Override
    public List<QuestionAllmessage> selectAllMessage() {
        return errorMapper.selectAllMessage();
    }

    @Override
    public PageBean<QuestionAllmessage> findErrorsSpaceByPage(String _currentPage, String _rows, Integer uid,int flag) {
        //1,将数字字符串转成数字
        int currentPage = Integer.parseInt(_currentPage);
        int rows = Integer.parseInt(_rows);

        if(currentPage <= 0) {
            currentPage = 1;
        }

        //2,创建一个空的分页对象
        PageBean<QuestionAllmessage> pb = new PageBean<>();
        //3,设置参数
        pb.setCurrentPage(currentPage);
        pb.setRows(rows);

        //4,调用dao查询错题总数
        int totalCount = errorMapper.selectSpaceCount(uid,flag);
        pb.setTotalCount(totalCount);

        //5,计算开始查询的索引
        int start = (currentPage-1)*rows;
        List<QuestionAllmessage> list = errorMapper.selectErrorSpaceMsg(start,rows,uid,flag);
        pb.setList(list);

        //6,计算总页码
        int totalPage = totalCount % rows == 0 ? totalCount / rows : (totalCount / rows) + 1;
        pb.setTotalPage(totalPage);
        return pb;
    }

    @Override
    public void updateFlag(Integer uid, Integer lid, Integer flag) {
        errorMapper.updateFlag(uid,lid,flag);
    }
}
