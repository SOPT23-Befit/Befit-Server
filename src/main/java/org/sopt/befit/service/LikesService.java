package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.mapper.LikesMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class LikesService {

    private final LikesMapper likesMapper;

    public LikesService(LikesMapper likesMapper) {
        this.likesMapper = likesMapper;
    }

    // 좋아요 한 브랜드 리스트 조회
    @Transactional
    public DefaultRes getLikeBrands() {
        //    final List<User> userList = userMapper.findAll();
        final List<Brands> brandList = likesMapper.getLikeBrands();
        if (brandList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, brandList);
    }

    // 브랜드 좋아요
    @Transactional
    public DefaultRes postLikeBrand(final int user_idx, final int brand_idx) {
        try {
            likesMapper.postLikeBrand(user_idx, brand_idx);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }

    }
}
