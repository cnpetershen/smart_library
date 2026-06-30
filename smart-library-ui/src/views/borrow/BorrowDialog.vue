<template>
  <el-dialog
    v-model="visible"
    title="办理借阅"
    width="520px"
    :close-on-click-modal="false"
    class="borrow-dialog"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      class="borrow-form"
    >
      <el-form-item label="选择读者" prop="readerId">
        <el-select
          v-model="formData.readerId"
          placeholder="请输入读者姓名或学号搜索"
          filterable
          remote
          :remote-method="searchReaders"
          :loading="readerLoading"
          style="width: 100%"
        >
          <el-option
            v-for="item in readerOptions"
            :key="item.id"
            :label="`${item.name} (${item.studentId})`"
            :value="item.id!"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="选择图书" prop="bookId">
        <el-select
          v-model="formData.bookId"
          placeholder="请输入书名或作者搜索"
          filterable
          remote
          :remote-method="searchBooks"
          :loading="bookLoading"
          style="width: 100%"
        >
          <el-option
            v-for="item in bookOptions"
            :key="item.id"
            :label="`${item.title} - ${item.author}`"
            :value="item.id!"
          >
            <div class="book-option">
              <span class="book-title">{{ item.title }}</span>
              <span class="book-author">{{ item.author }}</span>
              <el-tag v-if="item.availableStock > 0" type="success" size="small" class="stock-tag">
                可借: {{ item.availableStock }}
              </el-tag>
              <el-tag v-else type="danger" size="small" class="stock-tag">
                暂无库存
              </el-tag>
            </div>
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="应还日期" prop="expectedReturnTime">
        <el-date-picker
          v-model="formData.expectedReturnTime"
          type="date"
          placeholder="选择应还日期"
          style="width: 100%"
          :disabled-date="disabledDate"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { borrowApi, type BorrowCreateDTO } from '@/api/borrow'
import { readerApi, type Reader } from '@/api/reader'
import { bookApi, type Book } from '@/api/book'

interface Props {
  modelValue: boolean
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

const formRef = ref()
const submitting = ref(false)
const readerLoading = ref(false)
const bookLoading = ref(false)
const readerOptions = ref<Reader[]>([])
const bookOptions = ref<Book[]>([])

const formData = ref<Partial<BorrowCreateDTO>>({
  readerId: undefined,
  bookId: undefined,
  expectedReturnTime: ''
})

const formRules = {
  readerId: [{ required: true, message: '请选择读者', trigger: 'change' }],
  bookId: [
    { required: true, message: '请选择图书', trigger: 'change' },
    {
      validator: (rule: any, value: any, callback: any) => {
        const selectedBook = bookOptions.value.find(b => b.id === value)
        if (selectedBook && selectedBook.availableStock <= 0) {
          callback(new Error('该图书库存不足'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ],
  expectedReturnTime: [{ required: true, message: '请选择应还日期', trigger: 'change' }]
}

const searchReaders = async (query: string) => {
  if (!query) {
    readerOptions.value = []
    return
  }
  readerLoading.value = true
  try {
    const res = await readerApi.search(query)
    if (res.data) {
      readerOptions.value = res.data
    }
  } catch (error) {
    console.error('搜索读者失败:', error)
  } finally {
    readerLoading.value = false
  }
}

const searchBooks = async (query: string) => {
  if (!query) {
    bookOptions.value = []
    return
  }
  bookLoading.value = true
  try {
    const res = await bookApi.search(query)
    if (res.data) {
      bookOptions.value = res.data
    }
  } catch (error) {
    console.error('搜索图书失败:', error)
  } finally {
    bookLoading.value = false
  }
}

const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    // 转换为后端期望的格式 (userId 而不是 readerId)
    await borrowApi.borrow({
      userId: formData.value.readerId,
      bookId: formData.value.bookId
    })
    ElMessage.success({ message: '借阅办理成功', grouping: true, duration: 2000 })
    emit('success')
    handleClose()
  } catch (error: any) {
    ElMessage.error(error.message || '借阅办理失败')
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  formRef.value?.resetFields()
  formData.value = {
    readerId: undefined,
    bookId: undefined,
    expectedReturnTime: ''
  }
  readerOptions.value = []
  bookOptions.value = []
  visible.value = false
}
</script>

<style lang="scss" scoped>
.borrow-dialog {
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

.borrow-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }

  :deep(.el-input__wrapper),
  :deep(.el-select__wrapper) {
    border-radius: 8px;
  }
}

.book-option {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 0;

  .book-title {
    font-weight: 500;
    color: #334155;
  }

  .book-author {
    color: #94a3b8;
    font-size: 12px;
  }

  .stock-tag {
    margin-left: auto;
  }
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
