<template>
  <el-drawer
    v-model="visible"
    title="读者详情"
    direction="rtl"
    size="520px"
    class="reader-detail-drawer"
    @close="handleClose"
  >
    <div v-loading="loading" class="drawer-content">
      <el-descriptions :column="1" border class="reader-descriptions">
        <el-descriptions-item label="姓名">
          {{ readerInfo.name }}
        </el-descriptions-item>
        <el-descriptions-item label="学号">
          {{ readerInfo.studentId }}
        </el-descriptions-item>
        <el-descriptions-item label="性别">
          {{ readerInfo.gender || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="手机号">
          {{ readerInfo.phone || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">
          {{ readerInfo.email || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="班级">
          {{ readerInfo.className || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="readerInfo.status === 1 ? 'success' : 'danger'" size="small">
            {{ readerInfo.status === 1 ? '正常' : '已禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="注册时间">
          {{ readerInfo.registerTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="累计借阅">
          {{ readerInfo.borrowCount || 0 }} 次
        </el-descriptions-item>
      </el-descriptions>

      <div class="borrow-history-section">
        <div class="section-header">
          <span class="section-title">📚 借阅历史</span>
          <el-select v-model="historyStatus" placeholder="状态筛选" size="small" clearable style="width: 120px">
            <el-option label="全部" :value="undefined" />
            <el-option label="借阅中" :value="0" />
            <el-option label="已归还" :value="1" />
            <el-option label="已逾期" :value="2" />
          </el-select>
        </div>

        <el-table
          :data="borrowHistory"
          stripe
          size="small"
          class="history-table"
          max-height="400"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="40" />
          <el-table-column prop="bookTitle" label="书名" min-width="120" show-overflow-tooltip />
          <el-table-column prop="borrowTime" label="借阅日期" width="100" />
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" size="small">
                {{ row.statusText }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="overdueDays" label="逾期天数" width="80" align="center">
            <template #default="{ row }">
              <span :class="{ 'overdue-text': row.overdueDays > 0 }">
                {{ row.overdueDays > 0 ? row.overdueDays : '-' }}
              </span>
            </template>
          </el-table-column>
        </el-table>

        <el-empty v-if="borrowHistory.length === 0" description="暂无借阅记录" :image-size="60" />
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { readerApi, type Reader, type BorrowRecord } from '@/api/reader'

interface Props {
  modelValue: boolean
  readerId?: number | null
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const loading = ref(false)
const readerInfo = ref<Partial<Reader>>({})
const borrowHistory = ref<BorrowRecord[]>([])
const historyStatus = ref<number | undefined>(undefined)

const fetchReaderDetail = async () => {
  if (!props.readerId) return
  loading.value = true
  try {
    const res = await readerApi.getById(props.readerId)
    if (res.data) {
      readerInfo.value = res.data
    }
  } catch (error) {
    console.error('获取读者详情失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchBorrowHistory = async () => {
  if (!props.readerId) return
  try {
    const res = await readerApi.getBorrowHistory(props.readerId, historyStatus.value)
    if (res.data) {
      borrowHistory.value = res.data
    }
  } catch (error) {
    console.error('获取借阅历史失败:', error)
  }
}

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

const handleSelectionChange = (selection: BorrowRecord[]) => {
  console.log('selected:', selection)
}

const handleClose = () => {
  readerInfo.value = {}
  borrowHistory.value = []
  historyStatus.value = undefined
}

watch(visible, (val) => {
  if (val && props.readerId) {
    fetchReaderDetail()
    fetchBorrowHistory()
  }
})

watch(historyStatus, () => {
  if (props.readerId) {
    fetchBorrowHistory()
  }
})
</script>

<style lang="scss" scoped>
.reader-detail-drawer {
  :deep(.el-drawer__header) {
    padding: 20px 24px;
    margin-bottom: 0;
    border-bottom: 1px solid #f1f5f9;

    .el-drawer__title {
      font-size: 18px;
      font-weight: 600;
      color: #1e293b;
    }
  }

  :deep(.el-drawer__body) {
    padding: 0;
  }
}

.drawer-content {
  padding: 24px;
}

.reader-descriptions {
  margin-bottom: 24px;

  :deep(.el-descriptions__label) {
    width: 100px;
    background-color: #f8fafc;
    color: #64748b;
    font-weight: 500;
  }

  :deep(.el-descriptions__content) {
    color: #334155;
  }
}

.borrow-history-section {
  border-top: 1px solid #f1f5f9;
  padding-top: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  .section-title {
    font-size: 15px;
    font-weight: 600;
    color: #1e293b;
  }
}

.history-table {
  border-radius: 8px;
  overflow: hidden;

  :deep(.el-table__header th) {
    background-color: #f8fafc !important;
    color: #64748b !important;
    font-weight: 600 !important;
    font-size: 12px;
    padding: 10px 8px;
  }

  :deep(.el-table__cell) {
    padding: 8px;
    font-size: 12px;
  }

  :deep(.el-table__row) {
    &:hover {
      background-color: #f8fafc;
    }
  }
}

.overdue-text {
  color: #ef4444;
  font-weight: 500;
}

:deep(.el-empty) {
  padding: 24px 0;
}
</style>
