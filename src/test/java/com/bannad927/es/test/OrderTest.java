package com.bannad927.es.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chengbinbin
 * @date 2020.3.11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void test() {
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            SearchRequest searchRequest = new SearchRequest();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.multiMatchQuery("payWay", "3", "4", "20"))
                    .must(QueryBuilders.termQuery("payState", 1))
                    .must(QueryBuilders.termQuery("isDel", 0))
                    .must(QueryBuilders.rangeQuery("orderDate").from("2020-01-03").to("2020-01-03"));
            searchSourceBuilder.query(queryBuilder);

            TermsAggregationBuilder aggregation = AggregationBuilders.terms("branchOfficeAreaCodes")
                    .field("branchOfficeAreaCode").size(10000);

            aggregation.subAggregation(AggregationBuilders.topHits("branchOfficeAreaCode").size(1));


            aggregation.subAggregation(AggregationBuilders.count("countOrder").field("id"));
            aggregation.subAggregation(AggregationBuilders.sum("sumOrderPrice").field("price"));
            aggregation.subAggregation(AggregationBuilders.sum("sumRefundPrice").field("sumRefundPrice"));

            AggregationBuilder countAppOrder = AggregationBuilders.filter("countAppOrders", QueryBuilders.termQuery("platform", "0"));
            countAppOrder.subAggregation(AggregationBuilders.count("countAppOrder").field("id"));
            aggregation.subAggregation(countAppOrder);

            AggregationBuilder sumAppPrice = AggregationBuilders.filter("sum_app_price", QueryBuilders.termQuery("platform", "0"));
            sumAppPrice.subAggregation(AggregationBuilders.sum("sumAppOrderPrice").field("price"));
            aggregation.subAggregation(sumAppPrice);

            AggregationBuilder countWeChatOrder = AggregationBuilders.filter("countWechatOrders", QueryBuilders.termQuery("platform", "1"));
            countAppOrder.subAggregation(AggregationBuilders.count("countWechatOrder").field("id"));
            aggregation.subAggregation(countWeChatOrder);

            AggregationBuilder sumWeChatPrice = AggregationBuilders.filter("sum_wechat_price", QueryBuilders.termQuery("platform", "1"));
            sumWeChatPrice.subAggregation(AggregationBuilders.sum("sumWechatOrderPrice").field("price"));
            aggregation.subAggregation(sumWeChatPrice);

            List<FieldSortBuilder> fieldSorts=new ArrayList<>();
            fieldSorts.add(new FieldSortBuilder("sumOrderPrice").order(SortOrder.DESC));
            aggregation.subAggregation(new BucketSortPipelineAggregationBuilder("bucket_field", fieldSorts).from(10).size(10));


            searchSourceBuilder.aggregation(aggregation);
            searchSourceBuilder.size(0);//设置用于确定要返回的搜索命中次数的 size 选项。默认值为10。

            searchRequest.source(searchSourceBuilder);
            searchRequest.indices("device_order");

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            watch.stop();
            long millis = watch.getTotalTimeMillis();

            log.info("查询用时：{}", millis);
            Aggregations aggregations = searchResponse.getAggregations();

            List<JSONObject> list = new ArrayList<>();
            ParsedStringTerms agg1 = aggregations.get("branchOfficeAreaCodes");
            List<? extends Terms.Bucket> buckets = agg1.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                log.info("getKeyAsString:{}", bucket.getKeyAsString());
                log.info("getDocCount:{}", bucket.getDocCount());
                Aggregations aggregationResult = bucket.getAggregations();
                Map<String, Aggregation> mapAgg = aggregationResult.getAsMap();
                JSONObject data = new JSONObject();
                data.put("areaCode", bucket.getKeyAsString());
                data.put("areaCodeStr", "areaCodeStr" + bucket.getKeyAsString());
                for (Map.Entry<String, Aggregation> aggs : mapAgg.entrySet()) {
                    String key = aggs.getKey();
                    Aggregation agg = aggs.getValue();
                    log.info("mapKey:{}", key);
                    log.info("getMetaData:{}", agg.getMetaData());
                    log.info("getType:{}", agg.getType());
                    log.info("getName:{}", agg.getName());
                    if ("filter".equals(agg.getType())) {
                        Aggregations filter = ((ParsedFilter) agg).getAggregations();
                        Map<String, Aggregation> sub = filter.getAsMap();
                        for (Map.Entry<String, Aggregation> subAggs : sub.entrySet()) {
                            String subAggKey = subAggs.getKey();
                            Aggregation subAgg = subAggs.getValue();
                            log.info("subAggKey:{}", key);
                            log.info("getMetaData:{}", subAgg.getMetaData());
                            log.info("getType:{}", subAgg.getType());
                            log.info("getName:{}", subAgg.getName());
                            if ("sum".equals(subAgg.getType())) {
                                ParsedSum parsedSum = (ParsedSum) subAgg;
                                double subSun = ((ParsedSum) subAgg).getValue();
                                log.info("{}:{}", parsedSum.getName(), subSun);
                                data.put(parsedSum.getName(), parsedSum.getValue());
                            } else if ("value_count".equals(subAgg.getType())) {
                                ParsedValueCount parsedValueCount = (ParsedValueCount) subAgg;
                                long copunt = ((ParsedValueCount) subAgg).getValue();
                                log.info("{}:{}", parsedValueCount.getName(), copunt);
                                data.put(parsedValueCount.getName(), parsedValueCount.getValue());
                            }
                        }
                    } else if ("sum".equals(agg.getType())) {
                        ParsedSum parsedSum = (ParsedSum) agg;
                        double sun = ((ParsedSum) agg).getValue();
                        log.info("{}:{}", parsedSum.getName(), sun);
                        data.put(parsedSum.getName(), parsedSum.getValue());
                    } else if ("value_count".equals(agg.getType())) {
                        ParsedValueCount parsedValueCount = (ParsedValueCount) agg;
                        long copunt = ((ParsedValueCount) agg).getValue();
                        log.info("{}:{}", parsedValueCount.getName(), copunt);
                        data.put(parsedValueCount.getName(), parsedValueCount.getValue());
                    } else if ("top_hits".equals(agg.getType())) {
                        TopHits topHits = (TopHits) agg;
                        SearchHits searchHits = topHits.getHits();
                        SearchHit searchHit = searchHits.getAt(0);
                        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                        data.put("areaCodeStr", sourceAsMap.get("officeName") + " " + sourceAsMap.get("branchOfficeName"));
                        data.put("city", sourceAsMap.get("cityName"));
                    }
                }
                list.add(data);
            }
            log.info("result:{}", list.toString());
            log.info("a:{}", aggregation.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
