package com.xuecheng.api.course;


import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理接口",description = "课程管理接口。提供课程的增删改查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询（就是具体的某个课程内查询他的两级目录）")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划（就是具体的某个课程内添加他的两级目录）")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("查询课程图片")
    public CoursePic findCoursePic(String courseId);


    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    public CourseView courseView(String id);
}
