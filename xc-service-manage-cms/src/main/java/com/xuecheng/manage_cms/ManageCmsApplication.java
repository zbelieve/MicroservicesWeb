package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@EnableDiscoveryClient//一个EurekaClient从EurekaServer发现服务
//这个注解会扫描这个启动类所在的包以及其子包
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})//扫描接口类
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})//扫描本项目下的所有子包，其实不加也能扫描，加主要是为了看的更清楚
@ComponentScan(basePackages = {"com.xuecheng.framework"})//扫描conmmon项目下的包，这儿是为了可以处理异常
public class ManageCmsApplication {
    public static void main(String[] args){
        SpringApplication.run(ManageCmsApplication.class,args);
    }

    //在SpringBoot启动类中配置 RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
