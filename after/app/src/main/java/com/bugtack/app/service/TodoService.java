package com.bugtack.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugtack.app.entity.Todo;
import com.bugtack.app.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TodoService extends ServiceImpl<TodoMapper, Todo> {

    public IPage<Todo> getTodoPage(Integer userId, String status, String priority, Integer projectId,
                                    String deadlineFrom, String deadlineTo, String sort, String order,
                                    Integer page, Integer pageSize) {
        Page<Todo> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Todo> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Todo::getUserId, userId);
        
        if (StringUtils.hasText(status)) {
            wrapper.eq(Todo::getStatus, status);
        }
        if (StringUtils.hasText(priority)) {
            wrapper.eq(Todo::getPriority, priority);
        }
        
        if ("deadline".equals(sort)) {
            if ("asc".equalsIgnoreCase(order)) {
                wrapper.orderByAsc(Todo::getDeadline);
            } else {
                wrapper.orderByDesc(Todo::getDeadline);
            }
        } else if ("priority".equals(sort)) {
            wrapper.orderByAsc(Todo::getPriority);
        } else {
            wrapper.orderByDesc(Todo::getCreatedAt);
        }
        
        return this.page(pageParam, wrapper);
    }

    public Todo createTodo(Todo todo) {
        todo.setStatus("pending");
        this.save(todo);
        return todo;
    }

    public Todo updateTodo(Integer id, Todo todo) {
        todo.setId(id);
        this.updateById(todo);
        return this.getById(id);
    }

    public void completeTodo(Integer id) {
        Todo todo = this.getById(id);
        todo.setStatus("completed");
        todo.setCompletedAt(LocalDateTime.now());
        this.updateById(todo);
    }
}
