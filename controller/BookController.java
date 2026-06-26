package com.library.smart.controller;

import com.library.smart.common.PageResult;
import com.library.smart.common.Result;
import com.library.smart.dto.BookFormDTO;
import com.library.smart.dto.BookQueryDTO;
import com.library.smart.service.BookService;
import com.library.smart.vo.BookVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Result<PageResult<BookVO>> listBooks(BookQueryDTO queryDTO) {
        if (queryDTO.getPage() == null || queryDTO.getPage() < 1) {
            queryDTO.setPage(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(10);
        }
        if (queryDTO.getPageSize() > 100) {
            queryDTO.setPageSize(100);
        }
        PageResult<BookVO> result = bookService.listBooks(queryDTO);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<BookVO> getBookById(@PathVariable Long id) {
        BookVO book = bookService.getBookById(id);
        return Result.success(book);
    }

    @PostMapping
    public Result<Void> addBook(@Valid @RequestBody BookFormDTO bookFormDTO) {
        bookService.addBook(bookFormDTO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateBook(@PathVariable Long id, @Valid @RequestBody BookFormDTO bookFormDTO) {
        bookService.updateBook(id, bookFormDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Result.success();
    }

    @PostMapping("/batch-delete")
    public Result<Void> batchDeleteBook(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        bookService.batchDeleteBook(ids);
        return Result.success();
    }

    @PostMapping("/import")
    public Result<Void> importBooks(@RequestBody List<BookFormDTO> bookFormDTOS) {
        bookService.importBooks(bookFormDTOS);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<List<BookVO>> searchBooks(@RequestParam String keyword) {
        List<BookVO> books = bookService.searchBooks(keyword);
        return Result.success(books);
    }
}
