<template>
  <div class="smart-recommend-view">
    <el-card shadow="hover" v-loading="recommendLoading" element-loading-text="DeepSeek 正在分析...">
      <template #header>
        <span>智能推荐</span>
      </template>
      <div v-if="recommendError" class="error-container">
        <el-empty description="AI 服务暂时不可用" :image-size="60">
          <el-button type="primary" @click="fetchRecommendations">重试</el-button>
        </el-empty>
      </div>
      <div v-else-if="recommendedBooks.length === 0 && !recommendLoading" class="empty-container">
        <el-empty description="暂无推荐图书">
          <template #image>
            <div class="empty-illustration">📚</div>
          </template>
          <p class="empty-tip">借阅更多图书后，系统将根据您的阅读偏好生成个性化推荐</p>
          <el-button type="primary" @click="$router.push('/system/borrow')">去借书</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import aiApi from '@/api/ai'
import { ElMessage } from 'element-plus'

interface RecommendedBook {
  id: number
  title: string
  author: string
  cover?: string
  reason: string
}

const recommendLoading = ref(false)
const recommendError = ref(false)
const recommendedBooks = ref<RecommendedBook[]>([])

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
  } catch {
    recommendError.value = true
    ElMessage.error('获取推荐数据失败')
  } finally {
    recommendLoading.value = false
  }
}

onMounted(() => {
  fetchRecommendations()
})
</script>

<style lang="scss" scoped>
.smart-recommend-view {
  padding: 20px;
}

.error-container {
  min-height: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-container {
  min-height: 280px;
  display: flex;
  align-items: center;
  justify-content: center;

  .empty-illustration {
    font-size: 64px;
    line-height: 1;
  }

  .empty-tip {
    color: #909399;
    font-size: 14px;
    margin: 8px 0 16px;
    max-width: 360px;
    text-align: center;
  }
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
</style>