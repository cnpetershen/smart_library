package com.library.smart.controller;

import com.library.smart.common.Result;
import com.library.smart.entity.AiReport;
import com.library.smart.security.LoginUser;
import com.library.smart.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/trend")
    public Result<Map<String, Object>> getTrendData(@RequestParam(defaultValue = "30") Integer days) {
        Map<String, Object> result = aiService.getTrendData(days);
        return Result.success(result);
    }

    @GetMapping("/trend/analysis")
    public Result<Map<String, String>> getTrendAnalysis(@RequestParam(defaultValue = "30") Integer days) {
        String analysis = aiService.getTrendAnalysis(days);
        return Result.success(Map.of("analysis", analysis));
    }

    @GetMapping("/ranking")
    public Result<Map<String, Object>> getRankingData(@RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> result = aiService.getRankingData(limit);
        return Result.success(result);
    }

    @GetMapping("/ranking/insight")
    public Result<Map<String, String>> getRankingInsight(@RequestParam(defaultValue = "10") Integer limit) {
        String insight = aiService.getRankingInsight(limit);
        return Result.success(Map.of("insight", insight));
    }

    @GetMapping("/hot-books")
    public Result<Map<String, Object>> getHotBooks(@RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> result = aiService.getHotBooks(limit);
        return Result.success(result);
    }

    @GetMapping("/category")
    public Result<Map<String, Object>> getCategoryData() {
        Map<String, Object> result = aiService.getCategoryData();
        return Result.success(result);
    }

    @GetMapping("/category/portrait")
    public Result<Map<String, String>> getCategoryPortrait() {
        String portrait = aiService.getCategoryPortrait();
        return Result.success(Map.of("portrait", portrait));
    }

    @GetMapping("/portrait")
    public Result<Map<String, Object>> getPortrait() {
        Map<String, Object> result = aiService.getCategoryPortraitCombined();
        return Result.success(result);
    }

    @GetMapping("/user-portrait")
    public Result<Map<String, Object>> getUserPortrait() {
        return getPortrait();
    }

    @GetMapping("/recommend")
    public Result<List<Map<String, Object>>> getRecommendations(
            @AuthenticationPrincipal LoginUser loginUser) {
        List<Map<String, Object>> result = aiService.getRecommendations(loginUser.getUserId());
        return Result.success(result);
    }

    @GetMapping("/recommend/personal")
    public Result<List<Map<String, Object>>> getPersonalRecommendations(
            @AuthenticationPrincipal LoginUser loginUser) {
        return getRecommendations(loginUser);
    }

    @PostMapping("/report/generate")
    public Result<AiReport> generateReport() {
        AiReport report = aiService.generateMonthlyReport();
        return Result.success(report);
    }

    @GetMapping("/report")
    public Result<AiReport> getReport() {
        AiReport report = aiService.getLatestMonthlyReport();
        return Result.success(report);
    }

    @GetMapping("/report/monthly")
    public Result<AiReport> getMonthlyReport() {
        return getReport();
    }
}
