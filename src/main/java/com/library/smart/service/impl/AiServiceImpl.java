package com.library.smart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.smart.client.DeepSeekClient;
import com.library.smart.entity.AiReport;
import com.library.smart.entity.Book;
import com.library.smart.entity.BorrowRecord;
import com.library.smart.entity.User;
import com.library.smart.mapper.AiReportMapper;
import com.library.smart.mapper.BookMapper;
import com.library.smart.mapper.BorrowRecordMapper;
import com.library.smart.mapper.UserMapper;
import com.library.smart.service.AiService;
import com.library.smart.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl extends ServiceImpl<AiReportMapper, AiReport> implements AiService {

    private final DeepSeekClient deepSeekClient;
    private final BorrowRecordMapper borrowRecordMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;

    private static final String CACHE_KEY_TREND_DATA = "smart:ai:trend:data:%d";
    private static final String CACHE_KEY_TREND_ANALYSIS = "smart:ai:trend:analysis:%d";
    private static final String CACHE_KEY_RANKING_DATA = "smart:ai:ranking:data:%d";
    private static final String CACHE_KEY_RANKING_INSIGHT = "smart:ai:ranking:insight:%d";
    private static final String CACHE_KEY_CATEGORY_DATA = "smart:ai:category:data";
    private static final String CACHE_KEY_CATEGORY_PORTRAIT = "smart:ai:category:portrait";
    private static final String CACHE_KEY_PERSONAL_RECOMMEND = "smart:ai:personal_recommend:%d";
    private static final String CACHE_KEY_MONTHLY_REPORT = "smart:ai:report:monthly";
    private static final long CACHE_TTL_2H = 2;
    private static final long CACHE_TTL_30M = 30;
    private static final long CACHE_TTL_24H = 24;

    @Override
    public Map<String, Object> getTrendData(Integer days) {
        if (days == null || days <= 0) {
            days = 30;
        }

        String cacheKey = String.format(CACHE_KEY_TREND_DATA, days);
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(cacheKey, Map.class);
        if (cached != null) {
            log.debug("AI trend data cache hit: {}", cacheKey);
            return cached;
        }

        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        LocalDate today = LocalDate.now();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
            wrapper.ge("borrow_time", startOfDay);
            wrapper.lt("borrow_time", endOfDay);
            Long count = borrowRecordMapper.selectCount(wrapper);

            dates.add(date.format(formatter));
            counts.add(count.intValue());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("dates", dates);
        result.put("counts", counts);

        try {
            redisCache.set(cacheKey, result, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI trend data: {}", e.getMessage());
        }

        return result;
    }

    @Override
    public String getTrendAnalysis(Integer days) {
        if (days == null || days <= 0) {
            days = 30;
        }

        String cacheKey = String.format(CACHE_KEY_TREND_ANALYSIS, days);
        String cached = redisCache.get(cacheKey, String.class);
        if (cached != null) {
            log.debug("AI trend analysis cache hit: {}", cacheKey);
            return cached;
        }

        Map<String, Object> trendData = getTrendData(days);

        String systemPrompt = "你是图书馆数据分析专家。请根据以下借阅数据，分析趋势变化，指出高峰和低谷时段，给出针对性的运营建议。要求语言简洁专业，200字以内。";
        String analysis;
        try {
            String userMessage = "借阅数据：" + objectMapper.writeValueAsString(trendData);
            analysis = deepSeekClient.chat(systemPrompt, userMessage);
        } catch (Exception e) {
            log.warn("Failed to serialize trend data for AI analysis: {}", e.getMessage());
            analysis = "暂时无法获取趋势分析";
        }

        try {
            redisCache.set(cacheKey, analysis, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI trend analysis: {}", e.getMessage());
        }

        return analysis;
    }

    @Override
    public Map<String, Object> getRankingData(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        String cacheKey = String.format(CACHE_KEY_RANKING_DATA, limit);
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(cacheKey, Map.class);
        if (cached != null) {
            log.debug("AI ranking data cache hit: {}", cacheKey);
            return cached;
        }

        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.select("book_id, COUNT(*) as borrow_count");
        wrapper.groupBy("book_id");
        wrapper.orderByDesc("borrow_count");
        wrapper.last("LIMIT " + limit);
        List<Map<String, Object>> rawHotBooks = borrowRecordMapper.selectMaps(wrapper);

        List<Map<String, Object>> books = new ArrayList<>();
        for (Map<String, Object> item : rawHotBooks) {
            Long bookId = ((Number) item.get("book_id")).longValue();
            Book book = bookMapper.selectById(bookId);
            if (book != null) {
                Map<String, Object> bookInfo = new LinkedHashMap<>();
                bookInfo.put("name", book.getTitle());
                bookInfo.put("borrowCount", ((Number) item.get("borrow_count")).intValue());
                books.add(bookInfo);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("books", books);

        try {
            redisCache.set(cacheKey, result, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI ranking data: {}", e.getMessage());
        }

        return result;
    }

    @Override
    public String getRankingInsight(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        String cacheKey = String.format(CACHE_KEY_RANKING_INSIGHT, limit);
        String cached = redisCache.get(cacheKey, String.class);
        if (cached != null) {
            log.debug("AI ranking insight cache hit: {}", cacheKey);
            return cached;
        }

        Map<String, Object> rankingData = getRankingData(limit);

        String systemPrompt = "你是图书馆采购顾问。请根据以下热门图书排行数据，分析读者阅读偏好，指出哪些类别最受欢迎，给出新书采购建议和阅读推广策略。200字以内。";
        String insight;
        try {
            String userMessage = "热门图书排行：" + objectMapper.writeValueAsString(rankingData);
            insight = deepSeekClient.chat(systemPrompt, userMessage);
        } catch (Exception e) {
            log.warn("Failed to serialize ranking data for AI insight: {}", e.getMessage());
            insight = "暂时无法获取排行分析";
        }

        try {
            redisCache.set(cacheKey, insight, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI ranking insight: {}", e.getMessage());
        }

        return insight;
    }

    @Override
    public Map<String, Object> getCategoryData() {
        String cacheKey = CACHE_KEY_CATEGORY_DATA;
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(cacheKey, Map.class);
        if (cached != null) {
            log.debug("AI category data cache hit: {}", cacheKey);
            return cached;
        }

        List<BorrowRecord> allRecords = borrowRecordMapper.selectList(null);

        Map<String, Integer> categoryCountMap = new LinkedHashMap<>();
        for (BorrowRecord record : allRecords) {
            Book book = bookMapper.selectById(record.getBookId());
            if (book != null && StringUtils.hasText(book.getCategory())) {
                categoryCountMap.merge(book.getCategory(), 1, Integer::sum);
            }
        }

        List<Map<String, Object>> categories = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categoryCountMap.entrySet()) {
            Map<String, Object> cat = new LinkedHashMap<>();
            cat.put("name", entry.getKey());
            cat.put("value", entry.getValue());
            categories.add(cat);
        }

        categories.sort((a, b) -> {
            Integer v1 = (Integer) b.get("value");
            Integer v2 = (Integer) a.get("value");
            return v1.compareTo(v2);
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("categories", categories);

        try {
            redisCache.set(cacheKey, result, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI category data: {}", e.getMessage());
        }

        return result;
    }

    @Override
    public String getCategoryPortrait() {
        String cacheKey = CACHE_KEY_CATEGORY_PORTRAIT;
        String cached = redisCache.get(cacheKey, String.class);
        if (cached != null) {
            log.debug("AI category portrait cache hit: {}", cacheKey);
            return cached;
        }

        Map<String, Object> categoryData = getCategoryData();

        String systemPrompt = "你是图书馆用户研究专家。请根据以下读者借阅分类分布数据，生成群体画像描述，总结核心读者特征，给出个性化服务和活动策划建议。200字以内。";
        String portrait;
        try {
            String userMessage = "读者借阅分类分布：" + objectMapper.writeValueAsString(categoryData);
            portrait = deepSeekClient.chat(systemPrompt, userMessage);
        } catch (Exception e) {
            log.warn("Failed to serialize category data for AI portrait: {}", e.getMessage());
            portrait = "暂时无法获取用户画像";
        }

        try {
            redisCache.set(cacheKey, portrait, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI category portrait: {}", e.getMessage());
        }

        return portrait;
    }

    @Override
    public Map<String, Object> getHotBooks(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }

        String cacheKey = "smart:ai:hot-books:" + limit;
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(cacheKey, Map.class);
        if (cached != null) {
            log.debug("AI hot-books cache hit: {}", cacheKey);
            return cached;
        }

        // Build hotBooks list with full book info
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.select("book_id, COUNT(*) as borrow_count");
        wrapper.groupBy("book_id");
        wrapper.orderByDesc("borrow_count");
        wrapper.last("LIMIT " + limit);
        List<Map<String, Object>> rawData = borrowRecordMapper.selectMaps(wrapper);

        List<Map<String, Object>> hotBooks = new ArrayList<>();
        for (Map<String, Object> item : rawData) {
            Long bookId = ((Number) item.get("book_id")).longValue();
            Book book = bookMapper.selectById(bookId);
            if (book != null) {
                Map<String, Object> bookItem = new LinkedHashMap<>();
                bookItem.put("bookId", bookId);
                bookItem.put("title", book.getTitle());
                bookItem.put("author", book.getAuthor());
                bookItem.put("category", book.getCategory());
                bookItem.put("borrowCount", ((Number) item.get("borrow_count")).intValue());
                hotBooks.add(bookItem);
            }
        }

        // Get AI insight
        String insight = getRankingInsight(limit);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("hotBooks", hotBooks);
        result.put("insight", insight);

        try {
            redisCache.set(cacheKey, result, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI hot-books: {}", e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> getCategoryPortraitCombined() {
        String cacheKey = "smart:ai:portrait:combined";
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(cacheKey, Map.class);
        if (cached != null) {
            log.debug("AI portrait combined cache hit: {}", cacheKey);
            return cached;
        }

        // Get category data and transform to match frontend format
        Map<String, Object> categoryData = getCategoryData();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rawCategories = (List<Map<String, Object>>) categoryData.get("categories");
        List<Map<String, Object>> categories = new ArrayList<>();
        if (rawCategories != null) {
            for (Map<String, Object> cat : rawCategories) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("category", cat.get("name"));
                item.put("count", cat.get("value"));
                categories.add(item);
            }
        }

        String portrait = getCategoryPortrait();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("categories", categories);
        result.put("portrait", portrait);

        try {
            redisCache.set(cacheKey, result, CACHE_TTL_2H, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to cache AI portrait combined: {}", e.getMessage());
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getRecommendations(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        String cacheKey = String.format(CACHE_KEY_PERSONAL_RECOMMEND, userId);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cached = redisCache.get(cacheKey, List.class);
        if (cached != null) {
            log.debug("AI personal recommend cache hit: {}", cacheKey);
            return cached;
        }

        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        QueryWrapper<BorrowRecord> historyWrapper = new QueryWrapper<>();
        historyWrapper.eq("user_id", userId);
        historyWrapper.ge("borrow_time", threeMonthsAgo);
        List<BorrowRecord> userHistory = borrowRecordMapper.selectList(historyWrapper);

        if (userHistory.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> borrowedCategories = new HashSet<>();
        List<String> borrowedBookTitles = new ArrayList<>();
        for (BorrowRecord record : userHistory) {
            Book book = bookMapper.selectById(record.getBookId());
            if (book != null && StringUtils.hasText(book.getCategory())) {
                borrowedCategories.add(book.getCategory());
                borrowedBookTitles.add(book.getTitle());
            }
        }

        if (borrowedCategories.isEmpty()) {
            return Collections.emptyList();
        }

        QueryWrapper<Book> candidateWrapper = new QueryWrapper<>();
        candidateWrapper.in("category", borrowedCategories);
        candidateWrapper.notInSql("id", "SELECT book_id FROM t_borrow_record WHERE user_id = " + userId);
        candidateWrapper.eq("is_deleted", 0);
        candidateWrapper.last("LIMIT 20");
        List<Book> candidates = bookMapper.selectList(candidateWrapper);

        if (candidates.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder userHistoryText = new StringBuilder();
        userHistoryText.append("用户借阅历史：\n");
        userHistoryText.append("借阅分类偏好：").append(String.join("、", borrowedCategories)).append("\n");
        userHistoryText.append("借阅过的图书：").append(String.join("、", borrowedBookTitles)).append("\n\n");
        userHistoryText.append("候选推荐图书：\n");
        for (int i = 0; i < candidates.size(); i++) {
            Book b = candidates.get(i);
            userHistoryText.append(i + 1).append(".《").append(b.getTitle()).append("》- ").append(b.getAuthor());
            userHistoryText.append("（分类：").append(b.getCategory()).append("）\n");
        }

        String systemPrompt = "你是图书馆个性化推荐助手。请根据用户的借阅历史和候选图书列表，挑选最匹配的3-5本图书，为每本书生成个性化的推荐理由（结合用户兴趣点）。返回 JSON 格式：[{\"bookId\":1,\"reason\":\"...\"}]。只返回JSON数组，不要其他内容。";
        String userMessage = userHistoryText.toString();

        String aiResponse = deepSeekClient.chat(systemPrompt, userMessage);

        List<Map<String, Object>> recommendations = parseRecommendResponse(aiResponse, candidates);

        try {
            redisCache.set(cacheKey, recommendations, CACHE_TTL_30M, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Failed to cache AI personal recommend: {}", e.getMessage());
        }

        return recommendations;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiReport generateMonthlyReport() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = endOfMonth.plusDays(1).atStartOfDay();

        QueryWrapper<BorrowRecord> borrowWrapper = new QueryWrapper<>();
        borrowWrapper.ge("borrow_time", startDateTime);
        borrowWrapper.lt("borrow_time", endDateTime);
        Long totalBorrows = borrowRecordMapper.selectCount(borrowWrapper);

        QueryWrapper<Book> bookWrapper = new QueryWrapper<>();
        bookWrapper.eq("is_deleted", 0);
        bookWrapper.ge("create_time", startDateTime);
        Long newBooks = bookMapper.selectCount(bookWrapper);

        QueryWrapper<User> readerWrapper = new QueryWrapper<>();
        readerWrapper.eq("role", "reader");
        readerWrapper.eq("is_deleted", 0);
        readerWrapper.ge("register_time", startDateTime);
        Long newReaders = userMapper.selectCount(readerWrapper);

        QueryWrapper<BorrowRecord> overdueWrapper = new QueryWrapper<>();
        overdueWrapper.ge("borrow_time", startDateTime);
        overdueWrapper.lt("borrow_time", endDateTime);
        overdueWrapper.gt("overdue_days", 0);
        Long overdueBorrows = borrowRecordMapper.selectCount(overdueWrapper);

        double overdueRate = totalBorrows > 0 ? (double) overdueBorrows / totalBorrows * 100 : 0;

        Map<String, Object> monthlyData = new LinkedHashMap<>();
        monthlyData.put("统计周期", startOfMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
        monthlyData.put("本月借阅总次数", totalBorrows);
        monthlyData.put("本月新增图书", newBooks);
        monthlyData.put("本月新增读者", newReaders);
        monthlyData.put("本月逾期次数", overdueBorrows);
        monthlyData.put("本月逾期率", String.format("%.2f%%", overdueRate));

        String systemPrompt = "你是图书馆月度报告撰写专家。请根据以下汇总数据，生成一份完整的月度分析报告，包含：概述、数据分析、问题发现、改进建议、下月计划。使用 Markdown 格式，层级清晰，语言正式。";
        String reportContent;
        try {
            String userMessage = "月度统计数据：" + objectMapper.writeValueAsString(monthlyData);
            reportContent = deepSeekClient.chat(systemPrompt, userMessage);
        } catch (Exception e) {
            log.warn("Failed to serialize monthly data for AI report: {}", e.getMessage());
            reportContent = "报告生成失败，请稍后重试。";
        }

        AiReport report = new AiReport();
        report.setReportType("monthly");
        String reportTitle = now.format(DateTimeFormatter.ofPattern("yyyy年MM月")) + "数据分析报告";
        report.setTitle(reportTitle);
        report.setReportTitle(reportTitle);
        report.setReportContent(reportContent);
        report.setGenerateTime(LocalDateTime.now());
        report.setIsDeleted(0);

        save(report);

        try {
            redisCache.delete(CACHE_KEY_MONTHLY_REPORT);
        } catch (Exception e) {
            log.warn("Failed to clear monthly report cache: {}", e.getMessage());
        }

        return report;
    }

    @Override
    public AiReport getLatestMonthlyReport() {
        @SuppressWarnings("unchecked")
        AiReport cached = redisCache.get(CACHE_KEY_MONTHLY_REPORT, AiReport.class);
        if (cached != null) {
            log.debug("AI monthly report cache hit");
            return cached;
        }

        QueryWrapper<AiReport> wrapper = new QueryWrapper<>();
        wrapper.eq("report_type", "monthly");
        wrapper.eq("is_deleted", 0);
        wrapper.orderByDesc("generate_time");
        wrapper.last("LIMIT 1");
        AiReport report = getOne(wrapper);

        if (report != null) {
            try {
                redisCache.set(CACHE_KEY_MONTHLY_REPORT, report, CACHE_TTL_24H, TimeUnit.HOURS);
            } catch (Exception e) {
                log.warn("Failed to cache AI monthly report: {}", e.getMessage());
            }
        }

        return report;
    }

    private List<Map<String, Object>> parseRecommendResponse(String aiResponse, List<Book> candidates) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            String jsonStr = extractJsonArray(aiResponse);
            if (jsonStr != null) {
                List<Map<String, Object>> parsed = objectMapper.readValue(jsonStr,
                        new TypeReference<List<Map<String, Object>>>() {});
                for (Map<String, Object> item : parsed) {
                    Object bookIdObj = item.get("bookId");
                    if (bookIdObj != null) {
                        Long bookId = ((Number) bookIdObj).longValue();
                        Book book = candidates.stream()
                                .filter(b -> b.getId().equals(bookId))
                                .findFirst()
                                .orElse(null);
                        if (book != null) {
                            Map<String, Object> recommend = new LinkedHashMap<>();
                            recommend.put("id", bookId);
                            recommend.put("title", book.getTitle());
                            recommend.put("author", book.getAuthor());
                            recommend.put("cover", book.getCoverUrl());
                            recommend.put("reason", item.get("reason"));
                            result.add(recommend);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse AI recommend response: {}", e.getMessage());
        }

        if (result.isEmpty() && !candidates.isEmpty()) {
            for (int i = 0; i < Math.min(3, candidates.size()); i++) {
                Book book = candidates.get(i);
                Map<String, Object> recommend = new LinkedHashMap<>();
                recommend.put("id", book.getId());
                recommend.put("title", book.getTitle());
                recommend.put("author", book.getAuthor());
                recommend.put("cover", book.getCoverUrl());
                recommend.put("reason", "根据您的阅读偏好推荐");
                result.add(recommend);
            }
        }

        return result;
    }

    private String extractJsonArray(String text) {
        if (text == null) return null;
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return null;
    }
}
