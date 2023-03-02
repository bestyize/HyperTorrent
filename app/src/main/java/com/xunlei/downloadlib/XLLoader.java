package com.xunlei.downloadlib;

import android.content.Context;

import com.xunlei.downloadlib.parameter.BtIndexSet;
import com.xunlei.downloadlib.parameter.BtSubTaskDetail;
import com.xunlei.downloadlib.parameter.BtTaskStatus;
import com.xunlei.downloadlib.parameter.DcdnPeerResParam;
import com.xunlei.downloadlib.parameter.GetBooleanParam;
import com.xunlei.downloadlib.parameter.GetDownloadHead;
import com.xunlei.downloadlib.parameter.GetDownloadLibVersion;
import com.xunlei.downloadlib.parameter.GetFileName;
import com.xunlei.downloadlib.parameter.GetTaskId;
import com.xunlei.downloadlib.parameter.MaxDownloadSpeedParam;
import com.xunlei.downloadlib.parameter.ThunderUrlInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.UploadControlParam;
import com.xunlei.downloadlib.parameter.UploadInfo;
import com.xunlei.downloadlib.parameter.UrlQuickInfo;
import com.xunlei.downloadlib.parameter.XLFirstMediaState;
import com.xunlei.downloadlib.parameter.XLPremiumResInfo;
import com.xunlei.downloadlib.parameter.XLRangeInfo;
import com.xunlei.downloadlib.parameter.XLSessionInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfoEx;
import com.xunlei.downloadlib.parameter.XLTaskLocalUrl;


public class XLLoader {

    public XLLoader() {
        System.loadLibrary("xl_thunder_sdk");
    }

    public native int addBatchDcdnPeerRes(long j, int i, long j2, DcdnPeerResParam[] dcdnPeerResParamArr);

    public native int addBtTrackerNodes(long j, String str);

