package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *
 * 日期2023-06-03  10:40
 */
public enum HouseImageType {
    HOUSE_SOURCE(1,"房源"), HOUSE_PROPERTY(2,"房产");
    private Integer typeCode;
    private String typeMessage;

    public Integer getTypeCode() {
        return typeCode;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    HouseImageType(Integer typeCode, String typeMessage) {
        this.typeCode = typeCode;
        this.typeMessage = typeMessage;
    }
}
