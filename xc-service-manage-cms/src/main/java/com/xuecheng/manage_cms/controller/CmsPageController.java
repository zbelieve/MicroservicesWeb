package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//相当于@Controller加上@ResponseBody(主要就是把响应的结果集转成josn)
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    PageService pageService;

    //显示页面列表信息
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size, QueryPageRequest queryPageRequest){

        //暂时用静态数据

        /**QueryResult<CmsPage> queryResult = new QueryResult<>();
        List<CmsPage> list = new ArrayList<>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        list.add(cmsPage);
        queryResult.setList(list);
        queryResult.setTotal(1);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;**/
        //调用service
        return pageService.findList(page,size,queryPageRequest);
    }
    //新增页面，post是表单提交
    @Override
    @PostMapping("/add")      //@RequestBody的目的是把请求过来的数据转换为json对象
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")//因为要取url里面的值，所以就要这个注解@PathVariable("id")
    public CmsPage findById(@PathVariable("id") String id) {
        //调用service方法
        return pageService.getById(id);
    }

    /**编辑页面
    * */
    @Override
    @PutMapping("/edit/{id}")//这里使用put方法，http方法中put表示更新,@RequestBody是将数据转换为json数据，记得测试的时候就是不是post那种？=
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.update(id,cmsPage);
    }

    /**删除页面
     * */
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }

    /**页面发布
     * */
    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }

    @GetMapping("cms/zipkin")
    public String testZipkin()
    {
        return "我是31001端口返回";
    }
}
