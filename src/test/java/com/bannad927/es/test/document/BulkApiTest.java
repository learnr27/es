package com.bannad927.es.test.document;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-bulk.html
 *
 * @date 2020.3.10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BulkApiTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    public void bulkAsync1() {

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {//创建 BulkProcessor. 监听器
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                log.info("此方法在每次执行 BulkRequest 之前调用:{}", executionId);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                log.info("此方法在每次执行 BulkRequest 后调用:{}", executionId);

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {
                log.info("当 BulkRequest 失败时调用此方法:{}", executionId);

            }
        };


        BulkProcessor.Builder builder = BulkProcessor.builder(
                (request, bulkListener) ->
                        client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener);
        builder.setBulkActions(500);//根据当前添加的操作数设置何时刷新新的批量请求(默认为1000，使用 -1来禁用它)
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));//根据当前添加的操作大小设置何时刷新新的批量请求(默认为5Mb，使用 -1来禁用它)
        builder.setConcurrentRequests(0);//设置允许执行的并发请求的数量(默认为1，使用0只允许执行单个请求)
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));//设置一个刷新间隔，当间隔超过时刷新任何 BulkRequest 挂起(默认为不设置)
        builder.setBackoffPolicy(BackoffPolicy
                .constantBackoff(TimeValue.timeValueSeconds(1L), 3));//设置一个常量退出策略，最初等待1秒钟，重试3次。 参见后退政策。不后退() ，后退政策。

    }


    @Test
    public void bulkAsync() {
        BulkProcessor bulkProcessor = null;
        try {
            BulkRequest defaulted = new BulkRequest("posts");//包含全局索引的大容量请求，用于所有子请求，除非对子请求重写。 此参数为@nullable，只能在创建 BulkRequest 时设置。
            String jsonString = "{" +
                    "\"id\":\"5\"," +
                    "\"user\":\"5kimchy\"," +
                    "\"postDate\":\"2020-03-10\"," +
                    "\"message\":\"5trying out Elasticsearch\"" +
                    "}";
            String jsonString2 = "{" +
                    "\"id\":\"6\"," +
                    "\"user\":\"liuliu\"," +
                    "\"postDate\":\"2020-03-11\"," +
                    "\"message\":\"5trying out Elasticsearch liuliu\"" +
                    "}";
            defaulted.add(new IndexRequest().source(jsonString, XContentType.JSON));
            defaulted.add(new IndexRequest().source(jsonString2, XContentType.JSON));


            BulkProcessor.Listener listener = new BulkProcessor.Listener() {//创建 BulkProcessor. 监听器
                @Override
                public void beforeBulk(long executionId, BulkRequest request) {
                    log.info("此方法在每次执行 BulkRequest 之前调用:{}", executionId);
                    int numberOfActions = request.numberOfActions();//在每次执行 BulkRequest 之前调用该方法，可以知道 BulkRequest 中将要执行的操作数
                    log.info("Executing bulk [{}] with {} requests",
                            executionId, numberOfActions);
                }

                @Override
                public void afterBulk(long executionId, BulkRequest request,
                                      BulkResponse response) {
                    log.info("此方法在每次执行 BulkRequest 后调用:{}", executionId);
                    if (response.hasFailures()) {//在每次执行 BulkRequest 之后调用该方法，可以知道 BulkResponse 是否包含错误
                        log.info("Bulk [{}] executed with failures", executionId);
                    } else {
                        log.info("Bulk [{}] completed in {} milliseconds",
                                executionId, response.getTook().getMillis());
                    }

                }

                @Override
                public void afterBulk(long executionId, BulkRequest request,
                                      Throwable failure) {
                    log.info("当 BulkRequest 失败时调用此方法:{}", executionId);
                    log.info("Failed to execute bulk", failure);
                    //如果 BulkRequest 失败，则调用此方法，该方法允许知道失败

                }
            };
            //通过从 BulkProcessor 调用 build ()方法来创建 BulkProcessor。
            // 建造者。 Rethighlevelclient.bulkasync ()方法将用于执行头罩下的 BulkRequest。
            bulkProcessor = BulkProcessor.builder(
                    (request, bulkListener) ->
                            client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                    listener).build();

            IndexRequest one = new IndexRequest("posts").id("1")
                    .source(XContentType.JSON, "title",
                            "In which order are my Elasticsearch queries executed?");
            IndexRequest two = new IndexRequest("posts").id("2")
                    .source(XContentType.JSON, "title",
                            "Current status and upcoming changes in Elasticsearch");
            IndexRequest three = new IndexRequest("posts").id("3")
                    .source(XContentType.JSON, "title",
                            "The Future of Federated Search in Elasticsearch");

            bulkProcessor.add(one);
            bulkProcessor.add(two);
            bulkProcessor.add(three);

            //如果所有批量请求都已完成，则该方法返回 true; 如果在所有批量请求完成之前等待时间已过，则返回 false
            boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
            if (terminated) {
                log.info("所有批量请求都已完成");
            } else {
                log.info("有批量请求完成之前等待时间已过");
            }
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Close ()方法可用于立即关闭 BulkProcessor:
            if (null != bulkProcessor) {
                bulkProcessor.close();
            }

        }
    }


    public void bulkApi() {

        BulkRequest request = new BulkRequest();//创建 BulkRequest
        request.add(new IndexRequest("posts").id("1")
                .source(XContentType.JSON, "field", "foo"));
        request.add(new IndexRequest("posts").id("2")
                .source(XContentType.JSON, "field", "bar"));
        request.add(new IndexRequest("posts").id("3")
                .source(XContentType.JSON, "field", "baz"));

        request.add(new DeleteRequest("posts", "3"));//删除
        request.add(new UpdateRequest("posts", "2")//更新
                .doc(XContentType.JSON, "other", "test"));
        request.add(new IndexRequest("posts").id("4")
                .source(XContentType.JSON, "field", "baz"));//使用 SMILE 格式添加 IndexRequest

        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            //批量响应提供了一个快速检查一个或多个操作是否失败的方法:
            if (bulkResponse.hasFailures()) {
                log.info("如果至少有一个操作失败，则此方法返回 true");
            }
            //遍历所有操作结果，以检查操作是否失败，如果失败，则检索相应的失败:
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {//指示给定的操作是否失败
                    BulkItemResponse.Failure failure =
                            bulkItemResponse.getFailure();
                    log.info("检索失败操作的故障:{}", failure.getMessage());
                }
            }
            for (BulkItemResponse bulkItemResponse : bulkResponse) {//迭代所有操作的结果
                //检索操作的响应(成功与否) ，可以是 IndexResponse、 UpdateResponse 或 deletereesponse，它们都可以被视为 DocWriteResponse 实例
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();
                switch (bulkItemResponse.getOpType()) {
                    case INDEX:
                    case CREATE:
                        log.info("处理索引操作的响应");
                        IndexResponse indexResponse = (IndexResponse) itemResponse;
                        break;
                    case UPDATE:
                        log.info("处理更新操作的响应");
                        UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                        break;
                    case DELETE:
                        DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                        log.info("处理删除操作的响应");
                }
            }
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
