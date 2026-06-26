package com.library.smart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.library.smart.common.BusinessException;
import com.library.smart.common.PageResult;
import com.library.smart.constant.CacheKey;
import com.library.smart.dto.BookFormDTO;
import com.library.smart.dto.BookQueryDTO;
import com.library.smart.entity.Book;
import com.library.smart.mapper.BookMapper;
import com.library.smart.service.BookService;
import com.library.smart.service.FileStorageService;
import com.library.smart.utils.RedisCache;
import com.library.smart.vo.BookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final RedisCache redisCache;
    private final FileStorageService fileStorageService;

    private static final String COVER_URL_PREFIX = "/uploads/";
    private static final long DETAIL_CACHE_TTL = 30;

    @Override
    public PageResult<BookVO> listBooks(BookQueryDTO queryDTO) {
        String cacheKey = buildListCacheKey(queryDTO);
        PageResult<BookVO> cached = redisCache.get(cacheKey, PageResult.class);
        if (cached != null) {
            log.debug("Book list cache hit: {}", cacheKey);
            return cached;
        }

        Page<Book> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        QueryWrapper<Book> wrapper = buildQueryWrapper(queryDTO);

        IPage<Book> pageResult = page(page, wrapper);
        List<BookVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        PageResult<BookVO> result = PageResult.of(
                pageResult.getTotal(),
                voList,
                (long) queryDTO.getPage(),
                (long) queryDTO.getPageSize()
        );

        redisCache.set(cacheKey, result);
        log.debug("Book list cached: {}", cacheKey);
        return result;
    }

    @Override
    public BookVO getBookById(Long id) {
        String cacheKey = CacheKey.BOOK_DETAIL.format(id);
        BookVO cached = redisCache.get(cacheKey, BookVO.class);
        if (cached != null) {
            log.debug("Book detail cache hit: {}", cacheKey);
            return cached;
        }

        Book book = getById(id);
        if (book == null || book.getIsDeleted() == 1) {
            throw new BusinessException(404, "图书不存在");
        }

        BookVO vo = convertToVO(book);
        redisCache.set(cacheKey, vo, DETAIL_CACHE_TTL, TimeUnit.MINUTES);
        log.debug("Book detail cached: {}", cacheKey);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBook(BookFormDTO bookFormDTO) {
        validateCoverUrl(bookFormDTO.getCoverUrl());

        if (StringUtils.hasText(bookFormDTO.getIsbn())) {
            bookFormDTO.setIsbn(bookFormDTO.getIsbn().replaceAll("-", ""));
        }

        if (isIsbnExists(bookFormDTO.getIsbn(), null)) {
            throw new BusinessException(400, "ISBN已存在");
        }

        Book book = new Book();
        BeanUtils.copyProperties(bookFormDTO, book);

        if (StringUtils.hasText(bookFormDTO.getPublishDate())) {
            book.setPublishDate(LocalDate.parse(bookFormDTO.getPublishDate()));
        }

        book.setTotalStock(bookFormDTO.getTotalStock());
        book.setAvailableStock(bookFormDTO.getTotalStock());
        book.setStatus(0);
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());

        save(book);
        clearListCache();
        log.info("Book added: id={}, title={}", book.getId(), book.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(Long id, BookFormDTO bookFormDTO) {
        Book existingBook = getById(id);
        if (existingBook == null || existingBook.getIsDeleted() == 1) {
            throw new BusinessException(404, "图书不存在");
        }

        if (StringUtils.hasText(bookFormDTO.getCoverUrl())
                && !bookFormDTO.getCoverUrl().startsWith(COVER_URL_PREFIX)) {
            throw new BusinessException(400, "封面路径必须以/uploads/开头");
        }

        if (StringUtils.hasText(bookFormDTO.getIsbn())) {
            bookFormDTO.setIsbn(bookFormDTO.getIsbn().replaceAll("-", ""));
        }

        if (StringUtils.hasText(bookFormDTO.getIsbn())
                && isIsbnExists(bookFormDTO.getIsbn(), id)) {
            throw new BusinessException(400, "ISBN已存在");
        }

        if (StringUtils.hasText(bookFormDTO.getTitle())) {
            existingBook.setTitle(bookFormDTO.getTitle());
        }
        if (StringUtils.hasText(bookFormDTO.getAuthor())) {
            existingBook.setAuthor(bookFormDTO.getAuthor());
        }
        if (StringUtils.hasText(bookFormDTO.getIsbn())) {
            existingBook.setIsbn(bookFormDTO.getIsbn());
        }
        if (StringUtils.hasText(bookFormDTO.getCategory())) {
            existingBook.setCategory(bookFormDTO.getCategory());
        }
        if (StringUtils.hasText(bookFormDTO.getPublishDate())) {
            existingBook.setPublishDate(LocalDate.parse(bookFormDTO.getPublishDate()));
        }
        if (StringUtils.hasText(bookFormDTO.getCoverUrl())) {
            existingBook.setCoverUrl(bookFormDTO.getCoverUrl());
        }
        if (StringUtils.hasText(bookFormDTO.getDescription())) {
            existingBook.setDescription(bookFormDTO.getDescription());
        }

        existingBook.setUpdateTime(LocalDateTime.now());
        updateById(existingBook);

        clearDetailCache(id);
        clearListCache();
        log.info("Book updated: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long id) {
        Book book = getById(id);
        if (book == null || book.getIsDeleted() == 1) {
            throw new BusinessException(404, "图书不存在");
        }

        removeById(id);

        if (StringUtils.hasText(book.getCoverUrl())) {
            try {
                fileStorageService.deleteFile(book.getCoverUrl());
            } catch (Exception e) {
                log.warn("Failed to delete cover file: {}", book.getCoverUrl(), e);
            }
        }

        clearDetailCache(id);
        clearListCache();
        log.info("Book deleted: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteBook(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<Book> books = listByIds(ids);
        for (Book book : books) {
            if (StringUtils.hasText(book.getCoverUrl())) {
                try {
                    fileStorageService.deleteFile(book.getCoverUrl());
                } catch (Exception e) {
                    log.warn("Failed to delete cover file: {}", book.getCoverUrl(), e);
                }
            }
        }

        removeByIds(ids);
        clearListCache();
        log.info("Batch books deleted: count={}", ids.size());
    }

    @Override
    public void importBooks(List<BookFormDTO> bookFormDTOS) {
        throw new BusinessException(500, "功能开发中");
    }

    private QueryWrapper<Book> buildQueryWrapper(BookQueryDTO queryDTO) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like("title", queryDTO.getKeyword())
                    .or().like("author", queryDTO.getKeyword())
                    .or().like("isbn", queryDTO.getKeyword()));
        }

        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq("category", queryDTO.getCategory());
        }

        if (queryDTO.getStatus() != null) {
            wrapper.eq("status", queryDTO.getStatus());
        }

        String sortField = queryDTO.getSortField();
        String sortOrder = queryDTO.getSortOrder();

        if ("publishDate".equals(sortField)) {
            sortField = "publish_date";
        } else if ("totalStock".equals(sortField)) {
            sortField = "total_stock";
        } else {
            sortField = "create_time";
        }

        wrapper.orderBy(true, "asc".equalsIgnoreCase(sortOrder), sortField);
        wrapper.orderByDesc("create_time");

        return wrapper;
    }

    private String buildListCacheKey(BookQueryDTO queryDTO) {
        return String.format("smart:book:list:keyword:%s:category:%s:status:%s:page:%d:size:%d:sort:%s:order:%s",
                queryDTO.getKeyword() != null ? queryDTO.getKeyword() : "",
                queryDTO.getCategory() != null ? queryDTO.getCategory() : "",
                queryDTO.getStatus() != null ? queryDTO.getStatus() : "",
                queryDTO.getPage(),
                queryDTO.getPageSize(),
                queryDTO.getSortField(),
                queryDTO.getSortOrder()
        );
    }

    private BookVO convertToVO(Book book) {
        BookVO vo = new BookVO();
        BeanUtils.copyProperties(book, vo);
        vo.setStatusText(BookVO.getStatusText(book.getStatus()));
        return vo;
    }

    private boolean isIsbnExists(String isbn, Long excludeId) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("isbn", isbn);
        wrapper.eq("is_deleted", 0);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return count(wrapper) > 0;
    }

    private void validateCoverUrl(String coverUrl) {
        if (StringUtils.hasText(coverUrl) && !coverUrl.startsWith(COVER_URL_PREFIX)) {
            throw new BusinessException(400, "封面路径必须以/uploads/开头");
        }
    }

    private void clearListCache() {
        redisCache.deletePattern("smart:book:list:*");
        redisCache.deletePattern("smart:ai:*");
    }

    private void clearDetailCache(Long id) {
        redisCache.delete(CacheKey.BOOK_DETAIL.format(id));
    }

    @Override
    public List<BookVO> searchBooks(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0);
        wrapper.and(w -> w.like("title", keyword)
                .or().like("author", keyword));
        wrapper.last("LIMIT 20");
        List<Book> books = list(wrapper);
        return books.stream().map(this::convertToVO).collect(Collectors.toList());
    }
}
