<template>
  <div class="ai-analysis-view">
    <h2>🤖 AI 智能数据分析</h2>

    <el-row :gutter="20" class="module-row">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" v-loading="trendChartLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>📈 趋势分析</span>
          </template>
          <div ref="trendChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="ai-insight-card" v-loading="trendAILoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>🤖 AI 趋势洞察</span>
          </template>
          <div v-if="trendError" class="error-container">
            <el-empty description="AI 服务暂时不可用" :image-size="60">
              <el-button type="primary" @click="fetchTrendData">重试</el-button>
            </el-empty>
          </div>
          <div v-else class="ai-content" v-html="formatAIAnalysis(trendAnalysis)"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="module-row">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" v-loading="rankChartLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>🏆 热门排行</span>
          </template>
          <div ref="rankChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="ai-insight-card" v-loading="rankAILoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>🤖 AI 采购建议</span>
          </template>
          <div v-if="rankError" class="error-container">
            <el-empty description="AI 服务暂时不可用" :image-size="60">
              <el-button type="primary" @click="fetchRankData">重试</el-button>
            </el-empty>
          </div>
          <div v-else class="ai-content" v-html="formatAIAnalysis(rankInsight)"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="module-row">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" v-loading="categoryChartLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>👥 读者画像</span>
          </template>
          <div ref="categoryChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="ai-insight-card" v-loading="categoryAILoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>🤖 AI 读者画像</span>
          </template>
          <div v-if="categoryError" class="error-container">
            <el-empty description="AI 服务暂时不可用" :image-size="60">
              <el-button type="primary" @click="fetchCategoryData">重试</el-button>
            </el-empty>
          </div>
          <div v-else class="ai-content" v-html="formatAIAnalysis(readerPortrait)"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="module-row">
      <el-col :span="24">
        <el-card shadow="hover" v-loading="recommendLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>📚 智能推荐</span>
          </template>
          <div v-if="recommendError" class="error-container">
            <el-empty description="AI 服务暂时不可用" :image-size="60">
              <el-button type="primary" @click="fetchRecommendations">重试</el-button>
            </el-empty>
          </div>
          <div v-else class="recommend-container">
            <div class="recommend-scroll">
              <div v-for="book in recommendedBooks" :key="book.id" class="recommend-card">
                <div class="book-cover">
                  <img :src="book.cover || '/placeholder-book.png'" :alt="book.title" />
                </div>
                <div class="book-info">
                  <h4 class="book-title">{{ book.title }}</h4>
                  <p class="book-author">{{ book.author }}</p>
                  <el-tooltip :content="book.reason" placement="top" :disabled="!book.reason || book.reason.length <= 50">
                    <el-tag type="info" class="book-reason">{{ book.reason }}</el-tag>
                  </el-tooltip>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="module-row">
      <el-col :span="24">
        <el-card shadow="hover" v-loading="reportLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <div class="report-header">
              <span>📋 月度报告</span>
              <el-button type="primary" @click="generateReport" :loading="reportGenerating">
                生成报告
              </el-button>
            </div>
          </template>
          <div v-if="reportError" class="error-container">
            <el-empty description="AI 服务暂时不可用" :image-size="60">
              <el-button type="primary" @click="fetchMonthlyReport">重试</el-button>
            </el-empty>
          </div>
          <div v-else-if="monthlyReport" class="report-content" v-html="renderedMarkdown"></div>
          <div v-else class="empty-report">
            <el-empty description="暂无月度报告，点击上方按钮生成" :image-size="80"></el-empty>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, computed } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import MarkdownIt from 'markdown-it'
import aiApi from '@/api/ai'
import { ElMessage } from 'element-plus'
import type { TrendData, HotBooks, UserPortrait, PersonalRecommend, AiReport } from '@/api/ai'

interface RecommendedBook {
  id: number
  title: string
  author: string
  cover?: string
  reason: string
}

interface MonthlyReportData {
  content: string
}

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true
})

const trendChartRef = ref<HTMLDivElement>()
const rankChartRef = ref<HTMLDivElement>()
const categoryChartRef = ref<HTMLDivElement>()

let trendChart: ECharts | null = null
let rankChart: ECharts | null = null
let categoryChart: ECharts | null = null

const trendChartLoading = ref(false)
const trendAILoading = ref(false)
const trendError = ref(false)
const trendAnalysis = ref('')

const rankChartLoading = ref(false)
const rankAILoading = ref(false)
const rankError = ref(false)
const rankInsight = ref('')

const categoryChartLoading = ref(false)
const categoryAILoading = ref(false)
const categoryError = ref(false)
const readerPortrait = ref('')

