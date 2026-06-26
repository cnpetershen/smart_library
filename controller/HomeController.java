package com.library.smart.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.library.smart.common.Result;
import com.library.smart.entity.Book;
import com.library.smart.entity.BorrowRecord;
import com.library.smart.entity.User;
import com.library.smart.mapper.BookMapper;
import com.library.smart.mapper.BorrowRecordMapper;
import com.library.smart.mapper.UserMapper;
import com.library.smart.utils.RedisCache;
import com.library.smart.vo.BorrowRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final BorrowRecordMapper borrowRecordMapper;
    private final RedisCache redisCache;

    private static final String CACHE_KEY_STAT = "smart:home:stat";
    private static final String CACHE_KEY_TREND = "smart:home:trend";
    private static final String CACHE_KEY_DYNAMICS = "smart:home:dynamics";
    private static final String CACHE_KEY_ALERTS = "smart:home:alerts";
    private static final long CACHE_TTL = 5;

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(CACHE_KEY_STAT, Map.class);
        if (cached != null) {
            return Result.success(cached);
        }

        Map<String, Object> stat = new LinkedHashMap<>();

        QueryWrapper<Book> bookWrapper = new QueryWrapper<>();
        bookWrapper.eq("is_deleted", 0);
        Long bookTotal = bookMapper.selectCount(bookWrapper);
        stat.put("bookTotal", bookTotal);

        QueryWrapper<BorrowRecord> borrowWrapper = new QueryWrapper<>();
        borrowWrapper.eq("status", 0);
        Long borrowCurrent = borrowRecordMapper.selectCount(borrowWrapper);
        stat.put("borrowCurrent", borrowCurrent);

        QueryWrapper<User> readerWrapper = new QueryWrapper<>();
        readerWrapper.eq("role", "reader");
        readerWrapper.eq("status", 1);
        readerWrapper.eq("is_deleted", 0);
        Long readerTotal = userMapper.selectCount(readerWrapper);
        stat.put("readerTotal", readerTotal);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        QueryWrapper<BorrowRecord> todayWrapper = new QueryWrapper<>();
        todayWrapper.ge("borrow_time", startOfDay);
        todayWrapper.lt("borrow_time", endOfDay);
        Long todayBorrow = borrowRecordMapper.selectCount(todayWrapper);
        stat.put("todayBorrow", todayBorrow);

        stat.put("trend", "+12%");
        stat.put("borrowTrend", "+5%");
        stat.put("readerTrend", "+8%");

        redisCache.set(CACHE_KEY_STAT, stat, CACHE_TTL, TimeUnit.MINUTES);
        return Result.success(stat);
    }

    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> getTrend() {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cached = redisCache.get(CACHE_KEY_TREND, List.class);
        if (cached != null) {
            return Result.success(cached);
        }

        List<Map<String, Object>> trend = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

            QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
            wrapper.ge("borrow_time", startOfDay);
            wrapper.lt("borrow_time", endOfDay);
            Long count = borrowRecordMapper.selectCount(wrapper);

            Map<String, Object> day = new LinkedHashMap<>();
            day.put("date", date.format(formatter));
            day.put("count", count);
            trend.add(day);
        }

        redisCache.set(CACHE_KEY_TREND, trend, CACHE_TTL, TimeUnit.MINUTES);
        return Result.success(trend);
    }

    @GetMapping("/dynamics")
    public Result<List<Map<String, Object>>> getDynamics() {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cached = redisCache.get(CACHE_KEY_DYNAMICS, List.class);
        if (cached != null) {
            return Result.success(cached);
        }

        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("borrow_time");
        wrapper.last("LIMIT 10");
        List<BorrowRecord> records = borrowRecordMapper.selectList(wrapper);

        List<Map<String, Object>> dynamics = records.stream().map(record -> {
            Map<String, Object> item = new LinkedHashMap<>();

            User user = userMapper.selectById(record.getUserId());
            item.put("readerName", user != null ? user.getName() : "未知");

            Book book = bookMapper.selectById(record.getBookId());
            item.put("bookTitle", book != null ? book.getTitle() : "未知");

            item.put("time", record.getBorrowTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            item.put("type", "borrow");

            return item;
        }).collect(Collectors.toList());

        redisCache.set(CACHE_KEY_DYNAMICS, dynamics, CACHE_TTL, TimeUnit.MINUTES);
        return Result.success(dynamics);
    }

    @GetMapping("/alerts")
    public Result<Map<String, Object>> getAlerts() {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = redisCache.get(CACHE_KEY_ALERTS, Map.class);
        if (cached != null) {
            return Result.success(cached);
        }

        Map<String, Object> alerts = new LinkedHashMap<>();

        QueryWrapper<BorrowRecord> overdueWrapper = new QueryWrapper<>();
        overdueWrapper.eq("status", 0);
        overdueWrapper.apply("NOW() > expected_return_time");
        Long overdueCount = borrowRecordMapper.selectCount(overdueWrapper);
        alerts.put("overdueCount", overdueCount);

        List<Map<String, Object>> overdueList = new ArrayList<>();
        if (overdueCount > 0) {
            List<BorrowRecord> overdueRecords = borrowRecordMapper.selectList(overdueWrapper.last("LIMIT 5"));
            for (BorrowRecord record : overdueRecords) {
                Map<String, Object> item = new LinkedHashMap<>();
                User user = userMapper.selectById(record.getUserId());
                Book book = bookMapper.selectById(record.getBookId());
                item.put("readerName", user != null ? user.getName() : "未知");
                item.put("bookTitle", book != null ? book.getTitle() : "未知");
                item.put("overdueDays", record.getOverdueDays());
                overdueList.add(item);
            }
        }
        alerts.put("overdueList", overdueList);

        LocalDateTime threeDaysLater = LocalDateTime.now().plusDays(3);
        QueryWrapper<BorrowRecord> comingWrapper = new QueryWrapper<>();
        comingWrapper.eq("status", 0);
        comingWrapper.apply("expected_return_time <= {0} and expected_return_time > NOW()", threeDaysLater);
        Long comingCount = borrowRecordMapper.selectCount(comingWrapper);
        alerts.put("comingCount", comingCount);

        List<Map<String, Object>> comingList = new ArrayList<>();
        if (comingCount > 0) {
            List<BorrowRecord> comingRecords = borrowRecordMapper.selectList(comingWrapper.last("LIMIT 5"));
            for (BorrowRecord record : comingRecords) {
                Map<String, Object> item = new LinkedHashMap<>();
                User user = userMapper.selectById(record.getUserId());
                Book book = bookMapper.selectById(record.getBookId());
                item.put("readerName", user != null ? user.getName() : "未知");
                item.put("bookTitle", book != null ? book.getTitle() : "未知");
                item.put("expectedReturnTime", record.getExpectedReturnTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")));
                comingList.add(item);
            }
        }
        alerts.put("comingList", comingList);

        redisCache.set(CACHE_KEY_ALERTS, alerts, CACHE_TTL, TimeUnit.MINUTES);
        return Result.success(alerts);
    }
}
