package com.wangc.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.wangc.es.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author
 * @Description:
 * @date 2022/8/31 19:55
 */
@Slf4j
@Service("elasticsearchService")
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    RestHighLevelClient elasticsearchClient;

    @Override
    public boolean createIndex(String index) {
        if (isIndexExist(index)) {
            log.error("Index:{} is exists!", index);
            return false;
        }
        // 1.??????????????????
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 2.?????????????????????
        try {
            CreateIndexResponse response = elasticsearchClient.indices()
                    .create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("??????:{} ??????", index);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isIndexExist(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        try {
            return elasticsearchClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("??????:{}??????????????????", index);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteIndex(String index) {
        if (!isIndexExist(index)) {
            log.error("Index:{} is not exists!");
            return false;
        }
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        try {
            AcknowledgedResponse response = elasticsearchClient.indices().delete(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException e) {
            log.error("??????Index:{} ??????????????????", index);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String submitData(Object object, String index, String id) {
        if (!Strings.hasLength(id)) {
            return addData(object, index);
        }
        if (this.existsById(index, id)) {
            return this.updateDataByIdRealTime(object, index, id);
        } else {
            return addData(object, index, id);
        }
    }

    @Override
    public String addData(Object object, String index, String id) {
        if (!Strings.hasLength(id)) {
            return addData(object, index);
        }
        if (this.existsById(index, id)) {
            return this.updateDataByIdRealTime(object, index, id);
        }
        // ????????????
        IndexRequest request = new IndexRequest(index, FieldType.Text.name(), id);
        request.timeout(TimeValue.timeValueSeconds(1));
        // ?????????json???????????????
        request.source(JSON.toJSONString(object), XContentType.JSON);
        // ?????????????????????
        IndexResponse response = null;
        try {
            response = elasticsearchClient.index(request, RequestOptions.DEFAULT);
            log.info("?????????????????? ?????????:{}, response ??????:{}, id:{}", index, response.status().getStatus(), response.getId());
            return response.getId();
        } catch (IOException e) {
            log.error("???????????????:{}, id???:{}???????????????", index, id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String addData(Object object, String idex) {
        return addData(object, idex, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    @Override
    public String deleteDataById(String index, String id) {
        DeleteRequest request = new DeleteRequest(index, FieldType.Text.name(), id);
        DeleteResponse response = null;
        try {
            response = elasticsearchClient.delete(request, RequestOptions.DEFAULT);
            return response.getId();
        } catch (IOException e) {
            log.error("??????Index:{}, id:{}???????????????", index, id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String updateDataById(Object object, String index, String id) {
        UpdateRequest request = new UpdateRequest(index, FieldType.Text.name(), id);
        request.timeout("1s");
        request.doc(JSON.toJSONString(object), XContentType.JSON);
        try {
            UpdateResponse response = elasticsearchClient.update(request, RequestOptions.DEFAULT);
            return response.getId();
        } catch (IOException e) {
            log.error("??????Index:{}, Id:{} ???????????????", index, id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String updateDataByIdRealTime(Object object, String index, String id) {
        // ??????????????????
        UpdateRequest request = new UpdateRequest(index, FieldType.Text.name(), id);
        // ??????????????????
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        request.setRefreshPolicy("wait_for");
        request.timeout("1s");
        request.doc(JSON.toJSONString(object), XContentType.JSON);
        // ??????????????????
        try {
            UpdateResponse response = elasticsearchClient.update(request, RequestOptions.DEFAULT);
            log.info("????????????Index:{}, Id:{}???????????????", index, id);
            return response.getId();
        } catch (IOException e) {
            log.info("????????????Index:{}, Id:{}???????????????", index, id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> searchDataById(String index, String id, String fields) {
        GetRequest request = new GetRequest(index, FieldType.Text.name(), id);
        if (Strings.hasLength(fields)) {
            // ????????????????????????????????????????????????????????????????????????
            request.fetchSourceContext(new FetchSourceContext(true, fields.split(","), Strings.EMPTY_ARRAY));
        }
        try {
            GetResponse response = elasticsearchClient.get(request, RequestOptions.DEFAULT);
            return response.getSource();
        } catch (IOException e) {
            log.error("??????Index:{}, Id:{}?????????:{}??????", index, id, fields);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existsById(String index, String id) {
        GetRequest request = new GetRequest(index, FieldType.Text.name(), id);
        // ????????????????????????_source????????????
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        try {
            return elasticsearchClient.exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("??????Id:{}??????Index:{}???????????????????????????", id, index);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean bulkPost(String index, List<?> objects) {
        BulkRequest bulkequest = new BulkRequest();
        BulkResponse response = null;
        // ????????????????????????20w
        for (Object object : objects) {
            IndexRequest request = new IndexRequest(index);
            request.source(JSON.toJSONString(object), XContentType.JSON);
            bulkequest.add(request);
        }
        try {
            response = elasticsearchClient.bulk(bulkequest, RequestOptions.DEFAULT);
            return response.hasFailures();
        } catch (IOException e) {
            log.error("????????????Index:{}??????", index);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> high = hit.getHighlightFields();
            HighlightField title = high.get(highlightField);
            // ???????????????
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // ?????????????????????????????????????????????????????????
            if (title != null) {
                Text[] texts = title.fragments();
                StringBuilder nTitle = new StringBuilder();
                for (Text text : texts) {
                    nTitle.append(text);
                }
                // ??????
                sourceAsMap.put(highlightField, nTitle.toString());
            }
            list.add(sourceAsMap);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> searchListDataPage(String index, SearchSourceBuilder query, String highlightField) {
        SearchRequest request = new SearchRequest(index);
        // ??????
        HighlightBuilder highlight = new HighlightBuilder();
        highlight.field(highlightField);
        // ??????????????????
        highlight.requireFieldMatch(false);
        highlight.preTags("<span style='color:red'>");
        highlight.postTags("</span>");
        query.highlighter(highlight);
        // ????????????????????????????????????????????????
        request.source(query);
        try {
            SearchResponse response = elasticsearchClient.search(request, RequestOptions.DEFAULT);
            log.info("totalHits: {}", response.getHits().getTotalHits());
            if (response.status().getStatus() == 200) {
                return setSearchResponse(response, highlightField);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
