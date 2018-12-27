package org.sopt.befit.api;

import jdk.net.SocketFlow;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.User;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.SignUpReq;
import org.sopt.befit.model.UserupdateReq;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.UserService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;


    public UserController(final UserService userService, final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("") //befit 회원 조회 (O)
    public ResponseEntity getUser(@RequestHeader("Authorization") final String header) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(userService.findByIdx(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_CURRENT_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("") // befit 회원 가입
    public ResponseEntity signup(@RequestBody final SignUpReq signUpReq) {
        try {
            if(signUpReq!=null){
                log.info(signUpReq.toString());
                return new ResponseEntity<>(userService.save(signUpReq), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.INVALID_CREATED_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //befit 특정 회원, brand 수정
    @Auth
    @PutMapping("/brand")
    public ResponseEntity updateUser(
            @RequestHeader("Authorization") final String header,
            @RequestBody final UserupdateReq userupdateReq) {
        try {
            if(userupdateReq!=null){
                if(header != null){
                    int curIdx = jwtService.decode(header).getIdx();
                    return new ResponseEntity(userService.updateBrand(userupdateReq , curIdx), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.HAVE_NOT_UPDATE_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //befit 특정 회원, 통합 회원가입 수정
    @Auth
    @PutMapping("/combineForm")
    public ResponseEntity updateCombineForm(@RequestHeader("Authorization") final String header,
                                            @RequestBody final UserupdateReq userupdateReq){
        try {
            if(userupdateReq!=null){
//                log.info(userupdateReq.toString());
                if(header!=null){
                    int curIdx = jwtService.decode(header).getIdx();
                    return new ResponseEntity(userService.updateCombineForm(userupdateReq, curIdx), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.HAVE_NOT_UPDATE_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestHeader("Authorization") final String header) {
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity<>(userService.deleteByUserIdx(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}