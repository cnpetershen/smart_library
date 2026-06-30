import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfo } from '@/types'

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref<string>('')
    const userInfo = ref<UserInfo>({
      account: '',
      name: '',
      role: ''
    })

    const isLoggedIn = computed(() => !!token.value)
    const isAdmin = computed(() => userInfo.value.role === 'admin')

    const login = (newToken: string, info: UserInfo) => {
      token.value = newToken
      userInfo.value = info
      localStorage.setItem('token', newToken)
      localStorage.setItem('userInfo', JSON.stringify(info))
    }

    const logout = () => {
      token.value = ''
      userInfo.value = { account: '', name: '', role: '' }
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }

    const initFromStorage = () => {
      const storedToken = localStorage.getItem('token')
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedToken) {
        token.value = storedToken
      }
      if (storedUserInfo) {
        try {
          userInfo.value = JSON.parse(storedUserInfo)
        } catch {
          // ignore parse error
        }
      }
    }

    initFromStorage()

    return {
      token,
      userInfo,
      isLoggedIn,
      isAdmin,
      login,
      logout,
      initFromStorage
    }
  },
  {
    persist: false
  }
)
