<template>
  <div class="home-view">
    <div class="page-header">
      <h2>📊 首页数据总览</h2>
      <el-button type="primary" :loading="loading" @click="refreshData">
        <el-icon v-if="!loading"><Refresh /></el-icon>
        刷新数据
      </el-button>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon gradient-blue">
            <el-icon :size="28"><Collection /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">
              <el-statistic :value="stats.bookTotal" :animation="true" />
            </div>
            <div class="stat-label">总藏书</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon gradient-green">
            <el-icon :size="28"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">
              <el-statistic :value="stats.readerTotal" :animation="true" />
            </div>
            <div class="stat-label">总读者</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon gradient-purple">
            <el-icon :size="28"><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">
              <el-statistic :value="stats.todayBorrow" :animation="true" />
            </div>
            <div class="stat-label">今日借阅</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon gradient-red">
            <el-icon :size="28"><WarningFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">
              <el-statistic :value="stats.overdueCount" :animation="true" />
            </div>
            <div class="stat-label">逾期未还</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="main-content">
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
        <el-card shadow="hover">
          <template #header>
            <span>📈 近7天借阅趋势</span>
          </template>
          <div ref="trendChartRef" style="height: 320px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <el-card shadow="hover">
          <template #header>
            <span>🔔 待办提醒</span>
          </template>
          <div class="reminder-list">
            <el-timeline v-if="reminders.length > 0">
              <el-timeline-item
                v-for="item in reminders"
                :key="item.id"
                :type="getReminderType(item.type)"
                :hollow="item.type === 'info'"
              >
                <div class="reminder-item">
                  <div class="reminder-title">{{ item.title }}</div>
                  <div class="reminder-desc">{{ item.description }}</div>
                  <div class="reminder-time">{{ item.time }}</div>
                </div>
              </el-timeline-item>
            </el-timeline>
            <el-empty v-else description="暂无待办事项" :image-size="60" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="recent-section">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <span>📋 最近借阅动态</span>
          </template>
          <el-table :data="recentBorrows" stripe size="small" class="compact-table">
            <el-table-column prop="readerName" label="读者" min-width="100" show-overflow-tooltip />
            <el-table-column prop="bookTitle" label="图书" min-width="180" show-overflow-tooltip />
            <el-table-column prop="borrowTime" label="借阅时间" width="160" />
            <el-table-column prop="expectedReturnTime" label="应还时间" width="160" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ row.statusText }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="report-section">
      <el-col :span="24">
        <el-card shadow="hover" v-loading="reportLoading">
          <template #header>
            <div class="report-header">
              <span>📊 月度运营报告</span>
              <el-button type="primary" @click="generateReport" :loading="reportGenerating">
                一键生成报告
              </el-button>
            </div>
          </template>
          <div v-if="reportError" class="error-container">
            <el-empty description="报告获取失败">
              <el-button type="primary" @click="fetchMonthlyReport">重试</el-button>
            </el-empty>
          </div>
          <div v-else-if="monthlyReport" class="report-content" v-html="renderedMarkdown()"></div>
          <div v-else class="empty-report">
            <el-empty description="暂无月度报告，点击上方按钮生成" :image-size="60"></el-empty>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { Refresh, Collection, User, Document, WarningFilled } from '@element-plus/icons-vue'
import MarkdownIt from 'markdown-it'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import { homeApi, type HomeStats, type TrendData, type RecentBorrow, type Reminder } from '@/api/home'
import aiApi from '@/api/ai'
import { ElMessage } from 'element-plus'

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
})

const loading = ref(false)
const trendChartRef = ref<HTMLDivElement>()
let trendChart: ECharts | null = null

const stats = ref<HomeStats>({
  bookTotal: 0,
  borrowCurrent: 0,
  readerTotal: 0,
  todayBorrow: 0
})

const trendData = ref<TrendData[]>([])

const recentBorrows = ref<RecentBorrow[]>([])
const reminders = ref<Reminder[]>([])

// 月度报告相关
const reportLoading = ref(false)
const reportGenerating = ref(false)
const reportError = ref(false)
const monthlyReport = ref<{ content: string } | null>(null)

