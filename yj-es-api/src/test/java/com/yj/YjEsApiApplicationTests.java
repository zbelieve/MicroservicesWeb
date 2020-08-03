package com.yj;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


//类似于数据库，在Kibana操作就是操作命令行，而这儿是面向对象操作
@SpringBootTest
class YjEsApiApplicationTests {

    @Autowired//如果名字不对应，可以通过@Qualifier("restHighLevelClient")来操作，不然名字不一样，找不到对应
    private RestHighLevelClient restHighLevelClient;

    //1.测试索引的创建 Request
    @Test
    void testCreateIndex() throws IOException {
        //1、创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("zhongyi_index");
        //2、客户端执行请求IndicesClient,请求后获得响应
        CreateIndexResponse createIndexResponse = restHighLevelClient
                .indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

}
