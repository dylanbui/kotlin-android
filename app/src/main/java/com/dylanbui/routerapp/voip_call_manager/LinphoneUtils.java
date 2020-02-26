/*
 * Copyright (c) 2010-2019 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.dylanbui.routerapp.voip_call_manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dylanbui.routerapp.R;
import com.dylanbui.routerapp.easyphone.PhoneBean;

import org.linphone.core.AVPFMode;
import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.CallLog;
import org.linphone.core.CallParams;
import org.linphone.core.ChatMessage;
import org.linphone.core.Content;
import org.linphone.core.Core;
import org.linphone.core.CoreException;
import org.linphone.core.EventLog;
import org.linphone.core.Factory;
import org.linphone.core.LogCollectionState;
import org.linphone.core.ProxyConfig;
import org.linphone.core.TransportType;
import org.linphone.core.tools.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

/** Helpers. */
public final class LinphoneUtils {
    private static final String TAG = "LinphoneUtils";
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private LinphoneUtils() {}


    public static void dispatchOnUIThread(Runnable r) {
        sHandler.post(r);
    }

    public static void dispatchOnUIThreadAfter(Runnable r, long after) {
        sHandler.postDelayed(r, after);
    }

    public static void removeFromUIThreadDispatcher(Runnable r) {
        sHandler.removeCallbacks(r);
    }

    private static boolean isSipAddress(String numberOrAddress) {
        Factory.instance().createAddress(numberOrAddress);
        return true;
    }


    public static boolean isStrictSipAddress(String numberOrAddress) {
        return isSipAddress(numberOrAddress) && numberOrAddress.startsWith("sip:");
    }

    public static String getDisplayableAddress(Address addr) {
        return "sip:" + addr.getUsername() + "@" + addr.getDomain();
    }

    public static String getAddressDisplayName(String uri) {
        Address lAddress;
        lAddress = Factory.instance().createAddress(uri);
        return getAddressDisplayName(lAddress);
    }

    public static String getAddressDisplayName(Address address) {
        if (address == null) return null;

        String displayName = address.getDisplayName();
        if (displayName == null || displayName.isEmpty()) {
            displayName = address.getUsername();
        }
        if (displayName == null || displayName.isEmpty()) {
            displayName = address.asStringUriOnly();
        }
        return displayName;
    }

    public static String timestampToHumanDate(Context context, long timestamp, int format) {
        return timestampToHumanDate(context, timestamp, context.getString(format));
    }

