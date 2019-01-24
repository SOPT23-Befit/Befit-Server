package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.befit.dto.Closet;
import org.sopt.befit.dto.Products;
import org.sopt.befit.model.ClosetReq;
import org.sopt.befit.model.ClosetPostReq;
import org.sopt.befit.model.SizeCompareReq;
import org.sopt.befit.model.sizeCompareLink;

import java.util.List;

@Mapper
public interface ClosetMapper {

    // 나의 옷장 아이템 등록
    @Insert("INSERT INTO user_closet(user_idx, product_idx, product_size) " +
            "VALUES(#{user_idx}, #{closetReq.product_idx}, #{closetReq.product_size})")
    @Options(useGeneratedKeys = true, keyColumn = "like_brand.idx")
    void postClosetProduct(@Param("user_idx") final int user_idx, @Param("closetReq") final ClosetPostReq closetReq);

    // 옷장 중복 확인
    @Select("select count(*) from user_closet as uc where uc.user_idx = #{user_idx} " +
            "and uc.product_idx = #{closetReq.product_idx} and uc.product_size = #{closetReq.product_size}")
    int isCloset(@Param("user_idx") final int user_idx, @Param("closetReq") final ClosetPostReq closetReq);

    // 나의 옷장 아이템 삭제
    @Delete("DELETE FROM user_closet where idx = #{closet_idx}")
    void deleteClosetPrduct(@Param("user_idx") final int user_idx, @Param("closet_idx") final int closet_idx);

    // 옷장 idx로 아이템 조회 - 삭제 할 때 있는지 확인
    @Select("SELECT * FROM user_closet WHERE idx = #{closet_idx}")
    Closet findByClosetIdx(@Param("closet_idx") final int closet_idx);

    // 나의 옷장 리스트 조회
    @Select("select cwp.closet_idx, b.name_korean, b.name_english, cwp.name, cwp.image_url, cwp.product_category_idx, cwp.product_size, cwp.measure\n" +
            "    from brand as b,\n" +
            "            (select (closet.idx) as closet_idx, p.*, closet.product_size from product as p,\n" +
            "            (select uc.idx, uc.product_idx, uc.product_size from user_closet as uc\n" +
            "    where uc.user_idx = #{user_idx}) as closet where p.idx = closet.product_idx) as cwp\n" +
            "    where cwp.product_category_idx = #{category_idx} and cwp.brand_idx = b.idx order by cwp.closet_idx desc")
    List<ClosetReq> getClosetProduct(@Param("user_idx") final int user_idx,
                                     @Param("category_idx") final int category_idx);
    
    // 나의 옷장 특정 아이템 조회
    @Select("select cwp.closet_idx, b.name_korean, b.name_english, cwp.name, cwp.image_url, cwp.product_category_idx, cwp.product_size, cwp.measure\n" +
            "    from brand as b,\n" +
            "            (select (closet.idx) as closet_idx, p.*, closet.product_size from product as p,\n" +
            "            (select uc.idx, uc.product_idx, uc.product_size from user_closet as uc\n" +
            "    where uc.user_idx = #{user_idx}) as closet where p.idx = closet.product_idx) as cwp\n" +
            "    where cwp.closet_idx = #{closet_idx} and cwp.brand_idx = b.idx order by cwp.closet_idx desc")
    ClosetReq getClosetProductInfo(@Param("user_idx") final int user_idx,
                                   @Param("closet_idx") final int closet_idx);

    // 옷장 아이템 등록 시 상품 검색 - 브랜드명 + 카테고리
    @Select("SELECT * FROM product WHERE brand_idx = #{brand_idx} and product_category_idx = #{category_idx} " +
            "order by product.name desc")
    List<Products> getProductByBrandAndCategory (@Param("brand_idx") final int brand_idx,
                                           @Param("category_idx") final int category_idx);

    // 나의 옷장 아이템과 나의 선택 상품 사이즈 비교
    @Select("select p.product_category_idx, p.measure, uc.product_size from user_closet as uc, product as p where uc.idx = #{closet_idx} and p.idx = uc.product_idx")
    SizeCompareReq getCompareMyProduct (@Param("user_idx") final int user_idx,
                                        @Param("closet_idx") final int closet_idx);

    @Select("select * from product as p where p.idx = #{product_idx}")
    SizeCompareReq getCompareOtherProduct (@Param("product_idx") final int product_idx);

    @Select("select my as my_url, ${column} as compare_url from compare_image where product_category_idx = #{category_idx}")
    sizeCompareLink getLink (@Param("column") final String column, @Param("category_idx") final int category_idx);
}
