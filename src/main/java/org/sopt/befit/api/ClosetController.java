package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.model.ClosetPostReq;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.service.ClosetService;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("closet")
public class ClosetController {

    private final ClosetService closetService;

    private final JwtService jwtService;

    public ClosetController(ClosetService closetService, JwtService jwtService) {
        this.closetService = closetService;
        this.jwtService = jwtService;
    }

    // 나의 옷장 리스트 조회 - 카테고리 별
    @Auth
    @GetMapping("/category/{category_idx}")
    public ResponseEntity getClosetProduct(@RequestHeader("Authorization") final String header,
                                           @PathVariable(value = "category_idx") final int category_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(closetService.getClosetProduct(curIdx, category_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 특정 아이템 정보 조회
    @Auth
    @GetMapping("/{closet_idx}")
    public ResponseEntity getClosetProductInfo(@RequestHeader("Authorization") final String header,
                                           @PathVariable(value = "closet_idx") final int closet_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(closetService.getClosetProductInfo(curIdx, closet_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 아이템 등록
    @Auth
    @PostMapping("")
    public ResponseEntity postProductToCloset(@RequestHeader("Authorization") final String header,
                                              @RequestBody final ClosetPostReq closetReq) {
        try {
            if(closetReq!=null){
                if(header != null){
                    int curIdx = jwtService.decode(header).getIdx();
                    return new ResponseEntity(closetService.postProductToCloset(curIdx , closetReq), HttpStatus.OK);
                }
                return new ResponseEntity(new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUNT_CLOSET), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 아이템 삭제
    @Auth
    @DeleteMapping("/{closet_idx}")
    public ResponseEntity deleteProductToCloset(@RequestHeader("Authorization") final String header,
                                                @PathVariable(value = "closet_idx") final String  closet_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(closetService.deleteProductToCloset(curIdx,closet_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 브랜드명, 카테고리로 상품검색
    @Auth
    @GetMapping("brands/{brand_idx}/category/{category_idx}")
    public ResponseEntity getProductByBrandAndCategory(@RequestHeader("Authorization") final String header,
                                                       @PathVariable(value = "brand_idx") final int brand_idx,
                                                       @PathVariable(value = "category_idx") final int category_idx) {
        try {
            if(header != null)
                return new ResponseEntity<>(closetService.getProductByBrandAndCategory(brand_idx, category_idx), HttpStatus.OK);

            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 옷장 아이템과 나의 선택 상품 사이즈 비교
    @Auth
    @GetMapping("/{closet_idx}/compare/{product_idx}")
    public ResponseEntity compareProduct(@RequestHeader("Authorization") final String header,
                                         @PathVariable(value = "closet_idx") final int closet_idx,
                                         @PathVariable(value = "product_idx") final int product_idx,
                                         @RequestParam("product_size") final String product_size) {
        try {
            if(product_size!=null){
                if(header != null){
                    int curIdx = jwtService.decode(header).getIdx();
                    log.info(product_size);
                  //  String p_size = product_size.replaceAll("%20" , "");
                    //log.info(p_size);

                    return new ResponseEntity(closetService.compareProduct(curIdx , closet_idx, product_idx, product_size), HttpStatus.OK);
                }
                return new ResponseEntity(new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUNT_CLOSET), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
