package com.dylanbui.routerapp.voip_call_manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanbui.routerapp.R;

import org.linphone.core.AVPFMode;
import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;
import org.linphone.core.tools.Log;

/*

static let Domain = "propzyhcm160.ccall.vn"
static let Proxy = "sbcwrtchcm.ccall.vn:5060"
static let testAccount = "826"
static let testPassword = "!Wbs9ZuYZu"


static let testAccount = "1000"
static let testPassword = "B!.gXdFS%$"
static let Domain = "sipacc.ccall.vn"
static let Proxy = "sipacc.ccall.vn:5060"

 */





public class VoipConfigureAccountActivity extends AppCompatActivity {

//    private String testAccount = "1000";
//    private String testPassword = "B!.gXdFS%$";
//    private String testDomain = "sipacc.ccall.vn";
//    private String testProxy = "sipacc.ccall.vn:5060";

    private String testAccount = "826";
    private String testPassword = "!Wbs9ZuYZu";
    private String testDomain = "propzyhcm160.ccall.vn";
    private String testProxy = "sbcwrtchcm.ccall.vn:5060";

    private EditText mUsername, mPassword, mDomain, mProxy;
    private RadioGroup mTransport;
    private Button mConnect;

    private AccountCreator mAccountCreator;
    private CoreListenerStub mCoreListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voip_configure_account);

        // Account creator can help you create/config accounts, even not sip.linphone.org ones
        // As we only want to configure an existing account, no need for server URL to make requests
        // to know whether or not account exists, etc...
        mAccountCreator = LinphoneService.getCore().createAccountCreator(null);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mDomain = findViewById(R.id.domain);
        mProxy = findViewById(R.id.proxy);
        mTransport = findViewById(R.id.assistant_transports);

        mUsername.setText(testAccount);
        mPassword.setText(testPassword);
        mDomain.setText(testDomain);
        mProxy.setText(testProxy);


        mConnect = findViewById(R.id.configure);
        mConnect.setOnClickListener( v -> {
            configureAccount();
        });

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState state, String message) {
                if (state == RegistrationState.Ok) {
                    Toast.makeText(VoipConfigureAccountActivity.this, "RegistrationState.Ok", Toast.LENGTH_LONG).show();
                    finish();
                } else if (state == RegistrationState.Failed) {
                    Toast.makeText(VoipConfigureAccountActivity.this, "Failure: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinphoneService.getCore().addListener(mCoreListener);
    }

    @Override
    protected void onPause() {
        LinphoneService.getCore().removeListener(mCoreListener);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void configureAccount() {


        String strName = "826";
        String strPw = "!Wbs9ZuYZu";
        String strHost = "propzyhcm160.ccall.vn";
        String strPropxy = "sbcwrtchcm.ccall.vn:5060";

        // At least the 3 below values are required
        mAccountCreator.setUsername(strName);
        mAccountCreator.setPassword(strPw);
        mAccountCreator.setDomain(strHost);
        // mAccountCreator.setRoute("propzyhcm160.ccall.vn");


        mAccountCreator.setTransport(TransportType.Udp);

        // By default it will be UDP if not set, but TLS is strongly recommended
        switch (mTransport.getCheckedRadioButtonId()) {
            case R.id.transport_udp:
                mAccountCreator.setTransport(TransportType.Udp);
                break;
            case R.id.transport_tcp:
                mAccountCreator.setTransport(TransportType.Tcp);
                break;
            case R.id.transport_tls:
                mAccountCreator.setTransport(TransportType.Tls);
                break;
        }

        // This will automatically create the proxy config and auth info and add them to the Core
        ProxyConfig cfg = mAccountCreator.createProxyConfig();
        cfg.setAvpfMode(AVPFMode.Enabled);
        cfg.setAvpfRrInterval(0);
        cfg.enableQualityReporting(false);
        cfg.setQualityReportingCollector(null);
        cfg.setQualityReportingInterval(0);
        cfg.enableRegister(true);


        String identify = "sip:" + strName + "@" + strHost;
        // String proxy = "sip:" + host;

        // Make identify
//        Address identifyAdd = Factory.instance().createAddress(identify);
//        cfg.setIdentityAddress(identifyAdd);
//
        cfg.setRoute(strPropxy);
        cfg.setServerAddr(strPropxy);

        // Make sure the newly created one is the default
        LinphoneService.getCore().setDefaultProxyConfig(cfg);

        Log.i("getIdentityAddress = " + cfg.getIdentityAddress().asStringUriOnly());
        Log.i("getRoutes = " + cfg.getRoutes()[0]);
        Log.i("getServerAddr = " + cfg.getServerAddr());

//        createProxyConfig(String identity, String proxy, String route, boolean enableRegister)
//        String identify = "sip:" + name + "@" + host;
//        String proxy = "sip:" + host;
// createProxyConfig(String "sip:" + name + "@" + host,
//         "sip:" + host,
//         "sip:" + host,
//        boolean enableRegister)


        // ProxyConfig prxCfg = LinphoneService.getCore().createProxyConfig("", "", "", true);

    }
}
