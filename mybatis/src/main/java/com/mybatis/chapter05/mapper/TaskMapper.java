package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.Task;

public interface TaskMapper {
    int insert(Task task);
    Task getTaskById(Integer id);

}
