package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.mapper.SearchMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.SearchReq;
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
    public DefaultRes findBrandsByName(final int user_idx, final SearchReq searchReq) {
        searchReq.setName(searchReq.getName().replace(" ", "").toUpperCase());

        log.info(searchReq.getName());
        final List<Brands> brandsList= searchMapper.findBrandsByName(user_idx, searchReq.getName());
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.BRADN_SEARCH_SUCCESS, brandsList);
    }

    // 상품 이름으로 검색
}
