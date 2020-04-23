package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jiang
 **/
@Api(value="文件管理接口",description = "文件管理接口，提供文件的增、删、改、查")
public interface FileSystemControllerApi {

    //上传文件
    /**multipartFile代表文件本身
     * filetag文件标签
     * businesskey业务标识
     * metadata文件元信息：这个将来是一个json格式数据，传上来之后转成map存储到数据库中
   * */
    @ApiOperation("上传文件接口")
    public UploadFileResult  upload(MultipartFile multipartFile,
                                    String filetag,
                                    String businesskey,
                                    String metadata);

}
