package com.bannad927.es.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * @date 2020.3.10
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-update.html
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UpdateApiTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    /**
     * Update API 允许通过使用脚本或传递部分文档来更新现有文档。
     */
    @Test
    public void updateApi() {
        UpdateRequest request = new UpdateRequest(
                "posts",
                "1");

//        request.timeout(TimeValue.timeValueSeconds(1));//等待主碎片作为 TimeValue 可用的超时
//        request.timeout("1s");//等待主碎片作为字符串可用的超时
//        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);//作为 WriteRequest.RefreshPolicy 实例刷新策略
//        request.setRefreshPolicy("wait_for");//以字符串形式刷新策略
//        request.retryOnConflict(3);//如果要更新的文档由于更新操作的获取和索引阶段之间的另一个操作而更改，则重试更新操作的次数
//        request.fetchSource(true);//启用源检索，默认情况下禁用
//        request.detectNoop(false);//关闭 noop 检测
//        request.scriptedUpsert(true);//指出脚本必须运行，不管文档是否存在，即脚本负责创建文档，如果它不存在。
//        request.docAsUpsert(true);//指示部分文档如果尚不存在，则必须将其用作升级文档
//        request.waitForActiveShards(0);//设置在继续执行更新操作之前必须处于活动状态的分片副本数。
//        request.waitForActiveShards(ActiveShardCount.ALL);//作为 ActiveShardCount 提供的分片副本数: 可以是 ActiveShardCount.all、 ActiveShardCount.one 或 ActiveShardCount.default (默认值)

//        String[] includes = new String[]{"updated", "r*"};
//        String[] excludes = Strings.EMPTY_ARRAY;
//        request.fetchSource(
//                new FetchSourceContext(true, includes, excludes));//包含特定字段

        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = new String[]{"reason"};
        request.fetchSource(
                new FetchSourceContext(true, includes, excludes));//排除特定字段


        /********************脚本***************************/
        //该脚本可以作为行内脚本提供:
       Map<String, Object> parameters = singletonMap("count", 4);//作为对象映射提供的脚本参数

       /* Script inline = new Script(ScriptType.INLINE, "painless",
                "ctx._source.field += params.count", parameters);//使用无痛语言和前面的参数创建一个内联脚本
        request.script(inline);//将脚本设置为更新请求*/

        //或者作为一个存储脚本:
        Script stored = new Script(
                ScriptType.STORED, null, "increment-field", parameters);//引用存储在无痛语言的 name increment-field 下的脚本
        request.script(stored);//在更新请求中设置脚本
        /*********************文档**************************/
        try {
            //以 JSON 格式提供的 String 形式提供的部分文档源
            UpdateRequest jsonRequest = new UpdateRequest("posts", "1");
            String jsonString = "{" +
                    "\"user\":\"老王\"," +
                    "\"postDate\":\"2020-03-01\"," +
                    "\"reason\":\"daily update\"" +
                    "}";
          //  request.doc(jsonString, XContentType.JSON);

            //如果文档不存在，可以使用 upsert 方法定义一些内容作为新文档插入:
            request.upsert(jsonString, XContentType.JSON);

            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);

            //作为 Map 提供的部分文档源，可以自动转换为 JSON 格式
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("updated", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            jsonMap.put("reason", "daily update");
            UpdateRequest mapRequest = new UpdateRequest("posts", "1")
                    .doc(jsonMap);
            //部分文档源作为 XContentBuilder 对象提供，Elasticsearch 内置助手生成 JSON 内容
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.timeField("updated", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                builder.field("reason", "daily update");
            }
            builder.endObject();
            UpdateRequest xRequest = new UpdateRequest("posts", "1")
                    .doc(builder);

            //作为对象键对提供的部分文档源，它被转换为 JSON 格式
            UpdateRequest kvRequest = new UpdateRequest("posts", "1")
                    .doc("updated", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()),
                            "reason", "daily update");


            String index = updateResponse.getIndex();
            String id = updateResponse.getId();
            long version = updateResponse.getVersion();
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                log.info("处理第一次创建文档的情况(upsert)");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                log.info("处理文档更新的情况");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED) {
                log.info("处理文档被删除的情况");
            } else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP) {
                log.info("处理文档没有受到更新影响的情况(没有对文档执行任何操作(noop))");
            }

            GetResult result = updateResponse.getGetResult();//作为 GetResult 检索更新后的文档
            if (result.isExists()) {
                String sourceAsString = result.sourceAsString();//以字符串形式检索更新后的文档的源
                Map<String, Object> sourceAsMap = result.sourceAsMap();//以 Map String、 Object 的形式检索更新的文档的源
                byte[] sourceAsBytes = result.source();//以字节流的形式检索更新后的文档的源
            } else {
                log.info("处理响应中没有文档源的场景(默认情况下是这种情况)");
            }

            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                log.info("处理成功分片数量小于总分片数量的情况");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure :
                        shardInfo.getFailures()) {
                    String reason = failure.reason();
                    log.info("处理潜在的错误:{}", reason);
                }
            }
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                log.info("引发的异常表示返回了版本冲突错误。");
            }
            if (e.status() == RestStatus.NOT_FOUND) {
                log.info("处理由于文档不存在而引发的异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
