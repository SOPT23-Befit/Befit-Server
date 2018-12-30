package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.model.TestReq;

import java.util.List;

@Mapper
public interface TestMapper {
    @Select("SELECT * FROM json_test WHERE idx = 23")
    TestReq find23();
}
