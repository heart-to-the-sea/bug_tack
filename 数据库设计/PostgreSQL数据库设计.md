# Bug Track系统 PostgreSQL 数据库设计

## 一、数据库概述

**数据库名称**: `bug_track`

**字符集**: `UTF8`

**排序规则**: `zh_CN.UTF-8`

---

## 二、数据表结构

### 1. 用户表 (users)

存储系统用户信息

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 用户ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | - | 用户名 |
| password | VARCHAR(255) | NOT NULL | - | 密码（加密存储） |
| name | VARCHAR(100) | NOT NULL | - | 姓名 |
| email | VARCHAR(100) | UNIQUE, NOT NULL | - | 邮箱 |
| phone | VARCHAR(20) | - | - | 电话 |
| avatar | VARCHAR(500) | - | - | 头像URL |
| role | VARCHAR(50) | NOT NULL | 'member' | 角色 |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| last_login_at | TIMESTAMP | - | - | 最后登录时间 |

**索引**:
- `idx_users_username` (username)
- `idx_users_email` (email)
- `idx_users_role` (role)
- `idx_users_status` (status)

---

### 2. 项目表 (projects)

存储项目信息

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 项目ID |
| name | VARCHAR(200) | NOT NULL | - | 项目名称 |
| description | TEXT | - | - | 项目描述 |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**:
- `idx_projects_status` (status)
- `idx_projects_name` (name)

---

### 3. 项目成员关联表 (project_members)

项目与成员的关联关系

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 主键ID |
| project_id | INTEGER | NOT NULL, FOREIGN KEY | - | 项目ID |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 用户ID |
| role | VARCHAR(50) | NOT NULL | 'member' | 角色 |
| joined_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 加入时间 |

**索引**:
- `idx_project_members_project_id` (project_id)
- `idx_project_members_user_id` (user_id)
- 联合唯一索引: `uk_project_user` (project_id, user_id)

---

### 4. 项目负责人关联表 (project_leaders)

项目与负责人的关联关系

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 主键ID |
| project_id | INTEGER | NOT NULL, FOREIGN KEY | - | 项目ID |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 用户ID |
| assigned_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 分配时间 |

**索引**:
- `idx_project_leaders_project_id` (project_id)
- `idx_project_leaders_user_id` (user_id)
- 联合唯一索引: `uk_project_leader` (project_id, user_id)

---

### 5. 需求表 (requirements)

存储需求信息

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 需求ID |
| project_id | INTEGER | NOT NULL, FOREIGN KEY | - | 项目ID |
| title | VARCHAR(200) | NOT NULL | - | 需求标题 |
| description | TEXT | - | - | 需求描述 |
| priority | VARCHAR(20) | NOT NULL | 'P1' | 优先级 |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**:
- `idx_requirements_project_id` (project_id)
- `idx_requirements_status` (status)
- `idx_requirements_priority` (priority)

---

### 6. 问题表 (issues)

存储问题信息

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 问题ID |
| project_id | INTEGER | NOT NULL, FOREIGN KEY | - | 项目ID |
| requirement_id | INTEGER | FOREIGN KEY | - | 需求ID |
| title | VARCHAR(500) | NOT NULL | - | 问题标题 |
| description | TEXT | - | - | 问题描述 |
| type | VARCHAR(50) | NOT NULL | - | 问题类型 |
| severity | VARCHAR(20) | NOT NULL | 'medium' | 严重程度 |
| status | VARCHAR(50) | NOT NULL | 'pending' | 问题状态 |
| environment | VARCHAR(50) | - | - | 发现环境 |
| version | VARCHAR(50) | - | - | 发现版本 |
| assignee_id | INTEGER | FOREIGN KEY | - | 负责人ID |
| creator_id | INTEGER | NOT NULL, FOREIGN KEY | - | 创建人ID |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| resolved_at | TIMESTAMP | - | - | 解决时间 |
| closed_at | TIMESTAMP | - | - | 关闭时间 |

