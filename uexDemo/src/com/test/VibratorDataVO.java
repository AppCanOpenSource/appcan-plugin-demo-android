package com.test;

import java.io.Serializable;

public class VibratorDataVO implements Serializable{
    private static final long serialVersionUID = -4932933993422727397L;
    private double time;

    public double getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
