package com.usts.lem.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
        * @Package com.usts.lem.model;
        * @Description 设备信息类
        * @Author Xuan ye
        */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Apply {
    @Setter
    @Getter
    private int id;    // 数据库中id
    @Setter
    @Getter
    private String serialNumber;  // 生成的设备号
    @Setter
    @Getter
    private String type; // 型号
    @Setter
    @Getter
    private String name; // 设备名
    @Setter
    @Getter
    private String spec;  // 类型
    @Setter
    @Getter
    private double unitPrice; // 单价
    @Setter
    @Getter
    private String manufacture;   // 生产厂家
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date purchaseDate;  // 购置日期
    @Setter
    @Getter
    private int applytype;//申请单类型 0是报废 1是购买
    @Setter
    @Getter
    private String  applicant;   // 申请人
    @Setter
    @Getter
    private String approver;//审批人
    @Setter
    @Getter
    private boolean isvisible;//是否可视，也就是是否审批过
    @Setter
    @Getter
    private boolean result; // 审批结果

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }
}
