package com.xgp.questionbrushingsystem.service.impl;

import com.xgp.questionbrushingsystem.mapper.LibMapper;
import com.xgp.questionbrushingsystem.model.Lib;
import com.xgp.questionbrushingsystem.model.User;
import com.xgp.questionbrushingsystem.service.LibService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibServiceImpl  implements LibService {

    @Autowired
    protected LibMapper libMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return libMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Lib lib) {

        return libMapper.insert(lib);
    }

    @Override
    public int insertSelective(Lib lib) {

        return libMapper.insertSelective(lib);
    }

    @Override
    public Lib selectByPrimaryKey(Integer id) {

        return libMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Lib lib) {

        return libMapper.updateByPrimaryKeySelective(lib);
    }

    @Override
    public int updateByPrimaryKey(Lib lib) {

        return libMapper.updateByPrimaryKey(lib);
    }

    @Override
    public List<Lib> randLib(int num) {
        List<Lib> libs = libMapper.randLib(10);
        return libs;
    }

    /**
     * 顺序查询10条数据
     * @param lastindex
     * @param num
     * @return
     */
    @Override
    public List<Lib> orderLib(int lastindex, int num) {
        List<Lib> libs = libMapper.orderLib(lastindex, num);
        if(libs.size() < num) {
            List<Lib> libsother = libMapper.orderLib(0, num - libs.size());
            libs.addAll(libsother);
        }
        return libs;
    }

    @Override
    public int countLib() {

        return libMapper.countLib();
    }

    @Override
    public List<Lib> searchLib(String sealib) {
        return libMapper.searchLib(sealib);
    }
}
