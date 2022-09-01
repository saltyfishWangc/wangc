package com.wangc.es.service.impl;

import com.wangc.es.entity.DBBook;
import com.wangc.es.entity.ESBook;
import com.wangc.es.repository.BookRepository;
import com.wangc.es.repository.ESBookRepository;
import com.wangc.es.service.BookRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author
 * @Description:
 * @date 2022/9/1 17:16
 */
@Slf4j
@Service("bookRepositoryService")
public class BookRepositoryServiceImpl implements BookRepositoryService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ESBookRepository esBookRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Override
    public void addBook(DBBook book) {
        final DBBook saveESBook = transactionTemplate.execute((status) ->
                bookRepository.save(book)
        );
        final ESBook esBook = new ESBook();
        assert saveESBook != null;
        BeanUtils.copyProperties(saveESBook, esBook);
        esBook.setId(saveESBook.getId() + "");
        try {
            esBookRepository.save(esBook);
        } catch (Exception e) {
            log.error(String.format("保存ES错误！%s", e.getMessage()));
        }

    }

    @Override
    public List<ESBook> searchBook(String keyword) {
        return esBookRepository.findByTitleOrAuthor(keyword, keyword);
    }

    @Override
    public SearchHits searchBookTitle(String keyword) {
        return esBookRepository.find(keyword);
    }

    public List<DBBook> searchBookFromMysql(String key) {
        return bookRepository.findBookByAuthorOrTitle(key, key);
    }
}
