package org.sopt.befit.api;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.ProductsService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;
import static org.sopt.befit.model.DefaultRes.res;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductsController {

    final JwtService jwtService;

    final ProductsService productsService;

    ProductsController(final JwtService jwtService, final ProductsService productsService){
        this.jwtService = jwtService;
        this.productsService = productsService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity findAllProducts(@RequestHeader("Authorization") final String header){
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity(productsService.findAllProducts(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity(new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/{sort}/{type}/{type_idx}")
    public ResponseEntity findProductsByCategory(@RequestHeader("Authorization") final String header,
                                                  @PathVariable(value="sort") final String sort,
                                                  @PathVariable(value="type") final String type,
                                                  @PathVariable(value = "type_idx")final int type_idx){
        try{
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                switch (type){
                    case "category":
                        if(type_idx>=0 && type_idx<=15){//category 범위 올바른지 확인
                            switch (sort){ //sorting 방법
                                case "new" :
                                    return new ResponseEntity(productsService.findCategoryProductsByNew(curIdx, type_idx), HttpStatus.OK);
                                case "popular" :
                                    return new ResponseEntity(productsService.findCategoryProductsByPopular(curIdx, type_idx), HttpStatus.OK);
                            }
                        }
                    case "brand":
                        if(type_idx>=1 && type_idx<=43){
                            switch (sort){ //sorting 방법
                                case "new" :
                                    return new ResponseEntity(productsService.findBrandProductsByNew(curIdx, type_idx), HttpStatus.OK);
                                case "popular" :
                                    return new ResponseEntity(productsService.findBrandProductsByPopular(curIdx, type_idx), HttpStatus.OK);
                            }
                        }
                }
                return new ResponseEntity(new DefaultRes(StatusCode.NOT_FOUND, ResponseMessage.INVALID_PRODUCTS_READ), HttpStatus.NOT_FOUND); //404 에러 url 맞지 않음 (type 틀리거나, sort 틀림) or category 인덱스 벗어남 or
            }
            return new ResponseEntity(new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
