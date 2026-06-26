package com.library.smart.controller;

import com.library.smart.common.PageResult;
import com.library.smart.common.Result;
import com.library.smart.dto.BorrowDTO;
import com.library.smart.dto.BorrowQueryDTO;
import com.library.smart.dto.ReturnDTO;
import com.library.smart.service.BorrowService;
import com.library.smart.vo.BorrowRecordVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public Result<String> borrowBook(@Valid @RequestBody BorrowDTO borrowDTO) {
        String message = borrowService.borrowBook(borrowDTO);
        return Result.success(message);
    }

    @PostMapping("/return")
    public Result<String> returnBook(@Valid @RequestBody ReturnDTO returnDTO) {
        String message = borrowService.returnBook(returnDTO);
        return Result.success(message);
    }

    @GetMapping("/list")
    public Result<PageResult<BorrowRecordVO>> listBorrows(BorrowQueryDTO queryDTO) {
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }
        PageResult<BorrowRecordVO> result = borrowService.listBorrows(queryDTO);
        return Result.success(result);
    }

    @GetMapping("/overdue")
    public Result<List<BorrowRecordVO>> getOverdueList() {
        List<BorrowRecordVO> overdueList = borrowService.getOverdueList();
        return Result.success(overdueList);
    }

    @GetMapping("/today")
    public Result<Long> getTodayBorrowCount() {
        Long count = borrowService.getTodayBorrowCount();
        return Result.success(count);
    }
}
