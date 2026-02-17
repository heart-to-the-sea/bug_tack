// API接口常量 V1.2.0
// 需求管理模块
export const REQUIREMENT_API = {
  LIST: '/api/tack/requirements', // V1.2.0
  DETAIL: '/api/tack/requirements/{id}', // V1.2.0
  CREATE: '/api/tack/requirements', // V1.2.0
  UPDATE: '/api/tack/requirements/{id}', // V1.2.0
  DELETE: '/api/tack/requirements/{id}', // V1.2.0
  UPDATE_STATUS: '/api/tack/projects/{project_id}/requirements/{requirement_id}/status', // V1.2.0
  LINK_BUGS: '/api/tack/requirements/{id}/bugs', // V1.2.0
  TIMELINE: '/api/tack/requirements/{id}/timeline', // V1.2.0
} as const // V1.2.0

// 会议管理模块
export const MEETING_API = {
  LIST: '/api/tack/meetings', // V1.2.0
  DETAIL: '/api/tack/meetings/{id}', // V1.2.0
  CREATE: '/api/tack/meetings', // V1.2.0
  UPDATE: '/api/tack/meetings/{id}', // V1.2.0
  DELETE: '/api/tack/meetings/{id}', // V1.2.0
  UPLOAD_ATTACHMENT: '/api/tack/meetings/{id}/attachment', // V1.2.0
  DOWNLOAD_ATTACHMENT: '/api/tack/meetings/{id}/attachment', // V1.2.0
} as const // V1.2.0

// 项目管理模块
export const PROJECT_API = {
  LIST: '/api/tack/projects', // V1.2.0
  DETAIL: '/api/tack/projects/{id}', // V1.2.0
  CREATE: '/api/tack/projects', // V1.2.0
  UPDATE: '/api/tack/projects/{id}', // V1.2.0
  DELETE: '/api/tack/projects/{id}', // V1.2.0
  ADD_MEMBERS: '/api/tack/projects/{id}/members', // V1.2.0
  REMOVE_MEMBER: '/api/tack/projects/{id}/members/{user_id}', // V1.2.0
} as const // V1.2.0

// 问题管理模块
export const BUG_API = {
  LIST: '/api/tack/bugs', // V1.2.0
  DETAIL: '/api/tack/bugs/{id}', // V1.2.0
  CREATE: '/api/tack/bugs', // V1.2.0
  UPDATE: '/api/tack/bugs/{id}', // V1.2.0
  DELETE: '/api/tack/bugs/{id}', // V1.2.0
} as const // V1.2.0

// 待办管理模块
export const TODO_API = {
  LIST: '/api/tack/todos', // V1.2.0
  CREATE: '/api/tack/todos', // V1.2.0
  UPDATE: '/api/tack/todos/{id}', // V1.2.0
  DELETE: '/api/tack/todos/{id}', // V1.2.0
} as const // V1.2.0
