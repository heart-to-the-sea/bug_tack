# AGENTS.md - 项目开发指南

## 1. 项目概述

- **项目名称**: Bug Track 问题追踪管理系统
- **技术栈**: Spring Boot 3.x + MyBatis-Plus + PostgreSQL + Redis
- **端口**: 8080

---

## 2. 编码规范

1. 严格参照阿里巴巴编程标准编写代码
2. 接口满足 RESTful 规范
3. 接口统一使用 `/api/tack/` 前缀

---

## 3. 数据库配置

| 配置项 | 值 |
|--------|-----|
| IP | localhost |
| 端口 | 5432 |
| 数据库 | postgres |
| 用户名 | postgres |
| 密码 | 123456 |

### Redis 配置

| 配置项 | 值 |
|--------|-----|
| IP | localhost |
| 端口 | 6379 |
| 密码 | 无 |

---

## 4. 已实现的接口

### 项目管理
- `POST /api/tack/projects` - 创建项目
- `GET /api/tack/projects` - 获取项目列表
- `GET /api/tack/projects/{id}` - 获取项目详情
- `PUT /api/tack/projects/{id}` - 更新项目
- `DELETE /api/tack/projects/{id}` - 删除项目

### 需求管理
- `POST /api/tack/projects/{projectId}/requirements` - 创建需求
- `GET /api/tack/projects/{projectId}/requirements` - 获取需求列表
- `GET /api/tack/requirements/{id}` - 获取需求详情
- `PUT /api/tack/requirements/{id}` - 更新需求
- `DELETE /api/tack/requirements/{id}` - 删除需求
- `GET /api/tack/requirements/{id}/timeline` - 获取修改轨迹
- `POST /api/tack/requirements/{id}/bugs` - 关联BUG

### 会议管理
- `POST /api/tack/meetings` - 创建会议
- `GET /api/tack/meetings` - 获取会议列表
- `GET /api/tack/meetings/{id}` - 获取会议详情
- `PUT /api/tack/meetings/{id}` - 更新会议
- `DELETE /api/tack/meetings/{id}` - 删除会议

---

## 5. 运行命令

```bash
# 启动应用
./mvnw spring-boot:run

# 编译
./mvnw clean compile

# 打包
./mvnw clean package
```

---

## 6. 项目结构

```
src/main/java/com/bugtack/app/
├── AppApplication.java          # 启动类
├── config/                      # 配置类
│   ├── DatabaseInitConfig.java
│   └── MyBatisPlusConfig.java
├── controller/                  # 控制器
│   ├── ProjectController.java
│   ├── RequirementController.java
│   └── MeetingController.java
├── entity/                      # 实体类
│   ├── User.java
│   ├── Project.java
│   ├── ProjectMember.java
│   ├── ProjectLeader.java
│   ├── Requirement.java
│   ├── RequirementHistory.java
│   ├── Meeting.java
│   ├── Issue.java
│   ├── IssueHistory.java
│   ├── Todo.java
│   ├── TestingTask.java
│   ├── TestingRecord.java
│   ├── Attachment.java
│   ├── IssueComment.java
│   ├── Role.java
│   ├── Environment.java
│   └── IssueStatus.java
├── mapper/                      # Mapper接口
│   └── (各实体对应Mapper)
└── service/                     # 服务层
    ├── ProjectService.java
    ├── RequirementService.java
    └── MeetingService.java
```

---

## 7. 数据库表

共 17 张表：
- users, projects, project_members, project_leaders
- requirements, requirement_history, meetings
- issues, issue_history, todos
- testing_tasks, testing_records
- attachments, issue_comments
- roles, environments, issue_statuses

表在应用启动时自动创建。
