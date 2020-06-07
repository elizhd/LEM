package com.usts.lem.dao;

import com.usts.lem.model.Scrap;

import java.util.List;

public interface IScrapDao extends IMBaseDao<Scrap> {
    /**
     * @Description 型号计数基础函数
     * @Param spec 类型
     * @Return int 行数
     */
    int countSpec(String spec);

    /**
     * @Description 根据设备编号查找
     * @Param serialNumber
     * @Return Scrap 设备信息
     */
    Scrap findBySerialNumber(String serialNumber);
    /**
     * @Description 关键字模糊查找
     * @Param serialNumber
     * @Return List <Scrap> 设备信息
     */
    List<Scrap> fuzzSearch(String keyWord);

    /**
     * @Description 分页数据基础函数
     * @Return List<Scrap>
     */
    List<Scrap> findAll();

}
