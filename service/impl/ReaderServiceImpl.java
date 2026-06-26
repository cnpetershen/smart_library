package com.library.smart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.common.BusinessException;
import com.library.smart.common.PageResult;
import com.library.smart.dto.ReaderFormDTO;
import com.library.smart.dto.ReaderQueryDTO;
import com.library.smart.entity.Book;
import com.library.smart.entity.BorrowRecord;
import com.library.smart.entity.User;
import com.library.smart.mapper.BookMapper;
import com.library.smart.mapper.BorrowRecordMapper;
import com.library.smart.mapper.UserMapper;
import com.library.smart.service.ReaderService;
import com.library.smart.utils.RedisCache;
import com.library.smart.vo.BorrowHistoryVO;
import com.library.smart.vo.ReaderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReaderServiceImpl extends ServiceImpl<UserMapper, User> implements ReaderService {

    private final UserMapper userMapper;
    private final BorrowRecordMapper borrowRecordMapper;
    private final BookMapper bookMapper;
    private final RedisCache redisCache;
    private final PasswordEncoder passwordEncoder;

    private static final String CACHE_KEY_LIST = "smart:reader:list";
    private static final long CACHE_TTL = 10;

    @Override
    public PageResult<ReaderVO> listReaders(ReaderQueryDTO queryDTO) {
        String cacheKey = buildCacheKey(queryDTO);
        PageResult<ReaderVO> cached = redisCache.get(cacheKey, PageResult.class);
        if (cached != null) {
            log.debug("Reader list cache hit: {}", cacheKey);
            return cached;
        }

        Page<User> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        QueryWrapper<User> wrapper = buildQueryWrapper(queryDTO);

        IPage<User> pageResult = page(page, wrapper);
        List<ReaderVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        PageResult<ReaderVO> result = PageResult.of(
                pageResult.getTotal(),
                voList,
                (long) queryDTO.getPage(),
                (long) queryDTO.getPageSize()
        );

        redisCache.set(cacheKey, result, CACHE_TTL, TimeUnit.MINUTES);
        log.debug("Reader list cached: {}", cacheKey);
        return result;
    }

    @Override
    public ReaderVO getReaderById(Long id) {
        User user = getById(id);
        if (user == null || user.getIsDeleted() == 1) {
            throw new BusinessException(404, "读者不存在");
        }
        if (!"reader".equals(user.getRole())) {
            throw new BusinessException(404, "该用户不是读者");
        }
        return convertToVO(user);
    }

    @Override
    public List<BorrowHistoryVO> getBorrowHistory(Long readerId) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", readerId);
        wrapper.orderByDesc("borrow_time");

        List<BorrowRecord> records = borrowRecordMapper.selectList(wrapper);

        return records.stream().map(record -> {
            BorrowHistoryVO vo = new BorrowHistoryVO();
            vo.setId(record.getId());
            vo.setBookId(record.getBookId());
            vo.setBorrowTime(record.getBorrowTime());
            vo.setExpectedReturnTime(record.getExpectedReturnTime());
            vo.setActualReturnTime(record.getActualReturnTime());
            vo.setOverdueDays(record.getOverdueDays());
            vo.setStatus(record.getStatus());
            vo.setStatusText(BorrowHistoryVO.getStatusText(record.getStatus()));

            Book book = bookMapper.selectById(record.getBookId());
            if (book != null) {
                vo.setBookTitle(book.getTitle());
            }
            return vo;
        }).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReader(ReaderFormDTO readerFormDTO) {
        if (isAccountExists(readerFormDTO.getAccount(), null)) {
            throw new BusinessException(400, "账号已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(readerFormDTO, user);
        user.setRole("reader");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setStatus(1);
        user.setRegisterTime(LocalDateTime.now());

        save(user);
        clearListCache();
        log.info("Reader added: id={}, account={}", user.getId(), user.getAccount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReader(Long id, ReaderFormDTO readerFormDTO) {
        User existingUser = getById(id);
        if (existingUser == null || existingUser.getIsDeleted() == 1) {
            throw new BusinessException(404, "读者不存在");
        }
        if (!"reader".equals(existingUser.getRole())) {
            throw new BusinessException(400, "该用户不是读者");
        }

        if (StringUtils.hasText(readerFormDTO.getName())) {
            existingUser.setName(readerFormDTO.getName());
        }
        if (readerFormDTO.getGender() != null) {
            existingUser.setGender(readerFormDTO.getGender());
        }
        if (StringUtils.hasText(readerFormDTO.getPhone())) {
            existingUser.setPhone(readerFormDTO.getPhone());
        }

        updateById(existingUser);
        clearListCache();
        log.info("Reader updated: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id) {
        User user = getById(id);
        if (user == null || user.getIsDeleted() == 1) {
            throw new BusinessException(404, "读者不存在");
        }
        if (!"reader".equals(user.getRole())) {
            throw new BusinessException(400, "该用户不是读者");
        }

        if (user.getStatus() == 1) {
            int borrowingCount = countBorrowing(user.getId());
            if (borrowingCount > 0) {
                throw new BusinessException(400, "该读者有未归还图书，无法禁用");
            }
            user.setStatus(0);
        } else {
            user.setStatus(1);
        }

        updateById(user);
        clearListCache();
        log.info("Reader status toggled: id={}, newStatus={}", id, user.getStatus());
    }

    private QueryWrapper<User> buildQueryWrapper(ReaderQueryDTO queryDTO) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role", "reader");

        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like("account", queryDTO.getKeyword())
                    .or().like("name", queryDTO.getKeyword()));
        }

        if (queryDTO.getStatus() != null) {
            wrapper.eq("status", queryDTO.getStatus());
        }

        if (StringUtils.hasText(queryDTO.getGender())) {
            wrapper.eq("gender", queryDTO.getGender());
        }

        String sortField = switch (queryDTO.getSortField()) {
            case "registerTime" -> "register_time";
            default -> "register_time";
        };

        wrapper.orderBy(true, "asc".equalsIgnoreCase(queryDTO.getSortOrder()), sortField);

        wrapper.select("id", "account", "name", "role", "gender", "phone", "email", "avatar", "status", "register_time");

        return wrapper;
    }

    private ReaderVO convertToVO(User user) {
        ReaderVO vo = new ReaderVO();
        BeanUtils.copyProperties(user, vo);
        vo.setStatusText(ReaderVO.getStatusText(user.getStatus()));

        int borrowCount = countBorrowing(user.getId());
        int overdueCount = countOverdue(user.getId());
        vo.setBorrowCount(borrowCount);
        vo.setOverdueCount(overdueCount);

        return vo;
    }

    private int countBorrowing(Long userId) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", 0);
        return Math.toIntExact(borrowRecordMapper.selectCount(wrapper));
    }

    private int countOverdue(Long userId) {
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.gt("overdue_days", 0);
        return Math.toIntExact(borrowRecordMapper.selectCount(wrapper));
    }

    private boolean isAccountExists(String account, Long excludeId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account);
        wrapper.eq("is_deleted", 0);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return count(wrapper) > 0;
    }

    private String buildCacheKey(ReaderQueryDTO queryDTO) {
        return String.format("%s:keyword:%s:status:%s:gender:%s:page:%d:size:%d",
                CACHE_KEY_LIST,
                queryDTO.getKeyword() != null ? queryDTO.getKeyword() : "",
                queryDTO.getStatus() != null ? queryDTO.getStatus() : "",
                queryDTO.getGender() != null ? queryDTO.getGender() : "",
                queryDTO.getPage(),
                queryDTO.getPageSize()
        );
    }

    private void clearListCache() {
        redisCache.deletePattern(CACHE_KEY_LIST + ":*");
    }

    @Override
    public List<ReaderVO> searchReaders(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role", "reader");
        wrapper.eq("status", 1);
        wrapper.eq("is_deleted", 0);
        wrapper.and(w -> w.like("account", keyword)
                .or().like("name", keyword));
        wrapper.last("LIMIT 20");
        List<User> users = list(wrapper);
        return users.stream().map(this::convertToVO).collect(Collectors.toList());
    }
}
