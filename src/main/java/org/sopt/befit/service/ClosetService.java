package org.sopt.befit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Closet;
import org.sopt.befit.dto.Products;
import org.sopt.befit.mapper.ClosetMapper;
import org.sopt.befit.mapper.ProductsMapper;
import org.sopt.befit.model.*;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClosetService {

    private final ClosetMapper closetMapper;
    private final ProductsMapper productMapper;

    public ClosetService(ClosetMapper closetMapper, ProductsMapper productMapper) {
        this.closetMapper = closetMapper;
        this.productMapper = productMapper;
    }

    //mapper로 받은 measure의 string을 jsonNode로 변환
    public JsonNode parseJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObejct = mapper.readTree(jsonString);
            return jsonObejct;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ClosetReq> ListParse(List<ClosetReq> closetListReqList) {
        for (ClosetReq closetReq : closetListReqList) {
            closetReq.setMeasure(parseJson(closetReq.getMeasure().toString()).get(closetReq.getProduct_size()));
        }
        return closetListReqList;
    }

    public List<Products> ListParseProduct(List<Products> closetListReqList) {
        for (Products closetReq : closetListReqList) {
            closetReq.setMeasure(parseJson(closetReq.getMeasure().toString()));
        }
        return closetListReqList;
    }

    // 나의 옷장 리스트 조회
    public DefaultRes getClosetProduct(final int user_idx, final int category_idx) {
        final List<ClosetReq> brandsList = closetMapper.getClosetProduct(user_idx, category_idx);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_CLOSET_ITEM);

        final List<ClosetReq> resultList = ListParse(brandsList);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_READ_SUCCESS, resultList);
    }

    // 나의 옷장 특정 아이템 조회
    public DefaultRes getClosetProductInfo(final int user_idx, final int closet_idx) {
        final ClosetReq closetReq = closetMapper.getClosetProductInfo(user_idx, closet_idx);
        if (closetReq == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, "closet_idx : {" + closet_idx + "} 옷장 아이템 정보 조회 실패");

        closetReq.setMeasure(parseJson(closetReq.getMeasure().toString()).get(closetReq.getProduct_size()));
        return DefaultRes.res(StatusCode.OK, "closet_idx : {" + closet_idx + "} 옷장 아이템 조회 성공", closetReq);
    }

    // 나의 옷장 아이템 등록
    @Transactional
    public DefaultRes postProductToCloset(final int user_idx, final ClosetPostReq closetReq) {
        try {
            int isCloset = closetMapper.isCloset(user_idx, closetReq);
            if (isCloset == 0) {
                closetMapper.postClosetProduct(user_idx, closetReq);
                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CLOSET_CREATE_SUCCESS);
            }
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.CLOSET_CREATE_FAIL);
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

            for (int i = 0; i < idx.length; ++i) {
                int nowIdx = Integer.parseInt(idx[i]);
                Closet closet = closetMapper.findByClosetIdx(nowIdx);
                if (closet != null) {
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
    public DefaultRes getProductByBrandAndCategory(final int brand_idx, final int category_idx) {
        final List<Products> productsList = closetMapper.getProductByBrandAndCategory(brand_idx, category_idx);
        if (productsList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.CLOSET_SEARCH_PRODUCT_FAIL);

        final List<Products> resultList = ListParseProduct(productsList);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_SEARCH_PRODUCT_SUCCESS, resultList);
    }

    // 나의 옷장 아이템과 나의 선택 상품 사이즈 비교
    public DefaultRes compareProduct(final int user_idx, final int closet_idx, final int product_idx, final String product_size) {

        if (!product_size.equals("")) {

            DecimalFormat df = new DecimalFormat("#.##"); // 소숫점 뒤 0일 경우 제거
            MeasureConvert measureConvert = new MeasureConvert();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.createObjectNode();
            JsonNode resultObject = mapper.createObjectNode();

            final SizeCompareReq myProduct = closetMapper.getCompareMyProduct(user_idx, closet_idx);
            final SizeCompareReq compareProduct = closetMapper.getCompareOtherProduct(product_idx);

            if (myProduct == null)  return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUNT_CLOSET);
            if(compareProduct == null)  return DefaultRes.res(StatusCode.OK, ResponseMessage.CLOSET_SEARCH_PRODUCT_FAIL);

            int count = 0, elementCount = 0;
            double sum = 0.0, diffSum = 0.0;
            final int myCategory = myProduct.getProduct_category_index();
            final int compareCategory = compareProduct.getProduct_category_index();

            int weight;
            if (myProduct.getProduct_category_index() == 13) weight = 1;
            else if (myProduct.getProduct_category_index() == 12) weight = 2;
            else weight = 3;

            if ((myProduct != null && compareProduct != null)
                    && myCategory == compareCategory) {

                // 해당 카테고리에 필요한 치수 keys
                String[] measure = measureConvert.convert(myProduct.getProduct_category_index());

                // product_size json parsing
                myProduct.setMeasure(parseJson(myProduct.getMeasure().toString()).get(myProduct.getProduct_size()));
                compareProduct.setProduct_size(product_size);
                compareProduct.setMeasure(parseJson(compareProduct.getMeasure().toString()).get(compareProduct.getProduct_size().trim()));                JsonNode my = parseJson(myProduct.getMeasure().toString());
                JsonNode compare = null;
                if(compareProduct.getMeasure() != null) compare = parseJson(compareProduct.getMeasure().toString());

                if (compare != null && my != null) {
                    log.info(myProduct.getProduct_size() + " : " + myProduct.getProduct_category_index() + " : "
                            + myProduct.getMeasure());
                    log.info(compareProduct.getProduct_size() + " : " + compareProduct.getProduct_category_index() + " : "
                            + compareProduct.getMeasure());


                    for (int i = 0; i < measure.length; ++i) {
                        if ((my.get(measure[i]) != null && compare.get(measure[i]) != null) &&
                                (!my.get(measure[i]).toString().equals("null") &&
                                        !compare.get(measure[i]).toString().equals("null"))) {

                            String myValue = my.get(measure[i]).toString().replaceAll("\"", "");
                            String compareValue = compare.get(measure[i]).toString().replaceAll("\"", "");

                            if ((myValue != null && compareValue != null)) {
                                double myMeasure = Double.parseDouble(myValue);
                                double compareMeasure = Double.parseDouble(compareValue);

                                double different = compareMeasure - myMeasure;
                                diffSum += different;
                                if (different > 0) ++elementCount;
                                else if (different < 0) --elementCount;
                                ((ObjectNode) jsonObject).put(measure[i], df.format(different));
                                sum += (Math.abs(different) / myMeasure) * 1 / measure.length;

                                count++;
                            }
                        }
                    }

                    if (count == 0) return DefaultRes.res(StatusCode.OK, ResponseMessage.PRODUCT_COMPARE_FAIL);

                    sum = 100.0 - (sum * weight * 100);
                    ((ObjectNode) resultObject).put("measure", jsonObject);
                    ((ObjectNode) resultObject).put("percent", Long.toString(Math.round(sum)));

                    String column = "";
                    if (elementCount > 0) column = "large";
                    else if (elementCount < 0) column = "small";
                    else if (elementCount == 0) {
                        if (diffSum > 0) column = "large";
                        else if (diffSum < 0) column = "small";
                        else if (diffSum == 0) column = "medium";
                    }
                    sizeCompareLink link = closetMapper.getLink(column, myCategory);
                    ((ObjectNode) resultObject).put("my_url", link.getMy_url());
                    ((ObjectNode) resultObject).put("compare_url", link.getCompare_url());

                    return DefaultRes.res(StatusCode.OK, ResponseMessage.PRODUCT_COMPARE_SUCCESS, resultObject);
                }
                return DefaultRes.res(StatusCode.OK, ResponseMessage.PRODUCT_COMPARE_FAIL);
            }
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.PRODUCT_COMPARE_FAIL);
    }
}
