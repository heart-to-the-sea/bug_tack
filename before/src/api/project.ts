import { request } from './request' // V1.2.0
import { PROJECT_API } from './constants' // V1.2.0

export interface Project {
  id: number // V1.2.0
  name: string // V1.2.0
  description?: string // V1.2.0
  leader_ids: number[] // V1.2.0
  leaders?: Array<{ id: number; name: string }> // V1.2.0
  members?: Array<{ id: number; name: string; role: string }> // V1.2.0
  member_count?: number // V1.2.0
  issue_count?: number // V1.2.0
  requirements?: Array<{ id: number; title: string }> // V1.2.0
  created_at?: string // V1.2.0
  updated_at?: string // V1.2.0
} // V1.2.0

export interface ProjectListParams {
  page?: number // V1.2.0
  page_size?: number // V1.2.0
  keyword?: string // V1.2.0
} // V1.2.0

export interface ProjectCreateParams {
  name: string // V1.2.0
  description?: string // V1.2.0
  leader_ids: number[] // V1.2.0
  member_ids?: number[] // V1.2.0
} // V1.2.0

export interface ProjectUpdateParams {
  name?: string // V1.2.0
  description?: string // V1.2.0
  leader_ids?: number[] // V1.2.0
  member_ids?: number[] // V1.2.0
} // V1.2.0

export function getProjectList(params?: ProjectListParams) {
  // V1.2.0
  return request<{ data: { list: Project[]; total: number } }>({
    // V1.2.0
    url: PROJECT_API.LIST, // V1.2.0
    method: 'GET', // V1.2.0
    params, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function getProjectDetail(id: number) {
  // V1.2.0
  return request<{ data: Project }>({
    // V1.2.0
    url: PROJECT_API.DETAIL.replace('{id}', String(id)), // V1.2.0
    method: 'GET', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function createProject(data: ProjectCreateParams) {
  // V1.2.0
  return request<{ data: Project }>({
    // V1.2.0
    url: PROJECT_API.CREATE, // V1.2.0
    method: 'POST', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function updateProject(id: number, data: ProjectUpdateParams) {
  // V1.2.0
  return request<{ data: Project }>({
    // V1.2.0
    url: PROJECT_API.UPDATE.replace('{id}', String(id)), // V1.2.0
    method: 'PUT', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function deleteProject(id: number) {
  // V1.2.0
  return request({
    // V1.2.0
    url: PROJECT_API.DELETE.replace('{id}', String(id)), // V1.2.0
    method: 'DELETE', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function addProjectMembers(projectId: number, userIds: number[], role?: string) {
  // V1.2.0
  return request({
    // V1.2.0
    url: PROJECT_API.ADD_MEMBERS.replace('{id}', String(projectId)), // V1.2.0
    method: 'POST', // V1.2.0
    data: { user_ids: userIds, role }, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function removeProjectMember(projectId: number, userId: number) {
  // V1.2.0
  return request({
    // V1.2.0
    url: PROJECT_API.REMOVE_MEMBER.replace('{id}', String(projectId)).replace(
      '{user_id}',
      String(userId)
    ), // V1.2.0
    method: 'DELETE', // V1.2.0
  }) // V1.2.0
} // V1.2.0
