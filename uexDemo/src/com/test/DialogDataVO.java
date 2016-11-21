package com.test;

import java.io.Serializable;

public class DialogDataVO implements Serializable{
    private static final long serialVersionUID = 357428537916628604L;
    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
