package com.pys.crm.settings.web.controller;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.domain.ReturnObject;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.settings.domain.User;
import com.pys.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 这个url要和当前controller方法处理完请求之后，响应信息返回的页面资源目录保持一致
     */
    @RequestMapping(value = "/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    // '/'代表目录的根 也就是视图解析器那个地方
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login( HttpSession session,HttpServletRequest request, HttpServletResponse response, String loginAct, String loginPwd, String isRemPwd) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        map.put("isRemPwd", isRemPwd);
        //调用service层的方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
//        System.out.println("User:"+user);
        //根据查询结果生成响应信息
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            //登陆失败，用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");

        } else {//进一步判断，账号是否合法
            if (DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0) {
                //账号过期，登陆失败
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号过期");

            } else if ("0".equals(user.getLockState())) {
                //登陆失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");

            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //登陆失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");

            } else {
                //登陆成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //把user对象保存到session中
//                HttpSession session = request.getSession(true);

                //保存到application
                request.getSession().getServletContext().setAttribute(Contants.SESSION_USER,user);
//             //如果需要记住密码，则写cookie
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd",loginPwd);
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else{
                    //把没有过期的cookie删除
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("logPwd", "1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        //System.out.println("session:"+((User)session.getAttribute(Contants.SESSION_USER)).getName());
        return returnObject;
    }
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookie
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("logPwd", "1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        //跳转到首页,重定向
        return "redirect:/";//springmvc底层来重定向 默认 httpServletResponse.sendRedirect("/crm/");
    }
}
