package org.sopt.befit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.sopt.befit.domain.Brand_JPA;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.Products;
import org.sopt.befit.mapper.BrandsMapper;
import org.sopt.befit.mapper.LikesMapper;
import org.sopt.befit.mapper.ProductsMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.ProductReq;
import org.sopt.befit.repository.BrandRepository;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    ProductsService(final ProductsMapper productsMapper, final LikesMapper likesMapper, final BrandsMapper brandsMapper){
        this.productsMapper = productsMapper;
        this.likesMapper = likesMapper;
        this.brandsMapper = brandsMapper;
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


    public List<Products> listParseJson(List<Products> products){
        for(Products product: products){
            JsonNode node = parseJson(product.getMeasure().toString());
            product.setMeasure(node);
        }
        return products;
    }

    //controller 관련 method (return : DefaultRes)

    public DefaultRes findAllProducts(final int curIdx){ //curIdx : 현재 접속한 유저의 idx

        List<ProductReq> productReqList = new ArrayList<>();

        final List<Products> products = productsMapper.findAll();
        for(Products product: products){

            JsonNode node = parseJson(product.getMeasure().toString());
            product.setMeasure(node);

            boolean isLike = (likesMapper.isLike(curIdx, product.getBrand_idx()) != 0); //int to bool : (i != 0)

//            Optional<Brand_JPA> brand = brandRepository.findById(product.getBrand_idx());

            Brands brandItem = brandsMapper.getBrandsByIdx(product.getBrand_idx());
            log.info("brand" + brandItem);

            ProductReq reqItem = new ProductReq(product, brandItem.getName_korean(), brandItem.getName_english(), isLike);
            log.info("ReqItem" + reqItem);
            productReqList.add(reqItem);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_PRODUCTS, productReqList);
    }




}
