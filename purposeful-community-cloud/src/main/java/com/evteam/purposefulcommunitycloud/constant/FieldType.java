package com.evteam.purposefulcommunitycloud.constant;

import java.util.Locale;

public enum FieldType {
    STRING,
    BOOLEAN,
    DECIMAL,
    FLOAT,
    DOUBLE,
    DURATION,
    DATE_TIME,
    TIME,
    DATE;

    public String toLowerCaseString() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
