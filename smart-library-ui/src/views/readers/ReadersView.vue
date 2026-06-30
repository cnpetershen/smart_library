<template>
  <div class="readers-view">
    <el-card shadow="never" class="page-container">
      <template #header>
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索姓名/学号"
            style="width: 280px; height: 40px"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 140px; height: 40px" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="已禁用" :value="0" />
          </el-select>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增读者</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="tableData" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" min-width="100" show-overflow-tooltip />
        <el-table-column prop="studentId" label="学号" width="140" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="className" label="班级" min-width="120" show-overflow-tooltip />
        <el-table-column prop="borrowCount" label="借阅次数" width="100" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '正常' : '已禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="handleViewDetail(row)">详情</el-button>
            <el-button size="small" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm
              v-if="row.status === 1"
              title="确定禁用该读者吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleDisable(row)"
            >
              <template #reference>
                <el-button size="small" type="warning">禁用</el-button>
              </template>
            </el-popconfirm>
            <el-popconfirm
              v-else
              title="确定启用该读者吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleEnable(row)"
            >
              <template #reference>
                <el-button size="small" type="success">启用</el-button>
              </template>
            </el-popconfirm>
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

    <ReaderFormDialog v-model="formDialogVisible" :reader-id="currentReaderId" @success="fetchData" />

    <ReaderDetailDrawer
      v-model="detailDrawerVisible"
      :reader-id="currentReaderId"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { readerApi, type Reader } from '@/api/reader'
import ReaderFormDialog from './ReaderFormDialog.vue'
import ReaderDetailDrawer from './ReaderDetailDrawer.vue'

const loading = ref(false)
const searchKeyword = ref('')
const statusFilter = ref<number | null>(null)
const tableData = ref<Reader[]>([])
const formDialogVisible = ref(false)
const detailDrawerVisible = ref(false)
const currentReaderId = ref<number | null>(null)

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await readerApi.list({
      keyword: searchKeyword.value || undefined,
      status: statusFilter.value || undefined,
      page: pagination.page,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取读者列表失败:', error)
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
  statusFilter.value = null
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  currentReaderId.value = null
  formDialogVisible.value = true
}

const handleEdit = (row: Reader) => {
  currentReaderId.value = row.id!
  formDialogVisible.value = true
}

const handleViewDetail = (row: Reader) => {
  currentReaderId.value = row.id!
  detailDrawerVisible.value = true
}

const handleDisable = async (row: Reader) => {
  try {
    await readerApi.disable(row.id!)
    ElMessage.success({ message: '禁用成功', grouping: true, duration: 2000 })
    fetchData()
  } catch (error: any) {
    ElMessage.error(error.message || '禁用失败，该读者存在未归还图书')
  }
}

const handleEnable = async (row: Reader) => {
  try {
    await readerApi.enable(row.id!)
    ElMessage.success({ message: '启用成功', grouping: true, duration: 2000 })
    fetchData()
  } catch (error) {
    console.error('启用失败:', error)
  }
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
.readers-view {
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

  :deep(.el-button:not(.el-button--primary)) {
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
