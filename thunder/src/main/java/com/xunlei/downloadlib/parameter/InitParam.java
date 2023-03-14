package com.xunlei.downloadlib.parameter;

/* loaded from: classes16.dex */
public class InitParam {
    public String mAppVersion;
    public String mGuid;
    public String mLogSavePath;
    public int mPermissionLevel;
    public String mStatCfgSavePath;
    public String mStatSavePath;

    public InitParam() {
    }

    public InitParam(String str, String str2, String str3, String str4, int i, String str5) {
        this.mAppVersion = str;
        this.mStatSavePath = str2;
        this.mStatCfgSavePath = str3;
        this.mPermissionLevel = i;
        this.mLogSavePath = str4;
        this.mGuid = str5;
    }

    public boolean checkMemberVar() {
        return this.mAppVersion != null && this.mStatSavePath != null && this.mStatCfgSavePath != null && this.mLogSavePath != null;
    }
}
