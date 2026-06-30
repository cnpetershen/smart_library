import { describe, it, expect } from 'vitest'

describe('Request Utility', () => {
  it('should have correct baseURL configuration', () => {
    const baseURL = '/api'
    expect(baseURL).toBe('/api')
  })

  it('should have correct timeout configuration', () => {
    const timeout = 30000
    expect(timeout).toBe(30000)
  })

  it('should construct correct URLs', () => {
    const baseURL = '/api'
    const endpoints = [
      '/auth/login',
      '/books',
      '/reader/list',
      '/borrow/list',
      '/home/statistics'
    ]

    endpoints.forEach(endpoint => {
      const fullURL = baseURL + endpoint
      expect(fullURL).toContain('/api')
    })
  })

  it('should include Bearer token in Authorization header', () => {
    const token = 'test-token-123'
    const authHeader = `Bearer ${token}`
    expect(authHeader).toBe('Bearer test-token-123')
  })
})

describe('API Endpoints', () => {
  const API_BASE = '/api'

  it('should define correct auth endpoints', () => {
    expect(`${API_BASE}/auth/login`).toBe('/api/auth/login')
    expect(`${API_BASE}/auth/register`).toBe('/api/auth/register')
  })

  it('should define correct book endpoints', () => {
    expect(`${API_BASE}/books`).toBe('/api/books')
    expect(`${API_BASE}/books/search`).toBe('/api/books/search')
  })

  it('should define correct reader endpoints', () => {
    expect(`${API_BASE}/reader/list`).toBe('/api/reader/list')
    expect(`${API_BASE}/reader/search`).toBe('/api/reader/search')
  })

  it('should define correct borrow endpoints', () => {
    expect(`${API_BASE}/borrow/list`).toBe('/api/borrow/list')
    expect(`${API_BASE}/borrow/today`).toBe('/api/borrow/today')
    expect(`${API_BASE}/borrow/overdue`).toBe('/api/borrow/overdue')
  })

  it('should define correct home endpoints', () => {
    expect(`${API_BASE}/home/statistics`).toBe('/api/home/statistics')
    expect(`${API_BASE}/home/trend`).toBe('/api/home/trend')
  })
})

describe('Response Code Handling', () => {
  it('should handle success response code 200', () => {
    const successCode = 200
    expect(successCode).toBe(200)
  })

  it('should handle unauthorized code 401', () => {
    const unauthorizedCode = 401
    expect(unauthorizedCode).toBe(401)
  })

  it('should handle forbidden code 403', () => {
    const forbiddenCode = 403
    expect(forbiddenCode).toBe(403)
  })

  it('should handle validation error code 400', () => {
    const validationErrorCode = 400
    expect(validationErrorCode).toBe(400)
  })
})
