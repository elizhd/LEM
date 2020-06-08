package com.usts.lem.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Scrap {
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
    private String manufacture;   // 生产厂家
    @Setter
    @Getter
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date scrapDate;  // 报废日期
    @Setter
    @Getter
    private String approver;   // 受理人

    @Override
    public String toString() {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }

}
