package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.sopt.befit.dto.Brands;

import java.util.List;

@Mapper
public interface BrandsMapper {
    // 모든 브랜드 조회 - 사용 안할 듯
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = b.idx) as likeFlag from brand as b")
    List<Brands> getBrands(@Param("user_idx") final int user_idx);

    // 이니셜로 브랜드 조회
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b where u.idx = #{user_idx} and b.name_english like CONCAT(#{initial},'%')")
    List<Brands> getBrandsByInitial(@Param("user_idx") final int user_idx,
                                    @Param("initial") final char initial);

    //특정 브랜드 정보 조회
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = #{brand_idx}) as likeFlag from brand as b where b.idx = #{brand_idx}")
    Brands getBrandInfo(@Param("user_idx") final int user_idx,
                        @Param("brand_idx") final int brand_idx);

    // 랭킹 순 브랜드 조회
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = u.idx and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b where u.idx = 4")
    List<Brands> getBrandsByRank(@Param("user_idx") final int user_idx);

    // 성별 브랜드 리스트 조회 - 랭킹 순 브랜드 조회 시
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = b.idx) as likeFlag from brand as b where (gender = #{gender} or gender = '공용')")
    List<Brands> getBrandsByGender(@Param("user_idx") final int user_idx,
                                   @Param("gender") final String gender);
}
