package com.library.smart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.smart.common.PageResult;
import com.library.smart.dto.BookFormDTO;
import com.library.smart.dto.BookQueryDTO;
import com.library.smart.entity.Book;
import com.library.smart.vo.BookVO;

import java.util.List;

public interface BookService extends IService<Book> {

    PageResult<BookVO> listBooks(BookQueryDTO queryDTO);

    BookVO getBookById(Long id);

    void addBook(BookFormDTO bookFormDTO);

    void updateBook(Long id, BookFormDTO bookFormDTO);

    void deleteBook(Long id);

    void batchDeleteBook(List<Long> ids);

    void importBooks(List<BookFormDTO> bookFormDTOS);

    List<BookVO> searchBooks(String keyword);
}
