package com.bannad927.es.test.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search.html
 *
 * @date 2020.3.11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SearchApi {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void searchAggregation() {
        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("posts");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       /*     TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
                    .field("company.keyword");
            aggregation.subAggregation(AggregationBuilders.avg("average_age")
                    .field("age"));
            searchSourceBuilder.aggregation(aggregation);*/

            //为用户字段和文本 kmichy 创建新的 TermSuggestionBuilder
            SuggestionBuilder termSuggestionBuilder =
                    SuggestBuilders.termSuggestion("user").text("kmichy");
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
            searchSourceBuilder.suggest(suggestBuilder);

            //Profileapi 可用于分析特定搜索请求的查询和聚合的执行情况。 为了使用它，配置文件标志必须在 SearchSourceBuilder 上设置为 true:
            searchSourceBuilder.profile(true);

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


            Map<String, ProfileShardResult> profilingResults =
                    searchResponse.getProfileResults();
            for (Map.Entry<String, ProfileShardResult> profilingResult : profilingResults.entrySet()) {
                String key = profilingResult.getKey();
                ProfileShardResult profileShardResult = profilingResult.getValue();
            }


            Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
            for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
                for (TermSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                }
            }

           /* Aggregations aggregations = searchResponse.getAggregations();
            for (Aggregation agg : aggregations) {
                String type = agg.getType();
                if (type.equals(TermsAggregationBuilder.NAME)) {
                    Terms.Bucket elasticBucket = ((Terms) agg).getBucketByKey("Elastic");
                    long numberOfDocs = elasticBucket.getDocCount();
                }
            }*/

          /*  Aggregations aggregations = searchResponse.getAggregations();
            Terms byCompanyAggregation = aggregations.get("by_company");
            Terms.Bucket elasticBucket = byCompanyAggregation.getBucketByKey("Elastic");
            Avg averageAge = elasticBucket.getAggregations().get("average_age");
            double avg = averageAge.getValue();
*/


        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void searchTest() {
        try {

            // SearchRequest searchRequest = new SearchRequest();//创建 SearchRequest。如果没有参数，将对所有索引运行。

            SearchRequest searchRequest = new SearchRequest("posts");//将请求限制为索引
            //searchRequest.indices("posts");//知道索引
            searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());//设置 IndicesOptions 控制不可用索引的解析方式和通配符表达式的展开方式


            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();//大多数搜索参数都添加到 SearchSourceBuilder 中。 它为进入搜索请求主体的所有内容提供了设置器。
            //searchSourceBuilder.query(QueryBuilders.matchAllQuery());//在 SearchSourceBuilder 中添加一个匹配所有查询。
            // searchSourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));//设置查询。可以是任何类型的 QueryBuilder


            /**building queries**/
           /* MatchQueryBuilder messageQueryBuilder = new MatchQueryBuilder("message", "程彬彬");
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("user", "kimchy");
            matchQueryBuilder.fuzziness(Fuzziness.AUTO);//在匹配查询上启用模糊匹配
            matchQueryBuilder.prefixLength(3);//在匹配查询中设置前缀长度选项
            matchQueryBuilder.maxExpansions(10);//设置最大展开选项来控制查询的模糊进程
            searchSourceBuilder.query(messageQueryBuilder);
            searchSourceBuilder.query(matchQueryBuilder);*/

            /**queryBuiler
             *
             *  试用
             *  中文分词器
             *  "message": {
             *         "type": "text",
             *         "analyzer": "ik_max_word",
             *         "search_analyzer": "ik_max_word"
             *       }
             *
             * */
        /*    QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("message", "elasticsearch").prefixLength(3).maxExpansions(10).fuzziness(Fuzziness.AUTO);
            searchSourceBuilder.query(matchQueryBuilder);*/

            //不显示source
            // searchSourceBuilder.fetchSource(false);

            //该方法还接受一个由一个或多个通配符模式组成的数组，以更细粒度的方式控制哪些字段被包含或排除:
          /*  String[] includeFields = new String[]{"title", "user", "message", "postDate"};
            String[] excludeFields = new String[]{"id"};
            searchSourceBuilder.fetchSource(includeFields, excludeFields);*/

            searchSourceBuilder.from(0);//设置 from 选项，该选项确定要开始搜索的结果索引。默认值为0。
            searchSourceBuilder.size(50);//设置用于确定要返回的搜索命中次数的 size 选项。默认值为10。
            searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));//设置一个可选的超时时间，控制允许搜索的时间。

            //排序
           /* searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));//按分数降序排序(默认值)
            searchSourceBuilder.sort(new FieldSortBuilder("id").order(SortOrder.ASC));//也按 id 字段升序排序*/

            searchRequest.source(searchSourceBuilder);//将 SearchSourceBuilder 添加到 SearchRequest。

            //执行搜索返回的 SearchResponse 提供了关于搜索执行本身以及对返回文档的访问的详细信息。
            // 首先，有一些关于请求执行本身的有用信息，比如 HTTP状态码、执行时间或者请求是提前终止还是超时终止:
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            RestStatus status = searchResponse.status();
            TimeValue took = searchResponse.getTook();
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            boolean timedOut = searchResponse.isTimedOut();

            //其次，响应还通过提供受搜索影响的碎片总数的统计信息，以及成功与不成功的碎片的统计信息，提供碎片级别的执行信息。
            // 可能的失败也可以通过迭代 ShardSearchFailures 数组来处理，如下例所示:
            int totalShards = searchResponse.getTotalShards();
            int successfulShards = searchResponse.getSuccessfulShards();
            int failedShards = searchResponse.getFailedShards();
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                // failures should be handled here
            }

            //要访问返回的文档，我们需要首先获得响应中包含的 SearchHits:
            SearchHits hits = searchResponse.getHits();

            //Searchhits 提供了所有点击的全局信息，比如点击总数或最大得分:
            TotalHits totalHits = hits.getTotalHits();
            // the total number of hits, must be interpreted in the context of totalHits.relation
            long numHits = totalHits.value;
            // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the total (GREATER_THAN_OR_EQUAL_TO)
            TotalHits.Relation relation = totalHits.relation;
            float maxScore = hits.getMaxScore();

            //嵌套在 SearchHits 中的是可以遍历的单个搜索结果:
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // 提供了对基本信息的访问，比如索引、文档 ID 和每次搜索的得分:
                String index = hit.getIndex();
                String id = hit.getId();
                float score = hit.getScore();

                //此外，它允许您以简单的 json 字符串或键 / 值对映射的形式返回文档源。
                // 在此映射中，常规字段由字段名称键控，并包含字段值。
                // 多值字段作为对象列表返回，嵌套对象作为另一个键 / 值映射。
                // 需要对这些情况作出相应的说明:
                String sourceAsString = hit.getSourceAsString();
                log.info("sourceAsString:{}", sourceAsString);
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String documentTitle = (String) sourceAsMap.get("title");
                // List<Object> users = (List<Object>) sourceAsMap.get("user");
                Map<String, Object> innerObject =
                        (Map<String, Object>) sourceAsMap.get("innerObject");


             /*   //如果请求，可以从结果中的每个 SearchHit 中检索突出显示的文本片段。
                // 点击对象提供了到 HighlightField 实例的字段名映射，每个实例包含一个或多个高亮文本片段:
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("title");//获取标题字段的高亮显示
                Text[] fragments = highlight.fragments();//获取包含突出显示的字段内容的一个或多个片段
                String fragmentString = fragments[0].string();*/

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
