package com.pys.crm.workbench.service.impl;

import com.pys.crm.workbench.domain.TranRemark;
import com.pys.crm.workbench.mapper.TranRemarkMapper;
import com.pys.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService
{

    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public List<TranRemark> queryTranRemarkForDetailByTranId(String tranId) {
       return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }
}
