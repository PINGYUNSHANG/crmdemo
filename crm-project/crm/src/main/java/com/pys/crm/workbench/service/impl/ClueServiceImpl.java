package com.pys.crm.workbench.service.impl;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.commons.utils.UUIDUtils;
import com.pys.crm.settings.domain.User;
import com.pys.crm.workbench.domain.*;
import com.pys.crm.workbench.mapper.*;
import com.pys.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ClueMapper clueMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insetClue(clue);

    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvertClue(Map<String, Object> map) {
        //根据id查询线索信息
        String clueId = (String) map.get("clueId");
        Clue clue = clueMapper.selectClueById(clueId);
        User user = (User) map.get(Contants.SESSION_USER);
        //把线索中有关公司的信息转换到客户表中
        Customer c = new Customer();
        c.setAddress(clue.getAddress());
        c.setContactSummary(clue.getContactSummary());
        c.setCreateBy(user.getId());
        c.setCreateTime(DateUtils.formatDateTime(new Date()));
        c.setDescription(clue.getDescription());
        c.setId(UUIDUtils.getUUID());
        c.setName(clue.getCompany());
        c.setNextContactTime(clue.getNextContactTime());
        c.setOwner(user.getId());
        c.setPhone(clue.getPhone());
        c.setWebsite(clue.getWebsite());
        //调用客户mapper
        customerMapper.insertCustomer(c);

        //把该线索中有关联系人的信息转换到联系人表中
        Contacts co = new Contacts();
        co.setAddress(clue.getAddress());
        co.setAppellation(clue.getAppellation());
        co.setContactSummary(clue.getContactSummary());
        co.setCreateBy(user.getId());
        co.setCreateTime(DateUtils.formatDateTime(new Date()));
        co.setCustomerId(c.getId());
        co.setDescription(clue.getDescription());
        co.setEmail(clue.getEmail());
        co.setJob(clue.getJob());
        co.setFullname(clue.getFullname());
        co.setId(UUIDUtils.getUUID());
        co.setMphone(clue.getMphone());
        co.setNextContactTime(clue.getNextContactTime());
        co.setOwner(user.getId());
        co.setSource(clue.getSource());

        contactsMapper.insertContacts(co);


        //根据线索id查询该线索下所有备注
        List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        //如果有备注 把该线索下的所有备注转换到客户备注中 同时转换到联系人备注表
        CustomerRemark cur = null;
        ContactsRemark cor = null;
        List<CustomerRemark> curList = new ArrayList<CustomerRemark>();
        List<ContactsRemark> corList = new ArrayList<ContactsRemark>();
        if (crList != null && crList.size() > 0) {
            //遍历list
            for (ClueRemark cr : crList) {

                cur = new CustomerRemark();
                cor = new ContactsRemark();
                cur.setCreateBy(cr.getCreateBy());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCustomerId(c.getId());
                cur.setEditBy(cr.getEditBy());
                cur.setEditTime(cr.getEditTime());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                cur.setEditFlag(cr.getEditFlag());
                curList.add(cur);

                cor = new ContactsRemark();
                cor.setContactsId(co.getId());
                cor.setCreateBy(cr.getCreateBy());
                cor.setCreateTime(cr.getCreateTime());
                cor.setEditFlag(cr.getEditFlag());
                cor.setEditBy(cr.getEditBy());
                cor.setEditTime(cr.getEditTime());
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                corList.add(cor);
            }
            //调用客户备注mapper
            customerRemarkMapper.insertCustomerRemarkByList(curList);

            //调用联系人备注mapper
            contactsRemarkMapper.insertContactsRemarkByList(corList);

        }

        //根据clueId查询该线索和市场活动的关联关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        //把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        if (carList != null && carList.size() > 0) {
            ContactsActivityRelation coar = null;
            List<ContactsActivityRelation> coarList = new ArrayList<>();
            for (ClueActivityRelation car : carList) {
                coar = new ContactsActivityRelation();
                coar.setActivityId(car.getActivityId());
                coar.setContactsId(co.getId());
                coar.setId(UUIDUtils.getUUID());
                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }

        String isCreateTran=(String) map.get("isCreateTran");
        //如果需要创建交易，则往交易表中添加一条记录,还需要把该线索下的备注转换到交易备注表中一份
        if ("true".equals(isCreateTran)) {
            Tran tran = new Tran();
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(co.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formatDateTime(new Date()));
            tran.setCustomerId(c.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));
            tranMapper.insertTran(tran);

            if (crList != null && crList.size() > 0) {
                TranRemark tr = null;
                List<TranRemark> trList = new ArrayList<>();
                for (ClueRemark cr : crList) {
                    tr = new TranRemark();
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setEditBy(cr.getEditBy());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setEditTime(cr.getEditTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());
                    trList.add(tr);
                }

                tranRemarkMapper.insertTranRemarkByList(trList);
            }
        }

        //删除线索备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //删除线索市场活动关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //删除线索
        clueMapper.deleteClueById(clueId);

    }

}
