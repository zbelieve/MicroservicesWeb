package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;

import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsConfigRepository cmsConfigRepository;

    @Autowired
    RestTemplate restTemplate;//这个首先需要在启动类中配置，就是用来请求http接口的

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;//用来才做gridfs的
    //需要在config下创建这个类，然后注入
    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * //页面查询方法
     * @param page 页码，从1计数开始
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page,int size,QueryPageRequest queryPageRequest){
        /**
         * 1.这是自定义查询条件
         */
        CmsPage cmsPage = new CmsPage();
        //如果queryPageRequest传过来的是空的，那么他就不是一个对象，为了下面不报错，在此处理下
        if (queryPageRequest==null) {
            queryPageRequest = new QueryPageRequest();
        }
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.startsWith());
        //设置站点查询
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置别名查询
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //设置站点名称查询
        if(StringUtils.isNotEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }
        //设置模板id查询
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //定义条件对象

        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        /**
         * 2.分页
         */
        //分页参数
        if(page<=0){
            page = 1;
        }
        //未来真的传给dao的时候page一定要减去1，因为在上面正常认为第一页是1
        page = page - 1;
        //如果size不足的话，默认显示10条
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    //新增页面,不带判断异常的
//    public CmsPageResult add(CmsPage cmsPage){
//        //校验页面名称、站点id、页面webpath的唯一性
//        //根据页面名称、站点Id、页面webpath去查询cms_page集合，如果查到，则此页面已经存在，如果查不到，继续添加
//        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
//        //调用dao新增页面
//        if(cmsPage1==null){
//            //保证主键为空，防止用户恶意插入，这样就可以保证是由mongodb自动生成的主键
//            cmsPage.setPageId(null);
//            //调用dao新增页面，这个在CmsPageRepository中继承了mongodb方法，继承的这个父方法里面包含了增删改查
//            cmsPageRepository.save(cmsPage);
//            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
//        }
//        //当添加失败的时候返回为空
//        return new CmsPageResult(CommonCode.FAIL,null);
//
//    }
    //新增页面,带判断异常，先处理异常，再处理正常逻辑
    public CmsPageResult add(CmsPage cmsPage){
        if(cmsPage==null){
            //抛出异常，非法参数异常

        }
        //校验页面名称、站点id、页面webpath的唯一性
        //根据页面名称、站点Id、页面webpath去查询cms_page集合，如果查到，则此页面已经存在，如果查不到，继续添加
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
        //这儿如果页面不为空了，就没必要往下执行了,如果找到了有页面了，那么就不能添加了
        if(cmsPage1!=null){
            //页面已经存在
            //抛出异常，异常内容就是页面已经存在
            //本来用下面这一行代码即可，但是为了复用性强，将其封装到ExceptionCast类中
            //这儿传的参数只要是实现resultCode的类既可,下面传的是关于cms的code
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //保证主键为空，防止用户恶意插入，这样就可以保证是由mongodb自动生成的主键
        cmsPage.setPageId(null);
        //调用dao新增页面，这个在CmsPageRepository中继承了mongodb方法，继承的这个父方法里面包含了增删改查
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);


    }

    /**根据页面id查询页面
    * */
    public CmsPage getById(String id){
        //查询对象,Optional主要就是java1.8以后加的一个容器，它就是规范了必须判断是不是空指针，防止报错
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }
    /**修改页面,cmsPage代表传入数据对象和传入的id
     * */
    public CmsPageResult update(String id,CmsPage cmsPage){
        //根据id从数据库查询页面信息,this代表当前的对象
        CmsPage one =  this.getById(id);
        if(one!=null){
            //将值进行改变
            one.setSiteId(cmsPage.getSiteId());
            one.setPageAliase(cmsPage.getPageAliase());
            one.setPageWebPath(cmsPage.getPageWebPath());
            one.setPageName(cmsPage.getPageName());
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            one.setDataUrl(cmsPage.getDataUrl());
            one.setTemplateId(cmsPage.getTemplateId());
            //调用dao里面的方法提交保存修改,这个dao里面的方法是从mongodb里面继承的
            cmsPageRepository.save(one);
            //返回一个执行成功的result信息。
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }
    /**删除页面
     * */
    public ResponseResult delete(String id){
        CmsPage one =  this.getById(id);
        if(one!=null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**二。查询Cmsconfig的信息
     * */
    /**1。根据id查询CmsConfig
     * */
    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }


    //页面静态化方法
    /**
     * 静态化程序获取页面的DataUrl,
     *
     * 静态化程序远程请求DataUrl获取数据模型。
     *
     * 静态化程序获取页面的模板信息
     *
     * 执行页面静态化
     *
     * 在test中的PageServiceTest进行了测试
     */
    public String getPageHtml(String pageId){

        //1.获取“数据模型”，为什么要获取模型数据呢，因为最后要将模型数据model和模板template结合进行静态化
        //在这就是做一个轮播图的静态化，那么肯定要获得轮播图的模型数据，这个模型数据是通过
        //查询Cmspage的id获取Cmspage页面，然后获得其中的dataurl，这个是请求服务器的一个url
        //是为了获取服务器的/getmodel/{id}这个方法返回的CmsConfig的模型数据，模型数据里面存着三张照片的地址
        //之后将这个模型数据加载到模板中，进行页面静态化
        Map model = getModelByPageId(pageId);
        if(model == null){
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //2.获取页面的“模板信息”，就是没用数据加入的html页面，就是获取到存入gridFs的模板，这个要查当初
        //模板存的id，在这对应的是templateFiledId，这个模板就是GridFsTest测试存入的，就是将一个没有确切数据的
        //模板存入到GridFsTest，在这主要先是获取到根据cms_template的id去查cms_template的templateFiledId
        String template = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化，把模型数据+模板放到一起，变成html
        String html = generateHtml(template, model);
        return html;

    }
    //执行静态化
    private String generateHtml(String templateContent, Map model ){
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template");
            //调用api进行静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取页面的"模板"信息
    private String getTemplateByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中取模板文件内容
            //根据文件id查询文件，gridFsTemplate是无缝对接mongodbd的自带方法，取出gridfs对应的数据，就是根据id查询
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            //从流中取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    //获取"模型数据"
    /**就是获取id对应的cms_page，然后取出其中的dataurl，这个dataurl指向服务器的地址，拿出模型数据,
     * 之后会将他渲染进模板中
    * */
    private Map getModelByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //取出页面的dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            //页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求dataUrl获取数据，将其转换为map类型，这个方法是一个请求restfulhttp接口的方法
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;

    }

    /**
     * 页面发布功能
     * */
    public ResponseResult post(String pageId){
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化文件存储到GridFs中
        CmsPage cmsPage = saveHtml(pageId, pageHtml);
        //向MQ发消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);

    }

    //向mq 发送消息
    private void sendPostPage(String pageId){
        //得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //创建消息对象
        Map<String,String> msg = new HashMap<>();
        msg.put("pageId",pageId);
        //转成json串
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //站点id
        String siteId = cmsPage.getSiteId();
        //第一个参数是交换机,siteId这就是routinekey
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);
    }

    //保存html到GridFS
    private CmsPage saveHtml(String pageId,String htmlContent){
        //先得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage==null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        ObjectId objectId = null;
        try {
            //将htmlContent内容转成输入流
            InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
            //将html文件内容保存到GridFS
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

}
