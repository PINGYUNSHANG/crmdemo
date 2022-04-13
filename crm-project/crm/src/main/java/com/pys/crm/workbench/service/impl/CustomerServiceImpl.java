package com.pys.crm.workbench.service.impl;

import com.pys.crm.workbench.domain.Customer;
import com.pys.crm.workbench.mapper.CustomerMapper;
import com.pys.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<String> queryCustomerByName(String customerName){
        return customerMapper.queryCustomerByName(customerName);
    }

    @Override
    public Customer queryCustomerById(String id) {
       return customerMapper.selectCustomerById(id);
    }

    @Override
    public int saveCreateCustomer(Customer customer) {
        return  customerMapper.insertCustomer(customer);
    }

    @Override
    public List<Customer> queryCustomers() {
        return customerMapper.selectCustomers();
    }
}
