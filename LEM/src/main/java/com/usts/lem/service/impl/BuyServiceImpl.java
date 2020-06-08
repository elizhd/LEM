package com.usts.lem.service.impl;

import com.usts.lem.dao.IBuyDao;
import com.usts.lem.model.Buy;
import com.usts.lem.service.IBuyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("buyService")
public class BuyServiceImpl implements IBuyService {
    @Resource
    private IBuyDao buyDao;

    @Override
    public int countSpec(String spec) {
        return buyDao.countSpec(spec);
    }

    @Override
    public Buy findBySerialNumber(String serialNumber) {
        return buyDao.findBySerialNumber(serialNumber);
    }
    @Override
    public List<Buy> fuzzSearch(String keyWord) {
        return buyDao.fuzzSearch(keyWord);
    }

    @Override
    public int insert(Buy obj) {
        return buyDao.insert(obj);
    }
    @Override
    public int update(Buy obj) {
        return buyDao.update(obj);
    }

    @Override
    public List<Buy> findAllData(Integer offset, Integer rows, String sort, String order) {
        return buyDao.findAllData(offset,rows,sort,order);
    }
    @Override
    public int count() {
        return buyDao.count();
    }
    @Override
    public int delete(Integer id) {
        return buyDao.delete(id);
    }
    @Override
    public Buy findById(Integer id) {
        return buyDao.findById(id);
    }

    @Override
    public List<Buy> findAll() {
        return buyDao.findAll();
    }
}
