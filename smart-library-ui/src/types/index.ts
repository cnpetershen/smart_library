export interface UserInfo {
  id?: number
  account: string
  name: string
  role: string
  gender?: string
  phone?: string
  email?: string
  avatar?: string
}

export interface LoginForm {
  account: string
  password: string
  role: string
}

export interface RegisterForm {
  account: string
  password: string
  confirmPassword: string
  name: string
  gender?: string
  phone?: string
  email?: string
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: string
}

export interface TokenVO {
  token: string
  userInfo: UserInfo
}

export interface MenuItem {
  path: string
  name: string
  icon?: string
  children?: MenuItem[]
}
