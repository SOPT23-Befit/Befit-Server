package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.service.LikesService;
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

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    // 좋아요 한 브랜드 조회
    @GetMapping("/brands")
    public ResponseEntity getLikeBrands() {
        try {
            return new ResponseEntity<>(likesService.getLikeBrands(), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 브랜드 좋아요
    @PostMapping("/brands/{brand_idx}")
    public ResponseEntity postLIkeBrand(){
        try{
            return new ResponseEntity<>(likesService.postLikeBrand(1,2), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 브랜드 좋아요 취소
}
