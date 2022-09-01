package com.wangc.es.service;

import com.wangc.es.entity.DBBook;
import com.wangc.es.entity.ESBook;
import org.elasticsearch.search.SearchHits;

import java.util.List;

public interface BookRepositoryService {

    void addBook(DBBook DBBook);

    List<ESBook> searchBook(String keyword);

    SearchHits searchBookTitle(String keyword);

    List<DBBook> searchBookFromMysql(String key);
}
