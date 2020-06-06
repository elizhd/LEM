package com.usts.lem.controller;

import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.Apply;
import com.usts.lem.model.DataList;
import com.usts.lem.service.IApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/apply")
public class ApplyController {
    @Resource(name = "applyService")
    IApplyService applyService;

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
    public void deleteByIds1(@RequestParam(value = "ids") String ids, HttpServletResponse response) {
        log.debug("Get ids: " + ids);
        String[] idArray = ids.split(",");
        JSONObject result = new JSONObject();
        try {
            for (String id : idArray) {
                Apply te = applyService.findById(Integer.valueOf(id));
                te.setResult(true);
                applyService.updateResult(te);
                //在这将数据插入对应的申请表和报废表
//                if (te.getApplytype==0){
//                    //报废表
//                }else {
//                    //申请表
//                }
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
    public void deleteByIds2(@RequestParam(value = "ids") String ids, HttpServletResponse response) {
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
//                if (te.getApplytype==0){
//                    //报废表
//                }else {
//                    //申请表
//                }
            }
            result.put("flag", true);
        } catch (Exception e) {
            result.put("flag", false);
        }
        writeJSON2Response(result, response);
    }
}