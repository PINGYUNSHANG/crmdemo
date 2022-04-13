package com.pys.crm.workbench.service;

import com.pys.crm.workbench.domain.Contacts;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ContactsService {
    List<Contacts> queryContacts();

    public int saveCreateContacts(Contacts contacts);
}
