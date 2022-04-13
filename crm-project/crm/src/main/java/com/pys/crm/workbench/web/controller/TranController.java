package com.pys.crm.workbench.web.controller;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.domain.ReturnObject;
import com.pys.crm.settings.domain.DicValue;
import com.pys.crm.settings.domain.User;
import com.pys.crm.settings.service.DicValueService;
import com.pys.crm.settings.service.UserService;
import com.pys.crm.workbench.domain.Tran;
import com.pys.crm.workbench.domain.TranHistory;
import com.pys.crm.workbench.domain.TranRemark;
import com.pys.crm.workbench.mapper.TranHistoryMapper;
import com.pys.crm.workbench.mapper.TranRemarkMapper;
import com.pys.crm.workbench.service.CustomerService;
import com.pys.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    TranService tranService;

        @Autowired
        private TranRemarkMapper tranRemarkMapper;

        @Autowired
        private TranHistoryMapper tranHistoryMapper;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {
        //调用service层方法，查询数据
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //将数据放入作用域中
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("stageList", stageList);
        //请求转发
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request) {
        //调用service方法，查询user
        List<User> userList = userService.queryAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //将数据放入作用域中
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("userList", userList);

        return "workbench/transaction/save";

    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue) {
        //解析properties配置文件，根据阶段获取可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        //返回响应信息
        return possibility;
    }

    @RequestMapping("/workbench/transaction/queryAllCustomerByName.do")
    @ResponseBody
    public Object queryAllCustomerByName(String customerName) {
        List<String> customerNameList = customerService.queryCustomerByName(customerName);
        return customerNameList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        map.put(Contants.SESSION_USER, request.getSession().getServletContext().getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.saveCreateTran(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后～");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,HttpServletRequest request){
        //调用service方法 查询数据
        Tran tran = tranService.queryTranForDetailById(id);

        //调用交易备注service
        List<TranRemark> tranRemarkList= tranRemarkMapper.selectTranRemarkForDetailByTranId(id);


        //调用交易历史servcie
        List<TranHistory> historyList = tranHistoryMapper.selectTranHistoryForDetailByTranId(id);

        //根据交易所处的阶段名称，查找可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());

        //把数据保存到request中

        request.setAttribute("tran",tran);
        request.setAttribute("tranRemarkList",tranRemarkList);
        request.setAttribute("historyList",historyList);
        request.setAttribute("possibility",possibility);

        //调用service层方法，查询所有阶段
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute("stageList",stageList);


        return "workbench/transaction/detail";


    }
}
