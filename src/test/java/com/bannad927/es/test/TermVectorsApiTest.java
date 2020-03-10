package com.bannad927.es.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-term-vectors.html
 *
 * @date 2020.3.10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TermVectorsApiTest {
    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void termVectorsTest() {
        try {
            //Termvectorsrequest 需要一个索引、类型和 id 来指定某个文档以及为其检索信息的字段
           /* TermVectorsRequest request = new TermVectorsRequest("posts", "1");
            request.setFields("Elasticsearch");*/

            //工文档作为 XContentBuilder 对象提供，Elasticsearch 内置的助手用于生成 JSON 内容。
            XContentBuilder docBuilder = XContentFactory.jsonBuilder();
            docBuilder.startObject().field("message", "daily update").endObject();
            TermVectorsRequest request = new TermVectorsRequest("posts",
                    docBuilder);


           /* request.setFieldStatistics(false);//将 fieldStatistics 设置为 false (默认为 true)以省略文档计数、文档频率之和、术语频率之和。
            request.setTermStatistics(true);//将 termStatistics 设置为 true (默认为 false)以显示术语总频率和文档频率。
            request.setPositions(false);//将 positions 设置为 false (默认为 true)以省略 positions 的输出。
            request.setOffsets(false);//将偏移量设置为 false (默认为 true)以省略偏移量的输出。
            request.setPayloads(false);//将有效负载设置为 false (默认值为 true)以省略有效负载的输出。*/

           /* Map<String, Integer> filterSettings = new HashMap<>();
            filterSettings.put("max_num_terms", 3);
            filterSettings.put("min_term_freq", 1);
            filterSettings.put("max_term_freq", 10);
            filterSettings.put("min_doc_freq", 1);
            filterSettings.put("max_doc_freq", 100);
            filterSettings.put("min_word_length", 1);
            filterSettings.put("max_word_length", 10);
            request.setFilterSettings(filterSettings);//设置 filterSettings 以根据其 tf-idf 得分筛选可以返回的术语。*/

            Map<String, String> perFieldAnalyzer = new HashMap<>();
            perFieldAnalyzer.put("user", "程彬彬");
            request.setPerFieldAnalyzer(perFieldAnalyzer);//设置 perFieldAnalyzer 以指定与字段所拥有的分析器不同的分析器

            //request.setRealtime(false);//将 realtime 设置为 false (默认值为 true)以接近实时检索术语向量。
            //request.setRouting("routing");//设置路由参数


            TermVectorsResponse response  = client.termvectors(request, RequestOptions.DEFAULT);

            String index = response.getIndex();//文档的索引名称。
            String type = response.getType();//文档的类型名称。
            String id = response.getId();//文件的 id。
            boolean found = response.getFound();//指示是否找到文档。
            for (TermVectorsResponse.TermVector tv : response.getTermVectorsList()) {
                String fieldName = tv.getFieldName();//当前字段的名称
                int docCount = tv.getFieldStatistics().getDocCount();//当前字段文档计数的字段统计信息
                long sumTotalTermFreq = tv.getFieldStatistics().getSumTotalTermFreq();//当前字段的字段统计-总项频率之和
                long sumDocFreq = tv.getFieldStatistics().getSumDocFreq();//当前字段的字段统计-文档频率之和
                if (tv.getTerms() != null) {
                    List<TermVectorsResponse.TermVector.Term> terms = tv.getTerms();//当前字段的项
                    for (TermVectorsResponse.TermVector.Term term : terms) {
                        String termStr = term.getTerm();//名称
                        int termFreq = term.getTermFreq();//次数
                        int docFreq = term.getDocFreq();//出现次数
                        long totalTermFreq = term.getTotalTermFreq();//总次数
                        float score = term.getScore();//评分
                        List<TermVectorsResponse.TermVector.Token> tokens =
                                term.getTokens();//项标志
                        for (TermVectorsResponse.TermVector.Token token : tokens) {
                            int position = token.getPosition();//令牌的位置
                            int startOffset = token.getStartOffset();//令牌的启动偏移量
                            int endOffset = token.getEndOffset();//令牌的结束偏移量
                            String payload = token.getPayload();//令牌的有效载荷
                        }

                    }
                }


            }
        } catch (ElasticsearchException e) {
            log.info("status:{}", e.status());
            log.info("getDetailedMessage:{}", e.getDetailedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
