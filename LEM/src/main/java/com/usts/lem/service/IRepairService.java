package com.usts.lem.service;

import com.usts.lem.model.Repair;

import java.util.List;

public interface IRepairService extends IMBaseService<Repair>{


    /**
     * @Description 型号计数基础函数
     * @Param spec 类型
     * @Return int 行数
     */
    int countSpec(String spec);
   /**
    * @Author:  Tim
    * @Description //根据设备编号查找
    * @Date  
    * @Param  * @param serialNumber
    * @return com.usts.lem.model.Repair
    **/
   
    Repair findBySerialNumber(String serialNumber);


    /**
     * @Author:  Tim
     * @Description 关键字模糊查找
     * @Date
     * @Param  * @param keyWord
     * @return java.util.List<com.usts.lem.model.Repair>
     **/
    List<Repair> fuzzSearch(String keyWord);
}
