package com.usts.lem.dao;


import com.usts.lem.model.Buy;

import java.util.List;

public interface IBuyDao extends IMBaseDao<Buy> {
    /**
     * @Description 型号计数基础函数
     * @Param spec 类型
     * @Return int 行数
     */
    int countSpec(String spec);

    /**
     * @Description 根据设备编号查找
     * @Param serialNumber
     * @Return Buy 设备信息
     */
    Buy findBySerialNumber(String serialNumber);
    /**
     * @Description 关键字模糊查找
     * @Param serialNumber
     * @Return List <Buy> 设备信息
     */
    List<Buy> fuzzSearch(String keyWord);

    /**
     * @Description 分页数据基础函数
     * @Return List<Buy>
     */
    List<Buy> findAll();

}