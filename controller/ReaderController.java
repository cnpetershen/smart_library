package com.library.smart.controller;

import com.library.smart.common.PageResult;
import com.library.smart.common.Result;
import com.library.smart.dto.ReaderFormDTO;
import com.library.smart.dto.ReaderQueryDTO;
import com.library.smart.service.ReaderService;
import com.library.smart.vo.BorrowHistoryVO;
import com.library.smart.vo.ReaderVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reader")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping("/list")
    public Result<PageResult<ReaderVO>> listReaders(ReaderQueryDTO queryDTO) {
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }
        PageResult<ReaderVO> result = readerService.listReaders(queryDTO);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ReaderVO> getReaderById(@PathVariable Long id) {
        ReaderVO reader = readerService.getReaderById(id);
        return Result.success(reader);
    }

    @GetMapping("/{id}/borrow-history")
    public Result<List<BorrowHistoryVO>> getBorrowHistory(@PathVariable Long id) {
        List<BorrowHistoryVO> history = readerService.getBorrowHistory(id);
        return Result.success(history);
    }

    @PostMapping
    public Result<Void> addReader(@Valid @RequestBody ReaderFormDTO readerFormDTO) {
        readerService.addReader(readerFormDTO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateReader(@PathVariable Long id, @Valid @RequestBody ReaderFormDTO readerFormDTO) {
        readerService.updateReader(id, readerFormDTO);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        readerService.toggleStatus(id);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<List<ReaderVO>> searchReaders(@RequestParam String keyword) {
        List<ReaderVO> readers = readerService.searchReaders(keyword);
        return Result.success(readers);
    }
}
