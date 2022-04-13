package com.pys.crm.workbench.web.controller;

import com.pys.crm.commons.contants.Contants;
import com.pys.crm.commons.domain.ReturnObject;
import com.pys.crm.commons.utils.DateUtils;
import com.pys.crm.commons.utils.UUIDUtils;
import com.pys.crm.settings.domain.DicValue;
import com.pys.crm.settings.domain.User;
import com.pys.crm.settings.service.DicValueService;
import com.pys.crm.settings.service.UserService;
import com.pys.crm.workbench.domain.Contacts;
import com.pys.crm.workbench.domain.Customer;
import com.pys.crm.workbench.service.ContactsService;
import com.pys.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class ContactsController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;

    @RequestMapping("/workbench/contacts/toIndex.do")
    public String toIndex(HttpServletRequest request) {
        List<Contacts> contactsList = contactsService.queryContacts();
        List<User> userList = userService.queryAllUsers();
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        request.setAttribute("contactsList", contactsList);
        request.setAttribute("userList", userList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("appellationList", appellationList);
        return "workbench/contacts/index";
    }

    @RequestMapping("/workbench/contacts/saveCreateContacts.do")
    @ResponseBody
    public Object saveCreateContacts(String owner,
                                     String source,
                                     String fullname,
                                     String appellation,
                                     String job,
                                     String mphone,
                                     String email,
                                     String customerId,
                                     String customerName,
                                     String description,
                                     String contactSummary,
                                     String nextContactTime,
                                     String address, HttpServletRequest request) {
        User user = (User) request.getSession().getServletContext().getAttribute(Contants.SESSION_USER);
        Contacts contacts = new Contacts();

        ReturnObject returnObject = new ReturnObject();

        //查看客户表中是否存在，没有就新建
        Customer customer = customerService.queryCustomerById(customerId);
        if(customer==null){
            //新建客户
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setContactSummary(contactSummary);
            customer.setNextContactTime(nextContactTime);
            customer.setDescription(description);
            customer.setAddress(address);

            try {
                int rows = customerService.saveCreateCustomer(customer);
            } catch (Exception e) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后～");
                e.printStackTrace();
            }
        }

        contacts.setSource(source);
        contacts.setOwner(owner);
        contacts.setNextContactTime(nextContactTime);
        contacts.setMphone(mphone);
        contacts.setEmail(email);
        contacts.setFullname(fullname);
        contacts.setCreateBy(user.getId());
        contacts.setContactSummary(contactSummary);
        contacts.setJob(job);
        contacts.setDescription(description);
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setId(UUIDUtils.getUUID());
        contacts.setAppellation(appellation);
        contacts.setCustomerId(customer.getId());
        contacts.setAddress(address);

        try {
            int rows = contactsService.saveCreateContacts(contacts);
            if (rows > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                List<Contacts> contactsList = contactsService.queryContacts();
                returnObject.setRetData(contactsList);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
            e.printStackTrace();
        }
        return returnObject;
    }
}
