package com.xuecheng.manage_cms_client.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**这个页面是配置GridFSBucket注入的时候用的
 * */
@Configuration//这样sping容器启动的时候，就会扫描到这儿的bean，把这个bean注册到ioc容器中
public class MongoConfig {
    //注意这个value的选择
    @Value("${spring.data.mongodb.database}")//这个地址是mongodb的配置地址,因为下面需要指定数据库
            String db;
    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient){

        MongoDatabase database = mongoClient.getDatabase(db);
        GridFSBucket bucket = GridFSBuckets.create(database);
        return bucket;
    }
}
