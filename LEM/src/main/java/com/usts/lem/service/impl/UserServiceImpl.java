package com.usts.lem.service.impl;

import com.usts.lem.dao.IUserDao;
import com.usts.lem.model.User;
import com.usts.lem.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("userService")
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao userDao;

    @Override
    public User findByNameAndPassword(String name, String password) {
        return userDao.findByNameAndPassword(name, password);
    }

    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }
}
