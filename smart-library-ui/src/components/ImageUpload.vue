<template>
  <div class="image-upload">
    <!-- 未上传图片时显示上传按钮 -->
    <el-upload
      v-if="!modelValue"
      :http-request="handleUpload"
      accept=".jpg,.jpeg,.png"
      list-type="picture-card"
      :before-upload="beforeUpload"
      :show-file-list="false"
      :disabled="disabled"
    >
      <el-icon :size="24"><Plus /></el-icon>
      <template #tip>
        <div class="el-upload__tip">
          支持 JPG/PNG 格式，大小不超过 10MB
        </div>
      </template>
    </el-upload>

    <!-- 已上传图片时显示预览卡片 -->
    <div v-else class="image-card">
      <el-image
        :src="modelValue"
        fit="cover"
        style="width: 148px; height: 148px; border-radius: 6px; cursor: pointer;"
        @click="handlePreview"
      />
      <div class="image-card-actions">
        <span class="action-item" @click.stop="handlePreview">
          <el-icon><ZoomIn /></el-icon>
        </span>
        <span class="action-item" @click.stop="handleRemove">
          <el-icon><Delete /></el-icon>
        </span>
      </div>
    </div>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="previewVisible" title="图片预览" width="600px" destroy-on-close>
      <img :src="previewUrl" style="max-width: 100%; display: block; margin: 0 auto;" alt="预览图片" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Plus, ZoomIn, Delete } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadRawFile } from 'element-plus'
import { post, del } from '@/utils/request'

const props = withDefaults(defineProps<{
  modelValue?: string
  subDir?: string
  disabled?: boolean
}>(), {
  subDir: 'books',
  disabled: false
})

const emit = defineEmits<{
  'update:modelValue': [value: string | undefined]
}>()

const previewVisible = ref(false)
const previewUrl = ref('')

/** 上传前校验格式和大小 */
const beforeUpload = (rawFile: UploadRawFile): boolean => {
  const allowedTypes = ['image/jpeg', 'image/png']
  if (!allowedTypes.includes(rawFile.type)) {
    ElMessage.error('仅支持 JPG/PNG 格式的图片')
    return false
  }
  const maxSize = 10 * 1024 * 1024 // 10MB
  if (rawFile.size > maxSize) {
    ElMessage.error('图片大小不能超过 10MB')
    return false
  }
  return true
}

/** 自定义上传处理 */
const handleUpload = async (options: any) => {
  const formData = new FormData()
  formData.append('file', options.file)
  formData.append('subDir', props.subDir)

  try {
    const res = await post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    emit('update:modelValue', res.data)
    options.onSuccess(res.data)
  } catch (e) {
    options.onError(e)
  }
}

/** 预览图片 */
const handlePreview = () => {
  if (props.modelValue) {
    previewUrl.value = props.modelValue
    previewVisible.value = true
  }
}

/** 删除图片 */
const handleRemove = async () => {
  if (props.modelValue) {
    try {
      await del('/file/delete', { params: { filePath: props.modelValue } })
    } catch {
      // 删除失败不影响前端状态
    }
  }
  emit('update:modelValue', undefined)
}
</script>

<style scoped>
.image-upload {
  display: flex;
  flex-direction: column;
}

.image-card {
  position: relative;
  width: 148px;
  height: 148px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #d9d9d9;
  background: #fafafa;
}

.image-card .image-card-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 36px;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-card:hover .image-card-actions {
  opacity: 1;
}

.action-item {
  color: #fff;
  cursor: pointer;
  font-size: 18px;
  display: flex;
  align-items: center;
}

.action-item:hover {
  color: #409eff;
}
</style>