    public native int addPeerResource(long j, String str, long j2, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public native int addServerResource(long j, int i, String str, String str2, String str3, int i2, int i3, int i4);

    public native int btAddPeerResource(long j, int i, String str, long j2, String str2, String str3, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public native int btRemoveAddedResource(long j, int i, int i2);

    public native int changeOriginRes(long j, String str);

    public native int clearTaskFile(String str);

    public native int createBtMagnetTask(String str, String str2, String str3, GetTaskId getTaskId);

    public native int createBtTask(String str, String str2, int i, int i2, int i3, GetTaskId getTaskId);

    public native int createCDNTask(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, GetTaskId getTaskId);

    public native int createCIDTask(String str, String str2, String str3, String str4, String str5, long j, int i, int i2, GetTaskId getTaskId);

    public native int createEmuleTask(String str, String str2, String str3, int i, int i2, GetTaskId getTaskId);

    public native int createHLSTask(String str, String str2, String str3, String str4, String str5, String str6, String str7, long j, int i, int i2, int i3, GetTaskId getTaskId);

    public native int createP2spTask(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, GetTaskId getTaskId);

    public native int createShortVideoTask(String str, String str2, String str3, String str4, int i, int i2, int i3, GetTaskId getTaskId);

    public native int createVodTask(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i, int i2, int i3, GetTaskId getTaskId);

    public native int deselectBtSubTask(long j, BtIndexSet btIndexSet);

    public native int enterPrefetchMode(long j);

    public native int getBtSubTaskInfo(long j, int i, BtSubTaskDetail btSubTaskDetail);

    public native int getBtSubTaskStatus(long j, BtTaskStatus btTaskStatus, int i, int i2);

    public native int getDownloadHeader(long j, GetDownloadHead getDownloadHead);

    public native int getDownloadLibVersion(GetDownloadLibVersion getDownloadLibVersion);

    public native int getDownloadRangeInfo(long j, int i, XLRangeInfo xLRangeInfo);

    public native int getFileNameFromUrl(String str, GetFileName getFileName);

    public native int getFirstMediaState(long j, int i, XLFirstMediaState xLFirstMediaState);

    public native int getLocalUrl(String str, XLTaskLocalUrl xLTaskLocalUrl);

    public native int getMaxDownloadSpeed(MaxDownloadSpeedParam maxDownloadSpeedParam);

    public native int getNameFromUrl(String str, String str2);

    public native int getPremiumResInfo(long j, int i, XLPremiumResInfo xLPremiumResInfo);

    public native int getSessionInfoByUrl(String str, XLSessionInfo xLSessionInfo);

    public native int getSettingBoolean(String str, String str2, GetBooleanParam getBooleanParam, boolean z);

    public native int getTaskInfo(long j, int i, XLTaskInfo xLTaskInfo);

    public native int getTaskInfoEx(long j, XLTaskInfoEx xLTaskInfoEx);

    public native int getTorrentInfo(String str, TorrentInfo torrentInfo);

    public native int getUploadInfo(UploadInfo uploadInfo);

    public native int getUrlQuickInfo(long j, UrlQuickInfo urlQuickInfo);

    public native int init(Context context, String appVersion, String str2, String peerId, String guid, String str5, String str6, String str7, int i, int i2);

    public native boolean isLogTurnOn();

    public native int notifyNetWorkType(int i);

    public native int notifyUploadFileChanged(String str, String str2, long j);

    public native int parserThunderUrl(String str, ThunderUrlInfo thunderUrlInfo);

    public native int playShortVideoBegin(long j);

    public native int releaseTask(long j);

    public native int removeAccelerateToken(long j, int i);

    public native int removeAddedResource(long j, int i);

    public native int removeAddedServerResource(long j, int i);

    public native int requeryIndex(long j);

    public native int resetUploadInfo();

    public native int selectBtSubTask(long j, BtIndexSet btIndexSet);

    public native int setAccelerateToken(long j, int i, long j2, int i2, String str);

    public native int setBtPriorSubTask(long j, int i);

    public native int setBtSwitch(int i);

    public native int setCandidateResSpeed(long j, int i);

    public native int setDownloadTaskOrigin(long j, String str);

    public native int setEmuleSwitch(int i);

    public native int setFileName(long j, String str);

    public native int setHttpHeaderProperty(long j, String str, String str2);

    public native int setImei(String str);

    public native int setIndexInfo(long j, String str, String str2, String str3, long j2, int i);

    public native int setLocalProperty(String str, String str2);

    public native int setMac(String str);

    public native int setMiUiVersion(String str);

    public native int setNotifyNetWorkCarrier(int i);

    public native int setNotifyWifiBSSID(String str);

    public native int setOriginUserAgent(long j, String str);

    public native int setPlayerMode(long j, int i);

    public native int setReleaseLog(int i, String str, int i2, int i3);

    public native int setSlowAccelerateSpeed(long j, long j2);

    public native int setSpeedLimit(long j, long j2);

    public native int setStatReportSwitch(boolean z);

    public native int setTaskAllowUseResource(long j, int i);

    public native int setTaskAppInfo(long j, String str, String str2, String str3);

    public native int setTaskGsState(long j, int i, int i2);

    public native int setTaskLxState(long j, int i, int i2);

    public native int setTaskSpeedLimit(long j, long j2);

    public native int setTaskUid(long j, int i);

    public native int setUploadControlParam(UploadControlParam uploadControlParam);

    public native int setUploadInfo(UploadInfo uploadInfo);

    public native int setUploadSwitch(boolean z);

    public native int setUserId(String str);

    public native int setVipType(String str);

    public native int startDcdn(long j, int i, String str, String str2, String str3);

    public native int startTask(long j, boolean z);

    public native int statExternalInfo(long j, int i, String str, String str2);

    public native int statExternalInfoU64(long j, int i, String str, long j2, int i2);

    public native int stopDcdn(long j, int i);

    public native int stopTask(long j);

    public native int stopTaskWithReason(long j, int i);

    public native int switchOriginToAllResDownload(long j);

    public native int unInit();

}
