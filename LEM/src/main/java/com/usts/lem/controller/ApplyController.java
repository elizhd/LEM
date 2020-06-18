package com.usts.lem.controller;

import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.*;
import com.usts.lem.service.IApplyService;
import com.usts.lem.service.IBuyService;
import com.usts.lem.service.IEquipmentService;
import com.usts.lem.service.IScrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/apply")
public class ApplyController {
    @Resource(name = "applyService")
    IApplyService applyService;
    @Resource(name = "scrapService")
    IScrapService scrapService;
    @Resource(name = "buyService")
    IBuyService buyService;
    @Resource(name = "equipmentService")
    IEquipmentService equipmentService;

    // 类型于编号映射
    Map<String, String> specMapper = new HashMap<String, String>() {{
        put("显微镜", "A");
        put("精密仪器", "B");
        put("普通实验仪器", "C");
    }};

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

    //获取所有没有审批的记录
    @GetMapping(value = "/getVisiableInfo")
    @ResponseBody
    public void getVisibleInfo(@RequestParam(value = "limit", defaultValue = "10") Integer limit,
                           @RequestParam(value = "offset", defaultValue = "1") Integer offset,
                           @RequestParam(value = "sort") String sort,
                           @RequestParam(value = "order") String order,
                           HttpServletRequest request, HttpServletResponse response) {
        Apply apply = (Apply) request.getSession().getAttribute("equipObj");
        log.debug("SERVER Get Equipment: " + apply);
        int size = applyService.countVisible();
        DataList<Apply> equipList = new DataList<>();
        List<Apply> list = applyService.findVisibleData(offset, limit, sort, order);
        if (list != null) {
            equipList.setRows(list);
            equipList.setTotal(size);
        }
        writeJSON2Response(equipList.toString(), response);
    }

    //所有的申请表记录
    @GetMapping(value = "/getAllInfo")
    @ResponseBody
    public void getAllInfo(@RequestParam(value = "limit", defaultValue = "10") Integer limit,
                           @RequestParam(value = "offset", defaultValue = "1") Integer offset,
                           @RequestParam(value = "sort") String sort,
                           @RequestParam(value = "order") String order,
                           HttpServletRequest request, HttpServletResponse response) {
        Apply apply = (Apply) request.getSession().getAttribute("equipObj");
        log.debug("SERVER Get Equipment: " + apply);
        int size = applyService.count();
        DataList<Apply> equipList = new DataList<>();
        List<Apply> list = applyService.findAllData(offset, limit, sort, order);
        if (list != null) {
            equipList.setRows(list);
            equipList.setTotal(size);
        }
        writeJSON2Response(equipList.toString(), response);
    }

    @PostMapping(value = "/insertData")
    @ResponseBody
    public void insertData(@RequestBody Apply apply, HttpServletRequest request,
                           HttpServletResponse response) {
        log.debug("SERVER Get Equipment: " + apply.toString());
        System.out.println(apply.getPurchaseDate());
        String spec = apply.getSpec();

        String serialNumber;
        int specAmount;
        if (!specMapper.containsKey(spec)) {
            specAmount = equipmentService.countSpec("其他");
            apply.setSpec("其他");
            serialNumber = "T" + String.format("%04d", specAmount + 1);
        } else {
            serialNumber = specMapper.get(spec);
            specAmount = equipmentService.countSpec(spec);
            serialNumber += String.format("%04d", specAmount + 1);
        }
        apply.setSerialNumber(serialNumber);

        JSONObject result = new JSONObject();
        if (applyService.insert(apply) > 0)
            result.put("flag", true);
        else
            result.put("flag", false);
        writeJSON2Response(result, response);
    }

