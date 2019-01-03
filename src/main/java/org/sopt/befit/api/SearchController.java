package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Products;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.SearchReq;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.SearchService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchService searchService;

    private final JwtService jwtService;

    public SearchController(SearchService searchService, JwtService jwtService) {
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    // 브랜드 이름으로 검색 - u 라고만하면 'u'스펠링 있는거 전부 줘야하나?
    @Auth
    @GetMapping("/brands")
    public ResponseEntity getBrandsByInitial(@RequestHeader("Authorization") final String header,
                                             @RequestParam("name") final String name) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();

                if(!name.equals(""))
                    return new ResponseEntity<>(searchService.findBrandsByName(curIdx, name), HttpStatus.OK);
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.OK);
                return new ResponseEntity<>(
                        new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.INPUT_VALUE), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/brands")
//    public ResponseEntity getBrandsList(@RequestParam("name") final Optional<String> name) {
//        try {
////
//            String up_name = name.get().toUpperCase();
//            log.info(up_name);
//            if(name.isPresent()) return new ResponseEntity<>(searchService.findBrandsByName(up_name), HttpStatus.OK);
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.OK);
//        }catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    // 상품 이름으로 검색
    @Auth
    @GetMapping("/products/{type}")
    public ResponseEntity getBrandsByInitial(@RequestHeader("Authorization") final String header,
                                             @RequestParam("name") final String name,
                                             @PathVariable(value="type") final String type) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                if(!name.equals("")){
                    //upper string 처리
                    switch (type){
                        case "new":
                            return new ResponseEntity(searchService.findPrdocutsByNameForNew(curIdx,name.replaceAll(" ", "")), HttpStatus.OK);

                        case "popular":
                            return new ResponseEntity<>(searchService.findPrdocutsByNameForPopular(curIdx,name.replaceAll(" ", "")), HttpStatus.OK);

                    }
                    return new ResponseEntity(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.INVALID_PRODUCTS_READ), HttpStatus.OK);
                }
                return new ResponseEntity<>(
                        new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.INPUT_VALUE), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
