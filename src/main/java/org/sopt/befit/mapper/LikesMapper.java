package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Brands;

import java.util.List;

@Mapper
public interface LikesMapper {


    // 아직 안짬 - 사용자 추가되면~.~
    @Select("select b.*, 'true' as likeFlag from brand as b, user as u where u.idx = 1")
    List<Brands> getLikeBrands();

    // 브랜드 좋아요
    @Insert("INSERT INTO like_brand(user_idx, brand_idx) VALUES(#{user_idx}, #{brand_idx})")
    //@Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    void postLikeBrand(@Param("user_idx") final int user_idx, @Param("brand_idx") final int brand_idx);

    // 브랜드 좋아요 취소

}
