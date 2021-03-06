package com.pys.crm.workbench.mapper;

import com.pys.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Thu Apr 07 17:00:10 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Thu Apr 07 17:00:10 CST 2022
     */
    int insert(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Thu Apr 07 17:00:10 CST 2022
     */
    int insertSelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Thu Apr 07 17:00:10 CST 2022
     */
    Customer selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Thu Apr 07 17:00:10 CST 2022
     */
    int updateByPrimaryKeySelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Thu Apr 07 17:00:10 CST 2022
     */
    int updateByPrimaryKey(Customer record);

    /**
     * 保存创建的客户
     */
    int insertCustomer(Customer customer);

    /**
     * 查询所有客户名称
     *
     * @return
     */
    List<String> queryCustomerByName(String customerName);

    /**
     * 根据name精确查询客户
     * @param name
     * @return
     */
    Customer selectCustomerByName(String name);

    Customer selectCustomerById(String id);

    List<Customer> selectCustomers();
}