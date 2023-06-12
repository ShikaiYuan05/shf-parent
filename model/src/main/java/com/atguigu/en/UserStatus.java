package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *
 * @author Leevi
 * 日期2023-06-05  11:26
 */
public enum UserStatus {
    NORMAL(1,"正常"),
    LOCKED(0,"锁定");
    private Integer statusCode;
    private String statusMessage;

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    UserStatus(Integer statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
