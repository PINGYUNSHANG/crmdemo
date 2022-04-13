package com.pys.crm.workbench.web.controller;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.domain.ReturnObject;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.commons.utils.UUIDUtils;
import com.pys.crm.settings.domain.User;
import com.pys.crm.workbench.domain.ActivityRemark;
import com.pys.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpServletRequest request) {
        //二次封装参数
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formatDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);
        ReturnObject returnObject = new ReturnObject();

        try {
            int nums = activityRemarkService.saveCreateActivityRemark(remark);
            if (nums > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试～");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试～");
            e.printStackTrace();
        }

        return returnObject;
    }

    /**
     * 根据id删除市场活动
     */
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id) {
        ReturnObject returnObject = new ReturnObject();

        try {
            int nums = activityRemarkService.deleteActivityRemarkById(id);
            if (nums > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试～");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试～");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark remark, HttpServletRequest request) {
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        //封装参数
        remark.setEditTime(DateUtils.formatDateTime(new Date()));
        remark.setEditBy(user.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存修改的市场活动备注
            int ret = activityRemarkService.saveEditActivityRemark(remark);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }
}
