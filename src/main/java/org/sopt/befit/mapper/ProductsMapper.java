package org.sopt.befit.mapper;

import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Products;
import org.sopt.befit.model.ProductReq;

import java.util.List;

@Mapper
public interface ProductsMapper {

    @Select("SELECT p.*, " +
            "(SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like , " +
            "(SELECT b.name_korean from brand AS b WHERE p.brand_idx = b.idx) AS brand_Korean_name " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx}")
    List<ProductReq> findAll(@Param("curIdx") final int curIdx);

    @Select("SELECT p.*, " +
            "(SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like , " +
            "(SELECT b.name_korean from brand AS b WHERE p.brand_idx = b.idx) AS brand_Korean_name " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx} AND p.product_category_index = #{product_category_index} ORDER BY date DESC ")
    List<ProductReq> findByCategoryNew(@Param("product_category_index") final int product_category_index, @Param("curIdx") final int curIdx);

    @Select("SELECT p.*, " +
            "(SELECT COUNT(*) from like_product AS lp WHERE lp.user_idx = #{curIdx} AND lp.product_idx = p.idx ) AS product_like , " +
            "(SELECT b.name_korean from brand AS b WHERE p.brand_idx = b.idx) AS brand_Korean_name " +
            "FROM user AS u, product AS p WHERE u.idx = #{curIdx} AND p.product_category_index = #{product_category_index} ORDER BY like_score DESC ")
    List<ProductReq> findByCategoryPopular(@Param("product_category_index") final int product_category_index, @Param("curIdx") final int curIdx);
}
