package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.LikesService;
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
@RequestMapping("likes")
public class LikesController {

    private final LikesService likesService;

    private final JwtService jwtService;

    public LikesController(LikesService likesService, JwtService jwtService) {
        this.likesService = likesService;
        this.jwtService = jwtService;
    }

    // 좋아요 한 브랜드 조회
    @Auth
    @GetMapping("/brands")
    public ResponseEntity getLikeBrands(@RequestHeader("Authorization") final String header) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(likesService.getLikeBrands(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);

        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 브랜드 좋아요
    @Auth
    @PostMapping("/brands/{brand_idx}")
    public ResponseEntity postLIkeBrand(@RequestHeader("Authorization") final String header,
                                        @PathVariable(value = "brand_idx") final int brand_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(likesService.postLikeBrand(curIdx, brand_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 브랜드 좋아요 취소
    @Auth
    @DeleteMapping("/brands/{brand_idx}")
    public ResponseEntity deleteLikeBrand(@RequestHeader("Authorization") final String header,
                                        @PathVariable(value = "brand_idx") final int brand_idx) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(likesService.deleteLikeBrand(curIdx, brand_idx), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.AUTHORIZATION_FAIL), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
