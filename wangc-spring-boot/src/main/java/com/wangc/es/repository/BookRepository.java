package com.wangc.es.repository;

import com.wangc.es.entity.DBBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关系型数据库mysql Repository
 */
@Repository("bookRepository")
public interface BookRepository extends JpaRepository<DBBook, String> {

    /**
     * 这个方法将自动被转化成Elasticsearch Json查询语句
     * {
     *     "query" : {
     *         "bool" : {
     *             "must" : {
     *                 { "query_string" : { "query" : "?", "fields" : ["name"] } },
     *                 { "query_string" : { "query" : "?", "fields" : ["price"] } }
     *             }
     *         }
     *     }
     * }
     * @param author
     * @param title
     * @return
     */
    List<DBBook> findBookByAuthorOrTitle(String author, String title);

    /**
     * Failed to create query for method public abstract java.util.List com.wangc.es.repository.BookRepository.findBookByMsgOrName(java.lang.String,java.lang.String)! No property msg found for type DBBook!
     * 启动的时候就报错的，说明jpa在启动的时候就解析了方法名，如果对应实体类里面没有接口命中的字段，就会报错。
     * 所以，jpa是根据repository中方法名来写sql语句的
     * 定义了这种方法，启动时解析报错
     * @param author
     * @param title
     * @return
     */
//    List<DBBook> findBookByMsgOrName(String author, String title);
}
