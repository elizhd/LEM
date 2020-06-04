package com.usts.lem.service;

import com.usts.lem.model.Apply;

public interface IApplyService extends IABaseService<Apply> {

    /**
     * @Description 根据审批人查找
     * @Param serialNumber
     * @Return Apply 申请表信息
     */
    Apply findByApprover(String approver);



}
