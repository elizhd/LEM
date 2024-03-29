package com.usts.lem.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Package com.usts.lem.model;
 * @Description 设备信息类
 * @Author Haodong Zhao
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Equipment {
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseDate;  // 购置日期

    @Setter
    @Getter
    private String manager;   // 经办人
    @Setter
    @Getter
    private int eState; // 状态 0-申请 1-正常 2-维修 3-报废

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }

}