    // 同意申请
    @PostMapping(value = "/approval")
    @ResponseBody
    public void deleteByIds1(@RequestParam(value = "ids") String ids, HttpServletResponse response,HttpSession session) {
        log.debug("Get ids: " + ids);
        String[] idArray = ids.split(",");
        JSONObject result = new JSONObject();
        try {
            for (String id : idArray) {
                Apply te = applyService.findById(Integer.valueOf(id));
                te.setResult(true);
                applyService.updateResult(te);
                //在这将数据插入对应的申请表和报废表
                if (te.getApplytype() == 0) {
                    Scrap sc = new Scrap();
                    Date date = new Date();
                    sc.setSpec(te.getSpec());
                    sc.setName(te.getName());
                    sc.setType(te.getType());
                    sc.setSerialNumber(te.getSerialNumber());
                    sc.setManufacture(te.getManufacture());
                    sc.setScrapDate(date);
                    User user = (User) session.getAttribute("userObj");//获取当前登录用户信息
                    sc.setApprover(user.getName());
                    scrapService.insert(sc);
                    equipmentService.deleteBySerialNumber(te.getSerialNumber());

                }       //报废表
                else {
                    Buy buy = new Buy();
                    Date date = new Date();
                    buy.setSpec(te.getSpec());
                    buy.setName(te.getName());
                    buy.setType(te.getType());
                    buy.setSerialNumber(te.getSerialNumber());
                    buy.setManufacture(te.getManufacture());
                    buy.setApplyDate(date);
                    buy.setUnitPrice(te.getUnitPrice());
                    User user = (User) session.getAttribute("userObj");//获取当前登录用户信息
                    buy.setApprover(user.getName());
                    buy.setResult("批准");
                    buyService.insert(buy);

                    //数据插入到设备信息表
                    Equipment equipment = new Equipment();
                    equipment.setSpec(te.getSpec());
                    equipment.setName(te.getName());
                    equipment.setType(te.getType());
                    equipment.setUnitPrice(te.getUnitPrice());
                    equipment.setManufacture(te.getManufacture());
                    equipment.setPurchaseDate(date);
                    equipment.setManager(te.getApplicant());


                    String spec = equipment.getSpec();
                    String serialNumber;
                    int specAmount;
                    if (!specMapper.containsKey(spec)) {
                        specAmount = equipmentService.countSpec("其他");
                        equipment.setSpec("其他");
                        serialNumber = "T" + String.format("%04d", specAmount + 1);
                    } else {
                        serialNumber = specMapper.get(spec);
                        specAmount = equipmentService.countSpec(spec);
                        serialNumber += String.format("%04d", specAmount + 1);
                    }
                    equipment.setSerialNumber(serialNumber);
                    equipment.setEState(1);
                    equipmentService.insert(equipment);
                }//购买表
            }

            result.put("flag", true);
        } catch (Exception e) {
            result.put("flag", false);
        }
        writeJSON2Response(result, response);
    }

    // 拒绝申请
    @PostMapping(value = "/refuse")
    @ResponseBody
    public void deleteByIds2(@RequestParam(value = "ids") String ids, HttpServletResponse response,HttpSession session) {
        log.debug("Get ids: " + ids);
        String[] idArray = ids.split(",");
        JSONObject result = new JSONObject();
        try {
            for (String id : idArray) {
                Apply te = applyService.findById(Integer.valueOf(id));
                te.setResult(false);
                te.setIsvisible(false);
                applyService.updateResult(te);
                //在这将数据插入对应的申请表和报废表
                if (te.getApplytype()==0){
                    Equipment equipment = equipmentService.findBySerialNumberAll(te.getSerialNumber());
                    equipment.setEState(1);
                    equipmentService.update(equipment);
                }else {
                    Buy buy = new Buy();
                    Date date = new Date();
                    buy.setSpec(te.getSpec());
                    buy.setName(te.getName());
                    buy.setType(te.getType());
                    buy.setSerialNumber(te.getSerialNumber());
                    buy.setManufacture(te.getManufacture());
                    buy.setApplyDate(date);
                    buy.setUnitPrice(te.getUnitPrice());
                    User user = (User) session.getAttribute("userObj");//获取当前登录用户信息
                    buy.setApprover(user.getName());
                    buy.setResult("驳回");
                    buyService.insert(buy);
                    //申请购买表
                }
            }
            result.put("flag", true);
        } catch (Exception e) {
            result.put("flag", false);
        }
        writeJSON2Response(result, response);
    }

    // 报废申请
    @PostMapping(value = "/scrapByIds")
    @ResponseBody
    public void scrapByIds(@RequestParam(value = "ids") String ids, HttpServletResponse response,HttpSession session) {
        log.debug("Get ids: " + ids);
        String[] idArray = ids.split(",");
        JSONObject result = new JSONObject();
        try {
            for (String id : idArray) {
                Equipment equipment = equipmentService.findById(Integer.valueOf(id));
                equipment.setEState(3);
                equipmentService.update(equipment);
                Apply apply =new Apply();
                apply.setId(equipment.getId());
                apply.setSerialNumber(equipment.getSerialNumber());
                apply.setType(equipment.getType());
                apply.setName(equipment.getName());
                apply.setSpec(equipment.getSpec());
                apply.setUnitPrice(equipment.getUnitPrice());
                apply.setManufacture(equipment.getManufacture());
                apply.setPurchaseDate(equipment.getPurchaseDate());
                User user = (User) session.getAttribute("userObj");//获取当前登录用户信息
                apply.setApplicant(user.getName());
                apply.setApplytype(0);
                apply.setResult(false);
                apply.setIsvisible(true);
                applyService.insert(apply);
                //报废申请加入
            }
            result.put("flag", true);
        } catch (Exception e) {
            result.put("flag", false);
        }
        writeJSON2Response(result, response);
    }
}
