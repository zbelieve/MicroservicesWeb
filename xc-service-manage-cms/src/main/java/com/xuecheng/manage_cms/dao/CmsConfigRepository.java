package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;


/**查询CmsConfig类型数据用的
* */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
