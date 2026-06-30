package com.library.smart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.smart.common.PageResult;
import com.library.smart.dto.ReaderFormDTO;
import com.library.smart.dto.ReaderQueryDTO;
import com.library.smart.entity.User;
import com.library.smart.vo.BorrowHistoryVO;
import com.library.smart.vo.ReaderVO;

import java.util.List;

public interface ReaderService extends IService<User> {

    PageResult<ReaderVO> listReaders(ReaderQueryDTO queryDTO);

    ReaderVO getReaderById(Long id);

    List<BorrowHistoryVO> getBorrowHistory(Long readerId);

    void addReader(ReaderFormDTO readerFormDTO);

    void updateReader(Long id, ReaderFormDTO readerFormDTO);

    void toggleStatus(Long id);

    List<ReaderVO> searchReaders(String keyword);
}
