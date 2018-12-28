package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.mapper.BrandsMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BrandsService {

    private final BrandsMapper brandsMapper;

    public BrandsService(BrandsMapper brandsMapper) {
        this.brandsMapper = brandsMapper;
    }

//    // 이니셜로 브랜드 조회
//    public DefaultRes getBrandsByInitial(fianl) {
//        final List<Brands> brandList = brandsMapper.getBrandsByInitial();
//        if (brandList.isEmpty())
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BRAND);
//        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, brandList);
//    }

    // 이니셜로 브랜드 조회
    public DefaultRes getBrandsByInitial(final int user_idx, final Character initial) {
        final List<Brands> brandsList = brandsMapper.getBrandsByInitial(user_idx, initial);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, brandsList);
    }

    // 특정 브랜드 정보 조회
    public DefaultRes getBrandInfo(final int user_idx, final int brand_idx){
        final Brands brandInfo = brandsMapper.getBrandInfo(user_idx, brand_idx);
        if (brandInfo == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, "brand_idx : {" + brand_idx + "} 브랜드 정보 조회 실패");
        return DefaultRes.res(StatusCode.OK, "brand_idx : {" + brand_idx + "} 브랜드 정보 조회 성공", brandInfo);
    }

    // 해당 유저의 브랜드 랭킹 조회
    public DefaultRes getBrandsByRank(final int user_idx){
        final List<Brands> brandList = brandsMapper.getBrandsByRank(user_idx);

        if (brandList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.READ_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_BRAND, brandList);
    }
}
