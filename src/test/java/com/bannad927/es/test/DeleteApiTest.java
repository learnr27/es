package com.bannad927.es.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author cbb
 * @date 2020.3.10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DeleteApiTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void deleteApi() {
        DeleteRequest request = new DeleteRequest(
                "posts",
                "1");
        request.timeout(TimeValue.timeValueMinutes(2));//等待主碎片作为 TimeValue 可用的超时
        request.timeout("2m");//等待主碎片作为字符串可用的超时
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);//作为 WriteRequest.RefreshPolicy 实例刷新策略
        request.setRefreshPolicy("wait_for");//以字符串形式刷新策略
        request.version(2);//版本
        request.versionType(VersionType.EXTERNAL);//版本类型
        /**********同步*************/
        try {
            DeleteResponse deleteResponse = client.delete(
                    request, RequestOptions.DEFAULT);

            String index = deleteResponse.getIndex();
            String id = deleteResponse.getId();
            long version = deleteResponse.getVersion();
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                log.info("处理成功分片数量小于总分片数量的情况");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure :
                        shardInfo.getFailures()) {
                    String reason = failure.reason();
                    log.info("处理潜在的错误：{}", reason);
                }
            }
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.info("找不到要删除的文档");
            }
        } catch (ElasticsearchException e) {
            e.printStackTrace();
            if (e.status() == RestStatus.CONFLICT) {
                log.info("引发的异常表示返回了版本冲突错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
