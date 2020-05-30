package com.usts.lem.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IMBaseService<T> {
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

    /**
     * @Description 删除数据基础函数
     * @Author Haodong Zhao
     * @Date 2019/7/5 15:35
     * @Param id 待删除的数据信息id
     * @Return int 修改的行数
     */
    int delete(Integer id);

    /**
     * @Description 按数据类id搜索基础函数
     * @Author Haodong Zhao
     * @Date 2019/7/5 15:39
     * @Param id 待搜索的数据信息id
     * @Return T
     */
    T findById(Integer id);
}
