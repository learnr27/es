package com.bannad927.es.test.document;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cbb
 * @date 2020.3.10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IndexApiTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;


    public void getApi() {

    }

    @Test
    public void indexApi() throws IOException {
        /******************同步*********************/
       //索引
      IndexRequest request = new IndexRequest("posts");
        //请求的文档 id
        request.id("17");
        //以字符串形式提供的文档源
        String jsonString = "{" +
                "\"id\":\"17\"," +
                "\"age\":17," +
                "\"user\":\"kimchy\"," +
                "\"company\":\"bannad927\"," +
                "\"postDate\":\"2020-03-10\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);
        IndexResponse jsonResponse =  client.index(request, RequestOptions.DEFAULT);
        log.info("jsonResponse:{}",jsonResponse.toString());

       Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", "19");
        jsonMap.put("age", 19);
        jsonMap.put("user", "chengbinbin");
        jsonMap.put("company", "bannad927");
        jsonMap.put("postDate", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        jsonMap.put("message", "trying out Elasticsearch by 程彬彬");
        IndexRequest mapRequest = new IndexRequest("posts")
                .id("19").source(jsonMap);
        IndexResponse mapResponse = client.index(mapRequest, RequestOptions.DEFAULT);
        log.info("mapResponse:{}",mapResponse.toString());

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("age", 18);
            builder.field("id", 18);
            builder.field("user", "程彬彬");
            builder.field("company", "learnr27");
            builder.timeField("postDate", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            builder.field("message", "试用Elasticsearch");
        }
        builder.endObject();
        IndexRequest builderRequest = new IndexRequest("posts")
                .id("18").source(builder);

        IndexResponse builderResponse = client.index(builderRequest, RequestOptions.DEFAULT);
        log.info("builderResponse:{}",builderResponse.toString());
        log.info("+++++++++++++++++");




    }

    public void indexAsync(){
        /****************异步**************/
        try {
            XContentBuilder request = XContentFactory.jsonBuilder();
            request.startObject();
            {
                request.field("id", "4");
                request.field("user", "lisi");
                request.timeField("postDate", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                request.field("message", "lisi试用Elasticsearch");

            }
            request.endObject();

            IndexRequest builderRequest = new IndexRequest("posts").id("4").source(request);

            client.indexAsync(builderRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {

                @Override
                public void onResponse(IndexResponse indexResponse) {
                    //当执行成功完成时调用。
                    if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                        log.info("处理(如果需要)首次创建文档的情况");
                    } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                        log.info("处理(如果需要的话)已经存在的文档被重写的情况");
                    }

                    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                    if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                        log.info("处理成功分片数量小于总分片数量的情况");
                    }
                    if (shardInfo.getFailed() > 0) {
                        for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                            String reason = failure.reason();
                            log.info("处理潜在的故障:{}", reason);
                        }
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    //当整个 IndexRequest 失败时调用。
                    e.printStackTrace();
                    log.warn("处理潜在的故障");
                }
            });

        } catch (ElasticsearchException e) {
            //同样的情况也会发生在 opType 被设置为创建并且已经存在相同索引和 id 的文档时:
            if (e.status() == RestStatus.CONFLICT) {
                log.info("引发的异常表示返回了版本冲突错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
