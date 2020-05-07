package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *jpa方式查询
 * 这个jpa里面关于<a,b>,主要就是a实体类型，b就是实体的id类型，可以点进这个实体看一下。
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {
    //SELECT * FROM teachplan WHERE courseid = '297e7c7c62b888f00162b8a7dec20000' AND parentid='0'
    //根据courseId和parentId来查
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