    public static String timestampToHumanDate(Context context, long timestamp, String format) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp * 1000); // Core returns timestamps in seconds...

            SimpleDateFormat dateFormat;
            if (isToday(cal)) {
                dateFormat =
                        new SimpleDateFormat(
                                context.getResources().getString(R.string.today_date_format),
                                Locale.getDefault());
            } else {
                dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            }

            return dateFormat.format(cal.getTime());
        } catch (NumberFormatException nfe) {
            return String.valueOf(timestamp);
        }
    }

    private static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private static boolean isCallRunning(Call call) {
        if (call == null) {
            return false;
        }

        Call.State state = call.getState();

        return state == Call.State.Connected
                || state == Call.State.Updating
                || state == Call.State.UpdatedByRemote
                || state == Call.State.StreamsRunning
                || state == Call.State.Resuming;
    }

    public static boolean isCallEstablished(Call call) {
        if (call == null) {
            return false;
        }

        Call.State state = call.getState();

        return isCallRunning(call)
                || state == Call.State.Paused
                || state == Call.State.PausedByRemote
                || state == Call.State.Pausing;
    }

    public static boolean isHighBandwidthConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null
                && info.isConnected()
                && isConnectionFast(info.getType(), info.getSubtype()));
    }

    private static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false;
            }
        }
        // in doubt, assume connection is good.
        return true;
    }


    public static void displayErrorAlert(String msg, Context ctxt) {
        if (ctxt != null && msg != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setNeutralButton(ctxt.getString(R.string.ok), null)
                    .show();
        }
    }

    public static void registerUserAuth(String name, String password, String host, String proxy) {
        Log.e(TAG, "registerUserAuth name = " + name);
        Log.e(TAG, "registerUserAuth pw = " + password);
        Log.e(TAG, "registerUserAuth host = " + host);

        Core mCore = LinphoneService.getCore();
        if (mCore == null) {
            Log.e(TAG, "LinphoneService core == null");
            return;
        }

//        String identify = "sip:" + name + "@" + host;
//        proxy = "sip:" + host;

        AccountCreator mAccountCreator = mCore.createAccountCreator(null);

        // At least the 3 below values are required
        mAccountCreator.setUsername(name);
        mAccountCreator.setPassword(password);
        mAccountCreator.setDomain(host);
        mAccountCreator.setTransport(TransportType.Udp);

        // This will automatically create the proxy config and auth info and add them to the Core
        ProxyConfig cfg = mAccountCreator.createProxyConfig();
        cfg.setAvpfMode(AVPFMode.Enabled);
        cfg.setAvpfRrInterval(0);
        cfg.enableQualityReporting(false);
        cfg.setQualityReportingCollector(null);
        cfg.setQualityReportingInterval(0);
        cfg.enableRegister(true);


        // String identify = "sip:" + strName + "@" + strHost;
        // String proxy = "sip:" + host;

        // Make identify
//        Address identifyAdd = Factory.instance().createAddress(identify);
//        cfg.setIdentityAddress(identifyAdd);
//
        cfg.setRoute(proxy);
        cfg.setServerAddr(proxy);

        // Make sure the newly created one is the default
        mCore.setDefaultProxyConfig(cfg);

        Log.i("getIdentityAddress = " + cfg.getIdentityAddress().asStringUriOnly());
        Log.i("getRoutes = " + cfg.getRoutes()[0]);
        Log.i("getServerAddr = " + cfg.getServerAddr());


//        LinphoneAddress proxyAddr = LinphoneCoreFactory.instance().createLinphoneAddress(proxy);
//        LinphoneAddress identifyAddr = LinphoneCoreFactory.instance().createLinphoneAddress(identify);
//        LinphoneAuthInfo authInfo = LinphoneCoreFactory.instance().createAuthInfo(name, null, password,
//                null, null, host);
//        LinphoneProxyConfig prxCfg = mLinphoneCore.createProxyConfig(identifyAddr.asString(),
//                proxyAddr.asStringUriOnly(), proxyAddr.asStringUriOnly(), true);
//        prxCfg.enableAvpf(false);
//        prxCfg.setAvpfRRInterval(0);
//        prxCfg.enableQualityReporting(false);
//        prxCfg.setQualityReportingCollector(null);
//        prxCfg.setQualityReportingInterval(0);
//        prxCfg.enableRegister(true);
//        mLinphoneCore.addProxyConfig(prxCfg);
//        mLinphoneCore.addAuthInfo(authInfo);
//        mLinphoneCore.setDefaultProxyConfig(prxCfg);
    }

    public static Call startSingleCallingTo(PhoneBean bean) {
        Core mCore = LinphoneService.getCore();
        if (mCore == null) return null;

        Address address;

        address = mCore.interpretUrl(bean.getUserName() + "@" + bean.getHost());

        address.setDisplayName(bean.getDisplayName());
        CallParams params = mCore.createCallParams(null);
        return mCore.inviteAddressWithParams(address, params);
    }

    public static boolean acceptCall(Call call) {
        Core mCore = LinphoneService.getCore();
        if (mCore == null || call == null) return false;

        CallParams params = mCore.createCallParams(call);

//        boolean isLowBandwidthConnection =
//                !LinphoneUtils.isHighBandwidthConnection(
//                        LinphoneContext.instance().getApplicationContext());

        if (params != null) {
//            params.enableLowBandwidth(isLowBandwidthConnection);
//            params.setRecordFile(
//                    FileUtils.getCallRecordingFilename(mContext, call.getRemoteAddress()));
        } else {
            Log.e("[Call Manager] Could not create call params for call");
            return false;
        }

        call.acceptWithParams(params);
        return true;
    }

    /**
     * 挂断电话
     */
    public static void hangUp() {
        Core core = LinphoneService.getCore();
        if (core == null) return;
        if (core.getCallsNb() > 0) {
            Call call = core.getCurrentCall();
            if (call == null) {
                // Current call can be null if paused for example
                call = core.getCalls()[0];
            }
            call.terminate();
        }
//        Core mCore = LinphoneService.getCore();
//        if (mCore == null) return;
//        if (mCore.isInConference()) {
//            mCore.terminateConference();
//        } else {
//            mCore.terminateAllCalls();
//        }
    }

    public static void removeAuthConfig() {
        Core core = LinphoneService.getCore();
        if (core == null) return;
        // Remove old config
        core.removeProxyConfig(core.getDefaultProxyConfig());
    }
}
