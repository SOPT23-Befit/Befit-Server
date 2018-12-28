package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.User;
import org.sopt.befit.mapper.UserMapper;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.SignUpReq;
import org.sopt.befit.model.UserupdateReq;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;

    private final S3FileUploadService s3FileUploadService;

    /**
     * UserMapper 생성자 의존성 주입
     *
     * @param userMapper
     */
    public UserService(final UserMapper userMapper, S3FileUploadService s3fileUploadService) {
        this.userMapper = userMapper;
        this.s3FileUploadService = s3fileUploadService;
    }

    //befit index로 회원 조회 (O)
    public DefaultRes findByIdx(final int userIdx){
        final User user = userMapper.findByUserIdx(userIdx);
        if(user == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user);
    }

   //모든회원 조회 사용 X
    public DefaultRes getAllUsers() {
        final List<User> userList = userMapper.findAll();
        if (userList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
    }

    //회원가입
    @Transactional
    public DefaultRes save(final SignUpReq signUpReq) {
        //모든 항목이 있는지 검사
        if (signUpReq.checkProperties()) {
            final User user = userMapper.findByEmail(signUpReq.getEmail());
            if (user == null) {
                try {
                    userMapper.save(signUpReq);
                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
                }
            } else return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.ALREADY_USER);
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.INVALID_CREATED_USER);
    }

    //회원정보 수정 (brand 수정)
    @Transactional
    public DefaultRes updateBrand(final UserupdateReq userupdateReq, final int userIdx) {
        try {
            log.info(userupdateReq.toString() + ">" + userupdateReq.is_brand());
            if(userupdateReq.is_brand()){
                userMapper.updateBrand(userupdateReq, userIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.userNUM_message(userIdx, ResponseMessage.UPDATE_BRAND_USER));
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.INVALID_UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes updateCombineForm(final UserupdateReq userupdateReq, final int userIdx) {
        try {
            log.info(userupdateReq.toString() + ">" + userupdateReq.is_combineForm());
            if(userupdateReq.is_combineForm()){
                userMapper.updateCombineForm(userupdateReq, userIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.userNUM_message(userIdx, ResponseMessage.UPDATE_COMBINE_FROM_USER));
            }
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.INVALID_UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //회원 탈퇴
    @Transactional
    public DefaultRes deleteByUserIdx(final int userIdx) {
        final User user = userMapper.findByUserIdx(userIdx);
        try {
            if (user == null)
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
            userMapper.deleteByUserIdx(userIdx);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.userNUM_message(userIdx,ResponseMessage.DELETE_USER));
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}