const renderedMarkdown = () => {
  if (!monthlyReport.value?.content) return ''
  return md.render(monthlyReport.value.content)
}

const generateReport = async () => {
  reportGenerating.value = true
  reportError.value = false
  try {
    const res = await aiApi.generateReport()
    if (res.data) {
      console.log('Report response:', res.data)
      monthlyReport.value = { content: res.data.reportContent }
      if (!res.data.reportContent || res.data.reportContent.includes('不可用') || res.data.reportContent.includes('失败')) {
        ElMessage.warning('报告内容生成不完整，请稍后重试')
      } else {
        ElMessage.success('报告生成成功')
      }
    } else {
      ElMessage.error('生成报告失败：无返回数据')
    }
  } catch (error: any) {
    console.error('Generate report error:', error)
    reportError.value = true
    ElMessage.error(error.message || '生成报告失败')
  } finally {
    reportGenerating.value = false
  }
}

const fetchMonthlyReport = async () => {
  reportLoading.value = true
  reportError.value = false
  try {
    const res = await aiApi.getMonthlyReport()
    if (res.data) {
      monthlyReport.value = { content: res.data.reportContent }
    }
  } catch (error) {
    reportError.value = true
  } finally {
    reportLoading.value = false
  }
}

const initTrendChart = (data: TrendData[]) => {
  if (!trendChartRef.value) return
  if (trendChart) {
    trendChart.dispose()
  }
  trendChart = echarts.init(trendChartRef.value)

  const dates = data.map(item => item.date)
  const counts = data.map(item => item.count)

  const option: echarts.EChartsOption = {
    color: ['#4f46e5', '#06b6d4', '#8b5cf6'],
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e2e8f0',
      textStyle: {
        color: '#334155'
      }
    },
    legend: {
      data: ['借阅量'],
      bottom: 0,
      textStyle: {
        color: '#64748b'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: {
        lineStyle: {
          color: '#e2e8f0'
        }
      },
      axisLabel: {
        color: '#64748b'
      }
    },
    yAxis: {
      type: 'value',
      name: '借阅次数',
      nameTextStyle: {
        color: '#64748b'
      },
      axisLine: {
        lineStyle: {
          color: '#e2e8f0'
        }
      },
      axisLabel: {
        color: '#64748b'
      },
      splitLine: {
        lineStyle: {
          color: '#f1f5f9'
        }
      }
    },
    series: [
      {
        name: '借阅量',
        type: 'line',
        data: counts,
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(79, 70, 229, 0.3)' },
            { offset: 1, color: 'rgba(79, 70, 229, 0.05)' }
          ])
        },
        lineStyle: {
          color: '#4f46e5',
          width: 3
        },
        itemStyle: {
          color: '#4f46e5'
        },
        emphasis: {
          focus: 'series'
        }
      }
    ]
  }
  trendChart.setOption(option)
}

const getReminderType = (type: string) => {
  switch (type) {
    case 'overdue':
      return 'danger'
    case 'expiring':
      return 'warning'
    default:
      return 'primary'
  }
}

const getStatusType = (status: number) => {
  switch (status) {
    case 0:
      return 'success'
    case 1:
      return 'warning'
    case 2:
      return 'danger'
    default:
      return 'info'
  }
}

