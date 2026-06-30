<template>
  <div class="books-view">
    <el-card shadow="never" class="page-container">
      <template #header>
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索书名/作者"
            style="width: 280px; height: 40px"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增图书</el-button>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%"
        row-class-name="table-row"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <img
              v-if="row.coverUrl"
              :src="row.coverUrl"
              class="book-cover"
              alt="cover"
              @error="handleImgError(row)"
            />
            <div v-else class="book-cover-placeholder">
              <el-icon :size="24"><Collection /></el-icon>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名" min-width="180" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" min-width="120" show-overflow-tooltip />
        <el-table-column prop="isbn" label="ISBN" width="150" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="availableStock" label="库存" width="100">
          <template #default="{ row }">
            <el-tag :type="getStockType(row.availableStock, row.totalStock)" size="small">
              {{ row.availableStock }}/{{ row.totalStock }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row)" size="small">
              {{ getStatusText(row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm
              title="确定删除该图书吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleDelete(row.id)"
            >
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="tableData.length === 0 && !loading" class="empty-container">
        <el-empty description="暂无图书数据，点击右上角新增">
          <template #image>
            <svg width="120" height="120" viewBox="0 0 120 120" fill="none">
              <rect x="20" y="30" width="60" height="80" rx="4" fill="#f0f9ff" stroke="#409EFF" stroke-width="2"/>
              <rect x="40" y="10" width="60" height="80" rx="4" fill="#fff" stroke="#409EFF" stroke-width="2"/>
              <line x1="50" y1="30" x2="90" y2="30" stroke="#409EFF" stroke-width="2"/>
              <line x1="50" y1="45" x2="85" y2="45" stroke="#d0e8ff" stroke-width="2"/>
              <line x1="50" y1="55" x2="80" y2="55" stroke="#d0e8ff" stroke-width="2"/>
            </svg>
          </template>
        </el-empty>
      </div>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          :layout="paginationLayout"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <BookFormDialog
      v-model="dialogVisible"
      :book-id="currentBookId"
      @success="fetchData"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Collection } from '@element-plus/icons-vue'
import { bookApi, type Book } from '@/api/book'
import BookFormDialog from './BookFormDialog.vue'

const loading = ref(false)
const searchKeyword = ref('')
const tableData = ref<Book[]>([])
const selectedRows = ref<Book[]>([])
const dialogVisible = ref(false)
const currentBookId = ref<number | null>(null)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const paginationLayout = 'total, sizes, prev, pager, next, jumper'

const getStockType = (available: number, total: number) => {
  if (available === 0) return 'danger'
  if (available <= total * 0.2) return 'warning'
  return 'success'
}

const getStatusType = (row: Book) => {
  if (row.availableStock === 0) return 'info'
  if (row.availableStock <= row.totalStock * 0.2) return 'warning'
  return 'success'
}

const getStatusText = (row: Book) => {
  if (row.availableStock === 0) return '下架'
  if (row.availableStock <= row.totalStock * 0.2) return '库存紧张'
  return '上架'
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await bookApi.list({
      keyword: searchKeyword.value || undefined,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取图书列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  searchKeyword.value = ''
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  currentBookId.value = null
  dialogVisible.value = true
}

const handleEdit = (row: Book) => {
  currentBookId.value = row.id!
  dialogVisible.value = true
}

const handleDelete = async (id: number) => {
  try {
    await bookApi.delete(id)
    ElMessage.success({ message: '删除成功', grouping: true, duration: 2000 })
    fetchData()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

const handleSelectionChange = (rows: Book[]) => {
  selectedRows.value = rows
}

const handleImgError = (row: Book) => {
  row.coverUrl = ''
}

const handleSizeChange = () => {
  pagination.page = 1
  fetchData()
}

const handleCurrentChange = () => {
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style lang="scss" scoped>
.books-view {
  padding: 20px;
}

.page-container {
  border-radius: 12px;
  background: #fff;

  :deep(.el-card__header) {
    padding: 20px 24px;
    border-bottom: none;
  }

  :deep(.el-card__body) {
    padding: 0 24px 24px;
  }
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;

  :deep(.el-input__wrapper) {
    border-radius: 8px;
    height: 40px;
    box-shadow: 0 0 0 1px #dcdfe6 inset;

    &:hover {
      box-shadow: 0 0 0 1px #c0c4cc inset;
    }

    &.is-focus {
      box-shadow: 0 0 0 1px #409eff inset;
    }
  }

  :deep(.el-button--primary) {
    border-radius: 8px;
    height: 40px;
    padding: 0 20px;
  }

  :deep(.el-button:not(.el-button--primary)) {
    border-radius: 8px;
    height: 40px;
    padding: 0 20px;
  }
}

:deep(.table-row) {
  height: 64px;
  transition: background-color 0.2s ease;

  &:hover {
    background-color: #f1f5f9 !important;
  }
}

:deep(.el-table__header th) {
  background-color: #f8fafc !important;
  color: #64748b !important;
  font-weight: 600 !important;
  height: 48px;
}

:deep(.el-table__cell) {
  padding: 12px 0;
}

.book-cover {
  width: 48px;
  height: 64px;
  border-radius: 6px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.book-cover-placeholder {
  width: 48px;
  height: 64px;
  border-radius: 6px;
  background: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #94a3b8;
}

.empty-container {
  padding: 60px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;

  :deep(.el-pagination) {
    .el-pagination__total {
      color: #64748b;
    }

    .btn-prev,
    .btn-next {
      border-radius: 8px;
      background: #f8fafc;

      &:hover {
        background: #f1f5f9;
      }
    }

    .el-pager li {
      border-radius: 8px;
      margin: 0 2px;

      &.is-active {
        background: #409eff;
        color: #fff;
      }

      &:hover:not(.is-active) {
        background: #f1f5f9;
      }
    }
  }
}
</style>
