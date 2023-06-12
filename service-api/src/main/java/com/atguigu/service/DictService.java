package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    //根据parentId查子节点列表
    List<Map<String,Object>> selectByParentId(Long parentId);
    List<Dict> findDictListByParentId(Long parentId);
    List<Dict> selectByParentDictCode(String houseTypeList);

}
