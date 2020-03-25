package com.bannad927.es.test.document;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author cbb
 * @date 2020.3.10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GetApiTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;


    @Test
    public void getIndexApi() {
        /*********************同步***********************/
        GetRequest getRequest = new GetRequest(
                "posts",//索引
                "1");//文件 id
        getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);//禁用默认情况下启用的源检索

        //包括特定字段 {postDate=2020-03-10, message=trying out Elasticsearch}
       /* String[] includes = new String[]{"message", "*Date"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);*/

        //排除 {postDate=2020-03-10, id=1, user=kimchy}
        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = new String[]{"message"};
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);

        try {
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
            log.info("response:{}", response.toString());
            log.info("response getIndex:{}", response.getIndex());
            log.info("response getSource:{}", response.getSource());
        } catch (ElasticsearchException e) {
            e.printStackTrace();
            if (e.status() == RestStatus.NOT_FOUND) {
                log.info("处理由于索引不存在而引发的异常");
            }
            if (e.status() == RestStatus.CONFLICT) {
                log.info("引发的异常表示返回了版本冲突错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /****************异步*********************/
        try {


            ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
                @Override
                public void onResponse(GetResponse getResponse) {
                    String index = getResponse.getIndex();
                    String id = getResponse.getId();
                    if (getResponse.isExists()) {
                        long version = getResponse.getVersion();
                        String sourceAsString = getResponse.getSourceAsString();//以字符串形式检索文档
                        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();//检索作为映射字符串的文档，对象
                        byte[] sourceAsBytes = getResponse.getSourceAsBytes();//以字节[]的形式检索文档
                    } else {
                        //处理未找到文档的场景。
                        // 注意，虽然返回的响应有404状态码，但是返回的是一个有效的 GetResponse，而不是抛出的异常。
                        // 这种响应不包含任何源文档，其 isExists 方法返回 false
                        log.info("处理未找到文档的场景");
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            };
            Cancellable response = client.getAsync(getRequest, RequestOptions.DEFAULT, listener);

        } catch (ElasticsearchException e) {
            e.printStackTrace();
            if (e.status() == RestStatus.NOT_FOUND) {
                log.info("处理由于索引不存在而引发的异常");
            }
            if (e.status() == RestStatus.CONFLICT) {
                log.info("引发的异常表示返回了版本冲突错误");
            }
        }


    }
}
