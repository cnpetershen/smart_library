<template>
  <div class="profile-view">
    <el-row :gutter="24">
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <el-card shadow="hover" class="user-card">
          <div class="user-avatar-section">
            <ImageUpload
              v-model="userInfo.avatar"
              sub-dir="avatars"
              @update:model-value="handleAvatarUpdate"
            />
            <div class="user-avatar-mask">
              <el-icon :size="20"><Camera /></el-icon>
            </div>
          </div>
          <div class="user-name">{{ userInfo.name || userInfo.account }}</div>
          <div class="user-role">
            <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'success'">
              {{ userInfo.role === 'admin' ? '管理员' : '读者' }}
            </el-tag>
          </div>
          <el-divider />
          <div class="user-contact">
            <div class="contact-item">
              <el-icon><Message /></el-icon>
              <span>{{ userInfo.email || '未设置' }}</span>
            </div>
            <div class="contact-item">
              <el-icon><Phone /></el-icon>
              <span>{{ userInfo.phone || '未设置' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
        <el-card shadow="hover">
          <el-tabs v-model="activeTab" class="profile-tabs">
            <el-tab-pane label="基本信息" name="info">
              <el-descriptions :column="2" border class="info-descriptions">
                <el-descriptions-item label="账号">{{ userInfo.account }}</el-descriptions-item>
                <el-descriptions-item label="姓名">{{ userInfo.name || '-' }}</el-descriptions-item>
                <el-descriptions-item label="性别">{{ userInfo.gender || '-' }}</el-descriptions-item>
                <el-descriptions-item label="手机号">{{ userInfo.phone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="邮箱">{{ userInfo.email || '-' }}</el-descriptions-item>
                <el-descriptions-item label="角色">
                  <el-tag :type="userInfo.role === 'admin' ? 'danger' : 'success'" size="small">
                    {{ userInfo.role === 'admin' ? '管理员' : '读者' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="注册时间">{{ formatDate(userInfo.registerTime) }}</el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>

            <el-tab-pane label="编辑资料" name="edit">
              <div class="form-container">
                <el-form
                  ref="profileFormRef"
                  :model="profileForm"
                  :rules="profileRules"
                  label-width="100px"
                  class="profile-form"
                >
                  <el-form-item label="账号" prop="account">
                    <el-input v-model="profileForm.account" disabled />
                  </el-form-item>
                  <el-form-item label="姓名" prop="name">
                    <el-input v-model="profileForm.name" placeholder="请输入姓名" maxlength="50" />
                  </el-form-item>
                  <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="profileForm.gender">
                      <el-radio value="男">男</el-radio>
                      <el-radio value="女">女</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="手机号" prop="phone">
                    <el-input v-model="profileForm.phone" placeholder="请输入手机号" maxlength="11" />
                  </el-form-item>
                  <el-form-item label="邮箱" prop="email">
                    <el-input v-model="profileForm.email" placeholder="请输入邮箱" maxlength="100" />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" :loading="profileLoading" @click="handleUpdateProfile">保存</el-button>
                    <el-button @click="handleCancelEdit">取消</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>

            <el-tab-pane label="修改密码" name="password">
              <div class="form-container">
                <el-form
                  ref="passwordFormRef"
                  :model="passwordForm"
                  :rules="passwordRules"
                  label-width="100px"
                  class="password-form"
                >
                  <el-form-item label="旧密码" prop="oldPassword">
                    <el-input
                      v-model="passwordForm.oldPassword"
                      type="password"
                      placeholder="请输入旧密码"
                      show-password
                    />
                  </el-form-item>
                  <el-form-item label="新密码" prop="newPassword">
                    <el-input
                      v-model="passwordForm.newPassword"
                      type="password"
                      placeholder="请输入新密码"
                      show-password
                    />
                    <div class="password-strength">
                      <span>强度：</span>
                      <el-progress
                        :percentage="passwordStrength"
                        :color="strengthColor"
                        :show-text="false"
                        style="width: 200px"
                      />
                      <span :style="{ color: strengthColor }">{{ strengthText }}</span>
                    </div>
                  </el-form-item>
                  <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input
                      v-model="passwordForm.confirmPassword"
                      type="password"
                      placeholder="请再次输入新密码"
                      show-password
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" :loading="passwordLoading" @click="handleChangePassword">修改</el-button>
                    <el-button @click="handleCancelPassword">重置</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>

            <el-tab-pane v-if="userInfo.role === 'reader'" label="我的借阅" name="borrows">
              <div class="borrow-history">
                <div class="filter-bar">
                  <el-select v-model="borrowStatus" placeholder="状态筛选" style="width: 140px" clearable>
                    <el-option label="全部" :value="undefined" />
                    <el-option label="借阅中" :value="0" />
                    <el-option label="已归还" :value="1" />
                    <el-option label="已逾期" :value="2" />
                  </el-select>
                </div>
                <el-table :data="borrowHistory" stripe size="small" class="borrow-table">
                  <el-table-column prop="bookTitle" label="书名" min-width="150" show-overflow-tooltip />
                  <el-table-column prop="borrowTime" label="借阅日期" width="120" />
                  <el-table-column prop="expectedReturnTime" label="应还日期" width="120" />
                  <el-table-column prop="actualReturnTime" label="归还日期" width="120">
                    <template #default="{ row }">
                      {{ row.actualReturnTime || '-' }}
                    </template>
                  </el-table-column>
                  <el-table-column label="状态" width="90" align="center">
                    <template #default="{ row }">
                      <el-tag :type="getBorrowStatusType(row.status)" size="small">
                        {{ row.statusText }}
                      </el-tag>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>

            <el-tab-pane v-if="userInfo.role === 'reader'" label="AI 推荐" name="recommend">
              <div class="ai-recommendation">
                <div class="recommendation-header">
                  <h3>📚 为您推荐的好书</h3>
                  <el-button type="primary" size="small" @click="refreshRecommendations">
                    <el-icon v-if="!recommendLoading"><Refresh /></el-icon>
                    刷新推荐
                  </el-button>
                </div>
                <div v-loading="recommendLoading" class="recommendation-list">
                  <el-empty v-if="recommendations.length === 0" description="暂无推荐数据" :image-size="80" />
                  <div v-else class="book-grid">
                    <div v-for="book in recommendations" :key="book.id" class="book-card">
                      <img v-if="book.coverUrl" :src="book.coverUrl" class="book-cover" alt="cover" />
                      <div v-else class="book-cover-placeholder">
                        <el-icon :size="32"><Collection /></el-icon>
                      </div>
                      <div class="book-info">
                        <div class="book-title" :title="book.title">{{ book.title }}</div>
                        <div class="book-author">{{ book.author }}</div>
                        <div class="book-reason">{{ book.reason }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Camera, Message, Phone, Refresh, Collection } from '@element-plus/icons-vue'
import ImageUpload from '@/components/ImageUpload.vue'
import { userApi, type User } from '@/api/user'
import { readerApi, type BorrowRecord } from '@/api/reader'
import { bookApi, type Book } from '@/api/book'

const activeTab = ref('info')
const userInfo = ref<Partial<User>>({
  account: 'guest',
  name: '',
  role: 'reader'
})

const profileFormRef = ref()
const passwordFormRef = ref()
const profileLoading = ref(false)
const passwordLoading = ref(false)
const recommendLoading = ref(false)

const profileForm = reactive({
  account: '',
  name: '',
  gender: '男',
  phone: '',
  email: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const borrowStatus = ref<number | undefined>(undefined)
const borrowHistory = ref<BorrowRecord[]>([])
const recommendations = ref<(Book & { reason?: string })[]>([])

const passwordStrength = computed(() => {
  const pwd = passwordForm.newPassword
  if (!pwd) return 0
  let strength = 0
  if (pwd.length >= 8) strength += 25
  if (/[a-z]/.test(pwd)) strength += 25
  if (/[A-Z]/.test(pwd)) strength += 25
  if (/[0-9]/.test(pwd) || /[^a-zA-Z0-9]/.test(pwd)) strength += 25
  return strength
})

const strengthColor = computed(() => {
  if (passwordStrength.value <= 25) return '#ef4444'
  if (passwordStrength.value <= 50) return '#f97316'
  if (passwordStrength.value <= 75) return '#eab308'
  return '#22c55e'
})

const strengthText = computed(() => {
  if (passwordStrength.value <= 25) return '弱'
  if (passwordStrength.value <= 50) return '较弱'
  if (passwordStrength.value <= 75) return '中等'
  return '强'
})

const profileRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule: any, value: string, callback: any) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, message: '密码长度至少8位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const formatDate = (date?: string) => {
  if (!date) return '-'
  return date.split('T')[0]
}

const getBorrowStatusType = (status: number) => {
  switch (status) {
    case 0: return 'warning'
    case 1: return 'success'
    case 2: return 'danger'
    default: return 'info'
  }
}

const fetchUserInfo = async () => {
  try {
    const res = await userApi.getCurrentUser()
    if (res.data) {
      userInfo.value = res.data
      Object.assign(profileForm, {
        account: res.data.account,
        name: res.data.name || '',
        gender: res.data.gender || '男',
        phone: res.data.phone || '',
        email: res.data.email || ''
      })
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const fetchBorrowHistory = async () => {
  if (!userInfo.value.id) return
  try {
    const res = await readerApi.getBorrowHistory(userInfo.value.id, borrowStatus.value)
    if (res.data) {
      borrowHistory.value = res.data
    }
  } catch (error) {
    console.error('获取借阅历史失败:', error)
  }
}

const fetchRecommendations = async () => {
  recommendLoading.value = true
  try {
    const res = await bookApi.list({ pageSize: 20 })
    if (res.data.records) {
      recommendations.value = res.data.records.map(book => ({
        ...book,
        reason: '根据您的阅读历史推荐'
      }))
    }
  } catch (error) {
    console.error('获取推荐失败:', error)
  } finally {
    recommendLoading.value = false
  }
}

const refreshRecommendations = () => {
  fetchRecommendations()
}

const handleAvatarUpdate = async (url?: string) => {
  if (!url) return
  try {
    await userApi.updateProfile({ avatar: url })
    userInfo.value.avatar = url
    ElMessage.success({ message: '头像更新成功', grouping: true, duration: 2000 })
  } catch (error) {
    console.error('更新头像失败:', error)
  }
}

const handleUpdateProfile = async () => {
  const valid = await profileFormRef.value?.validate().catch(() => false)
  if (!valid) return

  profileLoading.value = true
  try {
    await userApi.updateProfile(profileForm)
    ElMessage.success({ message: '资料更新成功', grouping: true, duration: 2000 })
    await fetchUserInfo()
    activeTab.value = 'info'
  } catch (error) {
    console.error('更新失败:', error)
  } finally {
    profileLoading.value = false
  }
}

const handleCancelEdit = () => {
  profileFormRef.value?.resetFields()
  Object.assign(profileForm, {
    account: userInfo.value.account,
    name: userInfo.value.name || '',
    gender: userInfo.value.gender || '男',
    phone: userInfo.value.phone || '',
    email: userInfo.value.email || ''
  })
}

const handleChangePassword = async () => {
  const valid = await passwordFormRef.value?.validate().catch(() => false)
  if (!valid) return

  passwordLoading.value = true
  try {
    await userApi.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success({ message: '密码修改成功', grouping: true, duration: 2000 })
    handleCancelPassword()
  } catch (error: any) {
    ElMessage.error(error.message || '密码修改失败')
  } finally {
    passwordLoading.value = false
  }
}

const handleCancelPassword = () => {
  passwordFormRef.value?.resetFields()
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

watch(borrowStatus, () => {
  if (userInfo.value.role === 'reader') {
    fetchBorrowHistory()
  }
})

watch(activeTab, (val) => {
  if (val === 'borrows' && userInfo.value.role === 'reader') {
    fetchBorrowHistory()
  }
  if (val === 'recommend' && userInfo.value.role === 'reader') {
    if (recommendations.value.length === 0) {
      fetchRecommendations()
    }
  }
})

onMounted(() => {
  fetchUserInfo()
})
</script>

<style lang="scss" scoped>
.profile-view {
  padding: 20px;
}

.user-card {
  :deep(.el-card__body) {
    padding: 32px 24px;
    text-align: center;
  }
}

.user-avatar-section {
  position: relative;
  display: inline-block;
  margin-bottom: 16px;

  :deep(.image-upload) {
    .el-upload {
      width: 80px;
      height: 80px;
      border-radius: 50%;
    }

    .el-icon {
      font-size: 24px;
    }
  }

  .user-avatar-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    opacity: 0;
    transition: opacity 0.3s;
    cursor: pointer;
  }

  &:hover .user-avatar-mask {
    opacity: 1;
  }
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 8px;
}

.user-role {
  margin-bottom: 16px;
}

.user-contact {
  text-align: left;

  .contact-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    color: #64748b;
    font-size: 14px;

    .el-icon {
      color: #94a3b8;
    }
  }
}

.profile-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 24px;
  }

  :deep(.el-tabs__item) {
    font-size: 15px;
    height: 40px;
    line-height: 40px;
  }
}

.form-container {
  max-width: 480px;
  margin: 0 auto;
  padding: 20px 0;
}

.profile-form,
.password-form {
  :deep(.el-form-item) {
    margin-bottom: 24px;
  }

  :deep(.el-input__wrapper) {
    border-radius: 8px;
  }

  :deep(.el-radio__input.is-checked .el-radio__inner) {
    background-color: #409eff;
    border-color: #409eff;
  }
}

.password-strength {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 8px;
  font-size: 12px;
  color: #64748b;
}

.info-descriptions {
  :deep(.el-descriptions__label) {
    background-color: #f8fafc;
    color: #64748b;
    font-weight: 500;
    width: 100px;
  }

  :deep(.el-descriptions__content) {
    color: #334155;
  }
}

.borrow-history {
  .filter-bar {
    margin-bottom: 16px;
  }
}

.borrow-table {
  :deep(.el-table__header th) {
    background-color: #f8fafc !important;
    color: #64748b !important;
    font-weight: 600 !important;
    font-size: 13px;
  }

  :deep(.el-table__cell) {
    padding: 10px 0;
    font-size: 13px;
  }
}

.ai-recommendation {
  .recommendation-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 16px;
      color: #1e293b;
    }
  }
}

.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.book-card {
  background: #f8fafc;
  border-radius: 12px;
  padding: 16px;
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  }

  .book-cover {
    width: 100%;
    height: 140px;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 12px;
  }

  .book-cover-placeholder {
    width: 100%;
    height: 140px;
    background: #e2e8f0;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #94a3b8;
    margin-bottom: 12px;
  }

  .book-info {
    .book-title {
      font-size: 14px;
      font-weight: 600;
      color: #1e293b;
      margin-bottom: 4px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .book-author {
      font-size: 12px;
      color: #64748b;
      margin-bottom: 8px;
    }

    .book-reason {
      font-size: 11px;
      color: #8b5cf6;
      background: rgba(139, 92, 246, 0.1);
      padding: 4px 8px;
      border-radius: 4px;
      display: inline-block;
    }
  }
}
</style>
