package com.atguigu.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

    List<T> findAll();

    T findById(Integer id);

    boolean insert(T t);

    boolean update(T t);

    boolean delete(Integer id);

    List<T> findPageList(Map<String, String> filters);
}
