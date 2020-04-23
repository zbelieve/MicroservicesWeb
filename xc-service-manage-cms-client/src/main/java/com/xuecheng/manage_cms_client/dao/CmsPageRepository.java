package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**查询时候CmsPage用的
 * */
//主要要提供查询的类型和传入的类型
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称来查询(自定义方法查询)
    CmsPage findByPageName(String PageName);
    //根据页面名称、站点id、页面webpath查询，这儿的dao方法不用写，因为他底层自带了save方法，继承了mongod方法
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String sieteId, String pageWebPath);
}
