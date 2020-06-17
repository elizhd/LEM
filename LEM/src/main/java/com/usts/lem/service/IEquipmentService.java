package com.usts.lem.service;

import com.usts.lem.model.Equipment;

import java.util.List;

public interface IEquipmentService extends IMBaseService<Equipment> {

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
    Equipment findBySerialNumber(String serialNumber);

    /**
     * @Description 关键字模糊查找
     * @Param serialNumber
     * @Return List <Equipment> 设备信息
     */
    List<Equipment> fuzzSearch(String keyWord);

    /*
     * @Author:  Tim
     * @Description //根据设备编号查找所有信息包含维修的
     * @Date
     * @Param  * @param serialNumber
     * @return  Equipment 设备信息
     **/

    Equipment findBySerialNumberAll(String serialNumber);


    int deleteBySerialNumber(String serialNumber);
}
