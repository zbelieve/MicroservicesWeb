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
        outputStream.write(pageHtml.getBytes("utf-8"));
    }

}
