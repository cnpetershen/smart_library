<template>
  <el-container class="main-layout">
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon :size="24"><Reading /></el-icon>
        <span>智慧图书馆</span>
      </div>
      <el-menu
        :default-active="currentRoute"
        class="sidebar-menu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/system/home">
          <el-icon><DataLine /></el-icon>
          <span>首页数据总览</span>
        </el-menu-item>
        <el-sub-menu v-if="userStore.isAdmin" index="collection">
          <template #title>
            <el-icon><Collection /></el-icon>
            <span>馆藏管理</span>
          </template>
          <el-menu-item index="/system/books">
            <el-icon><Books /></el-icon>
            <span>图书管理</span>
          </el-menu-item>
          <el-menu-item index="/system/readers">
            <el-icon><User /></el-icon>
            <span>读者管理</span>
          </el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/system/borrow">
          <el-icon><Tickets /></el-icon>
          <span>借阅记录管理</span>
        </el-menu-item>
        <el-sub-menu index="ai">
          <template #title>
            <el-icon><DataAnalysis /></el-icon>
            <span>AI智能数据分析</span>
          </template>
          <el-menu-item index="/system/ai/trend">
            <el-icon><TrendCharts /></el-icon>
            <span>趋势分析</span>
          </el-menu-item>
          <el-menu-item index="/system/ai/ranking">
            <el-icon><Sort /></el-icon>
            <span>热门排行</span>
          </el-menu-item>
          <el-menu-item index="/system/ai/category">
            <el-icon><PieChart /></el-icon>
            <span>分类统计</span>
          </el-menu-item>
          <el-menu-item index="/system/ai/recommend">
            <el-icon><Star /></el-icon>
            <span>智能推荐</span>
          </el-menu-item>
        </el-sub-menu>
        <el-menu-item index="/system/profile">
          <el-icon><Avatar /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <span class="page-title">{{ pageTitle }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><UserFilled /></el-icon>
              <span class="username">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const currentRoute = computed(() => route.path)

const pageTitle = computed(() => {
  return (route.meta.title as string) || ''
})

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  } else if (command === 'profile') {
    router.push('/system/profile')
  }
}
</script>

<style lang="scss" scoped>
.main-layout {
  height: 100vh;
}

.sidebar {
  background-color: #304156;

  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #fff;
    font-size: 18px;
    font-weight: bold;
    border-bottom: 1px solid #3d4a5c;
  }

  .sidebar-menu {
    border-right: none;
  }
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;

  .page-title {
    font-size: 16px;
    font-weight: 500;
    color: #333;
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 6px;
    cursor: pointer;
    color: #606266;

    .username {
      font-size: 14px;
    }
  }
}

.main-content {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>
