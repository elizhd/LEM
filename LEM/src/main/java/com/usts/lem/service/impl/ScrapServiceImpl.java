package com.usts.lem.service.impl;

import com.usts.lem.dao.IScrapDao;
import com.usts.lem.model.Scrap;
import com.usts.lem.service.IScrapService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("scrapService")
public class ScrapServiceImpl implements IScrapService {
    @Resource
    private IScrapDao scrapDao;

    @Override
    public int countSpec(String spec) {
        return scrapDao.countSpec(spec);
    }

    @Override
    public Scrap findBySerialNumber(String serialNumber) {
        return scrapDao.findBySerialNumber(serialNumber);
    }

    @Override
    public List<Scrap> fuzzSearch(String keyWord) {
        return scrapDao.fuzzSearch(keyWord);
    }

    @Override
    public int insert(Scrap obj) {
        return scrapDao.insert(obj);
    }


    @Override
    public int update(Scrap obj) {
        return scrapDao.update(obj);
    }

    @Override
    public List<Scrap> findAllData(Integer offset, Integer rows, String sort, String order) {
        return scrapDao.findAllData(offset,rows,sort,order);
    }

    @Override
    public int count() {
        return scrapDao.count();
    }

    @Override
    public int delete(Integer id) {
        return scrapDao.delete(id);
    }

    @Override
    public Scrap findById(Integer id) {
        return scrapDao.findById(id);
    }

    @Override
    public List<Scrap> findAll() {
        return scrapDao.findAll();
    }
}