**索引**:
- `idx_issues_project_id` (project_id)
- `idx_issues_requirement_id` (requirement_id)
- `idx_issues_status` (status)
- `idx_issues_assignee_id` (assignee_id)
- `idx_issues_creator_id` (creator_id)
- `idx_issues_severity` (severity)
- `idx_issues_type` (type)
- `idx_issues_environment` (environment)
- `idx_issues_created_at` (created_at)

---

### 7. 问题历史表 (issue_history)

存储问题状态变更历史

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 历史记录ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| action | VARCHAR(50) | NOT NULL | - | 操作类型 |
| from_status | VARCHAR(50) | - | - | 原状态 |
| to_status | VARCHAR(50) | - | - | 新状态 |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 操作人ID |
| description | TEXT | - | - | 操作描述 |
| environment | VARCHAR(50) | - | - | 验证环境 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 操作时间 |

**索引**:
- `idx_issue_history_issue_id` (issue_id)
- `idx_issue_history_user_id` (user_id)
- `idx_issue_history_created_at` (created_at)

---

### 8. 问题回退记录表 (issue_rollbacks)

存储问题回退记录

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 回退记录ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| from_environment | VARCHAR(50) | NOT NULL | - | 回退的环境 |
| to_environment | VARCHAR(50) | - | - | 退回到的环境 |
| reason | TEXT | NOT NULL | - | 回退原因 |
| description | TEXT | - | - | 详细描述 |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 操作人ID |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 回退时间 |

**索引**:
- `idx_issue_rollbacks_issue_id` (issue_id)
- `idx_issue_rollbacks_user_id` (user_id)

---

### 9. 问题验证记录表 (issue_verifications)

存储各环境验证记录

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 验证记录ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| environment | VARCHAR(50) | NOT NULL | - | 验证环境 |
| result | VARCHAR(20) | - | - | 验证结果 |
| description | TEXT | - | - | 验证描述 |
| verifier_id | INTEGER | FOREIGN KEY | - | 验证人ID |
| verified_at | TIMESTAMP | - | - | 验证时间 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 记录创建时间 |

**索引**:
- `idx_issue_verifications_issue_id` (issue_id)
- `idx_issue_verifications_environment` (environment)
- `idx_issue_verifications_verifier_id` (verifier_id)

---

### 10. 附件表 (attachments)

存储附件信息

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 附件ID |
| filename | VARCHAR(255) | NOT NULL | - | 存储文件名 |
| original_name | VARCHAR(500) | NOT NULL | - | 原始文件名 |
| size | BIGINT | NOT NULL | - | 文件大小（字节） |
| type | VARCHAR(50) | - | - | 文件类型 |
| mime_type | VARCHAR(100) | - | - | MIME类型 |
| path | VARCHAR(500) | NOT NULL | - | 存储路径 |
| url | VARCHAR(500) | - | - | 访问URL |
| uploader_id | INTEGER | NOT NULL, FOREIGN KEY | - | 上传人ID |
| issue_id | INTEGER | FOREIGN KEY | - | 关联问题ID |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 上传时间 |

**索引**:
- `idx_attachments_issue_id` (issue_id)
- `idx_attachments_uploader_id` (uploader_id)
- `idx_attachments_type` (type)

---

### 11. 问题备注表 (issue_comments)

存储问题备注和评论

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 备注ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 发言人ID |
| content | TEXT | NOT NULL | - | 备注内容 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**:
- `idx_issue_comments_issue_id` (issue_id)
- `idx_issue_comments_user_id` (user_id)

---

### 12. 人员交接记录表 (user_transfer_records)

存储人员交接历史

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 交接记录ID |
| from_user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 原负责人ID |
| to_user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 新负责人ID |
| issue_ids | INTEGER[] | NOT NULL | - | 转移的问题ID列表 |
| reason | TEXT | - | - | 交接原因 |
| transferred_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 交接时间 |

**索引**:
- `idx_user_transfer_records_from_user_id` (from_user_id)
- `idx_user_transfer_records_to_user_id` (to_user_id)

---

