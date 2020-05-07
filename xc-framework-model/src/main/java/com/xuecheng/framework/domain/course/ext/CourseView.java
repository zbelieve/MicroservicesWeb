package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jiang
 **/
@Data
@NoArgsConstructor//会建立一个无参构造方法
@ToString
public class CourseView implements java.io.Serializable {//继承这个接口，防止这个信息将来被序列化
    private CourseBase courseBase;//基础信息
    private CoursePic coursePic;//课程营销
    private CourseMarket courseMarket;//课程图片
    private TeachplanNode teachplanNode;//教学计划
}
