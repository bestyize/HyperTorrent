package com.xunlei.downloadlib.parameter;

/* loaded from: classes16.dex */
public class SetIndexInfoParam {
    public String mBcid;
    public String mCid;
    public long mFileSize;
    public String mGcid;
    public int mGcidLevel;

    public SetIndexInfoParam() {
    }

    public SetIndexInfoParam(String str, String str2, String str3, long j, int i) {
        this.mCid = str;
        this.mGcid = str2;
        this.mBcid = str3;
        this.mFileSize = j;
        this.mGcidLevel = i;
    }

    public void setCid(String str) {
        this.mCid = str;
    }

    public void setGcid(String str) {
        this.mGcid = str;
    }

    public void setBcid(String str) {
        this.mBcid = str;
    }

    public void setFileSize(long j) {
        this.mFileSize = j;
    }

    public void setGcidLevel(int i) {
        this.mGcidLevel = i;
    }

    public boolean checkMemberVar() {
        return (this.mCid == null || this.mGcid == null || this.mBcid == null) ? false : true;
    }
}