### 13. 待办表 (todos)

存储个人待办

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 待办ID |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 用户ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| priority | VARCHAR(20) | NOT NULL | 'medium' | 优先级 |
| deadline | TIMESTAMP | - | - | 截止时间 |
| status | VARCHAR(20) | NOT NULL | 'pending' | 待办状态 |
| remark | TEXT | - | - | 备注 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| completed_at | TIMESTAMP | - | - | 完成时间 |

**索引**:
- `idx_todos_user_id` (user_id)
- `idx_todos_issue_id` (issue_id)
- `idx_todos_status` (status)
- `idx_todos_priority` (priority)
- `idx_todos_deadline` (deadline)
- 联合唯一索引: `uk_todo_user_issue` (user_id, issue_id)

---

### 14. 测试任务表 (testing_tasks)

存储快速测试任务

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 任务ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| environment | VARCHAR(50) | NOT NULL | - | 测试环境 |
| status | VARCHAR(20) | NOT NULL | 'pending' | 任务状态 |
| queue_order | INTEGER | NOT NULL | 0 | 队列顺序 |
| submitter_id | INTEGER | NOT NULL, FOREIGN KEY | - | 提交人ID |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| started_at | TIMESTAMP | - | - | 开始测试时间 |
| completed_at | TIMESTAMP | - | - | 完成时间 |

**索引**:
- `idx_testing_tasks_issue_id` (issue_id)
- `idx_testing_tasks_environment` (environment)
- `idx_testing_tasks_status` (status)
- `idx_testing_tasks_queue_order` (queue_order)

---

### 15. 测试记录表 (testing_records)

存储测试执行记录

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 记录ID |
| task_id | INTEGER | NOT NULL, FOREIGN KEY | - | 测试任务ID |
| issue_id | INTEGER | NOT NULL, FOREIGN KEY | - | 问题ID |
| environment | VARCHAR(50) | NOT NULL | - | 测试环境 |
| result | VARCHAR(20) | - | - | 测试结果 |
| description | TEXT | - | - | 测试描述 |
| tester_id | INTEGER | NOT NULL, FOREIGN KEY | - | 测试人ID |
| tested_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 测试时间 |

**索引**:
- `idx_testing_records_task_id` (task_id)
- `idx_testing_records_issue_id` (issue_id)
- `idx_testing_records_tester_id` (tester_id)
- `idx_testing_records_result` (result)

---

### 16. 用户配置表 (user_configs)

存储用户个性化配置

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 配置ID |
| user_id | INTEGER | NOT NULL, FOREIGN KEY | - | 用户ID |
| config_key | VARCHAR(100) | NOT NULL | - | 配置键 |
| config_value | TEXT | - | - | 配置值 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**:
- `idx_user_configs_user_id` (user_id)
- 联合唯一索引: `uk_user_config` (user_id, config_key)

---

### 17. 验证环境表 (environments)

存储可用的验证环境

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 环境ID |
| name | VARCHAR(100) | NOT NULL | - | 环境名称 |
| key | VARCHAR(50) | UNIQUE, NOT NULL | - | 环境标识 |
| description | TEXT | - | - | 环境描述 |
| order | INTEGER | NOT NULL | 0 | 排序顺序 |
| is_active | BOOLEAN | NOT NULL | true | 是否启用 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**:
- `idx_environments_key` (key)
- `idx_environments_order` (order)

---

### 18. 问题状态表 (issue_statuses)

存储可用的问题状态

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 状态ID |
| name | VARCHAR(100) | NOT NULL | - | 状态名称 |
| key | VARCHAR(50) | UNIQUE, NOT NULL | - | 状态标识 |
| type | VARCHAR(20) | NOT NULL | - | 状态类型 |
| description | TEXT | - | - | 状态描述 |
| order | INTEGER | NOT NULL | 0 | 排序顺序 |
| is_active | BOOLEAN | NOT NULL | true | 是否启用 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**:
- `idx_issue_statuses_key` (key)
- `idx_issue_statuses_order` (order)

---

