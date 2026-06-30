import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '@/store/user'

describe('User Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('should have empty initial state', () => {
    const userStore = useUserStore()
    expect(userStore.token).toBe('')
    expect(userStore.isLoggedIn).toBe(false)
  })

  it('should login correctly', () => {
    const userStore = useUserStore()
    const testToken = 'test-token-123'
    const testUserInfo = {
      id: 1,
      account: 'admin',
      name: '管理员',
      role: 'admin',
      gender: '男',
      phone: '13800138000',
      email: 'admin@test.com',
      avatar: '/avatar.jpg',
      status: 1,
      registerTime: '2024-01-01'
    }

    userStore.login(testToken, testUserInfo)

    expect(userStore.token).toBe(testToken)
    expect(userStore.isLoggedIn).toBe(true)
    expect(userStore.isAdmin).toBe(true)
    expect(userStore.userInfo.account).toBe('admin')
  })

  it('should logout correctly', () => {
    const userStore = useUserStore()
    const testToken = 'test-token-123'
    const testUserInfo = {
      id: 1,
      account: 'admin',
      name: '管理员',
      role: 'admin',
      gender: '男',
      phone: '13800138000',
      email: 'admin@test.com',
      avatar: '/avatar.jpg',
      status: 1,
      registerTime: '2024-01-01'
    }

    userStore.login(testToken, testUserInfo)
    expect(userStore.isLoggedIn).toBe(true)

    userStore.logout()
    expect(userStore.token).toBe('')
    expect(userStore.isLoggedIn).toBe(false)
    expect(userStore.isAdmin).toBe(false)
  })

  it('should detect admin role', () => {
    const userStore = useUserStore()

    userStore.login('token1', { id: 1, account: 'admin', name: 'Admin', role: 'admin', gender: '', phone: '', email: '', avatar: '', status: 1, registerTime: '' })
    expect(userStore.isAdmin).toBe(true)

    userStore.logout()
    userStore.login('token2', { id: 2, account: 'reader', name: 'Reader', role: 'reader', gender: '', phone: '', email: '', avatar: '', status: 1, registerTime: '' })
    expect(userStore.isAdmin).toBe(false)
  })
})