const fetchStats = async () => {
  try {
    const res = await homeApi.getStats()
    if (res.data) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

const fetchTrend = async () => {
  try {
    const res = await homeApi.getTrend()
    if (res.data) {
      trendData.value = res.data
      await import('vue').then(v => v.nextTick())
      initTrendChart(res.data)
    }
  } catch (error) {
    console.error('获取趋势数据失败:', error)
  }
}

const fetchDynamics = async () => {
  try {
    const res = await homeApi.getDynamics()
    if (res.data) {
      recentBorrows.value = res.data
    }
  } catch (error) {
    console.error('获取最近借阅失败:', error)
  }
}

const fetchAlerts = async () => {
  try {
    const res = await homeApi.getAlerts()
    if (res.data) {
      const alerts = []
      if (res.data.overdueCount > 0) {
        for (const item of res.data.overdueList || []) {
          alerts.push({
            id: Math.random(),
            type: 'overdue' as const,
            title: `读者 ${item.readerName} 逾期`,
            description: `《${item.bookTitle}》逾期 ${item.overdueDays} 天`,
            time: ''
          })
        }
      }
      if (res.data.comingCount > 0) {
        for (const item of res.data.comingList || []) {
          alerts.push({
            id: Math.random(),
            type: 'expiring' as const,
            title: `读者 ${item.readerName} 即将到期`,
            description: `《${item.bookTitle}》`,
            time: item.expectedReturnTime
          })
        }
      }
      reminders.value = alerts
    }
  } catch (error) {
    console.error('获取提醒失败:', error)
  }
}

const refreshData = async () => {
  loading.value = true
  try {
    await Promise.all([
      fetchStats(),
      fetchTrend(),
      fetchDynamics(),
      fetchAlerts(),
      fetchMonthlyReport()
    ])
  } finally {
    loading.value = false
  }
}

const handleResize = () => {
  trendChart?.resize()
}

onMounted(() => {
  refreshData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChart?.dispose()
})
</script>

<style lang="scss" scoped>
.home-view {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h2 {
    margin: 0;
    font-size: 20px;
    color: #1e293b;
  }

  :deep(.el-button) {
    border-radius: 8px;
  }
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  :deep(.el-card__body) {
    display: flex;
    align-items: center;
    padding: 20px;
    gap: 16px;
  }
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.gradient-blue {
  background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
}

.gradient-green {
  background: linear-gradient(135deg, #059669 0%, #10b981 100%);
}

.gradient-purple {
  background: linear-gradient(135deg, #8b5cf6 0%, #a855f7 100%);
}

.gradient-red {
  background: linear-gradient(135deg, #dc2626 0%, #ef4444 100%);
}

.stat-info {
  flex: 1;
  min-width: 0;

  .stat-value {
    :deep(.el-statistic__content) {
      font-size: 28px;
      font-weight: 700;
      color: #1e293b;
    }

    :deep(.el-statistic__head) {
      display: none;
    }
  }

  .stat-label {
    font-size: 14px;
    color: #64748b;
    margin-top: 4px;
  }
}

.main-content {
  margin-bottom: 20px;
}

.reminder-list {
  max-height: 320px;
  overflow-y: auto;
  padding-right: 8px;

  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-thumb {
    background: #d1d5db;
    border-radius: 2px;
  }
}

.reminder-item {
  .reminder-title {
    font-size: 14px;
    font-weight: 500;
    color: #334155;
    margin-bottom: 4px;
  }

  .reminder-desc {
    font-size: 12px;
    color: #64748b;
    margin-bottom: 4px;
    line-height: 1.4;
  }

  .reminder-time {
    font-size: 11px;
    color: #94a3b8;
  }
}

.recent-section {
  :deep(.el-table__header th) {
    background-color: #f8fafc !important;
    color: #64748b !important;
    font-weight: 600 !important;
    font-size: 13px;
    padding: 12px 0;
  }

  :deep(.el-table__cell) {
    padding: 10px 0;
    font-size: 13px;
  }

  :deep(.el-table__row) {
    &:hover {
      background-color: #f8fafc;
    }
  }
}

.report-section {
  margin-top: 20px;

  .report-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .report-content {
    max-height: 500px;
    overflow-y: auto;
    padding: 12px;
    background: #f8fafc;
    border-radius: 8px;
    line-height: 1.8;

    :deep(h1), :deep(h2), :deep(h3) {
      margin-top: 16px;
      margin-bottom: 8px;
      color: #1e293b;
    }

    :deep(p) {
      margin-bottom: 12px;
    }

    :deep(table) {
      width: 100%;
      border-collapse: collapse;
      margin: 12px 0;

      th, td {
        border: 1px solid #e2e8f0;
        padding: 8px 12px;
        text-align: left;
      }

      th {
        background: #f1f5f9;
        font-weight: 600;
      }
    }
  }
}
</style>
