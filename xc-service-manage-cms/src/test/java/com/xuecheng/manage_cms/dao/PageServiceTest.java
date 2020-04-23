package com.xuecheng.manage_cms.dao;


import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    PageService pageService;

    @Test//测试下面这个需要把服务器开着
    public void testGetPageHtml(){
        //这儿传的就是cms_page的id
        String pageHtml = pageService.getPageHtml("5e68b9cd3e3fcc4908faa742");
        System.out.println(pageHtml);

    }

}
