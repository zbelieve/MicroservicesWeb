package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RequestMapping("/test")
@Controller     //这儿记得不能用RestController，因为这个是返回的json数据，而这儿应该返回的是html等，这属于字符串
public class testController {
    @RequestMapping("/test2")
    public String index_banner(Model model){
        model.addAttribute("msg", "后台传的数据...");
        return "index";
    }

}
