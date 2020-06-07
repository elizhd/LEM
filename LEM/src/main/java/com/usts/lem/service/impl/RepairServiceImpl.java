package com.usts.lem.service.impl;

import com.usts.lem.dao.IRepairDao;
import com.usts.lem.model.Repair;
import com.usts.lem.service.IRepairService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("repairService")
public class RepairServiceImpl implements IRepairService {
    @Resource
    private IRepairDao repairDao;

    @Override
    public int countSpec(String spec) {
        return repairDao.countSpec(spec);
    }

    @Override
    public Repair findBySerialNumber(String serialNumber) {
        return repairDao.findBySerialNumber(serialNumber);
    }

    @Override
    public List<Repair> fuzzSearch(String keyWord) {
        return repairDao.fuzzSearch(keyWord);
    }

    @Override
    public int insert(Repair obj) {
        return repairDao.insert(obj);
    }


    @Override
    public int update(Repair obj) {
        return repairDao.update(obj);
    }

    @Override
    public List<Repair> findAllData(Integer offset, Integer rows, String sort, String order) {
        return repairDao.findAllData(offset,rows,sort,order);
    }

    @Override
    public int count() {
        return repairDao.count();
    }

    @Override
    public int delete(Integer id) {
        return repairDao.delete(id);
    }

    @Override
    public Repair findById(Integer id) {
        return repairDao.findById(id);
    }

    @Override
    public List<Repair> findAll(){
        return repairDao.findAll();
    }
}
