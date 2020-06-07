package com.usts.lem.service;

import com.usts.lem.model.Scrap;

import java.util.List;

public interface IScrapService extends IMBaseService<Scrap> {
    /**
     * @Description 型号计数基础函数
     * @Param spec 类型
     * @Return int 行数
     */
    int countSpec(String spec);

    /**
     * @Description 根据设备编号查找
     * @Param serialNumber
     * @Return Equipment 设备信息
     */
    Scrap findBySerialNumber(String serialNumber);

    /**
     * @Description 分页数据基础函数
     * @Return List<Scrap>
     */
    List<Scrap> findAll();

    /**
     * @Description 关键字模糊查找
     * @Param serialNumber
     * @Return List <Equipment> 设备信息
     */
    List<Scrap> fuzzSearch(String keyWord);
}
