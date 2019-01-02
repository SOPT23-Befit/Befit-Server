package org.sopt.befit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Closet;
import org.sopt.befit.dto.Products;
import org.sopt.befit.mapper.ClosetMapper;
import org.sopt.befit.model.ClosetReq;
import org.sopt.befit.model.ClosetPostReq;
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

    //mapper로 받은 measure의 string을 jsonNode로 변환
    public JsonNode parseJson(String jsonString){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObejct = mapper.readTree(jsonString);
            return jsonObejct;
        }
        catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ClosetReq> ListParse(List<ClosetReq> closetListReqList){
        for(ClosetReq closetReq: closetListReqList){
            closetReq.setMeasure(parseJson(closetReq.getMeasure().toString()).get(closetReq.getProduct_size()));
        }
        return closetListReqList;
    }

    public List<Products> ListParseProduct(List<Products> closetListReqList){
        for(Products closetReq: closetListReqList){
            closetReq.setMeasure(parseJson(closetReq.getMeasure().toString()));
        }
        return closetListReqList;
    }

    // 나의 옷장 리스트 조회
    public DefaultRes getClosetProduct(final int user_idx, final int category_idx) {
        final List<ClosetReq> brandsList = closetMapper.getClosetProduct(user_idx, category_idx);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.OK, ResponseMessage.NO_CLOSET_ITEM);

        final List<ClosetReq> resultList = ListParse(brandsList);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_READ_SUCCESS, resultList);
    }

    // 나의 옷장 특정 아이템 조회
    public DefaultRes getClosetProductInfo(final int user_idx, final int closet_idx) {
        final ClosetReq closetReq = closetMapper.getClosetProductInfo(user_idx, closet_idx);
        if (closetReq == null)
            return DefaultRes.res(StatusCode.OK, "closet_idx : {" + closet_idx + "} 옷장 아이템 정보 조회 실패");

        closetReq.setMeasure(parseJson(closetReq.getMeasure().toString()).get(closetReq.getProduct_size()));
        return DefaultRes.res(StatusCode.OK, "closet_idx : {" + closet_idx + "} 옷장 아이템 조회 성공", closetReq);
    }

    // 나의 옷장 아이템 등록
    @Transactional
    public DefaultRes postProductToCloset(final int user_idx, final ClosetPostReq closetReq) {
        try {
            int isCloset = closetMapper.isCloset(user_idx, closetReq);
            if(isCloset == 0) {
                closetMapper.postClosetProduct(user_idx, closetReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CLOSET_CREATE_SUCCESS);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_CREATE_FAIL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 나의 옷장 아이템 삭제
    @Transactional
    public DefaultRes deleteProductToCloset(final int user_idx, final String closet_idx) {
        try {
            int count_success = 0;
            String resultMsg = "";
            String[] idx = closet_idx.split(",");

            for(int i=0; i<idx.length; ++i){
                int nowIdx = Integer.parseInt(idx[i]);
                Closet closet = closetMapper.findByClosetIdx(nowIdx);
                if(closet != null) {
                    closetMapper.deleteClosetPrduct(user_idx, nowIdx);
                    count_success++;
                }
            }
            resultMsg += Integer.toString(count_success) + "개 아이템 삭제 성공";
            return DefaultRes.res(StatusCode.OK, resultMsg);
            //return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUNT_CLOSET);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    // 브랜드명, 카테고리 조회
    // 나의 옷장 아이템과 나의 선택 상품 사이즈 비교
    // 나의 옷장 리스트 조회
    public DefaultRes getProductByBrandAndCategory(final int brand_idx, final int category_idx) {
        final List<Products> productsList = closetMapper.getProductByBrandAndCategory(brand_idx, category_idx);
        if (productsList.isEmpty())
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_SEARCH_PRODUCT_FAIL);

        final List<Products> resultList = ListParseProduct(productsList);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_SEARCH_PRODUCT_SUCCESS, resultList);
    }
}
