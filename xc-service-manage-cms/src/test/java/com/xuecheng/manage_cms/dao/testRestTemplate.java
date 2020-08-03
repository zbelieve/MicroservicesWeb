package com.xuecheng.manage_cms.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**这个测试的是通过远程服务获得模型数据，之后用来渲染模板的
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class testRestTemplate {
    @Autowired
    RestTemplate restTemplate;
    @Test
    //测试远程连接获取模型数据，这个就是模型+数据中的数据
    public void testRestTemplate() {
        // 封装参数，这里是HashMap
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("id", "11111111");
        //测试这个的时候要把服务器开了
        //url请求返回的是json数据，之后在这儿转换map,就是键值对类型的，这儿获取的远程访问的数据是cmsconfig的数据，主要想获取他的图片数据在服务器的url,静态化分为模板和数据，这儿就是获得动态的数据
        //ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        ResponseEntity<Map> forEntity2 = restTemplate.getForEntity("http://127.0.0.1:8001/hello?id={id}", Map.class, paramMap);
        System.out.println(forEntity2);
        System.out.println("x");
        System.out.println("");
    }
}
