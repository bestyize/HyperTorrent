package com.xunlei.downloadlib.parameter;

/* loaded from: classes16.dex */
public class ServerResourceParam {
    public int mComeFrom;
    public String mCookie;
    public String mRefUrl;
    public int mResType;
    public int mStrategy;
    public String mUrl;

    public ServerResourceParam() {
    }

    public ServerResourceParam(String str, String str2, String str3, int i, int i2, int i3) {
        this.mUrl = str;
        this.mRefUrl = str2;
        this.mCookie = str3;
        this.mResType = i;
        this.mStrategy = i2;
        this.mComeFrom = i3;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }

    public void setRefUrl(String str) {
        this.mRefUrl = str;
    }

    public void setCookie(String str) {
        this.mCookie = str;
    }

    public void setRestype(int i) {
        this.mResType = i;
    }

    public void setStrategy(int i) {
        this.mStrategy = i;
    }

    public void setComeFrom(int i) {
        this.mComeFrom = i;
    }

    public boolean checkMemberVar() {
        return this.mUrl != null;
    }
}
