package com.yj;

import com.alibaba.fastjson.JSON;
import com.yj.pojo.Company;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.security.user.User;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonbTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


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

    //2.测试获取索引(是否存在)
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("zhongyi_index");
        Boolean exists = restHighLevelClient.
                indices().exists(request,RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //3.测试删除索引
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("zhongyi_index");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request,RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    //4.测试添加文档
    @Test
    void testAddDocument() throws IOException {
        //创建对象
        Company compay = new Company("中国移动",4);
        //创建请求
        IndexRequest request = new IndexRequest("zhongyi_index");
        //就类似于在Kibana下使用命令put /zhongyi_index/_doc/1
        request.id("1");
        //将对象转为json
        request.source(JSON.toJSONString(compay), XContentType.JSON);

        IndexResponse  indexResponse = restHighLevelClient.index(request,RequestOptions.DEFAULT);

        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());

    }
    //5.获取文档，判断是否存在get /index/_doc/1
    @Test
    void testIsExists() throws IOException {
        GetRequest getRequest = new GetRequest("zhongyi_index", "1");
        boolean exists = restHighLevelClient.exists(getRequest,RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //6.获取文档信息
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("zhongyi_index", "1");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
    }
    //7.更新文档信息
    @Test
    void testUpdateRequest() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("zhongyi_index","1");
        Company company = new Company("中移苏州",5);
        updateRequest.doc(JSON.toJSONString(company),XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    //8.删除文档记录
    @Test
    void testDeleteRequest() throws IOException {
        DeleteRequest request = new DeleteRequest("zhongyi_index","1");
        request.timeout("1s");
        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    //9.批量插入数据
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(new Company("中移1",1));
        companies.add(new Company("中移2",2));
        companies.add(new Company("中移3",3));
        companies.add(new Company("中移4",4));
        companies.add(new Company("中移5",5));
        companies.add(new Company("中移6",6));
        companies.add(new Company("中移7",7));

        for(int i=0;i<companies.size();i++){
            //批量插入与删改都是只要改这儿就够了，不设置id的话就是随机的
            bulkRequest.add(
              new IndexRequest("zhongyi_index").
                      id(""+(i+1)).source(JSON.toJSONString(companies.get(i)),XContentType.JSON)
            );
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        System.out.println(bulkResponse.hasFailures());//返回是否失败

    }
    //10.查询数据
    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("zhongyi_index");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //具体查询条件可以通过QueryBuilders工具来实现
        //精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "移");
        //查询所有
        //MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(JSON.toJSONString(response.getHits()));

        for(SearchHit documentFields:response.getHits().getHits()){
            System.out.println(documentFields.getSourceAsMap());
        }
    }








}











