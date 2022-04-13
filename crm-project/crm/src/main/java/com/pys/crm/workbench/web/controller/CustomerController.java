package com.pys.crm.workbench.web.controller;

import com.pys.crm.workbench.domain.Customer;
import com.pys.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @RequestMapping("/workbench/customer/toIndex.do")
    public String toIndex(HttpServletRequest request){
        List<Customer> customerList = customerService.queryCustomers();

        request.setAttribute("customerList",customerList);

        return "workbench/customer/index";
    }
}
