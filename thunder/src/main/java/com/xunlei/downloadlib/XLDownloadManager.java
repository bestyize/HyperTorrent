package com.xunlei.downloadlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.xunlei.downloadlib.util.XLLog;
import com.xunlei.downloadlib.util.XLUtil;
import com.xunlei.downloadlib.parameter.BtIndexSet;
import com.xunlei.downloadlib.parameter.BtSubTaskDetail;
import com.xunlei.downloadlib.parameter.BtTaskParam;
import com.xunlei.downloadlib.parameter.BtTaskStatus;
import com.xunlei.downloadlib.parameter.CIDTaskParam;
import com.xunlei.downloadlib.parameter.DcdnPeerResParam;
import com.xunlei.downloadlib.parameter.EmuleTaskParam;
import com.xunlei.downloadlib.parameter.ErrorCodeToMsg;
import com.xunlei.downloadlib.parameter.GetBooleanParam;
import com.xunlei.downloadlib.parameter.GetDownloadHead;
import com.xunlei.downloadlib.parameter.GetDownloadLibVersion;
import com.xunlei.downloadlib.parameter.GetFileName;
import com.xunlei.downloadlib.parameter.GetTaskId;
import com.xunlei.downloadlib.parameter.HLSTaskParam;
import com.xunlei.downloadlib.parameter.InitParam;
import com.xunlei.downloadlib.parameter.MagnetTaskParam;
import com.xunlei.downloadlib.parameter.MaxDownloadSpeedParam;
import com.xunlei.downloadlib.parameter.P2spTaskParam;
import com.xunlei.downloadlib.parameter.PeerResourceParam;
import com.xunlei.downloadlib.parameter.ServerResourceParam;
import com.xunlei.downloadlib.parameter.SetIndexInfoParam;
import com.xunlei.downloadlib.parameter.ThunderUrlInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.UploadControlParam;
import com.xunlei.downloadlib.parameter.UploadInfo;
import com.xunlei.downloadlib.parameter.UrlQuickInfo;
import com.xunlei.downloadlib.parameter.XLConstant;
import com.xunlei.downloadlib.parameter.XLFirstMediaState;
import com.xunlei.downloadlib.parameter.XLPremiumResInfo;
import com.xunlei.downloadlib.parameter.XLRangeInfo;
import com.xunlei.downloadlib.parameter.XLSessionInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfoEx;
import com.xunlei.downloadlib.parameter.XLTaskLocalUrl;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes16.dex */
public class XLDownloadManager {
    private static final int GET_GUID_FIRST_TIME = 5000;
    private static final int GET_GUID_INTERVAL_TIME = 60000;
    private static final int QUERY_GUID_COUNT = 5;
    private static final String TAG = "[xunlei]XLDownloadManager";
    public static XLConstant.XLManagerStatus mDownloadManagerState = XLConstant.XLManagerStatus.MANAGER_UNINIT;
    private static XLDownloadManager mInstance = null;
    private static int mRunningRefCount = 0;
    private static Map<String, Object> mErrcodeStringMap = null;
    private static boolean mIsLoadErrcodeMsg = false;
    private Timer mGetGuidTimer;
    private TimerTask mGetGuidTimerTask;
    private XLLoader mLoader;
    private Context mContext = null;
    private NetworkChangeReceiver mReceiver = null;
    private int mQueryGuidCount = 0;

    private XLDownloadManager() {
        this.mLoader = null;
        this.mLoader = new XLLoader();
    }

    private static void initGuid(XLDownloadManager xLDownloadManager) {
        int i = xLDownloadManager.mQueryGuidCount;
        xLDownloadManager.mQueryGuidCount = i + 1;
    }

    public static synchronized XLDownloadManager getInstance() {
        XLDownloadManager xLDownloadManager;
        synchronized (XLDownloadManager.class) {
            if (mInstance == null) {
                mInstance = new XLDownloadManager();
            }
            xLDownloadManager = mInstance;
        }
        return xLDownloadManager;
    }

    public XLConstant.XLManagerStatus getManagerStatus() {
        return mDownloadManagerState;
    }

    private void doMonitorNetworkChange() {
        XLLog.i(TAG, "doMonitorNetworkChange()");
        if (this.mContext == null || this.mReceiver != null) {
            return;
        }
        this.mReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        XLLog.i(TAG, "register Receiver");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
    }

    private void undoMonitorNetworkChange() {
        NetworkChangeReceiver networkChangeReceiver;
        XLLog.i(TAG, "undoMonitorNetworkChange()");
        Context context = this.mContext;
        if (context == null || (networkChangeReceiver = this.mReceiver) == null) {
            return;
        }
        try {
            context.unregisterReceiver(networkChangeReceiver);
            XLLog.i(TAG, "unregister Receiver");
        } catch (IllegalArgumentException unused) {
            XLLog.e(TAG, "Receiver not registered");
        }
        this.mReceiver = null;
    }

    private synchronized void increRefCount() {
        mRunningRefCount++;
    }

    private synchronized void decreRefCount() {
        mRunningRefCount--;
    }

