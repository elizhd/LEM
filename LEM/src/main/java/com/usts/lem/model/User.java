package com.usts.lem.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Setter
    @Getter
    private int id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String password;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private boolean role;



    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat);
    }
}
