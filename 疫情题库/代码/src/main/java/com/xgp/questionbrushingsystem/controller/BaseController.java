package com.xgp.questionbrushingsystem.controller;

import com.xgp.questionbrushingsystem.mapper.ErrorMapper;
import com.xgp.questionbrushingsystem.service.ErrorService;
import com.xgp.questionbrushingsystem.service.LibService;
import com.xgp.questionbrushingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    protected LibService libService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ErrorService errorService;
}
