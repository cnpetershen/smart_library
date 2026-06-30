import { get, post } from '@/utils/request'
import type { ApiResponse } from '@/types'

export interface TrendData {
  date: string
  count: number
}

export interface TrendAnalysisResult {
  dates: string[]
  counts: number[]
  analysis: string
}

export interface HotBooks {
  hotBooks: HotBookItem[]
  insight: string
}

export interface HotBookItem {
  bookId: number
  title: string
  author: string
  category: string
  borrowCount: number
}

export interface UserPortrait {
  categories: CategoryData[]
  portrait: string
}

export interface CategoryData {
  category: string
  count: number
}

export interface PersonalRecommend {
  id: number
  title: string
  author: string
  category: string
  cover: string
  reason: string
}

export interface AiReport {
  id: number
  reportType: string
  title: string
  reportContent: string
  generateTime: string
}

export const aiApi = {
  getTrendData: (days?: number): Promise<ApiResponse<{ dates: string[]; counts: number[] }>> => {
    return get('/ai/trend', { params: { days } })
  },

  getTrendAnalysis: (days?: number): Promise<ApiResponse<{ analysis: string }>> => {
    return get('/ai/trend/analysis', { params: { days } })
  },

  getHotBooks: (limit?: number): Promise<ApiResponse<HotBooks>> => {
    return get('/ai/hot-books', { params: { limit } })
  },

  getRankingData: (limit?: number): Promise<ApiResponse<{ books: { name: string; borrowCount: number }[] }>> => {
    return get('/ai/ranking', { params: { limit } })
  },

  getRankingInsight: (limit?: number): Promise<ApiResponse<{ insight: string }>> => {
    return get('/ai/ranking/insight', { params: { limit } })
  },

  getCategoryData: (): Promise<ApiResponse<{ categories: { name: string; value: number }[] }>> => {
    return get('/ai/category')
  },

  getCategoryPortrait: (): Promise<ApiResponse<{ portrait: string }>> => {
    return get('/ai/category/portrait')
  },

  getUserPortrait: (): Promise<ApiResponse<UserPortrait>> => {
    return get('/ai/user-portrait')
  },

  getPersonalRecommend: (): Promise<ApiResponse<PersonalRecommend[]>> => {
    return get('/ai/recommend/personal')
  },

  getRecommendations: (): Promise<ApiResponse<PersonalRecommend[]>> => {
    return get('/ai/recommend')
  },

  generateReport: (): Promise<ApiResponse<AiReport>> => {
    return post('/ai/report/generate', {})
  },

  getMonthlyReport: (): Promise<ApiResponse<AiReport>> => {
    return get('/ai/report/monthly')
  }
}

export default aiApi
