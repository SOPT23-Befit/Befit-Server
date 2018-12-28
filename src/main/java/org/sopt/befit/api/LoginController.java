package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.LoginReq;
import org.sopt.befit.service.AuthService;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    private static final DefaultRes FAIL_DEFAULT_RES = new DefaultRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    private final AuthService authService;

    public LoginController(final AuthService authService){
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody final LoginReq loginReq){
        try{
            log.info(loginReq.toString());
            return new ResponseEntity<>(authService.login(loginReq), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
