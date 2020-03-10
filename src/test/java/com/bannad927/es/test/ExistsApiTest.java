package com.bannad927.es.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cbb
 * @date 2020.3.10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExistsApiTest {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void existsApi() {
        GetRequest getRequest = new GetRequest(
                "posts",//索引
                "1");//id
   /*     try {

            getRequest.fetchSourceContext(new FetchSourceContext(false));//禁用抓取源
            getRequest.storedFields("_none_");//禁用获取存储字段

            Boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
            log.info("exists:{}", exists);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {

            client.existsAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<Boolean>() {
                @Override
                public void onResponse(Boolean exists) {
                    log.info("当执行成功完成时调用:{}", exists);
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    log.info("当整个 GetRequest 失败时调用");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
