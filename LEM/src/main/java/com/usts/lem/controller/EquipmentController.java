package com.usts.lem.controller;


import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.DataList;
import com.usts.lem.model.Equipment;
import com.usts.lem.service.IEquipmentService;
import com.usts.lem.service.IRepairService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/equip")
public class EquipmentController {
    // 类型于编号映射
    Map<String, String> specMapper = new HashMap<String, String>() {{
        put("显微镜", "A");
        put("精密仪器", "B");
        put("普通实验仪器", "C");
    }};


    @Resource
    IEquipmentService equipmentService;
    @Resource
    IRepairService repairService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    protected void writeJSON2Response(Object out, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(out);
            log.debug("SERVER TO FRONT END:" + out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/getAllInfo")
    @ResponseBody
    public void getAllInfo(@RequestParam(value = "limit", defaultValue = "10") Integer limit,
                           @RequestParam(value = "offset", defaultValue = "1") Integer offset,
                           @RequestParam(value = "sort") String sort,
                           @RequestParam(value = "order") String order,
                           HttpServletRequest request, HttpServletResponse response) {
        Equipment equipment = (Equipment) request.getSession().getAttribute("equipObj");
        log.debug("SERVER Get Equipment: " + equipment);
        int size = equipmentService.count();
        DataList<Equipment> equipList = new DataList<>();
        List<Equipment> list = equipmentService.findAllData(offset, limit, sort, order);
        if (list != null) {
            equipList.setRows(list);
            equipList.setTotal(size);
        }
        writeJSON2Response(equipList.toString(), response);
    }

//    @PostMapping(value = "/insertData")
//    @ResponseBody
//    public void insertData(@RequestBody Equipment equipment, HttpServletRequest request,
//                           HttpServletResponse response) {
//        log.debug("SERVER Get Equipment: " + equipment.toString());
//        String spec = equipment.getSpec();
//
//        String serialNumber;
//        int specAmount;
//        if (!specMapper.containsKey(spec)) {
//            specAmount = equipmentService.countSpec("其他");
//            equipment.setSpec("其他");
//            serialNumber = "T" + String.format("%04d", specAmount + 1);
//        } else {
//            serialNumber = specMapper.get(spec);
//            specAmount = equipmentService.countSpec(spec);
//            serialNumber += String.format("%04d", specAmount + 1);
//        }
//        equipment.setSerialNumber(serialNumber);
//        equipment.setEState(1);
//
//        JSONObject result = new JSONObject();
//
//        if (equipmentService.insert(equipment) > 0)
//            result.put("flag", true);
//        else
//            result.put("flag", false);
//        writeJSON2Response(result, response);
//    }


//    // 将state设为2 表明维修
//    @PostMapping(value = "/repairByIds")
//    @ResponseBody
//    public void repairByIds(@RequestParam(value = "ids") String ids, HttpServletResponse response) {
//        log.debug("Get ids: " + ids);
//        String[] idArray = ids.split(",");
//        JSONObject result = new JSONObject();
//        try {
//            for (String id : idArray) {
//                Equipment te = equipmentService.findById(Integer.valueOf(id));
//                Repair re = new Repair();
//                re.setSerialNumber(te.getSerialNumber());
//                re.setName(te.getName());
//                re.setSpec(te.getSpec());
//                re.setEState("2");
//                repairService.insert(re);
//                te.setEState(2);
//                equipmentService.update(te);
//            }
//            result.put("flag", true);
//        } catch (Exception e) {
//            result.put("flag", false);
//        }
//        writeJSON2Response(result, response);
//    }


    @PostMapping(value = "/updateData")
    @ResponseBody
    public void updateData(@RequestBody Equipment equipment,
                           HttpServletResponse response, HttpServletRequest request) {
        log.debug("SERVER Get Equipment " + equipment.toString());
        JSONObject result = new JSONObject();
        equipment.setEState(1);
        if (equipmentService.update(equipment) > 0)
            result.put("flag", true);
        else
            result.put("flag", false);
        writeJSON2Response(result, response);
    }


    @PostMapping(value = "/findBySerialNumber")
    @ResponseBody
    public void findByTime(@RequestParam(value = "serialNumber") String serialNumber,
                           HttpServletRequest request, HttpServletResponse response) {
        DataList<Equipment> equipmentList = new DataList<>();
        try {
            List<Equipment> list = new ArrayList<>();
            list.add(equipmentService.findBySerialNumber(serialNumber));
            equipmentList.setRows(list);
            equipmentList.setTotal(list.size());
        } catch (Exception e) {
            log.error("SERVER " + e.toString());

        }

        writeJSON2Response(equipmentList.toString(), response);
    }

    @PostMapping(value = "/fuzzSearch")
    @ResponseBody
    public void findByKeyWord(@RequestParam(value = "keyword") String keyword,
                              HttpServletResponse response, HttpServletRequest request) {
        DataList<Equipment> equipmentList = new DataList<>();
        try {
            List<Equipment> list = equipmentService.fuzzSearch(keyword);
            equipmentList.setRows(list);
            equipmentList.setTotal(list.size());

        } catch (Exception e) {
            log.error("SERVER " + e.toString());

        }
        writeJSON2Response(equipmentList.toString(), response);


    }
}
