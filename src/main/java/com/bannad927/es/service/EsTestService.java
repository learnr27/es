package com.bannad927.es.service;

import com.bannad927.es.entity.EsDeviceOrder;
import com.bannad927.es.repository.AreaDetailRepository;
import com.bannad927.es.repository.AreaRepository;
import com.bannad927.es.repository.DeviceOrderRepository;
import com.bannad927.es.utils.EsClientSingleton;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.omg.CORBA.ServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/topdandan/article/details/81436141
 *
 * @author chengbb@xmulife.com
 * @date 2020.3.5
 */
@Service
@Slf4j
public class EsTestService {

    @Autowired
    private DeviceOrderRepository deviceOrderRepository;

    @Autowired
    private AreaDetailRepository areaDetailRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Data
    public class QueryData {
        public int id;
        public String pics;
        public String asc;
        public String desc;
        public String site;
        public String minPrices;
        public String maxPrices;
        public String queryName;
        public String prices;
        public String city;
        public String createTime;
    }

    @PostConstruct
    public void aggregationBuilders1(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("t_device_order").types("_doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder= QueryBuilders.boolQuery()
                .mustNot(QueryBuilders.multiMatchQuery("payWay", "3", "4", "20"))
                .must(QueryBuilders.matchQuery("payState",1))
                .must(QueryBuilders.matchQuery("isDel",0));

        SearchQuery searchQuery1 = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder).build();


        Page<EsDeviceOrder> page = deviceOrderRepository.search(searchQuery1);
        List<EsDeviceOrder> deviceOrders = page.getContent();
        for (EsDeviceOrder deviceOrder : deviceOrders) {
            System.out.println(deviceOrders);
        }

    }

    /**
     * 聚合查询
     *
     */
    public void aggregationBuilders(){
        //首先新建一个用于存储数据的集合
        List<String> userNameList = new ArrayList<>();
        //1.创建查询条件，也就是QueryBuild
        //设置查询所有，相当于不设置查询条件
        QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        //2.构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //2.0 设置QueryBuilder
        nativeSearchQueryBuilder.withQuery(matchAllQuery);
        //2.1设置搜索类型，默认值就是QUERY_THEN_FETCH，参考https://blog.csdn.net/wulex/article/details/71081042
        //指定索引的类型，只先从各分片中查询匹配的文档，再重新排序和排名，取前size个文档
        nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);
        //2.2指定索引库和文档类型
        //指定要查询的索引库的名称和类型，其实就是我们文档@Document中设置的indedName和type
        nativeSearchQueryBuilder.withIndices("t_device_order").withTypes("_doc");
        //2.3重点来了！！！指定聚合函数,本例中以某个字段分组聚合为例（可根据你自己的聚合查询需求设置）
        //该聚合函数解释：计算该字段(假设为username)在所有文档中的出现频次，并按照降序排名（常用于某个字段的热度排名）

        AbstractAggregationBuilder termsAggregation =AggregationBuilders
                .global("price")
                .subAggregation(AggregationBuilders.terms("modelName").field("快速洗"));


        nativeSearchQueryBuilder.addAggregation(termsAggregation);


