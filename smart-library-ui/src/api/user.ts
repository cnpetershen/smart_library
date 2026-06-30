import { get, post, put } from '@/utils/request'
import type { ApiResponse } from '@/types'

export interface User {
  id?: number
  account: string
  name?: string
  avatar?: string
  phone?: string
  email?: string
  gender?: string
  role: string
  status?: number
  registerTime?: string
}

export interface LoginDTO {
  account: string
  password: string
  role: string
}

export interface RegisterDTO {
  account: string
  password: string
  confirmPassword?: string
  name: string
  gender?: string
  phone?: string
  email?: string
}

export interface PasswordChangeDTO {
  oldPassword: string
  newPassword: string
  confirmPassword?: string
}

export interface TokenVO {
  token: string
  userInfo: User
}

export const userApi = {
  login: (data: LoginDTO): Promise<ApiResponse<TokenVO>> => {
    return post('/auth/login', data)
  },

  register: (data: RegisterDTO): Promise<ApiResponse<void>> => {
    return post('/auth/register', data)
  },

  getCurrentUser: (): Promise<ApiResponse<User>> => {
    return get('/auth/profile')
  },

  updateProfile: (data: Partial<User>): Promise<ApiResponse<void>> => {
    return put('/auth/profile', data)
  },

  changePassword: (data: PasswordChangeDTO): Promise<ApiResponse<void>> => {
    return put('/auth/password', data)
  }
}

export default userApi
