package com.yj.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//把常用的对象注入到spring容器中后面直接自动注入使用
@Configuration//类似于xml文件
public class ElasticSearchClientConfig {
    @Bean//类似于springxml中<bean id="restHighLevelClient" class="RestHighLevelClient">//返回值，这样后面拿到这个注入对象即可使用
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        return client;
    }
}
