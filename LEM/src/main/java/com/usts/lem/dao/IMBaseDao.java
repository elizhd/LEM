package com.usts.lem.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package ccom.usts.lem.dao
 * @Description DAO层  管理DAO基础类
 * @Author Haodong Zhao
 */
public interface IMBaseDao<T> {
    /**
     * @Description 添加数据基础函数
     * @Param obj 数据类
     * @Return int 添加的行数
     */
    int insert(T obj);

    /**
     * @Description 更新数据基础函数
     * @Param obj 待更新的数据类
     * @Return int 修改的行数
     */
    int update(T obj);


    /**
     * @Description 分页数据基础函数
     * @Param id
     * @Param offset
     * @Param rows
     * @Param sort 排序
     * @Param order 顺序
     * @Return java.util.List<T>
     */
    List<T> findAllData(@Param(value = "offset") Integer offset,
                        @Param(value = "rows") Integer rows,
                        @Param(value = "sort") String sort,
                        @Param(value = "order") String order);

    /**
     * @Description 计数基础函数
     * @Author Haodong Zhao
     * @Return int 行数
     */
    int count();
}
