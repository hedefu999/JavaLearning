package com.hedefu.mybatis.mapper;

import com.hedefu.mybatis.model.TestBlob;

public interface TestBlobMapper {
    TestBlob getBlobFile(int id);
    int insertBlobFile(TestBlob testBlob);
}
