package com.test;

import java.io.Serializable;

public class ViewDataVO implements Serializable{

    private static final long serialVersionUID = 1194828283585702120L;

    private double left;
    private double top;
    private double width;
    private double height;

    private boolean isScrollWithWebView = true;

    public int getLeft() {
        return (int) left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public int getTop() {
        return (int) top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return (int) width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isScrollWithWebView() {
        return isScrollWithWebView;
    }

    public void setIsScrollWithWebView(boolean isScrollWithWebView) {
        this.isScrollWithWebView = isScrollWithWebView;
    }
}
