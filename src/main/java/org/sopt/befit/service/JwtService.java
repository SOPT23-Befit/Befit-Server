package org.sopt.befit.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.auth0.jwt.JWT.require;

@Slf4j
@Service
public class JwtService {

    @Value("${JWT.ISSUER}")
    private String ISSUER;

    @Value("${JWT.SECRET}")
    private String SECRET;


    //토큰 생성
    public String create(final int idx){
        try{
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("idx", idx);
            return b.sign(Algorithm.HMAC256(SECRET));
        }catch (JWTCreationException jwtCreationException){
            log.info(jwtCreationException.getMessage());
        }
        return null;
    }

    //토큰 해독
    public Token decode(final String token) {
        try {
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return new Token(decodedJWT.getClaim("idx").asLong().intValue());
        } catch (JWTVerificationException jve) {
            log.error(jve.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Token();
    }

    public int isUser(final String header, final int user_idx) { //userIdx 내가 수정하려는!!
        int curIdx = decode(header).getIdx();
        if (curIdx != -1) {
            if (curIdx == user_idx) {
                return 1;
            }
        }
        return 0;
    }

    public static class Token{
        private int idx = -1;

        public Token(){}

        public Token(final int user_idx){
            this.idx = user_idx;
        }
        public int getIdx(){return this.idx; }

    }

    public static class TokenRes{
        private String token;

        public TokenRes(){}
        public TokenRes(final String token){
            this.token = token;
        }
        public String getToken(){
            return token;
        }
        public void setToken(String token){
            this.token = token;
        }
    }
}


