package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

//@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    //页面查询
    //@ApiOperation("新增页面")这个是用swggger调试时候用的
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    //新增页面
    public CmsPageResult add(CmsPage cmsPage);

    //根据页面id查询页面信息
    public CmsPage findById(String id);

    //修改页面,要先执行上面的查询，这样就能把页面信息找到，然后传过去，之后饭就页面结果，是不是修改成功了
    public CmsPageResult edit(String id,CmsPage cmsPage);

    //删除页面，返回结果无需是CmsPage，只要返回是否成功的
    public ResponseResult delete(String id);

    //页面发布
    @ApiOperation("删除页面")
    public ResponseResult post(String pageId);
}
