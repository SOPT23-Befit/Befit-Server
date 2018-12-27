package org.sopt.befit.api;

import jdk.net.SocketFlow;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.SignUpReq;
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
                int curIdx = jwtService.decode(header).getUser_idx();//error이면 internal server error
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
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.INVAILD_CREATED_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    @Auth
//    @PutMapping("/{userIdx}")
//    public ResponseEntity updateUser(
//            @RequestHeader("Authorization") final String header,
//            @PathVariable(value = "userIdx") final int userIdx,
//            SignUpReq signUpReq, @RequestPart(value="profile", required=false) final MultipartFile profile) {
//        try {
//            if(jwtService.isUser(header, userIdx) == 1){ //user가 맞을 때.
//                if(profile != null)
//                    signUpReq.setProfile(profile);
//                return new ResponseEntity<>(userService.update(signUpReq, userIdx), HttpStatus.OK);
//            }
//            return new ResponseEntity(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_CURRENT_USER), HttpStatus.OK);
//
//        }catch (Exception e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/{userIdx}")
    public ResponseEntity deleteUser(@PathVariable(value = "userIdx") final int userIdx) {
        try {
            return new ResponseEntity<>(userService.deleteByUserIdx(userIdx), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}