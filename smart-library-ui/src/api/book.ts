import { get, post, put, del } from '@/utils/request'
import type { ApiResponse } from '@/types'

export interface Book {
  id?: number
  title: string
  author: string
  isbn: string
  category: string
  publishDate?: string
  coverUrl?: string
  totalStock: number
  availableStock: number
  description?: string
}

export interface BookQueryDTO {
  keyword?: string
  category?: string
  page?: number
  pageSize?: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  pageSize: number
}

export const bookApi = {
  list: (params?: BookQueryDTO): Promise<ApiResponse<PageResult<Book>>> => {
    return get('/books', { params })
  },

  getById: (id: number): Promise<ApiResponse<Book>> => {
    return get(`/books/${id}`)
  },

  create: (data: Book): Promise<ApiResponse<Book>> => {
    return post('/books', data)
  },

  update: (id: number, data: Book): Promise<ApiResponse<Book>> => {
    return put(`/books/${id}`, data)
  },

  delete: (id: number): Promise<ApiResponse<void>> => {
    return del(`/books/${id}`)
  },

  batchDelete: (ids: number[]): Promise<ApiResponse<void>> => {
    return post('/books/batch-delete', { ids })
  },

  search: (keyword: string): Promise<ApiResponse<Book[]>> => {
    return get('/books/search', { params: { keyword } })
  }
}

export default bookApi
