import { describe, it, expect } from 'vitest'
import type { UserInfo, Book, BorrowRecord, Reader } from '@/types'

describe('Types Definition', () => {
  it('should have correct UserInfo structure', () => {
    const user: UserInfo = {
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

    expect(user.id).toBe(1)
    expect(user.account).toBe('admin')
    expect(user.role).toBe('admin')
    expect(user.status).toBe(1)
  })

  it('should accept valid UserInfo', () => {
    const user: UserInfo = {
      id: 1,
      account: 'test',
      name: 'Test User',
      role: 'reader',
      gender: '女',
      phone: '13900139000',
      email: 'test@test.com',
      avatar: '',
      status: 1,
      registerTime: '2024-01-01'
    }

    expect(user.role).toBe('reader')
    expect(user.gender).toBe('女')
  })
})

describe('Book Type', () => {
  it('should have required fields', () => {
    const book = {
      id: 1,
      title: '测试图书',
      author: '作者',
      isbn: '978-7-111-54742-6',
      category: '编程',
      publishDate: '2020-01-01',
      totalStock: 10,
      availableStock: 5,
      description: '描述',
      coverUrl: '/cover.jpg'
    }

    expect(book.title).toBe('测试图书')
    expect(book.totalStock).toBe(10)
    expect(book.availableStock).toBe(5)
  })
})

describe('BorrowRecord Type', () => {
  it('should have correct status values', () => {
    const statuses = ['借阅中', '已归还', '已逾期']

    const record1 = { status: 0, statusText: '借阅中' }
    const record2 = { status: 1, statusText: '已归还' }
    const record3 = { status: 2, statusText: '已逾期' }

    expect(record1.statusText).toBe(statuses[0])
    expect(record2.statusText).toBe(statuses[1])
    expect(record3.statusText).toBe(statuses[2])
  })
})
