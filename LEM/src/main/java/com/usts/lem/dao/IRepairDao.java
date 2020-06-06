package com.usts.lem.dao;


import com.usts.lem.model.Repair;

import java.util.List;
public interface IRepairDao extends IMBaseDao<Repair> {


    int countSpec(String spec);
    /**
     * @Description 根据设备编号查找
     * @Date
     * @Param  * @param serialNumber
     * @return com.usts.lem.model.Repair
     **/

    Repair findBySerialNumber(String serialNumber);

    /**
     * @Description 关键字模糊查找
     * @Date
     * @Param  * @param keyWord
     * @return java.util.List<com.usts.lem.model.Repair>
     **/

    List<Repair> fuzzSearch(String keyWord);


}