        //2.4构建查询对象
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //3.执行查询
        //3.1方法1,通过reporitory执行查询,获得有Page包装了的结果集
        Page<EsDeviceOrder> search = deviceOrderRepository.search(nativeSearchQuery);
        List<EsDeviceOrder> content = search.getContent();
        for (EsDeviceOrder order : content) {
            userNameList.add(order.getOrderName());
        }
    }

    /**
     * 非聚合复杂查询
     */
    public void deviceOrder() {
        try {
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.must(QueryBuilders.fuzzyQuery("modelName", "快速洗"));
            //  builder.must(new QueryStringQueryBuilder("orderName").field("洗衣机"));

            QueryBuilder queryBuilder = QueryBuilders.rangeQuery("orderTime").from("2020-03-01T05:24:41.000Z").to("2020-03-06T05:24:41.000Z");
            builder.filter(queryBuilder);

            FieldSortBuilder sort = SortBuilders.fieldSort("id").order(SortOrder.DESC);
            Pageable page = PageRequest.of(1, 3);

            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(builder);
            nativeSearchQueryBuilder.withPageable(page);
            nativeSearchQueryBuilder.withSort(sort);

            NativeSearchQuery query = nativeSearchQueryBuilder.build();
            Page<EsDeviceOrder> result1 = deviceOrderRepository.search(query);

            Long total = result1.getTotalElements();
            List<EsDeviceOrder> content = result1.getContent();

            log.info("total:{}", total);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public Map<String, List<Map<String, Object>>> searchProductByQuery() {
        QueryData queryData = new QueryData();
        queryData.setQueryName("雪佛兰");
        int pageNum = 1;
        int pageSize = 100;
        //searchRequest 是用于查询
        SearchRequest searchRequest = new SearchRequest();
        //searchRequest1 用于统计查询的总数，由于Es中设置了from和size之后就无法查询出总数，所以就加了一次查询，如果大家有好的办法请务必告知于我，非常感谢！！！！
        SearchRequest searchRequest1 = new SearchRequest();
        //现在的版本已经不支持指定type啦，可以去掉，因为现在的type都是_doc
        searchRequest.indices("my_index").types("_doc");
        //将searchSourceBuilder加入到searchRequest中
        //由于好多的查询都用到了查询条件，所以我i直接将sourceBuilder进行了封装，用的时候可以直接调用，避免代码冗杂
        searchRequest.source(sourceBuilder(queryData, pageNum, pageSize));
        searchRequest1.source(sourceBuilderTotal(queryData));
        //现在的Es的查询返回值都是map集合，需要用map来接
        List<Map<String, Object>> listTotal = searchResult(searchRequest1);
        //同步执行
        List<Map<String, Object>> list = searchResult(searchRequest);
        Map<String, List<Map<String, Object>>> listMap = new HashMap<>();
        //查询出来的值
        listMap.put("List", list);
        //查询出的总数
        listMap.put("total", listTotal);
        return listMap;
    }

    //用于查询
    public SearchSourceBuilder sourceBuilder(QueryData queryData, int pageNum, int pageSize) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //开始搜索的位置
        searchSourceBuilder.from((pageNum - 1) * pageSize);
        //搜索结果的数量大小
        searchSourceBuilder.size(pageSize);
        //设置超时时间
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //按照名称查询
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", queryData.getQueryName());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //嵌套查询，满足其一即可
        boolQueryBuilder.should(matchQueryBuilder);

        //按照价格区间排序
        if (null != queryData.getMinPrices() && null != queryData.getMaxPrices()) {
            RangeQueryBuilder queryBuilder = QueryBuilders
                    .rangeQuery("prices")
                    .from(queryData.getMinPrices())
                    .to(queryData.getMaxPrices());
            searchSourceBuilder.query(queryBuilder);
        }


        if (StringUtils.equals("asc", queryData.getAsc())) {
            //按照价格升序
            searchSourceBuilder.sort(new FieldSortBuilder("prices")
                    .order(SortOrder.ASC));
        } else if (StringUtils.equals("desc", queryData.getDesc())) {
            //按照价格降序
            searchSourceBuilder.sort(new FieldSortBuilder("prices")
                    .order(SortOrder.DESC));
        } else if (StringUtils.isNotBlank(queryData.getSite())) {
            //按照地域排序
            searchSourceBuilder.sort(new FieldSortBuilder("city.raw")
                    .order(SortOrder.ASC));
        }
        //此为高亮字段，作为封装类方法在这里被引用

        searchSourceBuilder.highlighter(highlight());

        //需要查询出来的字段
        String[] includeFields = new String[]{"id", "name", "city", "pics", "createTime"};
        //不需要的字段
        String[] excludeFields = new String[]{""};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }

    public HighlightBuilder highlight() {
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置高亮的前缀和后缀
        String preTags = "<em class='titleColor' style='color: #f60'>";
        String postTags = "</em>";
        highlightBuilder.preTags(preTags);
        highlightBuilder.postTags(postTags);

        HighlightBuilder.Field pname = new HighlightBuilder.Field("name");

        highlightBuilder.field(pname);
        return highlightBuilder;
    }

    //获取总页数
    public SearchSourceBuilder sourceBuilderTotal(QueryData queryData) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //开始搜索的位置
        searchSourceBuilder.from(0);
        //搜索结果的数量大小
        searchSourceBuilder.size(10000);

        //按照商品/服务/解决方案名称查询
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", queryData.getQueryName());

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //嵌套查询，满足其一即可
        boolQueryBuilder.should(matchQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }

    public List<Map<String, Object>> searchResult(SearchRequest searchRequest) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> fieldList = new ArrayList();
        //需要高亮的字段
        fieldList.add("name");

        try {
            SearchResponse searchResponse = EsClientSingleton.getInstance().search(searchRequest, RequestOptions.DEFAULT);
            RestStatus status = searchResponse.status();
            log.info("状态：" + status);
            TimeValue took = searchResponse.getTook();
            log.info("执行时间：" + took);
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            log.info("请求终止：" + terminatedEarly);
            boolean timedOut = searchResponse.isTimedOut();
            log.info("超时时间：" + timedOut);
            //获得响应的文档
            SearchHits hits = searchResponse.getHits();
            //迭代取出数据
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                for (String field : fieldList) {
                    String keyValue = (String) sourceAsMap.get(field);
                    HighlightField highlightFieldValue = hit.getHighlightFields().get(field);
                    if (highlightFieldValue == null) {
                        sourceAsMap.put(field, keyValue);
                    } else {
                        String highLightContent = highlightFieldValue.getFragments()[0].toString();
                        sourceAsMap.put(field, highLightContent);
                    }
                }
                list.add(sourceAsMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 将查询出的数据通过实体类映射到es中
     * 注意：字段映射必须和es中的一致，可以直接映射，也可以转换为json映射
     */
    public void saveDocumentByIndex() {
        BulkRequest bulkRequest = new BulkRequest();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "3");
        map.put("pics", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1522577828,1774900301&fm=26&gp=0.jpg");
        map.put("name", "雪佛兰3");
        map.put("prices", "3");
        map.put("city", "厦门3");
        map.put("createTime", "2020-02-02 23:00:00");

        bulkRequest.add(new IndexRequest("my_index", "_doc", "3").source(map));
        bulkRequest.timeout("100m");

        try {
            BulkResponse bulkResponse = EsClientSingleton.getInstance().bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("bulkResponse buildFailureMessage:{}", bulkResponse.buildFailureMessage());
            log.info("bulkResponse status:{}", bulkResponse.status());
            log.info("bulkResponse:{}", bulkResponse.getIngestTookInMillis());
        } catch (IOException e) {
            log.info("同步失败");
            e.printStackTrace();
        }

        log.info("es同步完成");

    }


    /**
     * 创建mapping
     * 注意数据格式，此版本已经取去除String格式，改为text和keyword格式，其中text格式支持分词和建立索引，
     * 支持模糊查询和精确查询，不支持聚合，keyword不支持分词，支持模糊查询和精确查询，支持聚合查询，排序
     *
     * @return
     */
    public void createMapping() {
        PutMappingRequest putMappingRequest = new PutMappingRequest("my_index");
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "keyword")
                    .field("index", true)
                    .endObject()
                    .startObject("pics")
                    .field("type", "text")
                    .field("index", false)
                    .endObject()
                    .startObject("name")
                    .field("type", "text")
                    .field("index", true)
                    //分词器采用ik_smart分词器
                    .field("analyzer", "ik_smart")
                    .endObject()
                    .startObject("prices")
                    .field("type", "double")
                    .field("index", true)
                    .endObject()
                    //可以按照城市排序，需要在其中再套一层，并且格式为keyword
                    .startObject("city")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("raw")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    //支持指定时间格式
                    .startObject("createTime")
                    .field("type", "date")
                    .field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        putMappingRequest.source(builder);
        try {
            AcknowledgedResponse putMappingResponse = EsClientSingleton.getInstance().indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            System.out.println(putMappingResponse);
            if (!putMappingResponse.isAcknowledged()) {
                log.info("接口执行失败");
            } else {
                log.info("mapping已经存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("mapping创建接口异常");
        }

        log.info("mapping创建成功");
    }


    /**
     * 创建index
     */
    public void createIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("my_index");
        try {
            CreateIndexResponse createIndexResponse = EsClientSingleton.getInstance().indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                log.info("创建失败");
            } else {
                log.info("索引已经存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("接口异常");
        }
        log.info("创建成功");
    }


    /**
     * 判断index是否存在
     */
    public void existsIndex() {
        try {
            GetIndexRequest request = new GetIndexRequest("t_area_detail");
            boolean response = EsClientSingleton.getInstance().indices().exists(request, RequestOptions.DEFAULT);
            log.info("response:{}", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
