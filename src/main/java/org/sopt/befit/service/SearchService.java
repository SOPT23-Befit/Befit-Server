package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Products;
import org.sopt.befit.dto.User;
import org.sopt.befit.mapper.SearchMapper;
import org.sopt.befit.mapper.UserMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.ProductReq;
import org.sopt.befit.model.SearchReq;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SearchService {

    private final UserMapper userMapper;

    private final SearchMapper searchMapper;

    public SearchService(final SearchMapper searchMapper, final UserMapper userMapper) {
        this.searchMapper = searchMapper;
        this.userMapper = userMapper;
    }

    // 브랜드 이름으로 검색
    public DefaultRes findBrandsByName(final int user_idx, final String name) {
        String brand_name = name.replace(" ", "").toUpperCase();

        log.info(brand_name);
        final List<Brands> brandsList= searchMapper.findBrandsByName(user_idx, brand_name);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.BRADN_SEARCH_SUCCESS, brandsList);
    }

    // 상품 이름으로 검색 신상순
    public DefaultRes findPrdocutsByNameForNew(final int user_idx, final String name) {
        final List<ProductReq> productsList = searchMapper.findByProductByNameForNew(user_idx, name);

        if(productsList.isEmpty()){
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_PRODUCTS);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SEARCH_PRODUCTS_NEW,ProductsService.ListParse(productsList));
    }
    // 상품 이름으로 검색 인기순
    public DefaultRes findPrdocutsByNameForPopular(final int user_idx, final String name) {
        final List<ProductReq> productsList = searchMapper.findByProductByNameForPopular(user_idx, name);
        if(productsList.isEmpty()){
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_PRODUCTS);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SEARCH_PRODUCTS_POPULAR,ProductsService.ListParse(productsList));
    }

    // 상품 검색 초기화면
    public DefaultRes fisrtSearchPage(final int user_idx){
        String gender = userMapper.findByUserIdx(user_idx).getGender();
        List<ProductReq> productsList = searchMapper.firstSearchPage(gender);
        for(ProductReq productReq : productsList){
            productReq.setMeasure(ProductsService.parseJson(productReq.getMeasure().toString()));
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FIRST_SEARCH_PAGE_PRODUCTS, productsList);
    }
}
