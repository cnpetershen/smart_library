<template>
  <div class="hot-ranking-view">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" v-loading="rankChartLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>热门排行</span>
          </template>
          <div ref="rankChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="ai-insight-card" v-loading="rankAILoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>AI 采购建议</span>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import aiApi from '@/api/ai'
import { ElMessage } from 'element-plus'

const rankChartRef = ref<HTMLDivElement>()
let rankChart: ECharts | null = null

const rankChartLoading = ref(false)
const rankAILoading = ref(false)
const rankError = ref(false)
const rankInsight = ref('')

const formatAIAnalysis = (text: string): string => {
  if (!text) return ''
  return text.split('\n').map(line => `<p>${line}</p>`).join('')
}

const initRankChart = (data: { title: string; borrowCount: number }[]) => {
  if (!rankChartRef.value) return
  rankChart = echarts.init(rankChartRef.value)
  const reversed = [...data].reverse()
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
      data: reversed.map(b => b.title),
      axisLabel: {
        width: 100,
        overflow: 'truncate'
      }
    },
    series: [
      {
        name: '借阅次数',
        type: 'bar',
        data: reversed.map(b => b.borrowCount),
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
    const chartData = rankRes.data.books.map(b => ({
      title: b.name,
      borrowCount: b.borrowCount
    }))
    initRankChart(chartData)
    rankInsight.value = insightRes.data.insight || '暂无采购建议'
  } catch {
    rankError.value = true
    ElMessage.error('获取排行数据失败')
  } finally {
    rankChartLoading.value = false
    rankAILoading.value = false
  }
}

const handleResize = () => {
  rankChart?.resize()
}

onMounted(() => {
  fetchRankData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  rankChart?.dispose()
})
</script>

<style lang="scss" scoped>
.hot-ranking-view {
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