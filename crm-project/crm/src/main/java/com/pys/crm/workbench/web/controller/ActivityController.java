package com.pys.crm.workbench.web.controller;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.domain.ReturnObject;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.commons.utils.HSSFUtils;
import com.pys.crm.commons.utils.UUIDUtils;
import com.pys.crm.settings.domain.User;
import com.pys.crm.settings.service.UserService;
import com.pys.crm.workbench.domain.Activity;
import com.pys.crm.workbench.domain.ActivityRemark;
import com.pys.crm.workbench.service.ActivityRemarkService;
import com.pys.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //调用service层方法
        List<User> userList = userService.queryAllUsers();
        // 把数据保存到request中
        request.setAttribute("userList", userList);
        //请求转发到市场活动的主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpServletRequest request) {
        //继续封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service层方法
            int nums = activityService.saveCreateActivity(activity);
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

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,
                                                  String owner,
                                                  String startDate,
                                                  String endDate,
                                                  int pageNo,
                                                  int pageSize) {
        //封装参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        //调用service查询数据
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);

        //生成响应信息 放入map
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("activityList", activityList);
        resMap.put("totalRows", totalRows);
        return resMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();

        try {
            int nums = activityService.deleteActivityByIds(id);
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

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        return activityService.queryActivityById(id);
    }

    @RequestMapping("/workbench/activity/SaveEditActivity.do")
    @ResponseBody
    public Object SaveEditActivity(Activity activity, HttpServletRequest request) {
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            int nums = activityService.SaveActivity(activity);
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

    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
        //返回excel文件
        //1.设置响应类型
        response.setContentType("application/octet-stream,charset=UTF-8");
        //2. 获取输出流
        OutputStream out = response.getOutputStream();

        //  浏览器接收响应信息后，默认情况下，直接在显示窗口中打开响应信息，
        //  即使打不开，也会调用应用程序打开，只有在实在打不开的情况下，才会激活文件下载
        //可以设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开也打不开
        response.addHeader("Content-Disposition", "attachment;filename=mystudentList.xls");
        //从磁盘读文件excel(InputStream) ，把文件输出到浏览器(OutputStream)
        InputStream is = new FileInputStream("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/studentList.xls");
        byte[] data = new byte[1024];
        int len = 0;
        while ((len = is.read(data)) != -1) {
            out.write(data, 0, len);
        }
        //关闭资源
        is.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws Exception {
        List<Activity> activityList = activityService.queryAllActivities();
        //创建excel文件，写入信息
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动表");
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");

        cell = row.createCell(1);
        cell.setCellValue("所有者");

        cell = row.createCell(2);
        cell.setCellValue("名称");

        cell = row.createCell(3);
        cell.setCellValue("开始日期");

        cell = row.createCell(4);
        cell.setCellValue("结束日期");

        cell = row.createCell(5);
        cell.setCellValue("成本");

        cell = row.createCell(6);
        cell.setCellValue("描述");

        cell = row.createCell(7);
        cell.setCellValue("创建时间");

        cell = row.createCell(8);
        cell.setCellValue("创建者");

        cell = row.createCell(9);
        cell.setCellValue("修改时间");

        cell = row.createCell(10);
        cell.setCellValue("修改者");
        Activity activity = null;

        if (activityList != null && activityList.size() > 0) {
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(activity.getId());

                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());

                cell = row.createCell(2);
                cell.setCellValue(activity.getName());

                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());

                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());

                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());

                cell = row.createCell(6);
                cell.setCellValue(activity.getCost());

                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());

                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());

                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());

                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        //生成excel文件
//        OutputStream os = new FileOutputStream("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/文件导出excel/activityList.xls");
//        workbook.write(os);

//        os.close();

        //把生成的excel文件下载到用户客户端
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置响应头信息
        response.addHeader("Content-Disposition", "attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
//        InputStream is = new FileInputStream("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/文件导出excel/activityList.xls");
//        byte[] bytes = new byte[1024];
//        int len = 0;
//        while((len=is.read(bytes))!=-1){
//            out.write(bytes,0,len);
//        }
//        is.close();
        //内存到内存，直接写到浏览器中  ，不用生成文件
        workbook.write(out);
        System.out.println("ok～");
        out.flush();
    }

    /**
     * 配置springmvc文件上传解析器
     *
     * @param username
     * @param myFile
     */
    @RequestMapping("/workbench/activity/fileUpload.do")
    //MultipartFile 把文件所有内容封装到这个类
    @ResponseBody
    public Object fileUpload(String username, MultipartFile myFile) throws IOException {
        //把文本数据打印到控制台
        System.out.println("username" + username);
        //把文件在服务器指定的目录中生成一个同样的文件
        String originalFilename = myFile.getOriginalFilename();
        File file = new File("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/" + originalFilename);
        myFile.transferTo(file);
        //返回响应信息
        ReturnObject returnObject = new ReturnObject();
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("上传成功～");
        return returnObject;
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,HttpServletRequest request) {
        ReturnObject returnObject = new ReturnObject();

        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        try {
            //把接收到的excel文件写到磁盘目录中
//            String originalFilename = activityFile.getOriginalFilename();
//            File file = new File("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/" + originalFilename);
//            activityFile.transferTo(file);

            //解析excel文件根据文件生成workbook对象
//            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("/Users/pingyunshangpingyunshang/Downloads/crm-ssm项目/" + originalFilename));
            //优化 从内存--> 内存
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HSSFSheet sheet = workbook.getSheetAt(0);
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<Activity>();
            for(int i = 1;i<=sheet.getLastRowNum();i++){
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());
                for(int j = 0;j<row.getLastCellNum();j++){
                    cell = row.getCell(j);
                    String value = HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(value);
                    }else if(j==1){
                        activity.setStartDate(value);
                    }else if(j==2){
                        activity.setEndDate(value);
                    }else if(j==3){
                        activity.setCost(value);
                    }else if(j==4){
                        activity.setDescription(value);
                    }
                }
                activityList.add(activity);
            }
            //调用service层方法，保存市场活动
            int nums = activityService.saveCreateActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(nums);

        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试～");

        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据保存到作用域中
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        //跳转到明细页面
        //请求转发
        return "workbench/activity/detail";
    }

}
