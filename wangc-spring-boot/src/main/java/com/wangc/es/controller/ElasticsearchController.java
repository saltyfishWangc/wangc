package com.wangc.es.controller;

import com.wangc.es.entity.DBBook;
import com.wangc.model.RestResult;
import com.wangc.es.service.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * @author
 * @Description: Elasticsearch控制器
 * @date 2022/8/31 20:53
 */
@Api(tags = "ElasticSearch控制器")
@RestController
@RequestMapping("/es/")
public class ElasticsearchController {

    @Autowired
    ElasticsearchService elasticsearchService;

    /**
     * 新增索引
     *
     * @param index
     * @return
     */
    @ApiOperation("新增索引")
    @PostMapping("index")
    public RestResult<?> createIndex(@RequestParam(name = "index") String index) {
        return RestResult.ok(elasticsearchService.createIndex(index));
    }

    /**
     * 索引是否存在
     *
     * @param index index
     * @return RestResult
     */
    @ApiOperation("索引是否存在")
    @GetMapping("index/{index}")
    public RestResult<?> existIndex(@PathVariable String index) {
        return RestResult.ok(elasticsearchService.isIndexExist(index));
    }

    /**
     * 删除索引
     *
     * @param index index
     * @return RestResult
     */
    @ApiOperation("删除索引")
    @DeleteMapping("index/{index}")
    public RestResult<?> deleteIndex(@PathVariable String index) {
        return RestResult.ok(elasticsearchService.deleteIndex(index));
    }


    /**
     * 新增/更新数据
     *
     * @param entity 数据
     * @param index  索引
     * @param esId   esId
     * @return RestResult
     */
    @ApiOperation("新增/更新数据")
    @PostMapping("data")
    public RestResult<String> submitData(@RequestBody DBBook entity, String index, String esId) {
        return RestResult.ok(elasticsearchService.submitData(entity, index, esId));
    }

    /**
     * 通过id删除数据
     *
     * @param index index
     * @param id    id
     * @return RestResult
     */
    @ApiOperation("通过id删除数据")
    @DeleteMapping("data/{index}/{id}")
    public RestResult<String> deleteDataById(@PathVariable String index, @PathVariable String id) {
        return RestResult.ok(elasticsearchService.deleteDataById(index, id));
    }

    /**
     * 通过id查询数据
     *
     * @param index  index
     * @param id     id
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return RestResult
     */
    @ApiOperation("通过id查询数据")
    @GetMapping("data")
    public RestResult<Map<String, Object>> searchDataById(String index, String id, String fields) {
        return RestResult.ok(elasticsearchService.searchDataById(index, id, fields));
    }

    /**
     * 分页查询（这只是一个demo）
     *
     * @param index index
     * @return RestResult
     */
    @ApiOperation("分页查询")
    @GetMapping("data/page")
    public RestResult<?> selectPage(String index) {
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //精确查询
        //boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", "张三"));
        // 模糊查询
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("name", "张"));
        // 范围查询 from:相当于闭区间; gt:相当于开区间(>) gte:相当于闭区间 (>=) lt:开区间(<) lte:闭区间 (<=)
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("age").from(18).to(32));
        SearchSourceBuilder query = new SearchSourceBuilder();
        query.query(boolQueryBuilder);
        //需要查询的字段，缺省则查询全部
        String fields = "";
        //需要高亮显示的字段
        String highlightField = "name";
        if (Strings.hasLength(fields)) {
            //只查询特定字段。如果需要查询所有字段则不设置该项。
            query.fetchSource(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        //分页参数，相当于pageNum
        Integer from = 0;
        //分页参数，相当于pageSize
        Integer size = 2;
        //设置分页参数
        query.from(from);
        query.size(size);

        //设置排序字段和排序方式，注意：字段是text类型需要拼接.keyword
        //query.sort("age", SortOrder.DESC);
        query.sort("name" + ".keyword", SortOrder.ASC);

        return RestResult.ok(elasticsearchService.searchListDataPage(index, query, highlightField));
    }
}
