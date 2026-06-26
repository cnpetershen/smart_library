package com.library.smart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.library.smart.common.PageResult;
import com.library.smart.dto.BorrowDTO;
import com.library.smart.dto.BorrowQueryDTO;
import com.library.smart.dto.ReturnDTO;
import com.library.smart.entity.BorrowRecord;
import com.library.smart.vo.BorrowRecordVO;

import java.util.List;

public interface BorrowService extends IService<BorrowRecord> {

    String borrowBook(BorrowDTO borrowDTO);

    String returnBook(ReturnDTO returnDTO);

    PageResult<BorrowRecordVO> listBorrows(BorrowQueryDTO queryDTO);

    List<BorrowRecordVO> getOverdueList();

    Long getTodayBorrowCount();
}
