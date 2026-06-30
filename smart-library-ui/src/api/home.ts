import { get } from '@/utils/request'
import type { ApiResponse } from '@/types'

export interface HomeStats {
  bookTotal: number
  borrowCurrent: number
  readerTotal: number
  todayBorrow: number
  trend?: string
  borrowTrend?: string
  readerTrend?: string
}

export interface TrendData {
  date: string
  count: number
}

export interface RecentBorrow {
  readerName: string
  bookTitle: string
  time: string
  type: string
}

export interface Reminder {
  overdueCount: number
  overdueList: OverdueItem[]
  comingCount: number
  comingList: ComingItem[]
}

export interface OverdueItem {
  readerName: string
  bookTitle: string
  overdueDays: number
}

export interface ComingItem {
  readerName: string
  bookTitle: string
  expectedReturnTime: string
}

export const homeApi = {
  getStats: (): Promise<ApiResponse<HomeStats>> => {
    return get('/home/statistics')
  },

  getTrend: (): Promise<ApiResponse<TrendData[]>> => {
    return get('/home/trend')
  },

  getDynamics: (): Promise<ApiResponse<RecentBorrow[]>> => {
    return get('/home/dynamics')
  },

  getAlerts: (): Promise<ApiResponse<Reminder>> => {
    return get('/home/alerts')
  }
}

export default homeApi
