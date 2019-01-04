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
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = b.idx) as likeFlag " +
            "from user as u, brand as b where u.idx = #{user_idx} and b.name_english like CONCAT(#{initial},'%') " +
            "order by b.name_english ASC")
    List<Brands> getBrandsByInitial(@Param("user_idx") final int user_idx,
                                    @Param("initial") final char initial);

    // 이니셜로 브랜드 조회 ect
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = b.idx) as likeFlag " +
            "from user as u, brand as b where u.idx = #{user_idx} and b.name_english REGEXP '^[^A-Z]' " +
            "order by b.name_english ASC")
    List<Brands> getBrandsEct(@Param("user_idx") final int user_idx,
                                    @Param("initial") final char initial);

    //특정 브랜드 정보 조회 (user_idx 관여)
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = #{brand_idx}) as likeFlag from brand as b where b.idx = #{brand_idx}")
    Brands getBrandInfo(@Param("user_idx") final int user_idx,
                        @Param("brand_idx") final int brand_idx);

    // 성별 브랜드 리스트 조회 - 랭킹 순 브랜드 조회 시
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = #{user_idx} and lb.brand_idx = b.idx) as likeFlag from brand as b where (gender = #{gender} or gender = '공용')")
    List<Brands> getBrandsByGender(@Param("user_idx") final int user_idx,
                                   @Param("gender") final String gender);

    // 랜덤한 브랜드 3개 조회
    @Select("SELECT * FROM brand ORDER BY RAND() LIMIT 3")
    List<Brands> getBrandsByRandomThree();

   // 랭킹 순 브랜드 조회
    @Select("select b.*, (select count(*) from like_brand as lb where lb.user_idx = u.idx and lb.brand_idx = b.idx) as likeFlag from user as u, brand as b where u.idx = 4")
    List<Brands> getBrandsByRank(@Param("user_idx") final int user_idx);

    //특정 브랜드 정보 조회 (user_idx 관여 X)
    @Select("SELECT * FROM brand WHERE idx = #{brand_idx}")
    Brands getBrandsByIdx(@Param("brand_idx") final int brand_idx);

    // 해당 유저가 등록한 브랜드의 모든 스타일 조회
    @Select("select group_concat(brand.style1,\",\",brand.style2 ) from brand, (select brand1_idx, brand2_idx from user where idx=#{user_idx}) as b where b.brand1_idx=brand.idx or b.brand2_idx=brand.idx;")
    String getStylesByResisBrand(@Param("user_idx") final int user_idx);

}
