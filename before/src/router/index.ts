/**
 * 路由配置文件
 * V1.2 新增需求管理、会议纪要、系统管理功能
 */
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'dashboard',
    component: () => import('../views/dashboard/Dashboard.vue'),
  },
  {
    path: '/projects',
    name: 'projects',
    component: () => import('../views/project/list.vue'),
  },
  {
    path: '/projects/:id',
    name: 'project-detail',
    component: () => import('../views/project/detail.vue'),
  },
  {
    path: '/requirements',
    name: 'requirements',
    component: () => import('../views/requirement/list.vue'),
  },
  {
    path: '/requirements/edit',
    name: 'create-requirement',
    component: () => import('../views/requirement/edit.vue'),
  },
  {
    path: '/requirements/:id',
    name: 'requirement-detail',
    component: () => import('../views/requirement/detail.vue'),
  },
  {
    path: '/requirements/:id/edit',
    name: 'edit-requirement',
    component: () => import('../views/requirement/edit.vue'),
  },
  {
    path: '/bugs',
    name: 'bugs',
    component: () => import('../views/bug/list.vue'),
  },
  {
    path: '/bugs/edit',
    name: 'create-bug',
    component: () => import('../views/bug/edit.vue'),
  },
  {
    path: '/bugs/:id',
    name: 'bug-detail',
    component: () => import('../views/bug/detail.vue'),
  },
  {
    path: '/bugs/:id/edit',
    name: 'edit-bug',
    component: () => import('../views/bug/edit.vue'),
  },
  {
    path: '/todo',
    name: 'todo',
    component: () => import('../views/todo/TodoList.vue'),
  },
  {
    path: '/test',
    name: 'test',
    component: () => import('../views/test/QuickTest.vue'),
  },
  {
    path: '/meetings',
    name: 'meetings',
    component: () => import('../views/meeting/list.vue'),
  },
  {
    path: '/meetings/edit',
    name: 'create-meeting',
    component: () => import('../views/meeting/edit.vue'),
  },
  {
    path: '/meetings/:id',
    name: 'meeting-detail',
    component: () => import('../views/meeting/detail.vue'),
  },
  {
    path: '/meetings/:id/edit',
    name: 'edit-meeting',
    component: () => import('../views/meeting/edit.vue'),
  },
  {
    path: '/system/users',
    name: 'system-users',
    component: () => import('../views/system/user/list.vue'),
  },
  {
    path: '/system/users/edit',
    name: 'system-user-create',
    component: () => import('../views/system/user/edit.vue'),
  },
  {
    path: '/system/users/:id/edit',
    name: 'system-user-edit',
    component: () => import('../views/system/user/edit.vue'),
  },
  {
    path: '/system/dicts',
    name: 'system-dicts',
    component: () => import('../views/system/dict/list.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router
