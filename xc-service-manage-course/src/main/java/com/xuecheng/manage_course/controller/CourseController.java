package com.xuecheng.manage_course.controller;


import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    @Autowired
    RestTemplate restTemplate;

    /**查询和展示具体课程的课程计划
     * */
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable() String courseId){
        return courseService.findTeachplanList(courseId);
    }

    /**添加具体课程的课程计划，post方式
     * post接收的是json格式数据，@RequestBody可以将其转换为java对象
     * */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody  Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /**增加课程图片
     * */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic")String pic) {
        return courseService.addCoursePic(courseId,pic);
    }



    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursePic(courseId);
    }

    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    //呈现课程，就是返回课程信息的数据模型,这个数据模型在mysql中的coursebase中
    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseView(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }



    // ====================> zipkin+sleuth
    @GetMapping("/test/zipkin")
    public String testZipkin()
    {
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        String result = restTemplate.getForObject("http://"+serviceId+"/cms/page/cms/zipkin/", String.class);
        return "xx";
    }
}
