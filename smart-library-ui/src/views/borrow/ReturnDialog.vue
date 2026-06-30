<template>
  <el-dialog
    v-model="visible"
    title="归还确认"
    width="480px"
    :close-on-click-modal="false"
    class="return-dialog"
    @close="handleClose"
  >
    <div v-if="borrowRecord" class="return-info">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="图书名称">
          {{ borrowRecord.bookTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="借阅读者">
          {{ borrowRecord.readerName }}
        </el-descriptions-item>
        <el-descriptions-item label="借阅日期">
          {{ borrowRecord.borrowTime }}
        </el-descriptions-item>
        <el-descriptions-item label="应还日期">
          {{ borrowRecord.expectedReturnTime }}
        </el-descriptions-item>
        <el-descriptions-item label="逾期天数">
          <span :class="{ 'overdue-text': borrowRecord.overdueDays > 0 }">
            {{ borrowRecord.overdueDays > 0 ? borrowRecord.overdueDays + ' 天' : '无' }}
          </span>
        </el-descriptions-item>
      </el-descriptions>

      <el-form-item label="实际归还日期" prop="actualReturnTime" style="margin-top: 20px">
        <el-date-picker
          v-model="formData.actualReturnTime"
          type="date"
          placeholder="选择归还日期"
          style="width: 100%"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-popconfirm
          title="确认归还该图书？"
          confirm-button-text="确认归还"
          cancel-button-text="取消"
          @confirm="handleConfirm"
        >
          <template #reference>
            <el-button type="success" :loading="submitting">确认归还</el-button>
          </template>
        </el-popconfirm>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { borrowApi, type BorrowRecord } from '@/api/borrow'

interface Props {
  modelValue: boolean
  borrowRecord?: BorrowRecord | null
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const submitting = ref(false)
const formData = ref({
  actualReturnTime: ''
})

const getDefaultReturnDate = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const handleConfirm = async () => {
  if (!props.borrowRecord) return

  submitting.value = true
  try {
    await borrowApi.return(props.borrowRecord.id, {
      actualReturnTime: formData.value.actualReturnTime || getDefaultReturnDate()
    })
    ElMessage.success({ message: '归还成功', grouping: true, duration: 2000 })
    emit('success')
    handleClose()
  } catch (error) {
    console.error('归还失败:', error)
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  formData.value.actualReturnTime = ''
  visible.value = false
}

watch(visible, (val) => {
  if (val) {
    formData.value.actualReturnTime = getDefaultReturnDate()
  }
})
</script>

<style lang="scss" scoped>
.return-dialog {
  border-radius: 16px;

  :deep(.el-dialog__header) {
    padding: 20px 24px;
    border-bottom: 1px solid #f1f5f9;

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: #1e293b;
    }
  }

  :deep(.el-dialog__body) {
    padding: 24px;
  }

  :deep(.el-dialog__footer) {
    padding: 16px 24px;
    border-top: 1px solid #f1f5f9;
  }
}

.return-info {
  :deep(.el-descriptions__label) {
    width: 120px;
    background-color: #f8fafc;
    color: #64748b;
    font-weight: 500;
  }

  :deep(.el-descriptions__content) {
    color: #334155;
  }

  :deep(.el-form-item) {
    margin-bottom: 0;
  }

  :deep(.el-input__wrapper) {
    border-radius: 8px;
  }
}

.overdue-text {
  color: #ef4444;
  font-weight: 600;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  padding-top: 16px;

  :deep(.el-button) {
    border-radius: 8px;
    min-width: 80px;
  }
}
</style>
