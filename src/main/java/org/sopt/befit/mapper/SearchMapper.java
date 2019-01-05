package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Products;
import org.sopt.befit.model.ProductReq;

import java.util.List;

@Mapper
public interface SearchMapper {

    // 브랜드 검색
    @Select("select b.*, (select count(*) from like_brand as lb " +
            "where lb.user_idx = u.idx and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b " +
            "where u.idx = #{user_idx} and (REPLACE(b.name_korean , ' ', '') like CONCAT('%',#{name},'%') " +
            "or REPLACE(b.name_english , ' ', '') like CONCAT('%',#{name},'%'))")
    List<Brands> findBrandsByName(@Param("user_idx") final int user_idx,
                                    @Param("name") final String name);

    // 상품 검색 신상순
    @Select("SELECT (SELECT count(*) FROM like_product where product.idx=product_idx AND user_idx=#{user_idx}) AS is_liked, product.*, " +
            "(SELECT name_korean FROM brand WHERE product.brand_idx = idx) AS brand_name " +
            "FROM product WHERE REPLACE(name, ' ', '') LIKE CONCAT('%',#{name},'%') ORDER BY date DESC;")
    List<ProductReq> findByProductByNameForNew(@Param("user_idx") final int user_idx, @Param("name") final String name);

    // 상품 검색 인기순
    @Select("SELECT (SELECT count(*) FROM like_product where product.idx=product_idx AND user_idx=#{user_idx}) AS is_liked, product.*, " +
            "(SELECT name_korean FROM brand WHERE product.brand_idx = idx) AS brand_name " +
            "FROM product WHERE REPLACE(name, ' ', '') LIKE CONCAT('%',#{name},'%') ORDER BY like_score DESC;")
    List<ProductReq> findByProductByNameForPopular(@Param("user_idx") final int user_idx, @Param("name") final String name);

    // 상품 검색 초기화면
    // 24개 / 성별에 따라서 OK / 찜개수 /
    // 100개 성별 필터림 찜개수 높은거 뽑고, 그중 상위 24개

    @Select("(SELECT * FROM product where brand_idx in (SELECT idx FROM brand WHERE gender = '공용' OR gender = #{gender}) ORDER BY RAND() DESC LIMIT 100) ORDER BY like_score DESC LIMIT 24;")
    List<Products>  firstSearchPage(@Param("gender") final String gender);
}

