<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑图书' : '新增图书'"
    width="680px"
    :close-on-click-modal="false"
    class="book-form-dialog"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      class="book-form"
    >
      <el-form-item label="书名" prop="title">
        <el-input v-model="formData.title" placeholder="请输入书名" maxlength="100" show-word-limit />
      </el-form-item>

      <el-form-item label="作者" prop="author">
        <el-input v-model="formData.author" placeholder="请输入作者" maxlength="50" />
      </el-form-item>

      <el-form-item label="ISBN" prop="isbn">
        <el-input v-model="formData.isbn" placeholder="请输入ISBN" maxlength="20" />
      </el-form-item>

      <el-form-item label="分类" prop="category">
        <el-select v-model="formData.category" placeholder="请选择分类" style="width: 100%">
          <el-option label="文学" value="文学" />
          <el-option label="小说" value="小说" />
          <el-option label="历史" value="历史" />
          <el-option label="科幻" value="科幻" />
          <el-option label="悬疑" value="悬疑" />
          <el-option label="儿童" value="儿童" />
          <el-option label="科技" value="科技" />
          <el-option label="经济" value="经济" />
          <el-option label="哲学" value="哲学" />
          <el-option label="艺术" value="艺术" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-form-item>

      <el-form-item label="出版日期" prop="publishDate">
        <el-date-picker
          v-model="formData.publishDate"
          type="date"
          placeholder="选择出版日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="总库存" prop="totalStock">
        <el-input-number
          v-model="formData.totalStock"
          :min="0"
          :max="9999"
          placeholder="请输入总库存"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="可借库存" prop="availableStock">
        <el-input-number
          v-model="formData.availableStock"
          :min="0"
          :max="formData.totalStock"
          placeholder="请输入可借库存"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="封面图片" prop="coverUrl">
        <div class="cover-upload" @click="triggerUpload">
          <img v-if="formData.coverUrl" :src="formData.coverUrl" class="cover-preview" />
          <div v-else class="cover-placeholder">
            <el-icon :size="32"><Plus /></el-icon>
            <span>点击上传</span>
          </div>
        </div>
        <input ref="fileInput" type="file" accept="image/*" style="display: none" @change="handleFileChange" />
      </el-form-item>

      <el-form-item label="简介" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="4"
          placeholder="请输入图书简介"
          maxlength="500"
          show-word-limit
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
import { Plus } from '@element-plus/icons-vue'
import { bookApi, type Book } from '@/api/book'
import { post } from '@/utils/request'

const props = defineProps<{
  modelValue: boolean
  bookId: number | null
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isEdit = computed(() => props.bookId !== null)

const formRef = ref()
const fileInput = ref<HTMLInputElement>()
const submitting = ref(false)

const formData = ref<Book>({
  title: '',
  author: '',
  isbn: '',
  category: '',
  publishDate: '',
  coverUrl: '',
  totalStock: 1,
  availableStock: 1,
  description: ''
})

const formRules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  isbn: [
    { required: true, message: '请输入ISBN', trigger: 'blur' },
    { pattern: /^[\d-]+$/, message: 'ISBN必须为10位或13位数字（可含连字符）', trigger: 'blur' }
  ],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  totalStock: [{ required: true, message: '请输入总库存', trigger: 'blur' }],
  availableStock: [{ required: true, message: '请输入可借库存', trigger: 'blur' }]
}

const fetchBookDetail = async (id: number) => {
  try {
    const res = await bookApi.getById(id)
    if (res.data) {
      formData.value = { ...res.data }
    }
  } catch (error) {
    console.error('获取图书详情失败:', error)
    ElMessage.error('获取图书详情失败')
  }
}

const triggerUpload = () => {
  fileInput.value?.click()
}

const handleFileChange = async (e: Event) => {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 5MB')
    return
  }

  try {
    const fd = new FormData()
    fd.append('file', file)
    fd.append('subDir', 'books')
    const res = await post<{ code: number; data: string }>('/file/upload', fd)
    formData.value.coverUrl = res.data
    ElMessage.success('封面上传成功')
  } catch (e: any) {
    console.error('Upload error:', e)
    ElMessage.error('封面上传失败: ' + (e?.response?.data?.message || e?.message || '未知错误'))
  }

  target.value = ''
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    submitting.value = true
    if (isEdit.value) {
      await bookApi.update(props.bookId!, formData.value)
      ElMessage.success({ message: '编辑成功', grouping: true, duration: 2000 })
    } else {
      await bookApi.create(formData.value)
      ElMessage.success({ message: '新增成功', grouping: true, duration: 2000 })
    }
    emit('success')
    handleClose()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitting.value = false
  }
}

const handleClose = () => {
  formRef.value?.resetFields()
  formData.value = {
    title: '',
    author: '',
    isbn: '',
    category: '',
    publishDate: '',
    coverUrl: '',
    totalStock: 1,
    availableStock: 1,
    description: ''
  }
  visible.value = false
}

watch(() => props.bookId, (newVal) => {
  if (newVal) {
    fetchBookDetail(newVal)
  }
})

watch(visible, (newVal) => {
  if (newVal && props.bookId) {
    fetchBookDetail(props.bookId)
  }
})
</script>

<style lang="scss" scoped>
.book-form-dialog {
  :deep(.el-dialog) {
    border-radius: 16px;
    overflow: hidden;
  }

  :deep(.el-dialog__header) {
    padding: 20px 24px;
    border-bottom: 1px solid #ebeef5;
    margin: 0;

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }

  :deep(.el-dialog__body) {
    padding: 24px;
  }

  :deep(.el-dialog__footer) {
    padding: 0;
  }
}

.book-form {
  :deep(.el-form-item) {
    margin-bottom: 24px;
  }

  :deep(.el-form-item__label) {
    color: #606266;
    font-weight: 500;
  }

  :deep(.el-input__wrapper) {
    border-radius: 8px;
  }

  :deep(.el-input-number) {
    width: 100%;

    .el-input__wrapper {
      border-radius: 8px;
    }
  }

  :deep(.el-select) {
    width: 100%;
  }

  :deep(.el-textarea__inner) {
    border-radius: 8px;
  }
}

.cover-upload {
  width: 120px;
  height: 160px;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;

  &:hover {
    border-color: #409eff;

    .cover-placeholder {
      color: #409eff;
    }
  }
}

.cover-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  gap: 8px;

  span {
    font-size: 12px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  padding: 16px 24px;
  border-top: 1px solid #ebeef5;
  margin-top: 0;

  :deep(.el-button) {
    border-radius: 8px;
    padding: 12px 24px;
  }
}
</style>
