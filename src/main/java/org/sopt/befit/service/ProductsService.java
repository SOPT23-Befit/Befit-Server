package org.sopt.befit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Products;
import org.sopt.befit.dto.User;
import org.sopt.befit.mapper.BrandsMapper;
import org.sopt.befit.mapper.LikesMapper;
import org.sopt.befit.mapper.ProductsMapper;
import org.sopt.befit.mapper.UserMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.ProductReq;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class ProductsService
{

    final ProductsMapper productsMapper;

    final LikesMapper likesMapper;

//    final BrandRepository brandRepository;

    final BrandsMapper brandsMapper;

    final UserMapper userMapper;

    ProductsService(final ProductsMapper productsMapper, final LikesMapper likesMapper, final BrandsMapper brandsMapper, final UserMapper userMapper){
        this.productsMapper = productsMapper;
        this.likesMapper = likesMapper;
        this.brandsMapper = brandsMapper;
        this.userMapper = userMapper;
    }

    //mapper로 받은 measure의 string을 jsonNode로 변환
    static public JsonNode parseJson(String jsonString){
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

    static public List<ProductReq> ListParse(List<ProductReq> productReqList){
        for(ProductReq productReq: productReqList){
            productReq.setMeasure(parseJson(productReq.getMeasure().toString()));
        }
        return productReqList;
    }


    //controller 관련 method (return : DefaultRes)


    public DefaultRes findAllProducts(final int curIdx){ //curIdx : 현재 접속한 유저의 idx
        try{

            List<ProductReq> result = ListParse( productsMapper.findAll(curIdx));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_PRODUCTS, result);
        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_READ_PRODUCTS);
        }
    }

    //product의 category조회 신상품순
    public DefaultRes findCategoryProductsByNew(final int curIdx, final int category, final String gender){
        try{
            List<ProductReq> result = ListParse( productsMapper.findByCategoryNew(category, curIdx, gender));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CATEGORY_PRODUCTS_NEW,  result);
        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_READ_PRODUCTS);
        }
    }

    //product의 category조회 인기순
    public DefaultRes findCategoryProductsByPopular(final int curIdx, final int category, final String gender){
        try{
            List<ProductReq> result = ListParse( productsMapper.findByCategoryPopular(category, curIdx, gender));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CATEGORY_PRODUCTS_POPULAR, result);
        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_READ_PRODUCTS);
        }
    }

    //product의 brand 조회 신상품순
    public DefaultRes findBrandProductsByNew(final int curIdx, final int brand_idx){
        try{
            List<ProductReq> result = ListParse( productsMapper.findByBrandNew(brand_idx, curIdx));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND_PRODUCTS_NEW,  result);
        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_READ_PRODUCTS);
        }
    }

    //product의 brand 조회 인기순
    public DefaultRes findBrandProductsByPopular(final int curIdx, final int brand_idx){
        try{
            List<ProductReq> result = ListParse( productsMapper.findByBrandPopular(brand_idx, curIdx));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND_PRODUCTS_POPULAR, result);
        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_READ_PRODUCTS);
        }
    }

    //특정 product 상세 조회
    public DefaultRes findProductById(final int product_idx, final int curIdx){

        ProductReq productReq = productsMapper.findProductById(product_idx, curIdx);
        if(productReq != null){
            productReq.setMeasure(parseJson(productReq.getMeasure().toString()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_PRODUCT, productReq);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_PRODUCT);
    }

    //특정 유저를 위한 상품 추천 리스트 조회
    public DefaultRes getProductByStyle(final List<String> styles, final int curIdx) {
        try{
            final User curUser = userMapper.findByUserIdx(curIdx);
            final String gender = curUser.getGender();
            List<ProductReq> rawResult = new ArrayList<>();
            List<ProductReq> result = new ArrayList<>();
            List<Integer> limit_counts = new ArrayList<>();

            if(styles.size() == 2) {
                limit_counts.add(25);
                limit_counts.add(25);
            } else if (styles.size() == 3) {
                limit_counts.add(20);
                limit_counts.add(15);
                limit_counts.add(15);
            } else {
                limit_counts.add(16);
                limit_counts.add(16);
                limit_counts.add(8);
                limit_counts.add(8);
            }

            int i=0;

            for(String s : styles) {
                rawResult.addAll(productsMapper.getProductByStyle(gender, s, limit_counts.get(i)));
                i++;
            }

            for(ProductReq p : rawResult) {
                if (!result.contains(p)) {
                    p.setMeasure(ProductsService.parseJson(p.getMeasure().toString()));
                    result.add(p);
                } else {
                    continue;
                }
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FOR_USER_REC_PRODUCTS, result);
        }catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_READ_PRODUCTS);
        }
    }
}
