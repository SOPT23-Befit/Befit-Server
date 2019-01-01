package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Brands;

import java.util.List;

@Mapper
public interface SearchMapper {
    // 브랜드 검색
    @Select("select b.*, (select count(*) from like_brand as lb " +
            "where lb.user_idx = u.idx and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b " +
            "where u.idx = #{user_idx} and (b.name_korean like CONCAT('%',#{name},'%')" +
            "or b.name_english like CONCAT('%',#{name},'%'))")
    List<Brands> findBrandsByName(@Param("user_idx") final int user_idx,
                                    @Param("name") final String name);


    // 상품 검색

//    SELECT (SELECT count(*) FROM like_product where product.idx=product_idx AND user_idx=3) AS is_liked,product.* FROM product WHERE (name like '%코트%');

}

