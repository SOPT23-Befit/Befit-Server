package org.sopt.befit.utils;


import java.security.MessageDigest;

//복호화 키는 없음
//단방향 해시함수 ("해커가 여러개의 다이제스트된 값을 생성해서 비교하면, 사용자의 password가 쉽게 뚫릴 수 있음")
//따라서 사용자에게 긴 길이의 password 설정을 권장해야하는 방식이다.

public class SHA512EncryptUtils {

    public static String encrypt(String planText) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}