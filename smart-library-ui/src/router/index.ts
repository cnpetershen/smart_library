import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Landing',
    component: () => import('@/views/landing/LandingView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/system',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/system/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/HomeView.vue'),
        meta: { requiresAuth: true, title: '首页数据总览' }
      },
      {
        path: 'books',
        name: 'Books',
        component: () => import('@/views/books/BooksView.vue'),
        meta: { requiresAuth: true, title: '图书管理' }
      },
      {
        path: 'readers',
        name: 'Readers',
        component: () => import('@/views/readers/ReadersView.vue'),
        meta: { requiresAuth: true, title: '读者管理' }
      },
      {
        path: 'borrow',
        name: 'Borrow',
        component: () => import('@/views/borrow/BorrowView.vue'),
        meta: { requiresAuth: true, title: '借阅记录管理' }
      },
      {
        path: 'ai/trend',
        name: 'AITrend',
        component: () => import('@/views/ai/TrendAnalysisView.vue'),
        meta: { requiresAuth: true, title: '趋势分析' }
      },
      {
        path: 'ai/ranking',
        name: 'AIRanking',
        component: () => import('@/views/ai/HotRankingView.vue'),
        meta: { requiresAuth: true, title: '热门排行' }
      },
      {
        path: 'ai/category',
        name: 'AICategory',
        component: () => import('@/views/ai/CategoryStatsView.vue'),
        meta: { requiresAuth: true, title: '分类统计' }
      },
      {
        path: 'ai/recommend',
        name: 'AIRecommend',
        component: () => import('@/views/ai/SmartRecommendView.vue'),
        meta: { requiresAuth: true, title: '智能推荐' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/ProfileView.vue'),
        meta: { requiresAuth: true, title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth !== false)

  if (requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router