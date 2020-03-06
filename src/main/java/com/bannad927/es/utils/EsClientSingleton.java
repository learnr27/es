package com.bannad927.es.utils;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author chengbb@xmulife.com
 * @date 2020.3.5
 */
public class EsClientSingleton {

    private volatile static RestHighLevelClient instance = null;

    private EsClientSingleton() {
    }

    public static RestHighLevelClient getInstance() {
        if (instance == null) {
            synchronized (EsClientSingleton.class) {
                if (instance == null) {
                    try {
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        RestClientBuilder builder = RestClient.builder(new HttpHost("39.97.189.116", 9200, "http"))
                                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                                    @Override
                                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                                    }
                                }).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                                    @Override
                                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestBuilder) {
                                        requestBuilder.setConnectTimeout(5000);
                                        requestBuilder.setSocketTimeout(40000);
                                        requestBuilder.setConnectionRequestTimeout(1000);
                                        return requestBuilder;
                                    }
                                });
                        instance = new RestHighLevelClient(builder);
                        //记得关闭，不然进程会等待
                        //instance.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

}
