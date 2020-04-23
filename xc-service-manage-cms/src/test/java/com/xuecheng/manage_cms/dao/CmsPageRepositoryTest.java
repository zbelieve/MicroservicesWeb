package com.xuecheng.manage_cms.dao;


import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;
import java.util.Optional;

@SpringBootTest//找springboot启动器中的扫描注解，找到指定包下的bean，将其扫描至容器
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired    //拿容器中定义的接口，通过扫描dao中的CmsPageRepository，会生成此接口的代理对象，把这个代理对象注入到这个类中
    CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }
    //自定义条件查询
    @Test
    public void testFindAllByExample(){
        //分页参数
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        //条件值对象
        CmsPage cmsPage = new CmsPage();
         //查询某站点的页面
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //查询别名
        cmsPage.setPageAliase("首页");
        //查询pageName
        //cmsPage.setPageName("4028e58161bd3b380161bd3bcd2f0000.html");
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //一定记得这个等于号
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.startsWith());
        //定义Example
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        List<CmsPage> content = all.getContent();
        System.out.println(content);

    }
    //分页查询
    @Test
    public void testFindPage(){
        int page =0;//从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    //修改
    @Test
    public void testUpdate(){
        //查询对象,Optional主要就是java1.8以后加的一个容器，它就是规范了必须判断是不是空指针，防止报错
        Optional<CmsPage> optional = cmsPageRepository.findById("5a754adf6abb500ad05688d9");
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            //设置需要修改的值
            cmsPage.setPageAliase("首页");
            cmsPageRepository.save(cmsPage);
        }
    }

    //根据页面名称查询
    @Test
    public void testfindByPageName(){
        CmsPage cmsPage  = cmsPageRepository.findByPageName("index.html");
        System.out.println(cmsPage);
    }
}
