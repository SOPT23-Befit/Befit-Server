package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Likes;
import org.sopt.befit.mapper.LikesMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.ProductReq;
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
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NO_LIKE_BRAND);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_LIKE_BRAND, brandsList);
    }

    // 브랜드 좋아요
    @Transactional
    public DefaultRes postLikeBrand(final int user_idx, final int brand_idx) {
        try {
            int isLike = likesMapper.isLike(user_idx, brand_idx);
            if(isLike == 0) {
                likesMapper.postLikeBrand(user_idx, brand_idx);
                likesMapper.updateLikeUp(brand_idx);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LIKE_SUCCCESS);
            }
            return DefaultRes.res(StatusCode.CONFLICT, ResponseMessage.LIKE_FAIL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 브랜드 좋아요 취소
    @Transactional
    public DefaultRes deleteLikeBrand(final int user_idx, final int brand_idx) {
        try {
            int isLike = likesMapper.isLike(user_idx, brand_idx);
            if(isLike > 0) {
                likesMapper.deleteLikeBrand(user_idx, brand_idx);
                likesMapper.updateLikeDown(brand_idx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.LIKE_CANCEL_SUCCCESS);
            }
            return DefaultRes.res(StatusCode.CONFLICT, ResponseMessage.LIKE_CANCEL_FAIL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 좋아요 한 상품 리스트 조회
    public DefaultRes getLikeProductss(final int user_idx) {
        final List<ProductReq> productReqList = ProductsService.ListParse(likesMapper.getLikeProducts(user_idx));
        if (productReqList.isEmpty())
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NO_LIKE_PRODUCT);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_LIKE_PRODUCT, productReqList);
    }

    // 상품 좋아요
    @Transactional
    public DefaultRes postLikeProduct(final int user_idx, final int product_idx) {
        try {
            int isLike = likesMapper.isLikeProduct(user_idx, product_idx);
            if(isLike == 0) {
                likesMapper.insertLikeProduct(user_idx, product_idx);
                likesMapper.updateProductLikeUp(product_idx);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LIKE_SUCCCESS);
            }
            return DefaultRes.res(StatusCode.CONFLICT, ResponseMessage.LIKE_FAIL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 상품 좋아요 취소
    @Transactional
    public DefaultRes deleteLikeProduct(final int user_idx, final int product_idx) {
        try {
            int isLike = likesMapper.isLikeProduct(user_idx, product_idx);
            if(isLike > 0) {
                likesMapper.deleteLikeProduct(user_idx, product_idx);
                likesMapper.updateProductLikeDown(product_idx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.LIKE_CANCEL_SUCCCESS);
            }
            return DefaultRes.res(StatusCode.CONFLICT, ResponseMessage.LIKE_CANCEL_FAIL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

}
