package com.library.smart.service;

import com.library.smart.entity.AiReport;

import java.util.List;
import java.util.Map;

public interface AiService {

    Map<String, Object> getTrendData(Integer days);

    String getTrendAnalysis(Integer days);

    Map<String, Object> getRankingData(Integer limit);

    String getRankingInsight(Integer limit);

    Map<String, Object> getHotBooks(Integer limit);

    Map<String, Object> getCategoryData();

    String getCategoryPortrait();

    Map<String, Object> getCategoryPortraitCombined();

    List<Map<String, Object>> getRecommendations(Long userId);

    AiReport generateMonthlyReport();

    AiReport getLatestMonthlyReport();
}
