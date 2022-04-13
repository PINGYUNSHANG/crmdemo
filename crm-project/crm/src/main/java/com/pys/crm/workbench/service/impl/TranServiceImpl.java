package com.pys.crm.workbench.service.impl;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.commons.utils.UUIDUtils;
import com.pys.crm.settings.domain.User;
import com.pys.crm.workbench.domain.*;
import com.pys.crm.workbench.mapper.CustomerMapper;
import com.pys.crm.workbench.mapper.TranHistoryMapper;
import com.pys.crm.workbench.mapper.TranMapper;
import com.pys.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("tranService")
public class TranServiceImpl implements TranService {

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Autowired
    private CustomerMapper customerMapper;

   @Autowired
   private TranMapper tranMapper;

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        User user =(User) map.get(Contants.SESSION_USER);

        Customer customer = customerMapper.selectCustomerByName((String) map.get("name"));
        if(customer == null) {
            //新建客户
            customer = new Customer();

            customer.setOwner(user.getId());
            customer.setName((String) map.get("name"));
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setCreateBy(user.getId());

            customerMapper.insertCustomer(customer);
        }
        //封装交易对象，保存创建的交易
        Tran tran = new Tran();
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtils.getUUID());
        tran.setExpectedDate((String) map.get("expectDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));

        tranMapper.insertTran(tran);

        //保存交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());

        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public Tran queryTranForDetailById(String id) {
       return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
