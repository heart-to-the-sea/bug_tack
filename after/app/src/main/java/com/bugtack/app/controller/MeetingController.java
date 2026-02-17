package com.bugtack.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugtack.app.entity.Meeting;
import com.bugtack.app.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tack/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMeetings(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        IPage<Meeting> pageResult = meetingService.getMeetingPage(page, pageSize, projectId, status, keyword);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createMeeting(@RequestBody Map<String, Object> params) {
        Meeting meeting = new Meeting();
        meeting.setTitle((String) params.get("title"));
        meeting.setProjectId((Integer) params.get("project_id"));
        
        String meetingDateStr = (String) params.get("meeting_date");
        if (meetingDateStr != null) {
            meeting.setMeetingDate(LocalDate.parse(meetingDateStr));
        }
        
        meeting.setParticipants((String) params.get("participants"));
        meeting.setStatus((String) params.getOrDefault("status", "pending"));
        meeting.setContent((String) params.get("content"));
        meeting.setConclusion((String) params.get("conclusion"));
        meeting.setMeetingLink((String) params.get("meeting_link"));
        meeting.setAttachment((String) params.get("attachment"));
        
        Object relatedReqId = params.get("related_requirement_id");
        if (relatedReqId != null) {
            meeting.setRelatedRequirementId((Integer) relatedReqId);
        }
        
        Meeting created = meetingService.createMeeting(meeting, 1);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMeeting(@PathVariable Integer id) {
        Meeting meeting = meetingService.getMeetingDetail(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", meeting);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMeeting(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> params) {
        Meeting meeting = new Meeting();
        meeting.setId(id);
        meeting.setTitle((String) params.get("title"));
        meeting.setProjectId((Integer) params.get("project_id"));
        
        String meetingDateStr = (String) params.get("meeting_date");
        if (meetingDateStr != null) {
            meeting.setMeetingDate(LocalDate.parse(meetingDateStr));
        }
        
        meeting.setParticipants((String) params.get("participants"));
        meeting.setStatus((String) params.get("status"));
        meeting.setContent((String) params.get("content"));
        meeting.setConclusion((String) params.get("conclusion"));
        meeting.setMeetingLink((String) params.get("meeting_link"));
        meeting.setAttachment((String) params.get("attachment"));
        
        Meeting updated = meetingService.updateMeeting(meeting);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMeeting(@PathVariable Integer id) {
        meetingService.removeById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }
}
