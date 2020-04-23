package com.xuecheng.manage_cms_client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类

@ComponentScan(basePackages={"com.xuecheng.manage_cms_client"})//扫描本项目下的所有子包，其实不加也能扫描，加主要是为了看的更清楚
@ComponentScan(basePackages = {"com.xuecheng.framework"})//扫描conmmon项目下的包，这儿是为了可以处理异常
@SpringBootApplication
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
