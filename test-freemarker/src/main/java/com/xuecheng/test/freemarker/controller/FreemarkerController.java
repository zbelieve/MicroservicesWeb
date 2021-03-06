package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;



@RequestMapping("/freemarker")
@Controller     //这儿记得不能用RestController，因为这个是返回的json数据，而这儿应该返回的是html等，这属于字符串
public class FreemarkerController {
    //RestTemplate restTemplate;下面这个需要在启动类中进行配置
    @Autowired
    RestTemplate restTemplate;

    /**测试课程预览的静态化页面+模型数据好不好使
    * */
    @RequestMapping("/course")
    public String course(Map<String, Object> map){
        //使用restTemplate请求模型数据， //url请求返回的是json数据，之后在这儿转换map，关于后面的课程信息都是存在mysql中的coursebase
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseview/4028e581617f945f01617f9dabc40000", Map.class);
        //键值对存在body里面
        Map body = forEntity.getBody();
        //设置模型数据，put的是所有的，到时候模板中取数据的时候以键的名字来获取就可以了
        map.putAll(body);
        return "course1";
    }

    /**
     * 这个是远程请求服务器上地址，由这个地址返回的数据库中的cmsConfig数据，将其加载到模板文件index_banner中，
     * 循环取出所需要的图片地址值，注意要在数据库中将这个值改成ngix服务器下的图片地址
     * 这就完成了一个静态化，将模板和数据结合，生成了一个html文件，放在服务器上
    * */
    @RequestMapping("/banner")
    public String index_banner(Map<String, Object> map){
        //使用restTemplate请求轮播图的模型数据， //url请求返回的是json数据，之后在这儿转换map
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        //键值对存在body里面
        Map body = forEntity.getBody();
        //设置模型数据，put的是所有的，到时候模板中取数据的时候以键的名字来获取就可以了
        map.putAll(body);
        return "index_banner";
    }

    @RequestMapping("/test1")
    public String freemarker(Map<String, Object> map){
        //向数据模型放数据
        map.put("name","黑马程序员");
        map.put("num",1232312323);
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据，这种情况可以用到list标签
        map.put("stus",stus);

        //准备map数据
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        //向数据模型放数据
        map.put("stu1",stu1);
        //向数据模型放数据
        map.put("stuMap",stuMap);
        //返回模板文件名称
        return "test1";
    }
}
