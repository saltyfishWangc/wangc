package com.wangc.es.repository;

import com.wangc.es.entity.ESBook;
import org.elasticsearch.search.SearchHits;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ES存储 ESBook repository,定义Book与ES索引映射关系
 */
@Repository("esBookRepository")
public interface ESBookRepository extends ElasticsearchRepository<ESBook, String> {

    /**
     * 根据关键字查询title或author
     *
     * @param title
     * @param author
     * @return
     */
    List<ESBook> findByTitleOrAuthor(String title, String author);

//    @Highlight(fields = {
//            @HighlightField(name = "title"),
//            @HighlightField(name = "author")
//    })
    @Query("{\"match\":{\"title\":\"?0\"}}")
    SearchHits find(String keyword);
}
