package com.usts.lem.service.impl;


import com.usts.lem.dao.IEquipmentDao;
import com.usts.lem.model.Equipment;
import com.usts.lem.service.IEquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("equipmentService")
public class EquipmentServiceImpl implements IEquipmentService {
    @Resource
    private IEquipmentDao equipmentDao;

    @Override
    public int countSpec(String spec) {
        return equipmentDao.countSpec(spec);
    }

    @Override
    public Equipment findBySerialNumber(String serialNumber) {
        return equipmentDao.findBySerialNumber(serialNumber);
    }

    @Override
    public List<Equipment> fuzzSearch(String keyWord) {
        return equipmentDao.fuzzSearch(keyWord);
    }

    @Override
    public int insert(Equipment obj) {
        return equipmentDao.insert(obj);
    }

    @Override
    public int update(Equipment obj) {
        return equipmentDao.update(obj);
    }

    @Override
    public List<Equipment> findAllData(Integer offset, Integer rows, String sort, String order) {
        return equipmentDao.findAllData(offset, rows, sort, order);
    }

    @Override
    public int count() {
        return equipmentDao.count();
    }

    @Override
    public int delete(Integer id) {
        return equipmentDao.delete(id);
    }

    @Override
    public Equipment findById(Integer id) {
        return equipmentDao.findById(id);
    }

    @Override
    public Equipment findBySerialNumberAll(String serialNumber){
        return equipmentDao.findBySerialNumberAll(serialNumber);
    }
}
