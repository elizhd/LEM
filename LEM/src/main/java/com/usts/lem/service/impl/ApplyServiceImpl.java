package com.usts.lem.service.impl;


import com.usts.lem.dao.IApplyDao;
import com.usts.lem.model.Apply;
import com.usts.lem.service.IApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("applyService")
public class ApplyServiceImpl implements IApplyService {
    @Resource
    private IApplyDao applyDao;

    @Override
    public Apply findByApprover(String approver) {
        return applyDao.findByApprover(approver);
    }

    @Override
    public int insert(Apply obj) {
        return applyDao.insert(obj);
    }

    @Override
    public int updateResult(Apply obj) {
        return applyDao.updateResult(obj);
    }

    @Override
    public List<Apply> findAllData(Integer offset, Integer rows, String sort, String order) {
        return applyDao.findAllData(offset, rows, sort, order);
    }

    @Override
    public List<Apply> findVisibleData(Integer offset, Integer rows, String sort, String order) {
        return  applyDao.findVisibleData(offset, rows, sort, order);
    }

    @Override
    public int count() {
        return applyDao.count();
    }

    @Override
    public int countVisible() {
        return applyDao.countVisible();
    }

    @Override
    public Apply findById(Integer id) {
        return applyDao.findById(id);
    }
}
