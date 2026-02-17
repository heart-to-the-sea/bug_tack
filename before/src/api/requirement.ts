import { request } from './request' // V1.2.0
import { REQUIREMENT_API } from './constants' // V1.2.0

export interface Requirement {
  id: number // V1.2.0
  project_id?: number // V1.2.0
  project?: string // V1.2.0
  title: string // V1.2.0
  description?: string // V1.2.0
  priority: string // V1.2.0
  status: string // V1.2.0
  assignee?: string // V1.2.0
  creator?: string // V1.2.0
  bug_count?: number // V1.2.0
  created_at?: string // V1.2.0
  createdAt?: string // V1.2.0
  updated_at?: string // V1.2.0
  related_bugs?: Array<{ id: number; title: string; status: string }> // V1.2.0
  timeline?: Array<{
    // V1.2.0
    timestamp: string // V1.2.0
    type: string // V1.2.0
    title: string // V1.2.0
    description: string // V1.2.0
    operator: string // V1.2.0
  }> // V1.2.0
} // V1.2.0

export interface RequirementListParams {
  page?: number // V1.2.0
  page_size?: number // V1.2.0
  project_id?: number // V1.2.0
  keyword?: string // V1.2.0
} // V1.2.0

export interface RequirementCreateParams {
  project_id: number // V1.2.0
  title: string // V1.2.0
  description?: string // V1.2.0
  priority?: string // V1.2.0
} // V1.2.0

export interface RequirementUpdateParams {
  title?: string // V1.2.0
  description?: string // V1.2.0
  priority?: string // V1.2.0
  status?: string // V1.2.0
  assignee?: string // V1.2.0
} // V1.2.0

export interface RequirementListResponse {
  list: Requirement[] // V1.2.0
  total: number // V1.2.0
} // V1.2.0

export function getRequirementList(params?: RequirementListParams) {
  // V1.2.0
  return request<{ data: RequirementListResponse }>({
    // V1.2.0
    url: REQUIREMENT_API.LIST, // V1.2.0
    method: 'GET', // V1.2.0
    params, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function getRequirementDetail(id: number) {
  // V1.2.0
  return request<{ data: Requirement }>({
    // V1.2.0
    url: REQUIREMENT_API.DETAIL.replace('{id}', String(id)), // V1.2.0
    method: 'GET', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function createRequirement(data: RequirementCreateParams) {
  // V1.2.0
  return request({
    url: REQUIREMENT_API.CREATE, // V1.2.0
    method: 'POST', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function updateRequirement(id: number, data: RequirementUpdateParams) {
  // V1.2.0
  return request({
    url: REQUIREMENT_API.UPDATE.replace('{id}', String(id)), // V1.2.0
    method: 'PUT', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function deleteRequirement(id: number) {
  // V1.2.0
  return request({
    url: REQUIREMENT_API.DELETE.replace('{id}', String(id)), // V1.2.0
    method: 'DELETE', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function updateRequirementStatus(projectId: number, requirementId: number, status: string) {
  // V1.2.0
  return request({
    url: REQUIREMENT_API.UPDATE_STATUS.replace('{project_id}', String(projectId)).replace(
      '{requirement_id}',
      String(requirementId)
    ), // V1.2.0
    method: 'PUT', // V1.2.0
    data: { status }, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function linkRequirementBugs(requirementId: number, bugIds: number[]) {
  // V1.2.0
  return request({
    url: REQUIREMENT_API.LINK_BUGS.replace('{id}', String(requirementId)), // V1.2.0
    method: 'POST', // V1.2.0
    data: { bug_ids: bugIds }, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function getRequirementTimeline(id: number) {
  // V1.2.0
  return request({
    url: REQUIREMENT_API.TIMELINE.replace('{id}', String(id)), // V1.2.0
    method: 'GET', // V1.2.0
  }) // V1.2.0
} // V1.2.0
