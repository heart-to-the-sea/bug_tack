package com.bugtack.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugtack.app.entity.Meeting;
import com.bugtack.app.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService extends ServiceImpl<MeetingMapper, Meeting> {

    public IPage<Meeting> getMeetingPage(Integer page, Integer pageSize, Integer projectId, String status, String keyword) {
        Page<Meeting> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Meeting> wrapper = new LambdaQueryWrapper<>();
        if (projectId != null) {
            wrapper.eq(Meeting::getProjectId, projectId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Meeting::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Meeting::getTitle, keyword);
        }
        wrapper.orderByDesc(Meeting::getMeetingDate);
        return this.page(pageParam, wrapper);
    }

    public Meeting getMeetingDetail(Integer id) {
        return this.getById(id);
    }

    public Meeting createMeeting(Meeting meeting, Integer creatorId) {
        meeting.setCreatorId(creatorId);
        meeting.setStatus("pending");
        this.save(meeting);
        return meeting;
    }

    public Meeting updateMeeting(Meeting meeting) {
        this.updateById(meeting);
        return meeting;
    }

    public List<Meeting> getProjectMeetings(Integer projectId) {
        LambdaQueryWrapper<Meeting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Meeting::getProjectId, projectId);
        wrapper.orderByDesc(Meeting::getMeetingDate);
        return this.list(wrapper);
    }
}
