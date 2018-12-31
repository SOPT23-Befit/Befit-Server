package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Closet;
import org.sopt.befit.mapper.ClosetMapper;
import org.sopt.befit.model.ClosetListReq;
import org.sopt.befit.model.ClosetReq;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class ClosetService {

    private final ClosetMapper closetMapper;

    public ClosetService(ClosetMapper closetMapper) {
        this.closetMapper = closetMapper;
    }

    // 나의 옷장 아이템 등록
    @Transactional
    public DefaultRes postProductToCloset(final int user_idx, final ClosetReq closetReq) {
        try {
            int isCloset = closetMapper.isCloset(user_idx, closetReq);
            if(isCloset == 0) {
                closetMapper.postClosetProduct(user_idx, closetReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CLOSET_CREATE_SUCCESS);
            }
            return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.CLOSET_CREATE_FAIL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    // 나의 옷장 아이템 삭제
    @Transactional
    public DefaultRes deleteProductToCloset(final int user_idx, final int closet_idx) {
        try {
            Closet closet = closetMapper.findByClosetIdx(closet_idx);
            if(closet != null) {
                closetMapper.deleteClosetPrduct(user_idx, closet_idx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_DELETE_SUCCESS);
            }
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUNT_CLOSET);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 나의 옷장 리스트 조회
    public DefaultRes getClosetProduct(final int user_idx, final int category_idx) {
        final List<ClosetListReq> brandsList = closetMapper.getClosetProduct(user_idx, category_idx);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_CLOSET_ITEM);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_READ_SUCCESS, brandsList);
    }

    // 나의 옷장 특정 아이템 조회

    // 나의 옷장 아이템과 나의 선택 상품 사이즈 비교
}
