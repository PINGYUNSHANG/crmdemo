package com.pys.crm.workbench.web.controller;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.domain.ReturnObject;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.commons.utils.UUIDUtils;
import com.pys.crm.settings.domain.DicValue;
import com.pys.crm.settings.domain.User;
import com.pys.crm.settings.service.DicValueService;
import com.pys.crm.settings.service.UserService;
import com.pys.crm.workbench.domain.Activity;
import com.pys.crm.workbench.domain.Clue;
import com.pys.crm.workbench.domain.ClueActivityRelation;
import com.pys.crm.workbench.domain.ClueRemark;
import com.pys.crm.workbench.service.ActivityService;
import com.pys.crm.workbench.service.ClueActivityRelationService;
import com.pys.crm.workbench.service.ClueRemarkService;
import com.pys.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        //将数据保存到作用域
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);

        //请求转发
        return "workbench/clue/index";

    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpServletRequest request) {
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        //二次封装
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        clue.setCreateBy(user.getId());
        //调用service
        ReturnObject returnObject = new ReturnObject();
        try {
            int nums = clueService.saveCreateClue(clue);
            if (nums > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试～");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试～");
            e.printStackTrace();
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id, HttpServletRequest request) {
        //调用service层方法查询数据
        Clue clue = clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        //把数据保存到作用域中
        request.setAttribute("clue", clue);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("activityList", activityList);

        //请求转发
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName, String clueId) {
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);

        //调用service方法去查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);

        //根据查询结果，返回响应信息
        //直接转成json数组
        return activityList;
    }

    //保存线索关联市场活动
    @RequestMapping("/workbench/clue/saveBund.do")
    public @ResponseBody
    Object saveBund(String[] activityId, String clueId) {
        //封装参数
        ClueActivityRelation car = null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for (String ai : activityId) {
            car = new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法，批量保存线索和市场活动的关联关系
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);

            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/clue/saveUnBund.do")
    @ResponseBody
    public Object saveUnBund(ClueActivityRelation relation) {
        ReturnObject returnObject = new ReturnObject();
        //调用service层方法
        try {
            int nums = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if (nums > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName, String clueId) {
        //封装参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityName", activityName);
        map.put("clueId", clueId);
        //调用service方法
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        //根据查询结果返回响应信息
        return activityList;

    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,
                              String money,
                              String name,
                              String expectedDate,
                              String stage,
                              String activityId,
                              String isCreateTran,
                              HttpServletRequest request) {
        //封装参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clueId", clueId);
        map.put("money", money);
        map.put("name", name);
        map.put("expectedDate", expectedDate);
        map.put("stage", stage);
        map.put("activityId", activityId);
        map.put("isCreateTran", isCreateTran);
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        map.put(Contants.SESSION_USER, user);

        ReturnObject returnObject = new ReturnObject();
        //调用service方法进行转换
        try {
            clueService.saveConvertClue(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试～");
            e.printStackTrace();
        }
        return returnObject;

    }

}
