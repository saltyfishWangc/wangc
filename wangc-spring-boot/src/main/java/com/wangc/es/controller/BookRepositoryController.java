package com.wangc.es.controller;

import com.wangc.es.entity.DBBook;
import com.wangc.es.entity.ESBook;
import com.wangc.es.service.BookRepositoryService;
import com.wangc.model.RestResult;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @Description:
 * @date 2022/9/1 17:41
 */
@RestController
@RequestMapping("/es/repository")
public class BookRepositoryController {

    @Autowired
    BookRepositoryService bookRepositoryService;

    /**
     * 添加book，这里我直接使用了Entity，为了演示有点不规范！
     */
    @PostMapping("/book")
    public RestResult addBook(@RequestBody DBBook ESBook) {
        bookRepositoryService.addBook(ESBook);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "ok");
        return RestResult.ok(map);
    }

    /**
     * 从ES中搜索
     */
    @GetMapping("/book/search/es")
    public RestResult searchES(String key) {
        return RestResult.ok(bookRepositoryService.searchBook(key));
    }

    @GetMapping("/book/search")
    public RestResult search(String key) {
        return RestResult.ok(bookRepositoryService.searchBookTitle(key));
    }

    @GetMapping("/book/search/mysql")
    public RestResult test(String key) {
        return RestResult.ok(bookRepositoryService.searchBookFromMysql(key));
    }
}
