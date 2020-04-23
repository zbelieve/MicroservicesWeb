package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**查询时候CmsPage用的
 * */
//主要要提供查询的类型和传入的类型
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
 
}
