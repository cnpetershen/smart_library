<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑读者' : '新增读者'"
    width="560px"
    :close-on-click-modal="false"
    class="reader-form-dialog"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      class="reader-form"
    >
      <el-form-item label="姓名" prop="name">
        <el-input v-model="formData.name" placeholder="请输入姓名" maxlength="50" show-word-limit />
      </el-form-item>

      <el-form-item label="学号" prop="studentId">
        <el-input v-model="formData.studentId" placeholder="请输入学号" maxlength="20" />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入手机号" maxlength="11" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" maxlength="100" />
      </el-form-item>

      <el-form-item label="班级" prop="className">
        <el-input v-model="formData.className" placeholder="请输入班级" maxlength="50" />
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="formData.gender">
          <el-radio value="男">男</el-radio>
          <el-radio value="女">女</el-radio>
        </el-radio-group>
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
import { readerApi, type Reader } from '@/api/reader'

interface Props {
  modelValue: boolean
  readerId?: number | null
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

const isEdit = computed(() => !!props.readerId)

const formRef = ref()
const submitting = ref(false)

const formData = ref<Partial<Reader>>({
  name: '',
  studentId: '',
  phone: '',
  email: '',
  className: '',
  gender: '男',
  status: 1
})

const formRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
  ]
}

const fetchReaderDetail = async () => {
  if (!props.readerId) return
  try {
    const res = await readerApi.getById(props.readerId)
    if (res.data) {
      formData.value = { ...res.data }
    }
  } catch (error) {
    console.error('获取读者详情失败:', error)
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    if (isEdit.value) {
      await readerApi.update(props.readerId!, formData.value as Reader)
      ElMessage.success({ message: '编辑成功', grouping: true, duration: 2000 })
    } else {
      await readerApi.create(formData.value as Reader)
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
    name: '',
    studentId: '',
    phone: '',
    email: '',
    className: '',
    gender: '男',
    status: 1
  }
  visible.value = false
}

watch(visible, (val) => {
  if (val && isEdit.value) {
    fetchReaderDetail()
  }
})
</script>

<style lang="scss" scoped>
.reader-form-dialog {
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

.reader-form {
  :deep(.el-form-item) {
    margin-bottom: 20px;
  }

  :deep(.el-input__wrapper) {
    border-radius: 8px;
  }

  :deep(.el-radio__input.is-checked .el-radio__inner) {
    background-color: #409eff;
    border-color: #409eff;
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