### 19. 角色表 (roles)

存储系统角色

| 字段名 | 类型 | 约束 | 默认值 | 说明 |
|--------|------|------|--------|------|
| id | SERIAL | PRIMARY KEY | - | 角色ID |
| name | VARCHAR(100) | UNIQUE, NOT NULL | - | 角色名称 |
| key | VARCHAR(50) | UNIQUE, NOT NULL | - | 角色标识 |
| description | TEXT | - | - | 角色描述 |
| permissions | JSONB | NOT NULL | '[]' | 权限列表 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**:
- `idx_roles_key` (key)

---

## 三、外键约束

```sql
-- 用户表外键约束（无）

-- 项目表外键约束（无）

-- 项目成员关联表
ALTER TABLE project_members
ADD CONSTRAINT fk_project_members_project
FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE project_members
ADD CONSTRAINT fk_project_members_user
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- 项目负责人关联表
ALTER TABLE project_leaders
ADD CONSTRAINT fk_project_leaders_project
FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE project_leaders
ADD CONSTRAINT fk_project_leaders_user
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- 需求表
ALTER TABLE requirements
ADD CONSTRAINT fk_requirements_project
FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

-- 问题表
ALTER TABLE issues
ADD CONSTRAINT fk_issues_project
FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE issues
ADD CONSTRAINT fk_issues_requirement
FOREIGN KEY (requirement_id) REFERENCES requirements(id) ON DELETE SET NULL;

ALTER TABLE issues
ADD CONSTRAINT fk_issues_assignee
FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE issues
ADD CONSTRAINT fk_issues_creator
FOREIGN KEY (creator_id) REFERENCES users(id);

-- 问题历史表
ALTER TABLE issue_history
ADD CONSTRAINT fk_issue_history_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

ALTER TABLE issue_history
ADD CONSTRAINT fk_issue_history_user
FOREIGN KEY (user_id) REFERENCES users(id);

-- 问题回退记录表
ALTER TABLE issue_rollbacks
ADD CONSTRAINT fk_issue_rollbacks_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

ALTER TABLE issue_rollbacks
ADD CONSTRAINT fk_issue_rollbacks_user
FOREIGN KEY (user_id) REFERENCES users(id);

-- 问题验证记录表
ALTER TABLE issue_verifications
ADD CONSTRAINT fk_issue_verifications_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

ALTER TABLE issue_verifications
ADD CONSTRAINT fk_issue_verifications_verifier
FOREIGN KEY (verifier_id) REFERENCES users(id) ON DELETE SET NULL;

-- 附件表
ALTER TABLE attachments
ADD CONSTRAINT fk_attachments_uploader
FOREIGN KEY (uploader_id) REFERENCES users(id);

ALTER TABLE attachments
ADD CONSTRAINT fk_attachments_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

-- 问题备注表
ALTER TABLE issue_comments
ADD CONSTRAINT fk_issue_comments_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

ALTER TABLE issue_comments
ADD CONSTRAINT fk_issue_comments_user
FOREIGN KEY (user_id) REFERENCES users(id);

-- 人员交接记录表
ALTER TABLE user_transfer_records
ADD CONSTRAINT fk_transfer_from_user
FOREIGN KEY (from_user_id) REFERENCES users(id);

ALTER TABLE user_transfer_records
ADD CONSTRAINT fk_transfer_to_user
FOREIGN KEY (to_user_id) REFERENCES users(id);

-- 待办表
ALTER TABLE todos
ADD CONSTRAINT fk_todos_user
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE todos
ADD CONSTRAINT fk_todos_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

-- 测试任务表
ALTER TABLE testing_tasks
ADD CONSTRAINT fk_testing_tasks_issue
FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;

ALTER TABLE testing_tasks
ADD CONSTRAINT fk_testing_tasks_submitter
FOREIGN KEY (submitter_id) REFERENCES users(id);

-- 测试记录表
ALTER TABLE testing_records
ADD CONSTRAINT fk_testing_records_task
FOREIGN KEY (task_id) REFERENCES testing_tasks(id) ON DELETE CASCADE;

ALTER TABLE testing_records
ADD CONSTRAINT fk_testing_records_issue
FOREIGN KEY (issue_id) REFERENCES issues(id);

ALTER TABLE testing_records
ADD CONSTRAINT fk_testing_records_tester
FOREIGN KEY (tester_id) REFERENCES users(id);

-- 用户配置表
ALTER TABLE user_configs
ADD CONSTRAINT fk_user_configs_user
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
```

