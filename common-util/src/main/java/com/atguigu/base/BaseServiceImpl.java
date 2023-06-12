package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> {
    public abstract BaseMapper<T> getBaseMapper();

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageInfo<T> findPage(Map<String, String> filters) {
        //1. 使用分页插件开启分页
        PageHelper.startPage(CastUtil.castInt(filters.get("pageNum"), 1), CastUtil.castInt(filters.get("pageSize"), 10));
        //2. 调用持久层的方法根据搜索条件进行搜索
        List<T> list = getBaseMapper().findPageList(filters);

        //10代表最多显示10个页码
        return new PageInfo<>(list, 10);
    }

    @Transactional
    public boolean insert(T t) {
        return getBaseMapper().insert(t);
    }

    @Transactional
    public boolean update(T t) {
        return getBaseMapper().update(t);
    }

    @Transactional
    public boolean delete(Integer id) {
        return getBaseMapper().delete(id);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<T> findAll() {
        return getBaseMapper().findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T findById(Integer id) {
        return getBaseMapper().findById(id);
    }
}

