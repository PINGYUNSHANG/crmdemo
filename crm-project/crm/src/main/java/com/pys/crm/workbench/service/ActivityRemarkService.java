package com.pys.crm.workbench.service;

import com.pys.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String ActivityId);
    int saveCreateActivityRemark(ActivityRemark remark);
    int deleteActivityRemarkById(String id);
    int saveEditActivityRemark(ActivityRemark remark);
}
