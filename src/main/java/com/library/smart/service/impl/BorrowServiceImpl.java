package com.library.smart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.common.BusinessException;
import com.library.smart.common.PageResult;
import com.library.smart.constant.CacheKey;
import com.library.smart.dto.BorrowDTO;
import com.library.smart.dto.BorrowQueryDTO;
import com.library.smart.dto.ReturnDTO;
import com.library.smart.entity.Book;
import com.library.smart.entity.BorrowRecord;
import com.library.smart.entity.User;
import com.library.smart.mapper.BookMapper;
import com.library.smart.mapper.BorrowRecordMapper;
import com.library.smart.mapper.UserMapper;
import com.library.smart.service.BorrowService;
import com.library.smart.utils.RedisCache;
import com.library.smart.vo.BorrowRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowService {

    private final BorrowRecordMapper borrowRecordMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;
    private final RedisCache redisCache;

    private static final int BORROW_DAYS = 30;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String borrowBook(BorrowDTO borrowDTO) {
        User reader = userMapper.selectById(borrowDTO.getUserId());
        if (reader == null || reader.getIsDeleted() == 1) {
            throw new BusinessException(400, "读者不存在");
        }
        if (!"reader".equals(reader.getRole())) {
            throw new BusinessException(400, "该用户不是读者");
        }
        if (reader.getStatus() == 0) {
            throw new BusinessException(400, "该读者账号已被禁用");
        }

        Book book = bookMapper.selectById(borrowDTO.getBookId());
        if (book == null || book.getIsDeleted() == 1) {
            throw new BusinessException(400, "图书不存在");
        }
        if (book.getAvailableStock() == null || book.getAvailableStock() <= 0) {
            throw new BusinessException(400, "该图书已借空");
        }

        BorrowRecord record = new BorrowRecord();
        record.setBookId(borrowDTO.getBookId());
        record.setUserId(borrowDTO.getUserId());
        record.setBorrowTime(LocalDateTime.now());
        record.setExpectedReturnTime(LocalDateTime.now().plusDays(BORROW_DAYS));
        record.setStatus(0);
        save(record);

        book.setAvailableStock(book.getAvailableStock() - 1);
        if (book.getAvailableStock() == 0) {
            book.setStatus(1);
        }
        bookMapper.updateById(book);

        clearHomeCache();
        clearBookDetailCache(book.getId());

        log.info("Book borrowed: recordId={}, bookId={}, userId={}", record.getId(), book.getId(), reader.getId());
        return reader.getName() + "成功借阅《" + book.getTitle() + "》";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String returnBook(ReturnDTO returnDTO) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", returnDTO.getBookId());
        wrapper.eq("user_id", returnDTO.getUserId());
        wrapper.eq("status", 0);

        BorrowRecord record = borrowRecordMapper.selectOne(wrapper);
        if (record == null) {
            throw new BusinessException(400, "未找到借阅记录");
        }

        record.setActualReturnTime(LocalDateTime.now());
        long overdueDays = 0;
        if (record.getActualReturnTime().isAfter(record.getExpectedReturnTime())) {
            overdueDays = ChronoUnit.DAYS.between(record.getExpectedReturnTime(), record.getActualReturnTime());
        }
        record.setOverdueDays((int) overdueDays);
        record.setStatus(1);
        borrowRecordMapper.updateById(record);

        Book book = bookMapper.selectById(returnDTO.getBookId());
        if (book != null) {
            book.setAvailableStock(book.getAvailableStock() + 1);
            if (book.getStatus() == 1) {
                book.setStatus(0);
            }
            bookMapper.updateById(book);
            clearBookDetailCache(book.getId());
        }

        clearHomeCache();

        log.info("Book returned: recordId={}, overdueDays={}", record.getId(), overdueDays);

        if (overdueDays > 0) {
            return "归还完成，已逾期" + overdueDays + "天，请提醒读者缴纳滞纳金";
        }
        return "归还完成";
    }

    @Override
    public PageResult<BorrowRecordVO> listBorrows(BorrowQueryDTO queryDTO) {
        Page<BorrowRecord> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        QueryWrapper<BorrowRecord> wrapper = buildQueryWrapper(queryDTO);

        IPage<BorrowRecord> pageResult = page(page, wrapper);
        List<BorrowRecordVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        return PageResult.of(
                pageResult.getTotal(),
                voList,
                (long) queryDTO.getPage(),
                (long) queryDTO.getPageSize()
        );
    }

    @Override
    public List<BorrowRecordVO> getOverdueList() {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.apply("NOW() > expected_return_time");

        List<BorrowRecord> records = borrowRecordMapper.selectList(wrapper);
        return records.stream().map(this::convertToVO).toList();
    }

    @Override
    public Long getTodayBorrowCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.ge("borrow_time", startOfDay);
        wrapper.lt("borrow_time", endOfDay);

        return count(wrapper);
    }

    private QueryWrapper<BorrowRecord> buildQueryWrapper(BorrowQueryDTO queryDTO) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like("book_id", queryDTO.getKeyword())
                    .or().like("user_id", queryDTO.getKeyword()));
        }

        if (queryDTO.getStatus() != null) {
            wrapper.eq("status", queryDTO.getStatus());
        }

        if (queryDTO.getOverdueFilter() != null) {
            if (queryDTO.getOverdueFilter() == 1) {
                wrapper.apply("NOW() > expected_return_time and status = 0");
            } else if (queryDTO.getOverdueFilter() == 2) {
                wrapper.apply("NOW() <= expected_return_time or status = 1");
            }
        }

        String sortField = switch (queryDTO.getSortField()) {
            case "borrowTime" -> "borrow_time";
            case "expectedReturnTime" -> "expected_return_time";
            default -> "borrow_time";
        };

        wrapper.orderBy(true, "asc".equalsIgnoreCase(queryDTO.getSortOrder()), sortField);

        return wrapper;
    }

    private BorrowRecordVO convertToVO(BorrowRecord record) {
        BorrowRecordVO vo = new BorrowRecordVO();
        vo.setId(record.getId());
        vo.setBookId(record.getBookId());
        vo.setUserId(record.getUserId());
        vo.setBorrowTime(record.getBorrowTime());
        vo.setExpectedReturnTime(record.getExpectedReturnTime());
        vo.setActualReturnTime(record.getActualReturnTime());
        vo.setOverdueDays(record.getOverdueDays());
        vo.setStatus(record.getStatus());
        vo.setStatusText(BorrowRecordVO.getStatusText(record.getStatus()));

        Book book = bookMapper.selectById(record.getBookId());
        if (book != null) {
            vo.setBookTitle(book.getTitle());
        }

        User user = userMapper.selectById(record.getUserId());
        if (user != null) {
            vo.setReaderName(user.getName());
        }

        vo.setOverdueStatus(vo.getOverdueStatus());

        return vo;
    }

    private void clearHomeCache() {
        redisCache.deletePattern("smart:home:*");
    }

    private void clearBookDetailCache(Long bookId) {
        redisCache.delete(CacheKey.BOOK_DETAIL.format(bookId));
        redisCache.deletePattern("smart:book:list:*");
    }
}
