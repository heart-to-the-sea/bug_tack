import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0

const service: AxiosInstance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || '/api/tack', // V1.2.0
  timeout: 15000, // V1.2.0
}) // V1.2.0

service.interceptors.request.use(
  (config) => {
    // V1.2.0
    const token = localStorage.getItem('token') // V1.2.0
    if (token) {
      config.headers.Authorization = `Bearer ${token}` // V1.2.0
    }
    return config
  },
  (error) => {
    // V1.2.0
    console.error('Request error:', error) // V1.2.0
    return Promise.reject(error)
  }
) // V1.2.0

service.interceptors.response.use(
  (response: AxiosResponse) => {
    // V1.2.0
    const res = response.data // V1.2.0
    if (res.code !== 0) {
      ElMessage.error(res.message || '请求失败') // V1.2.0
      return Promise.reject(new Error(res.message || '请求失败')) // V1.2.0
    }
    return res
  },
  (error) => {
    // V1.2.0
    console.error('Response error:', error) // V1.2.0
    const message = error.response?.data?.message || error.message || '网络错误' // V1.2.0
    ElMessage.error(message) // V1.2.0
    return Promise.reject(error)
  }
) // V1.2.0

export default service // V1.2.0

export function request<T = unknown>(config: AxiosRequestConfig): Promise<T> {
  // V1.2.0
  return service(config) as Promise<T> // V1.2.0
}
