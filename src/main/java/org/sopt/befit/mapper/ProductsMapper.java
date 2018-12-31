package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Products;

import java.util.List;

@Mapper
public interface ProductsMapper {

    @Select("SELECT * FROM product")
    List<Products> findAll();



}
