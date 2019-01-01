package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.mapper.SearchMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.ProductReq;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SearchService {

    private final SearchMapper searchMapper;

    public SearchService(SearchMapper searchMapper) {
        this.searchMapper = searchMapper;
    }

    // 브랜드 이름으로 검색
    public DefaultRes findBrandsByName(final int user_idx, final String name) {
        final List<Brands> brandsList= searchMapper.findBrandsByName(user_idx, name);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, brandsList);
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
}
