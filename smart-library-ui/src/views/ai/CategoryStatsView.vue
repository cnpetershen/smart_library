<template>
  <div class="category-stats-view">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" v-loading="categoryChartLoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>分类统计</span>
          </template>
          <div ref="categoryChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card shadow="hover" class="ai-insight-card" v-loading="categoryAILoading" element-loading-text="DeepSeek 正在分析...">
          <template #header>
            <span>AI 读者画像</span>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import type { ECharts } from 'echarts'
import aiApi from '@/api/ai'
import { ElMessage } from 'element-plus'

const categoryChartRef = ref<HTMLDivElement>()
let categoryChart: ECharts | null = null

const categoryChartLoading = ref(false)
const categoryAILoading = ref(false)
const categoryError = ref(false)
const readerPortrait = ref('')

const formatAIAnalysis = (text: string): string => {
  if (!text) return ''
  return text.split('\n').map(line => `<p>${line}</p>`).join('')
}

const initCategoryChart = (data: { category: string; count: number }[]) => {
  if (!categoryChartRef.value) return
  categoryChart = echarts.init(categoryChartRef.value)
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#B37FEB', '#73D13D', '#36CFC9']
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
        data: data.map((item, index) => ({
          value: item.count,
          name: item.category,
          itemStyle: {
            color: colors[index % colors.length]
          }
        }))
      }
    ]
  }
  categoryChart.setOption(option)
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
    const chartData = chartRes.data.categories.map(c => ({
      category: c.name,
      count: c.value
    }))
    initCategoryChart(chartData)
    readerPortrait.value = portraitRes.data.portrait || '暂无读者画像'
  } catch {
    categoryError.value = true
    ElMessage.error('获取分类数据失败')
  } finally {
    categoryChartLoading.value = false
    categoryAILoading.value = false
  }
}

const handleResize = () => {
  categoryChart?.resize()
}

onMounted(() => {
  fetchCategoryData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  categoryChart?.dispose()
})
</script>

<style lang="scss" scoped>
.category-stats-view {
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