package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Likes;
import org.sopt.befit.model.ProductReq;

import java.util.List;

@Mapper
public interface LikesMapper {

    // 좋아요 한 브랜드 조회
    @Select("select b.*, 1 as likeFlag ,lt.idx AS like_idx from brand as b, (select * from like_brand as lb where lb.user_idx = #{user_idx}) as lt  where lt.brand_idx = b.idx ORDER BY like_idx DESC;")
    List<Brands> getLikeBrands(@Param("user_idx") final int user_idx);

    // 브랜드 좋아요
    @Insert("INSERT INTO like_brand(user_idx, brand_idx) VALUES(#{user_idx}, #{brand_idx})")
    @Options(useGeneratedKeys = true, keyColumn = "like_brand.idx")
    void postLikeBrand(@Param("user_idx") final int user_idx, @Param("brand_idx") final int brand_idx);

    // 좋아요 중복 확인
    @Select("select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = #{brand_idx}")
    int isLike(@Param("user_idx") final int user_idx, @Param("brand_idx") final int brand_idx);

    // 브랜드 좋아요 취소
    @Delete("DELETE FROM like_brand where user_idx = #{user_idx} and brand_idx = #{brand_idx}")
    void deleteLikeBrand(@Param("user_idx") final int user_idx, @Param("brand_idx") final int brand_idx);

    // 브랜드 좋아요 갯수 1 증가
    @Update("UPDATE brand SET like_score = like_score + 1 WHERE idx = #{brand_idx}")
    void updateLikeUp(@Param("brand_idx") final int brand_idx);

    // 브랜드 좋아요 갯수 1 감소
    @Update("UPDATE brand SET like_score = like_score - 1 WHERE idx = #{brand_idx}")
    void updateLikeDown(@Param("brand_idx") final int brand_idx);

    //brand like 여부 확인 : 1이면 like, 0이면 unlike
    @Select("SELECT COUNT(*) FROM like_brand where user_idx = #{user_idx} and brand_idx = #{brand_idx}")
    int isLikeBrand(@Param("user_idx") final int user_idx, @Param("brand_idx") final int brand_idx);


    //좋아요한 product 조회
    @Select("select k.*, 1 AS product_like, b.name_english, b.name_korean from brand as b, " +
            "(SELECT p.*, lt.idx as like_idx FROM product AS p, (SELECT * FROM like_product AS lp WHERE lp.user_idx = #{user_idx}) AS lt WHERE lt.product_idx = p.idx) as k  " +
            "WHERE k.brand_idx = b.idx ORDER BY like_idx DESC;")
    List<ProductReq> getLikeProducts(@Param("user_idx") final int user_idx);

    //product like 여부 확인 : 1이면 like, 0이면 unlike
    @Select("SELECT COUNT(*) FROM like_product where user_idx = #{user_idx} and product_idx = #{product_idx}")
    int isLikeProduct(@Param("user_idx") final int user_idx, @Param("product_idx") final int product_idx);

    //product 좋아요
    @Insert("INSERT INTO like_product(user_idx, product_idx) VALUES(#{user_idx}, #{product_idx})")
    void insertLikeProduct(@Param("user_idx") final int user_idx, @Param("product_idx") final int product_idx);

    //product 좋아요 취소
    @Delete("DELETE FROM like_product WHERE user_idx = #{user_idx} AND product_idx = #{product_idx}")
    void deleteLikeProduct(@Param("user_idx") final int user_idx, @Param("product_idx") final int product_idx);

    // product 좋아요 갯수 1 증가
    @Update("UPDATE product SET like_score = like_score + 1 WHERE idx = #{product_idx}")
    void updateProductLikeUp(@Param("product_idx") final int product_idx);

    // product 좋아요 갯수 1 감소
    @Update("UPDATE product SET like_score = like_score - 1 WHERE idx = #{product_idx}")
    void updateProductLikeDown(@Param("product_idx") final int product_idx);


}
