package com.xunlei.downloadlib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import androidx.exifinterface.media.ExifInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes16.dex */
public class XLUtil {

    /* renamed from: a  reason: collision with root package name */
    private static final String TAG = "XLUtil";

    private static final ConfigFile config = new ConfigFile();

    public static String getIMEI(Context context) {
        return null;
    }

    public static String getMAC(Context context) {
        return null;
    }

    public static long getCurrentUnixTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getPeerid(Context context) {
        if (context == null) {
            return null;
        }
        String peerId = config.get(context, "peerid", null);
        if (TextUtils.isEmpty(peerId)) {
            String a2 = getEncryptedUUID(context);
            if (TextUtils.isEmpty(a2)) {
                return null;
            }
            String str2 = a2 + ExifInterface.GPS_MEASUREMENT_INTERRUPTED;
            config.set(context, "peerid", str2);
            return str2;
        }
        return peerId;
    }

    private static String getEncryptedUUID(Context context) {
        String uuid = getUUID(context);
        if (TextUtils.isEmpty(uuid)) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(uuid.getBytes(StandardCharsets.UTF_8));
            return byteToHex(messageDigest.digest()).substring(0, 15);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static String getUUID(Context context) {
        if (context == null) {
            return null;
        }
        String str = config.get(context, "UUID", null);
        if (TextUtils.isEmpty(str)) {
            String upperCase = UUID.randomUUID().toString().toUpperCase();
            if (!TextUtils.isEmpty(upperCase)) {
                config.set(context, "UUID", upperCase);
            }
            return upperCase;
        }
        return str;
    }

    private static String byteToHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString);
        }
        return sb.toString().toUpperCase(Locale.CHINA);
    }

    public static GuidInfo generateGuid(Context context) {
        GuidInfo guidInfo = new GuidInfo();
        guidInfo.mGuid = "000000000000000_000000000000";
        guidInfo.mType = GUID_TYPE.DEFAULT;
        if (context != null) {
            String b2 = getUUID(context);
            if (!TextUtils.isEmpty(b2)) {
                guidInfo.mGuid = b2;
                guidInfo.mType = GUID_TYPE.UUID;
            }
        }
        return guidInfo;
    }

    public static String getOSVersion(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("SDKV = ").append(Build.VERSION.RELEASE);
        sb.append("_MANUFACTURER = ").append(Build.MANUFACTURER);
        sb.append("_MODEL = ").append(Build.MODEL);
        sb.append("_PRODUCT = ").append(Build.PRODUCT);
        sb.append("_FINGERPRINT = ").append(Build.FINGERPRINT);
        sb.append("_CPU_ABI = ").append(Build.CPU_ABI);
        sb.append("_ID = ").append(Build.ID);
        return sb.toString();
    }

    public static String getAPNName(Context context) {
        ConnectivityManager connectivityManager;
        NetworkInfo networkInfo;
        if (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)) == null || (networkInfo = connectivityManager.getNetworkInfo(0)) == null) {
            return null;
        }
        return networkInfo.getExtraInfo();
    }

    public static String getSSID(Context context) {
        return null;
    }

    public static String getBSSID(Context context) {

        return null;
    }

    public static int getNetworkTypeComplete(Context context) {
        return 1;
    }

    public static int getNetworkTypeSimple(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null) {
            XLLog.e(TAG, "getNetworkTypeSimple, context invalid");
            return 0;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return 1;
        }
        int type = activeNetworkInfo.getType();
        if (type == 1) {
            return 2;
        }
        if (type == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    return 3;
                default:
                    return 1;
            }
        }
        return 1;
    }

    public static Map<String, Object> parseJSONString(String str) {
        if (str == null) {
            XLLog.e(TAG, "parseJSONString, JSONString invalid");
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                hashMap.put(next, jSONObject.getString(next));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return hashMap;
    }

    public static NetWorkCarrier getNetWorkCarrier(Context context) {
        return NetWorkCarrier.UNKNOWN;
    }

    public enum GUID_TYPE {
        DEFAULT,
        JUST_IMEI,
        JUST_MAC,
        ALL,
        UUID
    }

    public enum NetWorkCarrier {
        UNKNOWN,
        CMCC,
        CU,
        CT
    }

    public static class GuidInfo {
        public GUID_TYPE mType = GUID_TYPE.DEFAULT;
        public String mGuid = null;
    }

    public static class ConfigFile {
        private static final String IDENTIFY_FILE_NAME = "Identify.txt";
        private final Map<String, String> configMap;
        private final ReadWriteLock lock;

        private ConfigFile() {
            this.configMap = new HashMap<>();
            this.lock = new ReentrantReadWriteLock();
        }

        String get(Context context, String str, String str2) {
            this.lock.readLock().lock();
            String str3 = this.configMap.get(str);
            if (str3 == null) {
                loadFile(context);
                str3 = this.configMap.get(str);
            }
            this.lock.readLock().unlock();
            return str3 != null ? str3 : str2;
        }

        void set(Context context, String str, String str2) {
            this.lock.writeLock().lock();
            this.configMap.put(str, str2);
            saveFile(context);
            this.lock.writeLock().unlock();
            XLLog.i(XLUtil.TAG, "ConfigFile set key=" + str + " value=" + str2);
        }

        private void loadFile(Context context) {
            XLLog.i(XLUtil.TAG, "loadAndParseFile start");
            if (context == null) {
                XLLog.e(XLUtil.TAG, "loadAndParseFile end, parameter invalid, fileName:Identify.txt");
                return;
            }
            String readFromFile = readFromFile(context, IDENTIFY_FILE_NAME);
            if (readFromFile == null) {
                XLLog.i(XLUtil.TAG, "loadAndParseFile end, fileContext is empty");
                return;
            }
            this.configMap.clear();
            for (String str : readFromFile.split("\n")) {
                String[] split = str.split("=");
                if (split.length == 2) {
                    this.configMap.put(split[0], split[1]);
                    XLLog.i(XLUtil.TAG, "ConfigFile loadFile key=" + split[0] + " value=" + split[1]);
                }
            }
            XLLog.i(XLUtil.TAG, "loadAndParseFile end");
        }

        private void saveFile(Context context) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : this.configMap.entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            writeToFile(context, sb.toString(), IDENTIFY_FILE_NAME);
        }

        private void writeToFile(Context context, String str, String str2) {
            if (context == null || str == null || str2 == null) {
                XLLog.e(XLUtil.TAG, "writeToFile, Parameter invalid, fileName:" + str2);
                return;
            }
            try {
                FileOutputStream openFileOutput = context.openFileOutput(str2, 0);
                try {
                    openFileOutput.write(str.getBytes(StandardCharsets.UTF_8));
                    openFileOutput.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            }
        }

        private String readFromFile(Context context, String str) {
            if (context == null || str == null) {
                XLLog.e(XLUtil.TAG, "readFromFile, parameter invalid, fileName:" + str);
                return null;
            }
            StringBuilder sb = new StringBuilder();
            try {
                FileInputStream openFileInput = context.openFileInput(str);
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception unused) {
                XLLog.i(XLUtil.TAG, str + " File Not Found");
            }
            return sb.toString();
        }
    }
}
