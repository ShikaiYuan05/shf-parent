package com.atguigu.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(interfaceClass = DictService.class)
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;
    @Override
    public BaseMapper<Dict> getBaseMapper() {
        return dictMapper;
    }

    @Override
    public List<Map<String, Object>> selectByParentId(Long parentId) {
        List<Dict> dictList = dictMapper.selectByParentId(parentId);

        //2. 进行数据的转换将dictList转成List<Map<String, Object>>:包含三个键值对id,name,isParent
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(dictList)){
            HashMap<String, Object> map = new HashMap<>();
            resultList =  dictList.stream().map(dict -> {
                map.put("name",dict.getName());
                map.put("id",dict.getId());
                map.put("isParent",CollectionUtils.isNotEmpty(dictMapper.selectByParentId(dict.getId())));
                return map;
            }).collect(Collectors.toList());

//            for (Dict dict : dictList) {
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("name",dict.getName());
//                map.put("id",dict.getId());
//                map.put("isParent",CollectionUtils.isNotEmpty(dictMapper.selectByParentId(dict.getId())));
//                resultList.add(map);
//            }
//            return resultList;
        }
        //如果没有子节点，则返回一个空集合
        return new ArrayList<>();
    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.selectByParentId(parentId);
    }

    @Override
    public List<Dict> selectByParentDictCode(String dictCode) {
        //1. 调用持久层的方法根据父节点的dict_code查询子节点列表
        List<Dict> dictList = dictMapper.selectByParentDictCode(dictCode);
        //2. 返回子节点列表
        return dictList;
    }

}
