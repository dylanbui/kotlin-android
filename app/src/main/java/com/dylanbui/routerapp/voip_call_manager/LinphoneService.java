package com.dylanbui.routerapp.voip_call_manager;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dylanbui.routerapp.R;

import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.LogCollectionState;
import org.linphone.core.MediaDirection;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.tools.Log;
import org.linphone.mediastream.Version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.ACTION_MAIN;

public class LinphoneService extends Service {

    private static final String START_LINPHONE_LOGS = " ==== Device information dump ====";
    // Keep a static reference to the Service so we can access it from anywhere in the app
    private static LinphoneService sInstance;

    private Handler mHandler;
    private Timer mTimer;

    private Core mCore;
    private CoreListenerStub mCoreListener;
    private CoreListenerStub mCoreRegistrationListener;

    public static boolean isReady() {
        return sInstance != null;
    }

    public static LinphoneService getInstance() {
        return sInstance;
    }

    public static Core getCore() {
        return sInstance.mCore;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // The first call to liblinphone SDK MUST BE to a Factory method
        // So let's enable the library debug logs & log collection
        String basePath = getFilesDir().getAbsolutePath();
        Factory.instance().setLogCollectionPath(basePath);
        Factory.instance().enableLogCollection(LogCollectionState.Enabled);
        Factory.instance().setDebugMode(true, getString(R.string.app_name));

        // Dump some useful information about the device we're running on
        Log.i(START_LINPHONE_LOGS);
        dumpDeviceInformation();
        dumpInstalledLinphoneInformation();

        mHandler = new Handler();

        // Account creator can help you create/config accounts, even not sip.linphone.org ones
        // As we only want to configure an existing account, no need for server URL to make requests
        // to know whether or not account exists, etc...
        mCoreRegistrationListener = new CoreListenerStub() {
            @Override
            public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState state, String message) {
                if (state == RegistrationState.Ok) {
                    Toast.makeText(LinphoneService.this, "RegistrationState.Ok", Toast.LENGTH_LONG).show();
                } else if (state == RegistrationState.Failed) {
                    Toast.makeText(LinphoneService.this, "Failure: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        // This will be our main Core listener, it will change activities depending on events
        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {

                // Hien thi cac trang thai goi
                Toast.makeText(LinphoneService.this, "LinphoneService : " + message, Toast.LENGTH_SHORT).show();
                Log.i("[LinphoneService] Call state is [", state, "]");

                // Hien notification khi co cuoc goi den
//                if (mContext.getResources().getBoolean(R.bool.enable_call_notification)) {
//                    mNotificationManager.displayCallNotification(call);
//                }

                if (state == Call.State.IncomingReceived
                        || state == Call.State.IncomingEarlyMedia) {
                    // Starting SDK 24 (Android 7.0) we rely on the fullscreen intent of the
                    // call incoming notification
//                    if (Version.sdkStrictlyBelow(Version.API24_NOUGAT_70)) {
//                        if (!mLinphoneManager.getCallGsmON()) onIncomingReceived();
//                    }
                    // Kiem tra neu dang co cuoc goi thi tu choi cuoc goi
                    onIncomingReceived();

                    // In case of push notification Service won't be started until here
                    if (!LinphoneService.isReady()) {
                        Log.i("[LinphoneService] Service not running, starting it");
                        Intent intent = new Intent(ACTION_MAIN);
                        intent.setClass(LinphoneService.this, LinphoneService.class);
                        startService(intent);
                    }
                } else if (state == Call.State.OutgoingInit) {
                    onOutgoingStarted();

                } else if (state == Call.State.Connected) {
                    // onCallStarted();

                } else if (state == Call.State.End
                        || state == Call.State.Released
                        || state == Call.State.Error) {
                    if (LinphoneService.isReady()) {
                        // LinphoneService.instance().destroyOverlay();
                    }

                    if (state == Call.State.Released
                            && call.getCallLog().getStatus() == Call.Status.Missed) {
                        // mNotificationManager.displayMissedCallNotification(call);
                    }
                }

//                if (state == Call.State.IncomingReceived) {
//                    Toast.makeText(LinphoneService.this, "Incoming call received, answering it automatically", Toast.LENGTH_LONG).show();
//                    // For this sample we will automatically answer incoming calls
//                    CallParams params = getCore().createCallParams(call);
//                    // params.enableVideo(true);
//                    params.enableVideo(false);
//                    call.acceptWithParams(params);
//                } else if (state == Call.State.Connected) {
//                    // This stats means the call has been established, let's start the call activity
//                    Intent intent = new Intent(LinphoneService.this, VoipCallActivity.class);
//                    // As it is the Service that is starting the activity, we have to give this flag
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
            }
        };

        try {
            // Let's copy some RAW resources to the device
            // The default config file must only be installed once (the first time)
            copyIfNotExist(R.raw.linphonerc_default, basePath + "/.linphonerc");
            // The factory config is used to override any other setting, let's copy it each time
            copyFromPackage(R.raw.linphonerc_factory, "linphonerc");
        } catch (IOException ioe) {
            Log.e(ioe);
        }

        // Create the Core and add our listener
        mCore = Factory.instance()
                .createCore(basePath + "/.linphonerc", basePath + "/linphonerc", this);
        mCore.addListener(mCoreListener);
        mCore.addListener(mCoreRegistrationListener);
        // Core is ready to be configured
        configureCore();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // If our Service is already running, no need to continue
        if (sInstance != null) {
            return START_STICKY;
        }

        // Our Service has been started, we can keep our reference on it
        // From now one the Launcher will be able to call onServiceReady()
        sInstance = this;

        // Core must be started after being created and configured
        mCore.start();
        // We also MUST call the iterate() method of the Core on a regular basis
        TimerTask lTask = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mCore != null) {
                                            mCore.iterate();
                                        }
                                    }
                                });
                    }
                };
        mTimer = new Timer("Linphone scheduler");
        mTimer.schedule(lTask, 0, 20);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mCore.removeListener(mCoreListener);
        mCore.removeListener(mCoreRegistrationListener);
        mTimer.cancel();
        mCore.stop();
        // A stopped Core can be started again
        // To ensure resources are freed, we must ensure it will be garbage collected
        mCore = null;
        // Don't forget to free the singleton as well
        sInstance = null;

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // For this sample we will kill the Service at the same time we kill the app
        stopSelf();

        super.onTaskRemoved(rootIntent);
    }

    private void configureCore() {
        // We will create a directory for user signed certificates if needed
        String basePath = getFilesDir().getAbsolutePath();
        String userCerts = basePath + "/user-certs";
        File f = new File(userCerts);
        if (!f.exists()) {
            if (!f.mkdir()) {
                Log.e(userCerts + " can't be created.");
            }
        }
        mCore.setUserCertificatesPath(userCerts);
    }

    private void dumpDeviceInformation() {
        StringBuilder sb = new StringBuilder();
        sb.append("DEVICE=").append(Build.DEVICE).append("\n");
        sb.append("MODEL=").append(Build.MODEL).append("\n");
        sb.append("MANUFACTURER=").append(Build.MANUFACTURER).append("\n");
        sb.append("SDK=").append(Build.VERSION.SDK_INT).append("\n");
        sb.append("Supported ABIs=");
        for (String abi : Version.getCpuAbis()) {
            sb.append(abi).append(", ");
        }
        sb.append("\n");
        Log.i(sb.toString());
    }

    private void dumpInstalledLinphoneInformation() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException nnfe) {
            Log.e(nnfe);
        }

        if (info != null) {
            Log.i(
                    "[Service] Linphone version is ",
                    info.versionName + " (" + info.versionCode + ")");
        } else {
            Log.i("[Service] Linphone version is unknown");
        }
    }

    private void copyIfNotExist(int ressourceId, String target) throws IOException {
        File lFileToCopy = new File(target);
        if (!lFileToCopy.exists()) {
            copyFromPackage(ressourceId, lFileToCopy.getName());
        }
    }

    private void copyFromPackage(int ressourceId, String target) throws IOException {
        FileOutputStream lOutputStream = openFileOutput(target, 0);
        InputStream lInputStream = getResources().openRawResource(ressourceId);
        int readByte;
        byte[] buff = new byte[8048];
        while ((readByte = lInputStream.read(buff)) != -1) {
            lOutputStream.write(buff, 0, readByte);
        }
        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }


    /* Call activities */

    private void onIncomingReceived() {
        Intent intent = new Intent(LinphoneService.this, CallIncomingActivity.class);
        // This flag is required to start an Activity from a Service context
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void onOutgoingStarted() {
        Intent intent = new Intent(LinphoneService.this, CallOutgoingActivity.class);
        // This flag is required to start an Activity from a Service context
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    private void onCallStarted() {
//        Intent intent = new Intent(LinphoneService.this, CallActivity.class);
//        // This flag is required to start an Activity from a Service context
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
}
