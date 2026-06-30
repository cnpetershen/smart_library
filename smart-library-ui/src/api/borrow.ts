import { get, post } from '@/utils/request'
import type { ApiResponse } from '@/types'

export interface BorrowRecord {
  id: number
  bookId: number
  bookTitle: string
  userId: number
  readerName: string
  borrowTime: string
  expectedReturnTime: string
  actualReturnTime?: string
  overdueDays: number
  status: number
  statusText: string
  overdueStatus?: string
}

export interface BorrowQueryDTO {
  keyword?: string
  status?: number
  overdueFilter?: number
  page?: number
  pageSize?: number
}

export interface BorrowDTO {
  bookId: number
  userId: number
}

export interface ReturnDTO {
  bookId: number
  userId: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  pageSize: number
}

export const borrowApi = {
  list: (params?: BorrowQueryDTO): Promise<ApiResponse<PageResult<BorrowRecord>>> => {
    return get('/borrow/list', { params })
  },

  borrow: (data: BorrowDTO): Promise<ApiResponse<string>> => {
    return post('/borrow', data)
  },

  returnBook: (data: ReturnDTO): Promise<ApiResponse<string>> => {
    return post('/borrow/return', data)
  },

  getOverdueList: (): Promise<ApiResponse<BorrowRecord[]>> => {
    return get('/borrow/overdue')
  },

  getTodayCount: (): Promise<ApiResponse<number>> => {
    return get('/borrow/today')
  }
}

export default borrowApi