const recommendLoading = ref(false)
const recommendError = ref(false)
const recommendedBooks = ref<RecommendedBook[]>([])

const reportLoading = ref(false)
const reportGenerating = ref(false)
const reportError = ref(false)
const monthlyReport = ref<MonthlyReportData | null>(null)

const renderedMarkdown = computed(() => {
  if (!monthlyReport.value?.content) return ''
  return md.render(monthlyReport.value.content)
})

const formatAIAnalysis = (text: string): string => {
  if (!text) return ''
  return text.split('\n').map(line => `<p>${line}</p>`).join('')
}

const initTrendChart = (data: TrendData) => {
  if (!trendChartRef.value) return
  trendChart = echarts.init(trendChartRef.value)

  const dates = data.map(item => item.date)
  const counts = data.map(item => item.count)

  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: dates,
      boundaryGap: false
    },
    yAxis: {
      type: 'value',
      name: '借阅次数'
    },
    series: [
      {
        name: '借阅次数',
        type: 'line',
        data: counts,
        smooth: true,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ])
        },
        lineStyle: {
          color: '#409EFF'
        },
        itemStyle: {
          color: '#409EFF'
        }
      }
    ],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    }
  }
  trendChart.setOption(option)
}

const initRankChart = (data: HotBooks) => {
  if (!rankChartRef.value) return
  rankChart = echarts.init(rankChartRef.value)
  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'value',
      name: '借阅次数'
    },
    yAxis: {
      type: 'category',
      data: data.hotBooks.map(b => b.title).reverse(),
      axisLabel: {
        width: 100,
        overflow: 'truncate'
      }
    },
    series: [
      {
        name: '借阅次数',
        type: 'bar',
        data: data.hotBooks.map(b => b.borrowCount).reverse(),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#67C23A' },
            { offset: 1, color: '#95d475' }
          ])
        },
        barWidth: '60%'
      }
    ],
    grid: {
      left: '3%',
      right: '10%',
      bottom: '3%',
      top: '3%',
      containLabel: true
    }
  }
  rankChart.setOption(option)
}

const initCategoryChart = (data: UserPortrait) => {
  if (!categoryChartRef.value) return
  categoryChart = echarts.init(categoryChartRef.value)
  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'middle'
    },
    series: [
      {
        name: '分类分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data.categories.map((cat, index) => ({
          value: cat.count,
          name: cat.category,
          itemStyle: {
            color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#B37FEB', '#73D13D', '#36CFC9'][index % 8]
          }
        }))
      }
    ]
  }
  categoryChart.setOption(option)
}

const fetchTrendData = async () => {
  trendChartLoading.value = true
  trendAILoading.value = true
  trendError.value = false
  try {
    const [trendRes, analysisRes] = await Promise.all([
      aiApi.getTrendData(),
      aiApi.getTrendAnalysis()
    ])
    await nextTick()
    const chartData: TrendData[] = trendRes.data.dates.map((date, i) => ({
      date,
      count: trendRes.data.counts[i]
    }))
    initTrendChart(chartData)
    trendAnalysis.value = analysisRes.data.analysis || '暂无趋势分析'
  } catch (error) {
    trendError.value = true
    ElMessage.error('获取趋势数据失败')
  } finally {
    trendChartLoading.value = false
    trendAILoading.value = false
  }
}

const fetchRankData = async () => {
  rankChartLoading.value = true
  rankAILoading.value = true
  rankError.value = false
  try {
    const [rankRes, insightRes] = await Promise.all([
      aiApi.getRankingData(),
      aiApi.getRankingInsight()
    ])
    await nextTick()
    const chartData: HotBooks = {
      hotBooks: rankRes.data.books.map(b => ({
        bookId: 0,
        title: b.name,
        author: '',
        category: '',
        borrowCount: b.borrowCount
      })),
      insight: insightRes.data.insight || '暂无采购建议'
    }
    initRankChart(chartData)
    rankInsight.value = chartData.insight
  } catch (error) {
    rankError.value = true
    ElMessage.error('获取排行数据失败')
  } finally {
    rankChartLoading.value = false
    rankAILoading.value = false
  }
}

const fetchCategoryData = async () => {
  categoryChartLoading.value = true
  categoryAILoading.value = true
  categoryError.value = false
  try {
    const [chartRes, portraitRes] = await Promise.all([
      aiApi.getCategoryData(),
      aiApi.getCategoryPortrait()
    ])
    await nextTick()
    const chartData: UserPortrait = {
      categories: chartRes.data.categories.map(c => ({
        category: c.name,
        count: c.value
      })),
      portrait: portraitRes.data.portrait || '暂无读者画像'
    }
    initCategoryChart(chartData)
    readerPortrait.value = chartData.portrait
  } catch (error) {
    categoryError.value = true
    ElMessage.error('获取分类数据失败')
  } finally {
    categoryChartLoading.value = false
    categoryAILoading.value = false
  }
}

