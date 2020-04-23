package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Jiang
 **/
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
