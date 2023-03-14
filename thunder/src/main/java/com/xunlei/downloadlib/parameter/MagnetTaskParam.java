package com.xunlei.downloadlib.parameter;

/* loaded from: classes16.dex */
public class MagnetTaskParam {
    public String mFileName;
    public String mFilePath;
    public String mUrl;

    public MagnetTaskParam() {
    }

    public MagnetTaskParam(String str, String str2, String str3) {
        this.mFileName = str;
        this.mFilePath = str2;
        this.mUrl = str3;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }

    public void setFileName(String str) {
        this.mFileName = str;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public boolean checkMemberVar() {
        return (this.mFileName == null || this.mFilePath == null || this.mUrl == null) ? false : true;
    }
}
