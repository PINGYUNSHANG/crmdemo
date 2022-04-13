package com.pys.crm.workbench.service;

import com.pys.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String ClueId);
}
