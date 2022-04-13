package com.pys.crm.settings.service;

import com.pys.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
