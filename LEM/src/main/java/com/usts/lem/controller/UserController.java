package com.usts.lem.controller;


import com.alibaba.fastjson.JSONObject;
import com.usts.lem.model.User;
import com.usts.lem.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("/user")
public class UserController {
    @Resource(name = "userService")
    IUserService userService;

    private Logger log = LoggerFactory.getLogger(this.getClass());


    /**
     * @Description 传输JSON数据至前端
     * @Author Haodong Zhao
     * @Param out
     * @Param response
     * @Return void
     */
    protected void writeJSON2Response(Object out, HttpServletResponse response) {
        try {
            response.getWriter().print(out);
            log.debug("SERVER TO FRONT END:" + out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 用户登录
     * @Author Haodong Zhao
     * @Param username 用户名
     * @Param password 密码
     * @Param request
     * @Param response
     * @Return void
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public void userLogin(@RequestParam(value = "username") String username,
                          @RequestParam(value = "password") String password,
                          HttpServletRequest request, HttpServletResponse response) {
        log.debug("FRONT END TO SERVER: " + username + " " + password);
        JSONObject resultJson = new JSONObject();
        User user = userService.findByNameAndPassword(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userObj", user);
            resultJson.put("result", true);
        } else {
            resultJson.put("result", false);
        }
        writeJSON2Response(resultJson, response);
    }


    /**
     * @Description 用户注册
     * @Author Haodong Zhao
     * @Param username 用户名
     * @Param password 密码
     * @Param email 电子邮箱
     * @Param request
     * @Param response
     * @Return void
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public void userRegister(@RequestParam(value = "username") String username,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "email") String email,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        User user = new User(0, username, password, email, false);
        log.debug(user.toString());
        log.debug("SAVE TO DB: " + user);
        JSONObject resultJson = new JSONObject();
        if (userService.insertUser(user) > 0)
            resultJson.put("result", true);
        else
            resultJson.put("result", false);
        writeJSON2Response(resultJson, response);
    }


    /**
     * @Description 检查用户名是否已经被注册
     * @Author Haodong Zhao
     * @Param username 需要查询的用户名
     * @Param response
     */
    @PostMapping(value = "/isRegistered")
    @ResponseBody
    public void isRegistered(@RequestParam(value = "username") String username, HttpServletResponse response) {
        log.debug("FRONT END TO SERVER: " + username);
        JSONObject resultJson = new JSONObject();
        if (userService.findUserByName(username) == null)
            resultJson.put("result", true);
        else
            resultJson.put("result", false);
        writeJSON2Response(resultJson, response);
    }

    /**
     * @Description 用户退出
     * @Param request
     * @Param response
     * @Return void
     */
    @PostMapping(value = "/logout")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        try {
            request.getSession().invalidate();
            result.put("flag", true);
            log.debug("User logout");
        } catch (Exception e) {
            log.error(e.toString());
            result.put("flag", false);
        }
        writeJSON2Response(result, response);
    }

}
