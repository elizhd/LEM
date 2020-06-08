package com.usts.lem.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Package com.usts.lem.model;
 * @Description 数据表类 便于按照格式传至前端
 * @Author Haodong Zhao
 */

@AllArgsConstructor
@NoArgsConstructor
public class DataList<T> {
    @Setter
    @Getter
    private int total;
    @Setter
    @Getter
    private List<T> rows;

    @Override
    public String toString() {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd";
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }
}