    public synchronized int init(Context context, InitParam initParam) {
        if (!mIsLoadErrcodeMsg) {
            loadErrcodeString(context);
            mIsLoadErrcodeMsg = true;
        }
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (context != null && initParam != null && initParam.checkMemberVar()) {
            this.mContext = context;
            if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING) {
                XLLog.i(TAG, "XLDownloadManager is already init");
                return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
            }
            if (this.mLoader != null) {
                String peerid = getPeerid();
                String guid = getGuid();
                if (!TextUtils.isEmpty(initParam.mGuid)) {
                    guid = initParam.mGuid;
                }
                String str = guid;
                XLLog.i(TAG, "peerId:" + new String(Base64.encode(peerid.getBytes(), 0)));
                XLLog.i(TAG, "guid:" + new String(Base64.encode(str.getBytes(), 0)));
                try {
                    i = this.mLoader.init(context, initParam.mAppVersion, "", peerid, str, initParam.mStatSavePath, initParam.mStatCfgSavePath, initParam.mLogSavePath, XLUtil.getNetworkTypeComplete(context), initParam.mPermissionLevel);
                } catch (Exception e) {
                    Log.i(TAG, e.getMessage());
                }
                if (i != 9000) {
                    mDownloadManagerState = XLConstant.XLManagerStatus.MANAGER_INIT_FAIL;
                    XLLog.e(TAG, "XLDownloadManager init failed ret=" + i);
                } else {
                    mDownloadManagerState = XLConstant.XLManagerStatus.MANAGER_RUNNING;
                    doMonitorNetworkChange();
                    setLocalProperty("PhoneModel", Build.MODEL);
                }
            }
            return i;
        }
        return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
    }

    public synchronized int uninit() {
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (mRunningRefCount != 0) {
            XLLog.i(TAG, "some function of XLDownloadManager is running, uninit failed!");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        if (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_UNINIT && this.mLoader != null) {
            if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING) {
                undoMonitorNetworkChange();
            }
            stopGetGuidTimer();
            i = this.mLoader.unInit();
            mDownloadManagerState = XLConstant.XLManagerStatus.MANAGER_UNINIT;
            this.mContext = null;
        }
        return i;
    }

    public void notifyNetWorkInfo(Context context) {
        int networkTypeComplete = XLUtil.getNetworkTypeComplete(context);
        XLLog.d(TAG, "NetworkChangeHandlerThread nettype=" + networkTypeComplete);
        notifyNetWorkType(networkTypeComplete);
        String bssid = XLUtil.getBSSID(context);
        XLLog.d(TAG, "NetworkChangeHandlerThread bssid=" + bssid);
        notifyWifiBSSID(bssid);
        XLUtil.NetWorkCarrier netWorkCarrier = XLUtil.getNetWorkCarrier(context);
        XLLog.d(TAG, "NetworkChangeHandlerThread NetWorkCarrier=" + ((Object) netWorkCarrier));
        notifyNetWorkCarrier(netWorkCarrier.ordinal());
    }

    int notifyNetWorkType(int i) {
        XLLoader xLLoader;
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            try {
                return xLLoader.notifyNetWorkType(i);
            } catch (Error e2) {
                XLLog.e(TAG, "notifyNetWorkType failed," + e2.getMessage());
            }
        }
        return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
    }

    public int createP2spTask(P2spTaskParam p2spTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (p2spTaskParam == null || getTaskId == null || !p2spTaskParam.checkMemberVar()) {
            XLLog.e(TAG, "createP2spTask failed, para=" + ((Object) p2spTaskParam) + ", cTaskId=" + ((Object) getTaskId));
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            i = xLLoader.createP2spTask(p2spTaskParam.mUrl, p2spTaskParam.mRefUrl, p2spTaskParam.mCookie, p2spTaskParam.mUser, p2spTaskParam.mPass, p2spTaskParam.mFilePath, p2spTaskParam.mFileName, p2spTaskParam.mCreateMode, p2spTaskParam.mSeqId, getTaskId);
            if (i != 9000) {
                XLLog.e(TAG, "createP2spTask failed, ret=" + i);
            }
            decreRefCount();
            return i;
        }
        XLLog.e(TAG, "createP2spTask failed, mDownloadManagerState=" + ((Object) mDownloadManagerState));
        decreRefCount();
        return i;
    }

    public int releaseTask(long j) {
        XLLoader xLLoader;
        increRefCount();
        int releaseTask = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.releaseTask(j);
        decreRefCount();
        return releaseTask;
    }

    public int setTaskAllowUseResource(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int taskAllowUseResource = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setTaskAllowUseResource(j, i);
        decreRefCount();
        return taskAllowUseResource;
    }

    public int setTaskUid(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int taskUid = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setTaskUid(j, i);
        decreRefCount();
        return taskUid;
    }

    public int startTask(long j, boolean z) {
        XLLoader xLLoader;
        increRefCount();
        int startTask = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.startTask(j, z);
        decreRefCount();
        return startTask;
    }

    public int startTask(long j) {
        return startTask(j, false);
    }

    int switchOriginToAllResDownload(long j) {
        XLLoader xLLoader;
        return (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.switchOriginToAllResDownload(j);
    }

    public int stopTask(long j) {
        XLLoader xLLoader;
        increRefCount();
        int stopTask = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.stopTask(j);
        XLLog.i(TAG, "XLStopTask()----- ret=" + stopTask);
        decreRefCount();
        return stopTask;
    }

    public int stopTaskWithReason(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int stopTaskWithReason = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.stopTaskWithReason(j, i);
        XLLog.i(TAG, "XLStopTask()----- ret=" + stopTaskWithReason);
        decreRefCount();
        return stopTaskWithReason;
    }

    public int getTaskInfo(long j, int i, XLTaskInfo xLTaskInfo) {
        XLLoader xLLoader;
        increRefCount();
        int taskInfo = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || xLTaskInfo == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getTaskInfo(j, i, xLTaskInfo);
        decreRefCount();
        return taskInfo;
    }

    public int getTaskInfoEx(long j, XLTaskInfoEx xLTaskInfoEx) {
        XLLoader xLLoader;
        increRefCount();
        int taskInfoEx = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || xLTaskInfoEx == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getTaskInfoEx(j, xLTaskInfoEx);
        decreRefCount();
        return taskInfoEx;
    }

    public int getPremiumResInfo(long j, int i, XLPremiumResInfo xLPremiumResInfo) {
        XLLoader xLLoader;
        increRefCount();
        int premiumResInfo = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || xLPremiumResInfo == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getPremiumResInfo(j, i, xLPremiumResInfo);
        decreRefCount();
        return premiumResInfo;
    }

    public int getLocalUrl(String str, XLTaskLocalUrl xLTaskLocalUrl) {
        int i = 0;
        XLLoader xLLoader;
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null && xLTaskLocalUrl != null && str != null) {
            try {
                i = xLLoader.getLocalUrl(str, xLTaskLocalUrl);
            } catch (UnsatisfiedLinkError e2) {
                e2.printStackTrace();
            }
            decreRefCount();
            return i;
        }
        i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        decreRefCount();
        return i;
    }

    public int addPeerResource(long j, PeerResourceParam peerResourceParam) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (peerResourceParam == null || !peerResourceParam.checkMemberVar()) {
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            i = xLLoader.addPeerResource(j, peerResourceParam.mPeerId, peerResourceParam.mUserId, peerResourceParam.mJmpKey, peerResourceParam.mVipCdnAuth, peerResourceParam.mInternalIp, peerResourceParam.mTcpPort, peerResourceParam.mUdpPort, peerResourceParam.mResLevel, peerResourceParam.mResPriority, peerResourceParam.mCapabilityFlag, peerResourceParam.mResType);
        }
        decreRefCount();
        return i;
    }

    public int addBatchDcdnPeerRes(long j, int i, long j2, DcdnPeerResParam[] dcdnPeerResParamArr) {
        if (dcdnPeerResParamArr == null) {
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        int addBatchDcdnPeerRes = this.mLoader.addBatchDcdnPeerRes(j, i, j2, dcdnPeerResParamArr);
        decreRefCount();
        return addBatchDcdnPeerRes;
    }

    public int removeAddedResource(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int removeAddedResource = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.removeAddedResource(j, i);
        decreRefCount();
        return removeAddedResource;
    }

    public int removeServerResource(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int removeAddedServerResource = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.removeAddedServerResource(j, i);
        decreRefCount();
        return removeAddedServerResource;
    }

    int requeryTaskIndex(long j) {
        XLLoader xLLoader;
        return (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.requeryIndex(j);
    }

    public int setOriginUserAgent(long j, String str) {
        XLLoader xLLoader;
        increRefCount();
        int originUserAgent = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setOriginUserAgent(j, str);
        decreRefCount();
        return originUserAgent;
    }

    public int setUserId(String str) {
        XLLoader xLLoader;
        if (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) {
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        try {
            return xLLoader.setUserId(str);
        } catch (UnsatisfiedLinkError e2) {
            throw e2;
        }
    }

    public int getDownloadHeader(long j, GetDownloadHead getDownloadHead) {
        XLLoader xLLoader;
        increRefCount();
        int downloadHeader = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || getDownloadHead == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getDownloadHeader(j, getDownloadHead);
        decreRefCount();
        return downloadHeader;
    }

    public int setFileName(long j, String str) {
        XLLoader xLLoader;
        increRefCount();
        int fileName = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setFileName(j, str);
        decreRefCount();
        return fileName;
    }

    int notifyNetWorkCarrier(int i) {
        XLLoader xLLoader;
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            try {
                return xLLoader.setNotifyNetWorkCarrier(i);
            } catch (Error e2) {
                XLLog.e(TAG, "notifyNetWorkCarrier failed," + e2.getMessage());
            }
        }
        return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
    }

    void notifyWifiBSSID(String str) {
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && this.mLoader != null) {
            try {
                this.mLoader.setNotifyWifiBSSID(str == null || str.length() == 0 ? "" : str);
            } catch (Error e2) {
                XLLog.e(TAG, "setNotifyWifiBSSID failed," + e2.getMessage());
            }
        }
    }

    public int setDownloadTaskOrigin(long j, String str) {
        XLLoader xLLoader;
        increRefCount();
        int downloadTaskOrigin = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setDownloadTaskOrigin(j, str);
        decreRefCount();
        return downloadTaskOrigin;
    }

    int setMac(String str) {
        XLLoader xLLoader;
        return (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setMac(str);
    }

    int setImei(String str) {
        XLLoader xLLoader;
        return (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setImei(str);
    }

    private int setLocalProperty(String str, String str2) {
        XLLoader xLLoader;
        return (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null || str2 == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setLocalProperty(str, str2);
    }

    public int setOSVersion(String str) {
        XLLoader xLLoader;
        increRefCount();
        int miUiVersion = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setMiUiVersion(str);
        decreRefCount();
        return miUiVersion;
    }

    public int setHttpHeaderProperty(long j, String str, String str2) {
        XLLoader xLLoader;
        increRefCount();
        int httpHeaderProperty = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || str == null || str2 == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setHttpHeaderProperty(j, str, str2);
        decreRefCount();
        return httpHeaderProperty;
    }

    public int getDownloadLibVersion(GetDownloadLibVersion getDownloadLibVersion) {
        XLLoader xLLoader;
        increRefCount();
        int downloadLibVersion = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || getDownloadLibVersion == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getDownloadLibVersion(getDownloadLibVersion);
        decreRefCount();
        return downloadLibVersion;
    }

    public String getPeerid() {
        String peerid = XLUtil.getPeerid(this.mContext);
        return peerid == null ? "000000000000000V" : peerid;
    }

    private String getGuid() {
        XLUtil.GuidInfo generateGuid = XLUtil.generateGuid(this.mContext);
        if (generateGuid.mType != XLUtil.GUID_TYPE.ALL) {
            XLLog.i(TAG, "Start the GetGuidTimer");
            startGetGuidTimer();
        }
        return generateGuid.mGuid;
    }

    private void startGetGuidTimer() {
        new Thread(() -> {
            if (XLDownloadManager.this.mQueryGuidCount >= 5) {
                XLDownloadManager.this.stopGetGuidTimer();
                return;
            }
            mQueryGuidCount++;
            XLUtil.GuidInfo generateGuid = XLUtil.generateGuid(XLDownloadManager.this.mContext);
            if (generateGuid.mType == XLUtil.GUID_TYPE.ALL) {
                XLDownloadManager.this.stopGetGuidTimer();
            }
            if (generateGuid.mType != XLUtil.GUID_TYPE.DEFAULT) {
                XLDownloadManager.this.mLoader.setLocalProperty("Guid", generateGuid.mGuid);
            }
        }).start();
    }

    public void stopGetGuidTimer() {
        Timer timer = this.mGetGuidTimer;
        if (timer != null) {
            timer.cancel();
            this.mGetGuidTimer.purge();
            this.mGetGuidTimer = null;
            XLLog.i(TAG, "stopGetGuidTimer");
        }
        TimerTask timerTask = this.mGetGuidTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mGetGuidTimerTask = null;
        }
    }

    public int enterPrefetchMode(long j) {
        XLLoader xLLoader;
        increRefCount();
        int enterPrefetchMode = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.enterPrefetchMode(j);
        decreRefCount();
        return enterPrefetchMode;
    }

    public int setTaskLxState(long j, int i, int i2) {
        XLLoader xLLoader;
        increRefCount();
        int taskLxState = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setTaskLxState(j, i, i2);
        decreRefCount();
        return taskLxState;
    }

    public int setTaskGsState(long j, int i, int i2) {
        XLLoader xLLoader;
        increRefCount();
        int taskGsState = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setTaskGsState(j, i, i2);
        decreRefCount();
        return taskGsState;
    }

    public int setReleaseLog(boolean z, String str, int i, int i2) {
        int i3;
        XLLoader xLLoader;
        increRefCount();
        if (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) {
            i3 = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        } else if (z) {
            i3 = xLLoader.setReleaseLog(1, str, i, i2);
        } else {
            i3 = xLLoader.setReleaseLog(0, null, 0, 0);
        }
        decreRefCount();
        return i3;
    }

    public int setReleaseLog(boolean z, String str) {
        return setReleaseLog(z, str, 0, 0);
    }

    public boolean isLogTurnOn() {
        XLLoader xLLoader;
        increRefCount();
        boolean isLogTurnOn = mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null && xLLoader.isLogTurnOn();
        decreRefCount();
        return isLogTurnOn;
    }

    public int setStatReportSwitch(boolean z) {
        XLLoader xLLoader;
        increRefCount();
        int statReportSwitch = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setStatReportSwitch(z);
        decreRefCount();
        return statReportSwitch;
    }

    public int addServerResource(long j, int i, ServerResourceParam serverResourceParam) {
        if (serverResourceParam == null) {
            return XLConstant.XLErrorCode.PARAM_ERROR;
        }
        if (!serverResourceParam.checkMemberVar()) {
            return XLConstant.XLErrorCode.PARAM_ERROR;
        }
        try {
            increRefCount();
            if (this.mLoader == null) {
                return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
            } else if (XLConstant.XLManagerStatus.MANAGER_RUNNING != mDownloadManagerState) {
                return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
            } else {
                int addServerResource = this.mLoader.addServerResource(j, i, serverResourceParam.mUrl, serverResourceParam.mRefUrl, serverResourceParam.mCookie, serverResourceParam.mResType, serverResourceParam.mStrategy, serverResourceParam.mComeFrom);
                return addServerResource;
            }
        } finally {
            decreRefCount();
        }
    }

    private void loadErrcodeString(Context context) {
        if (context == null) {
            XLLog.e(TAG, "loadErrcodeString, context invalid");
        } else {
            mErrcodeStringMap = XLUtil.parseJSONString(ErrorCodeToMsg.ErrCodeToMsg);
        }
    }

    public String getErrorCodeMsg(int i) {
        String num = Integer.toString(i);
        String r2 = null;
        Map<String, Object> map = mErrcodeStringMap;
        if (map != null) {
            Object obj = map.get(num);
            r2 = obj != null ? obj.toString().trim() : null;
            XLLog.i(TAG, "errcode:" + i + ", errcodeMsg:" + r2);
        }
        return r2;
    }

    public int getUrlQuickInfo(long j, UrlQuickInfo urlQuickInfo) {
        XLLoader xLLoader;
        increRefCount();
        int urlQuickInfo2 = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || urlQuickInfo == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getUrlQuickInfo(j, urlQuickInfo);
        decreRefCount();
        return urlQuickInfo2;
    }

    public int createCIDTask(CIDTaskParam cIDTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (cIDTaskParam == null || getTaskId == null || !cIDTaskParam.checkMemberVar()) {
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            i = xLLoader.createCIDTask(cIDTaskParam.mCid, cIDTaskParam.mGcid, cIDTaskParam.mBcid, cIDTaskParam.mFilePath, cIDTaskParam.mFileName, cIDTaskParam.mFileSize, cIDTaskParam.mCreateMode, cIDTaskParam.mSeqId, getTaskId);
        }
        decreRefCount();
        return i;
    }

    public String parserThunderUrl(String str) {
        ThunderUrlInfo thunderUrlInfo = new ThunderUrlInfo();
        XLLoader xLLoader = this.mLoader;
        if (9000 == ((xLLoader == null || str == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.parserThunderUrl(str, thunderUrlInfo))) {
            return thunderUrlInfo.mUrl;
        }
        return null;
    }

    public int getFileNameFromUrl(String str, GetFileName getFileName) {
        XLLoader xLLoader = this.mLoader;
        return (xLLoader == null || str == null || getFileName == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getFileNameFromUrl(str, getFileName);
    }

    public int getNameFromUrl(String str, String str2) {
        XLLoader xLLoader = this.mLoader;
        return (xLLoader == null || str == null || str2 == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getNameFromUrl(str, str2);
    }

    public int setSpeedLimit(long j, long j2) {
        XLLog.d(TAG, "debug: XLDownloadManager::setSpeedLimit beg, maxDownloadSpeed=[" + j + "] maxUploadSpeed=[" + j2 + "]");
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "error: XLDownloadManager::setSpeedLimit mLoader is null, maxDownloadSpeed=[" + j + "] maxUploadSpeed=[" + j2 + "] ret=[" + XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR + "]");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int speedLimit = xLLoader.setSpeedLimit(j, j2);
        XLLog.d(TAG, "debug: XLDownloadManager::setSpeedLimit end, maxDownloadSpeed=[" + j + "] maxUploadSpeed=[" + j2 + "] ret=[" + speedLimit + "]");
        return speedLimit;
    }

    public int setTaskSpeedLimit(long j, long j2) {
        XLLog.d(TAG, "debug: XLDownloadManager::setTaskSpeedLimit beg, taskId=[" + j + "] maxDownloadSpeed=[" + j2 + "]");
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "error: XLDownloadManager::setTaskSpeedLimit mLoader is null, taskId=[" + j + "] maxDownloadSpeed=[" + j2 + "] ret=[" + XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR + "]");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int taskSpeedLimit = xLLoader.setTaskSpeedLimit(j, j2);
        XLLog.d(TAG, "debug: XLDownloadManager::setTaskSpeedLimit end, taskId=[" + j + "] maxDownloadSpeed=[" + j2 + "] ret=[" + taskSpeedLimit + "]");
        return taskSpeedLimit;
    }

    public int getMaxDownloadSpeed(MaxDownloadSpeedParam maxDownloadSpeedParam) {
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::getMaxDownloadSpeed mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int maxDownloadSpeed = xLLoader.getMaxDownloadSpeed(maxDownloadSpeedParam);
        if (9000 != maxDownloadSpeed) {
            XLLog.e(TAG, "XLDownloadManager::getMaxDownloadSpeed end, ret=[" + maxDownloadSpeed + "]");
            return maxDownloadSpeed;
        }
        XLLog.d(TAG, "XLDownloadManager::getMaxDownloadSpeed end, speed=[" + maxDownloadSpeedParam.mSpeed + "] ret=[" + maxDownloadSpeed + "]");
        return maxDownloadSpeed;
    }

    public int statExternalInfo(long j, int i, String str, String str2) {
        XLLog.d(TAG, "XLDownloadManager::statExternalInfo beg, taskId=[" + j + "] fileIndex=[" + i + "] key=[" + str + "] value=[" + str2 + "]");
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::statExternalInfo mLoader is null, taskId=[" + j + "] fileIndex=[" + i + "]");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int statExternalInfo = xLLoader.statExternalInfo(j, i, str, str2);
        if (9000 != statExternalInfo) {
            XLLog.e(TAG, "XLDownloadManager::statExternalInfo end, taskId=[" + j + "] fileIndex=[" + i + "] ret=[" + statExternalInfo + "]");
            return statExternalInfo;
        }
        XLLog.d(TAG, "XLDownloadManager::statExternalInfo end, taskId=[" + j + "] fileIndex=[" + i + "] ret=[" + statExternalInfo + "]");
        return statExternalInfo;
    }

    public int statExternalInfo(long j, int i, String str, int i2) {
        return statExternalInfo(j, i, str, String.valueOf(i2));
    }

    public int clearTaskFile(String str) {
        XLLog.d(TAG, "XLDownloadManager::clearTaskFile filePath=[" + str + "]");
        if (TextUtils.isEmpty(str)) {
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::clearTaskFile mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int clearTaskFile = xLLoader.clearTaskFile(str);
        if (9000 != clearTaskFile) {
            XLLog.e(TAG, "XLDownloadManager::clearTaskFile end, ret=[" + clearTaskFile + "]");
            return clearTaskFile;
        }
        return 9000;
    }

    public int startDcdn(long j, int i, String str, String str2, String str3) {
        XLLoader xLLoader;
        increRefCount();
        int startDcdn = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.startDcdn(j, i, str, str2, str3);
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::startDcdn ret=[%d] taskId=[%d] subIndex=[%d] sessionId=[%s] productType=[%s] verifyInfo=[%s]", Integer.valueOf(startDcdn), Long.valueOf(j), Integer.valueOf(i), str, str2, str3));
        return startDcdn;
    }

    public int stopDcdn(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int stopDcdn = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.stopDcdn(j, i);
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::stopDcdn ret=[%d] taskId=[%d] subIndex=[%d]", Integer.valueOf(stopDcdn), Long.valueOf(j), Integer.valueOf(i)));
        return stopDcdn;
    }

    public int createShortVideoTask(String str, String str2, String str3, String str4, int i, int i2, int i3, GetTaskId getTaskId) {
        String str5 = str4;
        XLLog.d(TAG, "XLDownloadManager::createShortVideoTask beg, url=[" + str + "] path=[" + str2 + "] filename=[" + str3 + "] title=[" + str5 + "]");
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::createShortVideoTask mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        if (str5 == null) {
            str5 = "default Title";
        }
        int createShortVideoTask = this.mLoader.createShortVideoTask(str, str2, str3, str5, i, i2, i3, getTaskId);
        if (9000 != createShortVideoTask) {
            XLLog.e(TAG, "XLDownloadManager::createShortVideoTask end, ret=[" + createShortVideoTask + "]");
            return createShortVideoTask;
        }
        XLLog.d(TAG, "XLDownloadManager::createShortVideoTask end, taskId=[" + getTaskId.getTaskId() + "] ret=[" + createShortVideoTask + "]");
        return createShortVideoTask;
    }

    public int playShortVideoBegin(long j) {
        XLLog.d(TAG, "XLDownloadManager::playShortVideoBegin beg, taskId=[" + j + "]");
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::playShortVideoBegin mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int playShortVideoBegin = xLLoader.playShortVideoBegin(j);
        if (9000 != playShortVideoBegin) {
            XLLog.e(TAG, "XLDownloadManager::playShortVideoBegin end, ret=[" + playShortVideoBegin + "]");
            return playShortVideoBegin;
        }
        XLLog.d(TAG, "XLDownloadManager::playShortVideoBegin end, taskId=[" + j + "] ret=[" + playShortVideoBegin + "]");
        return playShortVideoBegin;
    }

    public int getSessionInfoByUrl(String str, XLSessionInfo xLSessionInfo) {
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::getSessionInfoByUrl mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int sessionInfoByUrl = xLLoader.getSessionInfoByUrl(str, xLSessionInfo);
        if (9000 != sessionInfoByUrl) {
            XLLog.e(TAG, "XLDownloadManager::getSessionInfoByUrl end, ret=[" + sessionInfoByUrl + "]");
        }
        return sessionInfoByUrl;
    }

    public boolean getSettingValue(String str, String str2, boolean z) {
        if (this.mLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::getSettingValue mLoader is null");
            return z;
        }
        GetBooleanParam getBooleanParam = new GetBooleanParam();
        int settingBoolean = this.mLoader.getSettingBoolean(str, str2, getBooleanParam, z);
        if (9000 != settingBoolean) {
            XLLog.e(TAG, "XLDownloadManager::getSettingValue end, ret=[" + settingBoolean + "]");
            return z;
        }
        return getBooleanParam.getValue();
    }

    public int getDownloadRangeInfo(long j, int i, XLRangeInfo xLRangeInfo) {
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::getDownloadRangeInfo mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        return xLLoader.getDownloadRangeInfo(j, i, xLRangeInfo);
    }

    public int statExternalInfoU64(long j, int i, String str, long j2, int i2) {
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::statExternalInfoU64 mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int statExternalInfoU64 = xLLoader.statExternalInfoU64(j, i, str, j2, i2);
        if (9000 != statExternalInfoU64) {
            XLLog.e(TAG, "XLDownloadManager::statExternalInfoU64 end, ret=[" + statExternalInfoU64 + "]");
        }
        return statExternalInfoU64;
    }

    public int setIndexInfo(long j, SetIndexInfoParam setIndexInfoParam) {
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (setIndexInfoParam == null || this.mLoader == null) {
            XLLog.e(TAG, "setIndexInfo param or mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING) {
            i = this.mLoader.setIndexInfo(j, setIndexInfoParam.mCid, setIndexInfoParam.mGcid, setIndexInfoParam.mBcid, setIndexInfoParam.mFileSize, setIndexInfoParam.mGcidLevel);
        }
        decreRefCount();
        return i;
    }

    public int setAccelerateToken(long j, int i, long j2, int i2, String str) {
        int i3;
        XLLoader xLLoader;
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            i3 = xLLoader.setAccelerateToken(j, i, j2, i2, str);
            decreRefCount();
            XLLog.d(TAG, String.format("XLDownloadManager::setAccelerateToken ret=[%d] taskId=[%d] subIndex=[%d] appTaskId=[%d] accelerateType=[%d] token=[%s]", Integer.valueOf(i3), Long.valueOf(j), Integer.valueOf(i), Long.valueOf(j2), Integer.valueOf(i2), str));
            return i3;
        }
        i3 = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::setAccelerateToken ret=[%d] taskId=[%d] subIndex=[%d] appTaskId=[%d] accelerateType=[%d] token=[%s]", Integer.valueOf(i3), Long.valueOf(j), Integer.valueOf(i), Long.valueOf(j2), Integer.valueOf(i2), str));
        return i3;
    }

    public int removeAccelerateToken(long j, int i) {
        XLLoader xLLoader;
        increRefCount();
        int removeAccelerateToken = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.removeAccelerateToken(j, i);
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::removeAccelerateToken ret=[%d] taskId=[%d] subIndex=[%d]", Integer.valueOf(removeAccelerateToken), Long.valueOf(j), Integer.valueOf(i)));
        return removeAccelerateToken;
    }

    public int setVipType(String str) {
        XLLoader xLLoader;
        increRefCount();
        int vipType = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setVipType(str);
        decreRefCount();
        XLLog.d(TAG, String.format("XLDownloadManager::setVipType ret=[%d] vipType=[%s]", Integer.valueOf(vipType), str));
        return vipType;
    }

    public int setSlowAccelerateSpeed(long j, long j2) {
        XLLoader xLLoader;
        int slowAccelerateSpeed = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setSlowAccelerateSpeed(j, j2);
        XLLog.d(TAG, String.format("XLDownloadManager::setSlowAccelerateSpeed ret=[%d] taskId=[%d] speed=[%d]", Integer.valueOf(slowAccelerateSpeed), Long.valueOf(j), Long.valueOf(j2)));
        return slowAccelerateSpeed;
    }

    public int getFirstMediaState(long j, int i, XLFirstMediaState xLFirstMediaState) {
        XLLoader xLLoader;
        increRefCount();
        int firstMediaState = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getFirstMediaState(j, i, xLFirstMediaState);
        decreRefCount();
        return firstMediaState;
    }

    public int setUploadInfo(UploadInfo uploadInfo) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.setUploadInfo(uploadInfo);
                if (i != 9000) {
                    XLLog.w(TAG, "setUploadInfo failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "setUploadInfo failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int getUploadInfo(UploadInfo uploadInfo) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.getUploadInfo(uploadInfo);
                if (i != 9000) {
                    XLLog.w(TAG, "getUploadInfo failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "getUploadInfo failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int notifyUploadFileChanged(String str, String str2, long j) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.notifyUploadFileChanged(str, str2, j);
                if (i != 9000) {
                    XLLog.w(TAG, "notifyUploadFileChanged failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "notifyUploadFileChanged failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int createCDNTask(P2spTaskParam p2spTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (p2spTaskParam == null || getTaskId == null || !p2spTaskParam.checkMemberVar()) {
            XLLog.e(TAG, "createCDNTask failed, para=" + ((Object) p2spTaskParam) + ", cTaskId=" + ((Object) getTaskId));
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.createCDNTask(p2spTaskParam.mUrl, p2spTaskParam.mRefUrl, p2spTaskParam.mCookie, p2spTaskParam.mUser, p2spTaskParam.mPass, p2spTaskParam.mFilePath, p2spTaskParam.mFileName, p2spTaskParam.mCreateMode, p2spTaskParam.mSeqId, getTaskId);
                if (i != 9000) {
                    XLLog.w(TAG, "createCDNTask failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "createCDNTask failed," + e2.getMessage());
            }
        }
        decreRefCount();
        return i;
    }

    public int setUploadSwitch(boolean z) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.setUploadSwitch(z);
                if (i != 9000) {
                    XLLog.w(TAG, "setUploadSwitch failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "setUploadSwitch failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int setUploadControlParam(UploadControlParam uploadControlParam) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.setUploadControlParam(uploadControlParam);
                if (i != 9000) {
                    XLLog.w(TAG, "setUploadControlParam failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "setUploadControlParam failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int resetUploadInfo() {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.resetUploadInfo();
                if (i != 9000) {
                    XLLog.w(TAG, "resetUploadInfo failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "resetUploadInfo failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int setCandidateResSpeed(long j, int i) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i2 = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i2 = xLLoader.setCandidateResSpeed(j, i);
                if (i2 != 9000) {
                    XLLog.w(TAG, "setCandidateResSpeed failed, ret=" + i2);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "setCandidateResSpeed failed," + e2.getMessage());
            }
        }
        return i2;
    }

    public int createVodTask(P2spTaskParam p2spTaskParam, int i, GetTaskId getTaskId) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i2 = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i2 = xLLoader.createVodTask(p2spTaskParam.mUrl, p2spTaskParam.mRefUrl, p2spTaskParam.mCookie, p2spTaskParam.mUser, p2spTaskParam.mPass, p2spTaskParam.mFilePath, p2spTaskParam.mFileName, p2spTaskParam.mCreateMode, p2spTaskParam.mSeqId, i, getTaskId);
                if (i2 != 9000) {
                    XLLog.w(TAG, "createVodTask failed, ret=" + i2);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "createVodTask failed," + e2.getMessage());
            }
        }
        return i2;
    }

    public int setPlayerMode(long j, int i) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i2 = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i2 = xLLoader.setPlayerMode(j, i);
                if (i2 != 9000) {
                    XLLog.w(TAG, "setPlayerMode failed, ret=" + i2);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "setPlayerMode failed," + e2.getMessage());
            }
        }
        return i2;
    }

    public int changeOriginRes(long j, String str) {
        XLLoader xLLoader;
        XLConstant.XLManagerStatus xLManagerStatus = mDownloadManagerState;
        XLConstant.XLManagerStatus xLManagerStatus2 = XLConstant.XLManagerStatus.MANAGER_RUNNING;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (xLManagerStatus == xLManagerStatus2 && (xLLoader = this.mLoader) != null) {
            try {
                i = xLLoader.changeOriginRes(j, str);
                if (i != 9000) {
                    XLLog.w(TAG, "changeOriginRes failed, ret=" + i);
                }
            } catch (Error e2) {
                XLLog.e(TAG, "changeOriginRes failed," + e2.getMessage());
            }
        }
        return i;
    }

    public int setBtSwitch(int i) {
        XLLoader xLLoader;
        increRefCount();
        int btSwitch = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setBtSwitch(i);
        decreRefCount();
        return btSwitch;
    }

    public int setEmuleSwitch(int i) {
        XLLoader xLLoader;
        increRefCount();
        int emuleSwitch = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.setEmuleSwitch(i);
        decreRefCount();
        return emuleSwitch;
    }

    public int createBtMagnetTask(MagnetTaskParam magnetTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (magnetTaskParam != null && getTaskId != null && magnetTaskParam.checkMemberVar()) {
            increRefCount();
            if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
                i = xLLoader.createBtMagnetTask(magnetTaskParam.mUrl, magnetTaskParam.mFilePath, magnetTaskParam.mFileName, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int createEmuleTask(EmuleTaskParam emuleTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (emuleTaskParam != null && getTaskId != null && emuleTaskParam.checkMemberVar()) {
            increRefCount();
            if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
                i = xLLoader.createEmuleTask(emuleTaskParam.mUrl, emuleTaskParam.mFilePath, emuleTaskParam.mFileName, emuleTaskParam.mCreateMode, emuleTaskParam.mSeqId, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int createBtTask(BtTaskParam btTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (btTaskParam != null && getTaskId != null && btTaskParam.checkMemberVar()) {
            increRefCount();
            if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
                i = xLLoader.createBtTask(btTaskParam.mTorrentPath, btTaskParam.mFilePath, btTaskParam.mMaxConcurrent, btTaskParam.mCreateMode, btTaskParam.mSeqId, getTaskId);
            }
            decreRefCount();
        }
        return i;
    }

    public int createHLSTask(HLSTaskParam hLSTaskParam, GetTaskId getTaskId) {
        XLLoader xLLoader;
        int i = XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        if (hLSTaskParam == null || getTaskId == null || !hLSTaskParam.checkMemberVar()) {
            XLLog.e(TAG, "createHLSTask failed, para=" + ((Object) hLSTaskParam) + ", cTaskId=" + ((Object) getTaskId));
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        increRefCount();
        if (mDownloadManagerState == XLConstant.XLManagerStatus.MANAGER_RUNNING && (xLLoader = this.mLoader) != null) {
            i = xLLoader.createHLSTask(hLSTaskParam.mUrl, hLSTaskParam.mRefUrl, hLSTaskParam.mCookie, hLSTaskParam.mUser, hLSTaskParam.mPass, hLSTaskParam.mFilePath, hLSTaskParam.mFileName, hLSTaskParam.mBandwidth, hLSTaskParam.mMaxConcurrent, hLSTaskParam.mCreateMode, hLSTaskParam.mSeqId, getTaskId);
            if (i != 9000) {
                XLLog.e(TAG, "createHLSTask failed, ret=" + i);
            }
            decreRefCount();
            return i;
        }
        XLLog.e(TAG, "createHLSTask failed, mDownloadManagerState=" + ((Object) mDownloadManagerState));
        decreRefCount();
        return i;
    }

    public int getTorrentInfo(String str, TorrentInfo torrentInfo) {
        increRefCount();
        XLLoader xLLoader = this.mLoader;
        int torrentInfo2 = (xLLoader == null || str == null || torrentInfo == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getTorrentInfo(str, torrentInfo);
        decreRefCount();
        return torrentInfo2;
    }

    public int getBtSubTaskStatus(long j, BtTaskStatus btTaskStatus, int i, int i2) {
        XLLoader xLLoader;
        increRefCount();
        int btSubTaskStatus = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || btTaskStatus == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getBtSubTaskStatus(j, btTaskStatus, i, i2);
        decreRefCount();
        return btSubTaskStatus;
    }

    public int getBtSubTaskInfo(long j, int i, BtSubTaskDetail btSubTaskDetail) {
        XLLoader xLLoader;
        increRefCount();
        int btSubTaskInfo = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || btSubTaskDetail == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.getBtSubTaskInfo(j, i, btSubTaskDetail);
        decreRefCount();
        return btSubTaskInfo;
    }

    public int selectBtSubTask(long j, BtIndexSet btIndexSet) {
        XLLoader xLLoader;
        increRefCount();
        int selectBtSubTask = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || btIndexSet == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.selectBtSubTask(j, btIndexSet);
        decreRefCount();
        return selectBtSubTask;
    }

    public int deselectBtSubTask(long j, BtIndexSet btIndexSet) {
        XLLoader xLLoader;
        increRefCount();
        int deselectBtSubTask = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null || btIndexSet == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.deselectBtSubTask(j, btIndexSet);
        decreRefCount();
        return deselectBtSubTask;
    }

    public int addBtTrackerNodes(long j, String str) {
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::addBtTrackerNodes mLoader is null");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int addBtTrackerNodes = xLLoader.addBtTrackerNodes(j, str);
        if (9000 != addBtTrackerNodes) {
            XLLog.e(TAG, "XLDownloadManager::addBtTrackerNodes end, ret=[" + addBtTrackerNodes + "]");
        }
        return addBtTrackerNodes;
    }

    public int btAddPeerResource(long j, int i, PeerResourceParam peerResourceParam) {
        if (peerResourceParam == null) {
            return XLConstant.XLErrorCode.PARAM_ERROR;
        }
        if (!peerResourceParam.checkMemberVar()) {
            return XLConstant.XLErrorCode.PARAM_ERROR;
        }
        try {
            increRefCount();
            if (this.mLoader == null) {
                return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
            } else if (XLConstant.XLManagerStatus.MANAGER_RUNNING != mDownloadManagerState) {
                return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
            } else {
                int btAddPeerResource = this.mLoader.btAddPeerResource(j, i, peerResourceParam.mPeerId, peerResourceParam.mUserId, peerResourceParam.mJmpKey, peerResourceParam.mVipCdnAuth, peerResourceParam.mInternalIp, peerResourceParam.mTcpPort, peerResourceParam.mUdpPort, peerResourceParam.mResLevel, peerResourceParam.mResPriority, peerResourceParam.mCapabilityFlag, peerResourceParam.mResType);
                return btAddPeerResource;
            }
        } finally {
            decreRefCount();
        }
    }

    public int btRemoveAddedResource(long j, int i, int i2) {
        XLLoader xLLoader;
        increRefCount();
        int btRemoveAddedResource = (mDownloadManagerState != XLConstant.XLManagerStatus.MANAGER_RUNNING || (xLLoader = this.mLoader) == null) ? XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR : xLLoader.btRemoveAddedResource(j, i, i2);
        decreRefCount();
        return btRemoveAddedResource;
    }

    public int setBtPriorSubTask(long j, int i) {
        XLLog.d(TAG, "XLDownloadManager::setBtPriorSubTask beg, taskId=[" + j + "] fileIndex=[" + i + "]");
        XLLoader xLLoader = this.mLoader;
        if (xLLoader == null) {
            XLLog.e(TAG, "XLDownloadManager::setBtPriorSubTask mLoader is null, taskId=[" + j + "] fileIndex=[" + i + "]");
            return XLConstant.XLErrorCode.DOWNLOAD_MANAGER_ERROR;
        }
        int btPriorSubTask = xLLoader.setBtPriorSubTask(j, i);
        if (9000 != btPriorSubTask) {
            XLLog.e(TAG, "XLDownloadManager::setBtPriorSubTask end, taskId=[" + j + "] fileIndex=[" + i + "] ret=[" + btPriorSubTask + "]");
            return btPriorSubTask;
        }
        XLLog.d(TAG, " XLDownloadManager::setBtPriorSubTask end, taskId=[" + j + "] fileIndex=[" + i + "]");
        return 9000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes16.dex */
    public class NetworkChangeReceiver extends BroadcastReceiver {
        private NetworkChangeReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || !action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    XLDownloadManager.this.notifyNetWorkInfo(context);
                }
            }).start();
        }
    }
}
