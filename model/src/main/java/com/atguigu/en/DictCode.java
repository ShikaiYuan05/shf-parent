package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *
 * @author Leevi
 * 日期2023-06-02  14:45
 */
public enum DictCode {
    BEIJING("bj"),HOUSETYPE("houseType"),FLOOR("floor"),
    BUILDSTRUCTURE("buildStructure"),DECORATION("decoration"),
    DIRECTION("direction"),HOUSEUSE("houseUse"),PROVINCE("province");
    private String code;

    public String getCode() {
        return code;
    }

    DictCode(String code) {
        this.code = code;
    }
}
