package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Dict;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {
    List<Dict> selectByParentId(Long parentId);


    List<Dict> selectByParentDictCode(String dictCode);
}