const fetchRecommendations = async () => {
  recommendLoading.value = true
  recommendError.value = false
  try {
    const res = await aiApi.getPersonalRecommend()
    if (res.data) {
      recommendedBooks.value = res.data.map(book => ({
        id: book.id,
        title: book.title,
        author: book.author,
        cover: book.cover,
        reason: book.reason
      }))
    }
  } catch (error) {
    recommendError.value = true
    ElMessage.error('获取推荐数据失败')
  } finally {
    recommendLoading.value = false
  }
}

const generateReport = async () => {
  reportGenerating.value = true
  reportError.value = false
  try {
    const res = await aiApi.generateReport()
    if (res.data) {
      monthlyReport.value = {
        content: res.data.content
      }
    }
    ElMessage.success('报告生成成功')
  } catch (error) {
    reportError.value = true
    ElMessage.error('生成报告失败')
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
      monthlyReport.value = {
        content: res.data.content
      }
    }
  } catch (error) {
    reportError.value = true
    ElMessage.error('获取报告失败')
  } finally {
    reportLoading.value = false
  }
}

const handleResize = () => {
  trendChart?.resize()
  rankChart?.resize()
  categoryChart?.resize()
}

onMounted(() => {
  fetchTrendData()
  fetchRankData()
  fetchCategoryData()
  fetchRecommendations()
  fetchMonthlyReport()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChart?.dispose()
  rankChart?.dispose()
  categoryChart?.dispose()
})
</script>

<style lang="scss" scoped>
.ai-analysis-view {
  padding: 20px;

  h2 {
    margin-bottom: 20px;
    font-size: 20px;
    color: #333;
  }
}

.module-row {
  margin-bottom: 20px;
}

.ai-insight-card {
  background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);

  :deep(.el-card__header) {
    background: linear-gradient(135deg, rgba(64, 158, 255, 0.1) 0%, rgba(14, 117, 255, 0.05) 100%);
  }
}

.ai-content {
  min-height: 280px;
  padding: 10px;
  font-size: 14px;
  line-height: 1.8;
  color: #303133;

  :deep(p) {
    margin: 8px 0;
  }
}

.error-container {
  min-height: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.recommend-container {
  overflow: hidden;
}

.recommend-scroll {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding: 10px 0;
  scroll-behavior: smooth;

  &::-webkit-scrollbar {
    height: 8px;
  }

  &::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 4px;
  }

  &::-webkit-scrollbar-track {
    background: #f5f7fa;
    border-radius: 4px;
  }
}

.recommend-card {
  flex: 0 0 200px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 12px;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-4px);
  }
}

.book-cover {
  width: 100%;
  height: 160px;
  border-radius: 4px;
  overflow: hidden;
  background: #f5f7fa;
  margin-bottom: 12px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.book-info {
  .book-title {
    font-size: 14px;
    font-weight: 600;
    margin: 0 0 4px 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .book-author {
    font-size: 12px;
    color: #909399;
    margin: 0 0 8px 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .book-reason {
    display: block;
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
  }
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.report-content {
  padding: 20px;
  min-height: 300px;
  line-height: 1.8;
  font-size: 14px;

  :deep(h1) {
    font-size: 24px;
    font-weight: 600;
    margin: 16px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #409EFF;
  }

  :deep(h2) {
    font-size: 20px;
    font-weight: 600;
    margin: 14px 0;
    color: #303133;
  }

  :deep(h3) {
    font-size: 16px;
    font-weight: 600;
    margin: 12px 0;
    color: #606266;
  }

  :deep(p) {
    margin: 10px 0;
  }

  :deep(ul),
  :deep(ol) {
    margin: 10px 0;
    padding-left: 24px;

    li {
      margin: 6px 0;
    }
  }

  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;

    th,
    td {
      border: 1px solid #ebeef5;
      padding: 10px 12px;
      text-align: left;
    }

    th {
      background: #f5f7fa;
      font-weight: 600;
    }

    tr:nth-child(even) {
      background: #fafafa;
    }
  }

  :deep(strong) {
    font-weight: 700;
    color: #303133;
  }

  :deep(code) {
    background: #f5f7fa;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Courier New', monospace;
  }

  :deep(blockquote) {
    margin: 12px 0;
    padding: 10px 16px;
    background: #f0f9ff;
    border-left: 4px solid #409EFF;
    color: #606266;
  }
}

.empty-report {
  min-height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
