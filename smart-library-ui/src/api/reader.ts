import { get, post, put } from '@/utils/request'
import type { ApiResponse } from '@/types'

export interface Reader {
  id?: number
  account?: string
  name: string
  gender?: string
  phone?: string
  email?: string
  avatar?: string
  status: number
  statusText?: string
  registerTime?: string
  borrowCount?: number
  overdueCount?: number
}

export interface ReaderQueryDTO {
  keyword?: string
  status?: number
  gender?: string
  page?: number
  pageSize?: number
}

export interface BorrowHistory {
  id: number
  bookId: number
  bookTitle: string
  borrowTime: string
  expectedReturnTime: string
  actualReturnTime?: string
  overdueDays: number
  status: number
  statusText: string
}

export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  pageSize: number
}

export const readerApi = {
  list: (params?: ReaderQueryDTO): Promise<ApiResponse<PageResult<Reader>>> => {
    return get('/reader/list', { params })
  },

  getById: (id: number): Promise<ApiResponse<Reader>> => {
    return get(`/reader/${id}`)
  },

  create: (data: Partial<Reader>): Promise<ApiResponse<void>> => {
    return post('/reader', data)
  },

  update: (id: number, data: Partial<Reader>): Promise<ApiResponse<void>> => {
    return put(`/reader/${id}`, data)
  },

  toggleStatus: (id: number): Promise<ApiResponse<void>> => {
    return put(`/reader/${id}/status`)
  },

  disable: (id: number): Promise<ApiResponse<void>> => {
    return put(`/reader/${id}/status`)
  },

  getBorrowHistory: (id: number): Promise<ApiResponse<BorrowHistory[]>> => {
    return get(`/reader/${id}/borrow-history`)
  },

  search: (keyword: string): Promise<ApiResponse<Reader[]>> => {
    return get('/reader/search', { params: { keyword } })
  }
}

export default readerApi
