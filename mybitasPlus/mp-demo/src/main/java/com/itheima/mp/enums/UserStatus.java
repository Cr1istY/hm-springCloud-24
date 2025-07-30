package com.itheima.mp.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),
    ;

    @EnumValue
    private final Integer value;
    @JsonValue
    private final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
