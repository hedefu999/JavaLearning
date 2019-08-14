package com.mytest.mybatis.mapper;

import com.mytest.mybatis.model.TestBlob;

public interface TestBlobMapper {
    TestBlob getBlobFile(int id);
    int insertBlobFile(TestBlob testBlob);
}
