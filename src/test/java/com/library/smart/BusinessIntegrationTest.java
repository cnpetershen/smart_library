package com.library.smart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.smart.dto.BookFormDTO;
import com.library.smart.dto.BorrowDTO;
import com.library.smart.dto.ReaderFormDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 业务功能集成测试
 * 测试各模块的核心API是否可正常访问和返回预期格式
 */
@SpringBootTest
@AutoConfigureMockMvc
class BusinessIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        // 登录获取管理员token
        if (adminToken == null) {
            Map<String, String> loginDTO = new HashMap<>();
            loginDTO.put("account", "admin");
            loginDTO.put("password", "admin123");
            loginDTO.put("role", "admin");

            MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO)))
                    .andExpect(status().isOk())
                    .andReturn();

            String response = loginResult.getResponse().getContentAsString();
            adminToken = objectMapper.readTree(response).get("data").get("token").asText();
        }
    }

    // ==================== 认证模块测试 ====================

    @Test
    void testAdminLogin() throws Exception {
        // 测试管理员登录返回token
        Map<String, String> loginDTO = new HashMap<>();
        loginDTO.put("account", "admin");
        loginDTO.put("password", "admin123");
        loginDTO.put("role", "admin");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());
        assertNotNull(json.get("data").get("token"));
        assertNotNull(json.get("data").get("userInfo"));
        System.out.println("=== 管理员登录成功 ===");
    }

    // ==================== 图书管理模块测试 ====================

    @Test
    void testBookListAPI() throws Exception {
        // 测试图书列表API
        MvcResult result = mockMvc.perform(get("/api/books")
                .header("Authorization", "Bearer " + adminToken)
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());

        var data = json.get("data");
        assertTrue(data.has("total"));
        assertTrue(data.has("records"));
        System.out.println("=== 图书列表API正常，总数: " + data.get("total").asInt() + " ===");
    }

    @Test
    void testBookSearchAPI() throws Exception {
        // 测试图书搜索API
        MvcResult result = mockMvc.perform(get("/api/books/search")
                .header("Authorization", "Bearer " + adminToken)
                .param("keyword", "Java"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());
        assertTrue(json.get("data").isArray());
        System.out.println("=== 图书搜索API正常，找到 " + json.get("data").size() + " 条结果 ===");
    }

    // ==================== 读者管理模块测试 ====================

    @Test
    void testReaderListAPI() throws Exception {
        // 测试读者列表API
        MvcResult result = mockMvc.perform(get("/api/reader/list")
                .header("Authorization", "Bearer " + adminToken)
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());

        var data = json.get("data");
        assertTrue(data.has("total"));
        assertTrue(data.has("records"));
        System.out.println("=== 读者列表API正常，总数: " + data.get("total").asInt() + " ===");
    }

    @Test
    void testReaderSearchAPI() throws Exception {
        // 测试读者搜索API
        MvcResult result = mockMvc.perform(get("/api/reader/search")
                .header("Authorization", "Bearer " + adminToken)
                .param("keyword", "管"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());
        assertTrue(json.get("data").isArray());
        System.out.println("=== 读者搜索API正常 ===");
    }

    // ==================== 借阅管理模块测试 ====================

    @Test
    void testBorrowListAPI() throws Exception {
        // 测试借阅记录API
        MvcResult result = mockMvc.perform(get("/api/borrow/list")
                .header("Authorization", "Bearer " + adminToken)
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());

        var data = json.get("data");
        assertTrue(data.has("total"));
        assertTrue(data.has("records"));
        System.out.println("=== 借阅记录API正常，总数: " + data.get("total").asInt() + " ===");
    }

    @Test
    void testBorrowTodayCountAPI() throws Exception {
        // 测试今日借阅统计API
        MvcResult result = mockMvc.perform(get("/api/borrow/today")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());
        System.out.println("=== 今日借阅统计API正常 ===");
    }

    @Test
    void testOverdueListAPI() throws Exception {
        // 测试逾期列表API
        MvcResult result = mockMvc.perform(get("/api/borrow/overdue")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());
        assertTrue(json.get("data").isArray());
        System.out.println("=== 逾期列表API正常 ===");
    }

    // ==================== 首页统计模块测试 ====================

    @Test
    void testHomeStatisticsAPI() throws Exception {
        // 测试首页统计数据API
        MvcResult result = mockMvc.perform(get("/api/home/statistics")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response);
        assertEquals("200", json.get("code").asText());

        var data = json.get("data");
        assertTrue(data.has("bookTotal"));
        assertTrue(data.has("readerTotal"));
        assertTrue(data.has("borrowCurrent"));
        System.out.println("=== 首页统计API正常 ===");
        System.out.println("  图书总数: " + data.get("bookTotal"));
        System.out.println("  读者总数: " + data.get("readerTotal"));
        System.out.println("  在借数量: " + data.get("borrowCurrent"));
    }

    // ==================== 分页边界测试 ====================

    @Test
    void testPaginationBoundaries() throws Exception {
        // 测试分页参数边界处理
        MvcResult result = mockMvc.perform(get("/api/books")
                .header("Authorization", "Bearer " + adminToken)
                .param("page", "0")
                .param("pageSize", "500"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        var json = objectMapper.readTree(response).get("data");
        // 验证边界值被正确处理
        assertTrue(json.get("page").asInt() >= 1);
        assertTrue(json.get("pageSize").asInt() <= 100);
        System.out.println("=== 分页边界处理正常 ===");
    }
}