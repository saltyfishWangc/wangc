package com.wangc.es.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author
 * @Description:
 * @date 2022/8/31 19:41
 */
public interface ElasticsearchService {

    /**
     * 创建索引
     * @param index
     * @return
     */
    boolean createIndex(String index);

    /**
     * 判断索引是否存在
     * @param index
     * @return
     */
    boolean isIndexExist(String index);

    /**
     * 删除索引
     * @param index
     * @return
     */
    boolean deleteIndex(String index);

    /**
     * 新增/更新数据
     * @param object 要新增/更新的数据
     * @param index 索引(类似数据库)
     * @param id 数据ID
     * @return
     */
    String submitData(Object object, String index, String id);

    /**
     * 新增数据，自定义id
     * @param object 要增加的数据
     * @param index 索引(类似数据库)
     * @param id 数据ID,为null时由es随机生成
     * @return
     */
    String addData(Object object, String index, String id);

    /**
     * 添加数据，随机id
     * @param object
     * @param idex
     * @return
     */
    String addData(Object object, String idex);

    /**
     * 通过ID删除数据
     * @param index
     * @param id
     * @return
     */
    String deleteDataById(String index, String id);

    /**
     * 通过ID更新数据
     * @param object
     * @param index
     * @param id
     * @return
     */
    String updateDataById(Object object, String index, String id);

    /**
     * 实时性的通过ID更新数据
     * @param object
     * @param index
     * @param id
     * @return
     */
    String updateDataByIdRealTime(Object object, String index, String id);

    /**
     * 通过ID获取数据
     * @param index
     * @param id
     * @param fields 需要显示的字段，逗号分隔，缺省为全部字段
     * @return
     */
    Map<String, Object> searchDataById(String index, String id, String fields);

    /**
     * 通过ID判断文档是否存在
     * @param index
     * @param id
     * @return
     */
    boolean existsById(String index, String id);

    /**
     * 批量插入数据
     * @param index
     * @param objects
     * @return
     */
    boolean bulkPost(String index, List<?> objects);

    /**
     * 高亮结果集
     * @param searchResponse
     * @param highlightField
     * @return
     */
    List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField);

    /**
     * 查询并分页
     * @param index
     * @param query
     * @param highlightField
     * @return
     */
    List<Map<String, Object>> searchListDataPage(String index, SearchSourceBuilder query, String highlightField);

}
