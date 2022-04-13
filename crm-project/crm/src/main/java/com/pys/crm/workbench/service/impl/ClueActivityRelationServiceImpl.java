package com.pys.crm.workbench.service.impl;

import com.pys.crm.workbench.domain.ClueActivityRelation;
import com.pys.crm.workbench.mapper.ClueActivityRelationMapper;
import com.pys.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list) {
        return  clueActivityRelationMapper.insertClueActivityRelationByList(list);
    }

    @Override
    public int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation) {
        return  clueActivityRelationMapper.deleteClueActivityRelationByClueIdActivityId(relation);
    }
}
