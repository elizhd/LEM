package com.usts.lem.dao;
import com.usts.lem.model.Apply;

public interface IApplyDao extends IABaseDao<Apply> {

    /**
     * @Description 根据审批人
     * @Param serialNumber
     * @Return Equipment 审批信息
     */
    Apply findByApprover(String approver);

}
