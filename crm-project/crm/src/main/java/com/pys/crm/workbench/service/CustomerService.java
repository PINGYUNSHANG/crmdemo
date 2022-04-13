package com.pys.crm.workbench.service;

import com.pys.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<String> queryCustomerByName(String customerName);
    Customer  queryCustomerById(String id);
    int saveCreateCustomer(Customer customer);
    List<Customer> queryCustomers();
}
