package com.usts.lem.dao;


import com.usts.lem.model.User;
import org.apache.ibatis.annotations.Param;


public interface IUserDao {
    User findByNameAndPassword(@Param(value = "name") String name, @Param(value = "password") String password);

    int insertUser(User user);

    User findUserByName(String name);

}
