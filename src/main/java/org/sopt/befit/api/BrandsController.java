package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;

import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.service.BrandsService;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.UserService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by khh on 2018-12-26.
 */

@Slf4j
@RestController
@RequestMapping("brands")
public class BrandsController {

    private final UserService userService;
    private final BrandsService brandsService;

    private final JwtService jwtService;

    public BrandsController(UserService userService, BrandsService brandsService, JwtService jwtService) {
        this.userService = userService;
        this.brandsService = brandsService;
        this.jwtService = jwtService;
    }


    //이니셜로 브랜드 리스트 검색
    @Auth
    @GetMapping("")
    public ResponseEntity getBrandsByInitial(@RequestHeader("Authorization") final String header,
                                             @RequestParam("initial") final Optional<Character> initial) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                if(initial.isPresent())
                        return new ResponseEntity<>(
                                brandsService.getBrandsByInitial(curIdx, initial.get()), HttpStatus.OK);
                return new ResponseEntity<>(brandsService.getBrands(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 특정 브랜드 정보 조회 - 왜하는지 모르겠음
    @Auth
    @GetMapping("/{brand_idx}")
    public ResponseEntity getBrandInfo(@RequestHeader("Authorization") final String header,
                                       @PathVariable(value = "brand_idx") final int brand_idx) {
        try {
                if(header != null) {
                    int curIdx = jwtService.decode(header).getIdx();
                    return new ResponseEntity<>(brandsService.getBrandInfo(curIdx, brand_idx), HttpStatus.OK);
                }
                return new ResponseEntity<>(
                        new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 해당 유저의 브랜드 랭킹 조회 - 브랜드 랭킹 내림차순 리스트 조회
    @Auth
    @GetMapping("/preference")
    public ResponseEntity getBrandsByRank(@RequestHeader("Authorization") final String header) {
        try {
            if(header != null) {
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(brandsService.getBrandsByRank(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 랜덤 3개 브랜드 별 인기 상품 리스트 조회
    @Auth
    @GetMapping("/randomPopular/three")
    public ResponseEntity getProductsByRandomBrands(@RequestHeader("Authorization") final String header) {
        try {
            if(header != null) {
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(brandsService.getProductsByThreeBrands(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
