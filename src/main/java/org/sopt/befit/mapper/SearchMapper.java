package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Brands;

import java.util.List;

@Mapper
public interface SearchMapper {
    // 브랜드 이름으로 조회
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = u.idx and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b where u.idx = 4 and (b.name_korean like CONCAT('%',#{name},'%') or b.name_english like CONCAT('%',#{name},'%'))")
    List<Brands> findBrandsByName(@Param("name") final String name);

    // 검색으로 이동해야함
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = u.idx and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b where u.idx = 4 and (b.name_korean like CONCAT('%',#{initial},'%') or b.name_english like CONCAT('%',#{initial},'%'))")
    List<Brands> getBrands(@Param("initial") final String initial);
}

