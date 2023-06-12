package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    List<T> findAll();

    PageInfo<T> findPage(Map<String,String> filters);

    T findById(Integer id);

    boolean insert(T t);

    boolean update(T t);

    boolean delete(Integer id);
}
