import { request } from './request' // V1.2.0
import { MEETING_API } from './constants' // V1.2.0

export interface Meeting {
  id: number // V1.2.0
  title: string // V1.2.0
  project_id: number // V1.2.0
  project?: string // V1.2.0
  meeting_date: string // V1.2.0
  participants: string[] // V1.2.0
  status: string // V1.2.0
  content: string // V1.2.0
  conclusion?: string // V1.2.0
  meeting_link?: string // V1.2.0
  attachment?: string // V1.2.0
  related_requirement_id?: number // V1.2.0
  related_requirement?: {
    // V1.2.0
    id: number // V1.2.0
    title: string // V1.2.0
    status: string // V1.2.0
  } // V1.2.0
  created_at?: string // V1.2.0
} // V1.2.0

export interface MeetingListParams {
  page?: number // V1.2.0
  page_size?: number // V1.2.0
  project_id?: number // V1.2.0
  status?: string // V1.2.0
  keyword?: string // V1.2.0
} // V1.2.0

export interface MeetingCreateParams {
  title: string // V1.2.0
  project_id: number // V1.2.0
  meeting_date: string // V1.2.0
  participants: (number | string)[] // V1.2.0
  status?: string // V1.2.0
  content: string // V1.2.0
  conclusion?: string // V1.2.0
  meeting_link?: string // V1.2.0
  related_requirement_id?: number // V1.2.0
} // V1.2.0

export interface MeetingUpdateParams {
  title?: string // V1.2.0
  project_id?: number // V1.2.0
  meeting_date?: string // V1.2.0
  participants?: (number | string)[] // V1.2.0
  status?: string // V1.2.0
  content?: string // V1.2.0
  conclusion?: string // V1.2.0
  meeting_link?: string // V1.2.0
  related_requirement_id?: number // V1.2.0
} // V1.2.0

export interface MeetingListResponse {
  list: Meeting[] // V1.2.0
  total: number // V1.2.0
} // V1.2.0

export function getMeetingList(params?: MeetingListParams) {
  // V1.2.0
  return request<{ data: MeetingListResponse }>({
    // V1.2.0
    url: MEETING_API.LIST, // V1.2.0
    method: 'GET', // V1.2.0
    params, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function getMeetingDetail(id: number) {
  // V1.2.0
  return request<{ data: Meeting }>({
    // V1.2.0
    url: MEETING_API.DETAIL.replace('{id}', String(id)), // V1.2.0
    method: 'GET', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function createMeeting(data: MeetingCreateParams) {
  // V1.2.0
  return request({
    url: MEETING_API.CREATE, // V1.2.0
    method: 'POST', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function updateMeeting(id: number, data: MeetingUpdateParams) {
  // V1.2.0
  return request({
    url: MEETING_API.UPDATE.replace('{id}', String(id)), // V1.2.0
    method: 'PUT', // V1.2.0
    data, // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function deleteMeeting(id: number) {
  // V1.2.0
  return request({
    url: MEETING_API.DELETE.replace('{id}', String(id)), // V1.2.0
    method: 'DELETE', // V1.2.0
  }) // V1.2.0
} // V1.2.0

export function uploadMeetingAttachment(id: number, file: FormData) {
  // V1.2.0
  return request({
    url: MEETING_API.UPLOAD_ATTACHMENT.replace('{id}', String(id)), // V1.2.0
    method: 'POST', // V1.2.0
    data: file, // V1.2.0
    headers: { 'Content-Type': 'multipart/form-data' }, // V1.2.0
  }) // V1.2.0
} // V1.2.0