---

## 四、初始化数据

### 1. 初始化验证环境

```sql
INSERT INTO environments (name, `key`, description, `order`) VALUES
('开发环境', 'dev', '开发人员自测环境', 1),
('测试环境', 'test', 'QA测试环境', 2),
('Beta环境', 'beta', '预发布环境', 3),
('线上环境', 'production', '生产环境', 4);
```

### 2. 初始化问题状态

```sql
INSERT INTO issue_statuses (name, `key`, type, description, `order`) VALUES
('待分配', 'pending', 'open', '问题已创建，等待分配', 1),
('待办中', 'todo', 'open', '已分配到待办列表', 2),
('处理中', 'in_progress', 'open', '正在处理中', 3),
('待验证', 'pending_verify', 'open', '等待验证', 4),
('未通过', 'failed', 'open', '验证未通过', 5),
('已修复', 'resolved', 'closed', '问题已修复', 6),
('已关闭', 'closed', 'closed', '问题已关闭', 7);
```

### 3. 初始化角色

```sql
INSERT INTO roles (name, `key`, description, permissions) VALUES
('项目负责人', 'leader', '项目负责人，拥有项目全部权限', '["project:manage", "requirement:manage", "issue:all", "user:manage", "report:view", "testing:manage", "todo:manage"]'),
('开发人员', 'developer', '开发人员，负责问题修复', '["issue:view", "issue:edit", "issue:verify", "todo:manage", "testing:submit"]'),
('测试人员', 'tester', '测试人员，负责问题验证和快速测试', '["issue:view", "issue:verify", "testing:all", "report:view"]'),
('现场人员', 'onsite', '现场人员，可查看和提交问题', '["issue:view", "issue:create", "issue:comment"]'),
('管理员', 'admin', '系统管理员，拥有全部权限', '["all"]');
```

---

## 五、常用查询示例

### 1. 查询用户负责的所有问题

```sql
SELECT * FROM issues
WHERE assignee_id = ?
ORDER BY created_at DESC;
```

### 2. 查询问题的完整处理轨迹

```sql
SELECT h.*, u.name as user_name
FROM issue_history h
LEFT JOIN users u ON h.user_id = u.id
WHERE h.issue_id = ?
ORDER BY h.created_at ASC;
```

### 3. 查询项目的待验证问题

```sql
SELECT i.*, p.name as project_name, u.name as assignee_name
FROM issues i
LEFT JOIN projects p ON i.project_id = p.id
LEFT JOIN users u ON i.assignee_id = u.id
WHERE i.project_id = ? AND i.status = 'pending_verify'
ORDER BY i.severity DESC, i.created_at ASC;
```

### 4. 查询用户待办列表

```sql
SELECT t.*, i.title as issue_title, i.status as issue_status, i.severity
FROM todos t
LEFT JOIN issues i ON t.issue_id = i.id
WHERE t.user_id = ? AND t.status = 'pending'
ORDER BY 
  CASE t.priority 
    WHEN 'high' THEN 1 
    WHEN 'medium' THEN 2 
    WHEN 'low' THEN 3 
  END,
  t.deadline ASC;
```

### 5. 查询测试队列

```sql
SELECT tt.*, i.title as issue_title, u.name as submitter_name
FROM testing_tasks tt
LEFT JOIN issues i ON tt.issue_id = i.id
LEFT JOIN users u ON tt.submitter_id = u.id
WHERE tt.environment = ? AND tt.status = 'pending'
ORDER BY tt.queue_order ASC;
```
