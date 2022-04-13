package com.pys.crm.workbench.service.impl;

import com.pys.crm.workbench.domain.Contacts;
import com.pys.crm.workbench.mapper.ContactsMapper;
import com.pys.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;
    @Override
    public List<Contacts> queryContacts() {
        return contactsMapper.selectContacts();
    }
    @Override
    public int saveCreateContacts(Contacts contacts){
       return contactsMapper.insertContacts(contacts);
    }

}
