package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.User;
import org.sopt.befit.mapper.UserMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.LoginReq;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.SHA512EncryptUtils;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserMapper userMapper;

    private final JwtService jwtService;

    public AuthService(final UserMapper userMapper, JwtService jwtService){
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    public DefaultRes<JwtService.TokenRes> login(final LoginReq loginReq){
        String passEncrypt = loginReq.getPassword();
        loginReq.setPassword(SHA512EncryptUtils.encrypt(passEncrypt));
        log.info(loginReq.toString());
        final User user = userMapper.findByEmailAndPassword(loginReq.getEmail(), loginReq.getPassword());
        if(user !=null){
            final JwtService.TokenRes tokenDto = new JwtService.TokenRes(jwtService.create(user.getIdx()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, tokenDto);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.LOGIN_FAIL);
    }
}