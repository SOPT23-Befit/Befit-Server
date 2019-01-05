package org.sopt.befit.utils;

public class StatusCode {
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409; //409(충돌) : 리소스의 현재 상태와 충돌해서 요청을 처리할 수 없으므로 클라이언트가 요청을 다시 클라이언트가 이 충돌을 수정해서 요청을 다시 보낼 경우
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int DB_ERROR = 600;
}
