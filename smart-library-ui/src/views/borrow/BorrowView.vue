<template>
  <div class="borrow-view">
    <el-card shadow="never" class="page-container">
      <template #header>
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索书名/读者"
            style="width: 280px; height: 40px"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-select v-model="statusFilter" placeholder="借阅状态" style="width: 140px; height: 40px" clearable>
            <el-option label="全部" :value="undefined" />
            <el-option label="借阅中" :value="0" />
            <el-option label="已归还" :value="1" />
            <el-option label="已逾期" :value="2" />
          </el-select>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleBorrow">办理借阅</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="tableData" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="bookTitle" label="书名" min-width="180" show-overflow-tooltip />
        <el-table-column prop="bookAuthor" label="作者" min-width="120" show-overflow-tooltip />
        <el-table-column prop="readerName" label="读者" width="100" show-overflow-tooltip />
        <el-table-column prop="borrowTime" label="借阅日期" width="120" />
        <el-table-column prop="expectedReturnTime" label="应还日期" width="120" />
        <el-table-column prop="actualReturnTime" label="归还日期" width="120">
          <template #default="{ row }">
            {{ row.actualReturnTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">
              {{ row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status !== 1"
              size="small"
              type="success"
              @click="handleReturn(row)"
            >
              归还
            </el-button>
            <el-tag v-else type="info" size="small">已归还</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <BorrowDialog v-model="borrowDialogVisible" @success="fetchData" />

    <ReturnDialog
      v-model="returnDialogVisible"
      :borrow-record="currentRecord"
      @success="fetchData"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { borrowApi, type BorrowRecord } from '@/api/borrow'
import BorrowDialog from './BorrowDialog.vue'
import ReturnDialog from './ReturnDialog.vue'

const loading = ref(false)
const searchKeyword = ref('')
const statusFilter = ref<number | undefined>(undefined)
const tableData = ref<BorrowRecord[]>([])
const borrowDialogVisible = ref(false)
const returnDialogVisible = ref(false)
const currentRecord = ref<BorrowRecord | null>(null)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const getStatusType = (status: number) => {
  switch (status) {
    case 0:
      return 'warning'
    case 1:
      return 'success'
    case 2:
      return 'danger'
    default:
      return 'info'
  }
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await borrowApi.list({
      keyword: searchKeyword.value || undefined,
      status: statusFilter.value,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取借阅记录失败:', error)
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
  statusFilter.value = undefined
  pagination.page = 1
  fetchData()
}

const handleBorrow = () => {
  borrowDialogVisible.value = true
}

const handleReturn = (row: BorrowRecord) => {
  currentRecord.value = row
  returnDialogVisible.value = true
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
.borrow-view {
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
  flex-wrap: wrap;

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

  :deep(.el-button--success) {
    border-radius: 8px;
    height: 40px;
    padding: 0 20px;
  }

  :deep(.el-button:not(.el-button--primary):not(.el-button--success)) {
    border-radius: 8px;
    height: 40px;
    padding: 0 20px;
  }
}

:deep(.el-table__header th) {
  background-color: #f8fafc !important;
  color: #64748b !important;
  font-weight: 600 !important;
  height: 48px;
}

:deep(.el-table__row) {
  &:hover {
    background-color: #f8fafc;
  }
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;

  :deep(.el-pagination) {
    .btn-prev,
    .btn-next {
      border-radius: 8px;
      background: #f8fafc;
    }

    .el-pager li {
      border-radius: 8px;

      &.is-active {
        background: #409eff;
        color: #fff;
      }
    }
  }
}
</style>
