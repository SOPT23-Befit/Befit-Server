package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Likes;
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
    public DefaultRes getLikeBrands(final int user_idx) {
        final List<Brands> brandsList = likesMapper.getLikeBrands(user_idx);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_LIKE_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_LIKE_BRAND, brandsList);
    }

    // 브랜드 좋아요
    @Transactional
    public DefaultRes postLikeBrand(final int user_idx, final int brand_idx) {
        try {
            int isLike = likesMapper.isLike(user_idx, brand_idx);
            if(isLike == 0) {
                likesMapper.postLikeBrand(user_idx, brand_idx);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LIKE_SUCCCESS);
            }
            return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.LIKE_FAIL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 브랜드 좋아요
    @Transactional
    public DefaultRes deleteLikeBrand(final int user_idx, final int brand_idx) {
        try {
            int isLike = likesMapper.isLike(user_idx, brand_idx);
            if(isLike > 0) {
                likesMapper.deleteLikeBrand(user_idx, brand_idx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.LIKE_CANCEL_SUCCCESS);
            }
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.LIKE_CANCEL_SUCCCESS);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

}
