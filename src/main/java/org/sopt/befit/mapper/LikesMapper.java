package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Likes;

import java.util.List;

@Mapper
public interface LikesMapper {

    // 좋아요 한 브랜드 조회
    @Select("select b.*, 1 as likeFlag from brand as b, (select * from like_brand as lb where lb.user_idx = #{user_idx}) as lt where lt.brand_idx = b.idx;")
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
    @Update("UPDATE brand SET score = score + 1 WHERE idx = #{brand_idx}")
    void updateLikeUp(@Param("brand_idx") final int brand_idx);

    // 브랜드 좋아요 갯수 1 감소
    @Update("UPDATE brand SET score = score - 1 WHERE idx = #{brand_idx}")
    void updateLikeDown(@Param("brand_idx") final int brand_idx);

}
