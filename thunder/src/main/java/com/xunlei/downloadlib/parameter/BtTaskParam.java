package com.xunlei.downloadlib.parameter;

/* loaded from: classes16.dex */
public class BtTaskParam {
    public int mCreateMode;
    public String mFilePath;
    public int mMaxConcurrent;
    public int mSeqId;
    public String mTorrentPath;

    public BtTaskParam() {
    }

    public void setTorrentPath(String str) {
        this.mTorrentPath = str;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
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
        return this.mTorrentPath != null && this.mFilePath != null;
    }
}
