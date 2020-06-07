package com.usts.lem.controller;

import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.DataList;
import com.usts.lem.model.Equipment;
import com.usts.lem.model.Repair;
import com.usts.lem.service.IEquipmentService;
import com.usts.lem.service.IRepairService;
import com.usts.lem.util.RepairExportExcelUtil;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/repair")
public class RepairController {
    RepairExportExcelUtil exportExcelUtil = new RepairExportExcelUtil();

    // 类型于编号映射
    Map<String, String> specMapper = new HashMap<String, String>() {{
        put("显微镜", "A");
        put("精密仪器", "B");
        put("普通实验仪器", "C");
    }};

    @Resource
    IRepairService repairService;
    @Resource
    IEquipmentService equipmentService;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    protected void writeJSON2Response(Object out, HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(out);
            log.debug("SERVER TO FRONT END"+out);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/getAllInfo")
    @ResponseBody
    public void getAllInfo(@RequestParam(value="limit",defaultValue = "10")Integer limit,
                           @RequestParam(value = "offset", defaultValue = "1") Integer offset,
                           @RequestParam(value = "sort") String sort,
                           @RequestParam(value = "order") String order,
                           HttpServletRequest request, HttpServletResponse response){
        Repair repair = (Repair)request.getSession().getAttribute("repairObj");
        log.debug("SERVER Get Repair: " + repair);
        int size  = repairService.count();
        DataList<Repair> repairList = new DataList<>();
        List<Repair> list = repairService.findAllData(offset,limit,sort,order);
        if(list != null){
            repairList.setRows(list);
            repairList.setTotal(size);
        }
        writeJSON2Response(repairList.toString(),response);
    }

//    @PostMapping(value = "/insert")
//    @ResponseBody
//    public void insert(@RequestBody Repair repair, HttpServletRequest request,
//                       HttpServletResponse response){
//        log.debug("SERVER Get Repair: " + repair.toString());
//        String spec = repair.getSpec();
//
//        String serialNumber;
//        int specAmount;
//        if (!specMapper.containsKey(spec)) {
//            specAmount = repairService.countSpec("其他");
//            serialNumber = "T" + String.format("%04d", specAmount + 1);
//        } else {
//            serialNumber = specMapper.get(spec);
//            specAmount = repairService.countSpec(spec);
//            serialNumber += String.format("%04d", specAmount + 1);
//        }
//        repair.setSerialNumber(serialNumber);
//        repair.setEState("2");
//
//        JSONObject result = new JSONObject();
//
//        if (repairService.insert(repair) > 0)
//            result.put("flag", true);
//        else
//            result.put("flag", false);
//        writeJSON2Response(result, response);
//    }


    //维修完成，将状态改为1
    @PostMapping(value = "/repairFinish")
    @ResponseBody
    public void finishByIds(@RequestParam(value = "ids")String ids,HttpServletResponse response){
        log.debug("Get ids" + ids);
        String[] idArray = ids.split(",");
        JSONObject result = new JSONObject();
        try {
            for(String id : idArray){
                Repair re = repairService.findById(Integer.valueOf(id));
                Equipment te = equipmentService.findBySerialNumberAll(re.getSerialNumber());
                te.setEState(1);
                equipmentService.update(te);
                re.setEState("1");
                repairService.update(re);
            }
            result.put("flag",true);
        }catch (Exception e){
            result.put("flag",false);
        }
        writeJSON2Response(result,response);
    }

    @PostMapping(value = "/updateData")
    @ResponseBody
    public void updateData(@RequestBody Repair repair,
                           HttpServletResponse response, HttpServletRequest request) {
        log.debug("SERVER Get Repair " + repair.toString());
        JSONObject result = new JSONObject();
        repair.setEState("2");
        if (repairService.update(repair) > 0)
            result.put("flag", true);
        else
            result.put("flag", false);
        writeJSON2Response(result, response);
    }

    @PostMapping(value = "/findBySerialNumber")
    @ResponseBody
    public void findByTime(@RequestParam(value = "serialNumber") String serialNumber,
                           HttpServletRequest request, HttpServletResponse response) {
        DataList<Repair> repairList = new DataList<>();
        try {
            List<Repair> list = new ArrayList<>();
            list.add(repairService.findBySerialNumber(serialNumber));
            repairList.setRows(list);
            repairList.setTotal(list.size());
        } catch (Exception e) {
            log.error("SERVER " + e.toString());

        }

        writeJSON2Response(repairList.toString(), response);
    }


    @PostMapping(value = "/fuzzSearch")
    @ResponseBody
    public void findByKeyWord(@RequestParam(value = "keyword") String keyword,
                              HttpServletResponse response, HttpServletRequest request) {
        DataList<Repair> repairList = new DataList<>();
        try {
            List<Repair> list = repairService.fuzzSearch(keyword);
            repairList.setRows(list);
            repairList.setTotal(list.size());

        } catch (Exception e) {
            log.error("SERVER " + e.toString());

        }
        writeJSON2Response(repairList.toString(), response);


    }


    /**
     * @Author:  Tim
     * @Description //导出数据
     * @Date
     * @Param  * @param response
     * @return java.lang.String
     **/

    @RequestMapping(value = "downloadExcel")
    public @ResponseBody
    String downloadExcel(HttpServletResponse response) {
        response.setContentType("application/binary;charset=UTF-8");
        try {
            ServletOutputStream out = response.getOutputStream();
            try {
                response.setHeader("Content-Disposition",
                        "attachment;fileName=" + URLEncoder.encode("设备维修表" + ".xls","UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String[] titles = {"设备号","设备名","类型","修理价格","修理厂商","修理日期","修理状态","责任人"};
            exportExcelUtil.export(titles,repairService.findAll(),out);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出信息失败";
        }
    }
}
