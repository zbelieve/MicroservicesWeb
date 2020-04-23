package com.xuecheng.manage_cms.dao;



import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**查询CmsTemplate使用的
 * */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
