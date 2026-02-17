import { request } from './request' // V1.2.0
import { BUG_API } from './constants' // V1.2.0

export interface Bug {
  id: number // V1.2.0
  title: string // V1.2.0
  project_id?: number // V1.2.0
  project?: string // V1.2.0
  requirement_id?: number // V1.2.0
  description?: string // V1.2.0
  type?: string // V1.2.0
  severity?: string // V1.2.0
  status?: string // V1.2.0
  assignee?: string // V1.2.0
  reporter?: string // V1.2.0
  environment?: string // V1.2.0
  version?: string // V1.2.0
  created_at?: string // V1.2.0
  updated_at?: string // V1.2.0
} // V1.2.0

export interface BugListParams {
  page?: number // V1.2.0
  page_size?: number // V1.2.0
  project_id?: number // V1.2.0
  requirement_id?: number // V1.2.0
  status?: string // V1.2.0
  assignee?: string // V1.2.0
  keyword?: string // V1.2.0
} // V1.2.0

export interface BugCreateParams {
  title: string // V1.2.0
  project_id?: number // V1.2.0
  requirement_id?: number // V1.2.0
  description?: string // V1.2.0
  type?: string // V1.2.0
  severity?: string // V1.2.0
  environment?: string // V1.2.0
  version?: string // V1.2.0
} // V1.2.0

export interface BugUpdateParams {
  title?: string // V1.2.0
  description?: string // V1.2.0
  type?: string // V1.2.0
  severity?: string // V1.2.0
  status?: string // V1.2.0
  assignee?: string // V1.2.0
  environment?: string // V1.2.0
  version?: string // V1.2.0
} // V1.2.0

export function getBugList(params?: BugListParams) {
  // V1.2.0
  return request<{ data: { list: Bug[]; total: number } }>({
    // V1.2.0
    url: BUG_API.LIST, // V1.2.0
    method: 'GET', // V1.2.0
    params, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function getBugDetail(id: number) {
  // V1.2.0
  return request<{ data: Bug }>({
    // V1.2.0
    url: BUG_API.DETAIL.replace('{id}', String(id)), // V1.2.0
    method: 'GET', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function createBug(data: BugCreateParams) {
  // V1.2.0
  return request<{ data: Bug }>({
    // V1.2.0
    url: BUG_API.CREATE, // V1.2.0
    method: 'POST', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function updateBug(id: number, data: BugUpdateParams) {
  // V1.2.0
  return request<{ data: Bug }>({
    // V1.2.0
    url: BUG_API.UPDATE.replace('{id}', String(id)), // V1.2.0
    method: 'PUT', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function deleteBug(id: number) {
  // V1.2.0
  return request({
    // V1.2.0
    url: BUG_API.DELETE.replace('{id}', String(id)), // V1.2.0
    method: 'DELETE', // V1.2.0
  }) // V1.2.0
} // V1.2.0
