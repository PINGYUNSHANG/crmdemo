package com.pys.crm.workbench.service;

import com.pys.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
