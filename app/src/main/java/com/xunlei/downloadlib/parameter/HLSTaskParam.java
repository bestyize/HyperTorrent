package com.xunlei.downloadlib.parameter;

/* loaded from: classes16.dex */
public class HLSTaskParam {
    public long mBandwidth;
    public String mCookie;
    public int mCreateMode;
    public String mFileName;
    public String mFilePath;
    public int mMaxConcurrent;
    public String mPass;
    public String mRefUrl;
    public int mSeqId;
    public String mUrl;
    public String mUser;

    public HLSTaskParam() {
    }

    public HLSTaskParam(String str, String str2, String str3, String str4, String str5, String str6, String str7, long j, int i, int i2, int i3) {
        this.mUrl = str;
        this.mRefUrl = str2;
        this.mCookie = str3;
        this.mUser = str4;
        this.mPass = str5;
        this.mFilePath = str6;
        this.mFileName = str7;
        this.mBandwidth = j;
        this.mMaxConcurrent = i;
        this.mCreateMode = i2;
        this.mSeqId = i3;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }

    public void setCookie(String str) {
        this.mCookie = str;
    }

    public void setRefUrl(String str) {
        this.mRefUrl = str;
    }

    public void setUser(String str) {
        this.mUser = str;
    }

    public void setPass(String str) {
        this.mPass = str;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public void setFileName(String str) {
        this.mFileName = str;
    }

    public void setBandwidth(int i) {
        this.mBandwidth = i;
    }

    public void setMaxConcurrent(int i) {
        this.mMaxConcurrent = i;
    }

    public void setCreateMode(int i) {
        this.mCreateMode = i;
    }

    public void setSeqId(int i) {
        this.mSeqId = i;
    }

    public boolean checkMemberVar() {
        return (this.mUrl == null || this.mFilePath == null || this.mFileName == null) ? false : true;
    }
}
