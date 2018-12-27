package org.sopt.befit.mapper;

import org.apache.ibatis.annotations.*;
import org.sopt.befit.dto.User;
import org.sopt.befit.model.SignUpReq;
import org.sopt.befit.model.UserupdateReq;

import java.util.List;

@Mapper
public interface UserMapper {
    //모든 회원 리스트 조회
    @Select("SELECT * FROM user")
    List<User> findAll();

    //회원 이름으로 조회
    @Select("SELECT * FROM user WHERE name = #{name}")
    User findByName(@Param("name") final String name);

    //회원 고유 번호로 조회 befit
    @Select("SELECT * FROM user WHERE Idx = #{Idx}")
    User findByUserIdx(@Param("Idx") final int userIdx);

    @Select("SELECT * FROM user WHERE email = #{email} AND password= #{password}")
    User findByEmailAndPassword(@Param("email") final String email, @Param("password") final String password);

    //회원 가입 befit
    @Insert("INSERT INTO user(email, password, name, gender, brand1_idx, brand2_idx, birthday) VALUES(#{signUpReq.email}, #{signUpReq.password}, #{signUpReq.name}, #{signUpReq.gender}, #{signUpReq.brand1_idx}, #{signUpReq.brand2_idx}, #{signUpReq.birthday})")
    void save(@Param("signUpReq") final SignUpReq signUpReq);

    //회원 정보 수정 brand 수정 befit
    @Update("UPDATE user SET brand1_idx = #{userupdateReq.brand1_idx}, brand2_idx = #{userupdateReq.brand2_idx} WHERE idx = #{idx}")
    void updateBrand(@Param("userupdateReq") final UserupdateReq userupdateReq, @Param("idx") final int idx);



    //회원 정보 수정 통합회원가입 폼 수정 befit
    @Update("UPDATE user SET phone = #{userupdateReq.phone}, post_number = #{userupdateReq.post_number}, home_address = #{userupdateReq.home_address}, detail_address = #{userupdateReq.detail_address}  WHERE idx = #{idx}")
    void updateCombineForm(@Param("userupdateReq") final UserupdateReq userupdateReq, @Param("idx") final int idx);

    //회원 삭제
    @Delete("DELETE FROM user where userIdx = #{userIdx}")
    void deleteByUserIdx(@Param("userIdx") final int userIdx);
}


