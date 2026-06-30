<template>
  <div class="trend-analysis-view">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" v-loading="trendChartLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>趋势分析</span>
          </template>
          <div ref="trendChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="ai-insight-card" v-loading="trendAILoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>AI 趋势洞察</span>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import aiApi from '@/api/ai'
import { ElMessage } from 'element-plus'

const trendChartRef = ref<HTMLDivElement>()
let trendChart: ECharts | null = null

const trendChartLoading = ref(false)
const trendAILoading = ref(false)
const trendError = ref(false)
const trendAnalysis = ref('')

const formatAIAnalysis = (text: string): string => {
  if (!text) return ''
  return text.split('\n').map(line => `<p>${line}</p>`).join('')
}

const initTrendChart = (data: { date: string; count: number }[]) => {
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
    const chartData = trendRes.data.dates.map((date, i) => ({
      date,
      count: trendRes.data.counts[i]
    }))
    initTrendChart(chartData)
    trendAnalysis.value = analysisRes.data.analysis || '暂无趋势分析'
  } catch {
    trendError.value = true
    ElMessage.error('获取趋势数据失败')
  } finally {
    trendChartLoading.value = false
    trendAILoading.value = false
  }
}

const handleResize = () => {
  trendChart?.resize()
}

onMounted(() => {
  fetchTrendData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  trendChart?.dispose()
})
</script>

<style lang="scss" scoped>
.trend-analysis-view {
  padding: 20px;
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
</style>