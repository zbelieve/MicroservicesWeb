package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**主要是用来预览页面用的
 * */
@Controller
public class CmsPagePreviewController extends BaseController {
    @Autowired
    PageService pageService;

    //页面预览，输入就是一个cms_page的主键
    @RequestMapping(value = "cms/preview/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String Id) throws IOException {
        //执行静态化
        String pageHtml = pageService.getPageHtml(Id);
        //通过response对象输出内容,建立一个输出流
        ServletOutputStream outputStream = response.getOutputStream();
        //原来做轮播图并没用用到ssi,所以没有加ssi，而现在做课程预览界面是有ssi的，所以加了这个
        //由于Nginx先请求cms的课程预览功能得到html页面，再解析页面中的ssi标签，这里必须保证cms页面预览返回的
        //页面的Content-Type为text/html;charset=utf-8
        response.setHeader("Content-type","text/html;charset=utf-8");
        outputStream.write(pageHtml.getBytes("utf-8"));
    }

}
