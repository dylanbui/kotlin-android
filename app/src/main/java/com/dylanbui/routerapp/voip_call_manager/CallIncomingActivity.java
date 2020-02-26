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

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.dylanbui.routerapp.R;

import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.Call.State;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.tools.Log;


import java.util.ArrayList;

public class CallIncomingActivity extends AppCompatActivity {
    private TextView mName, mNumber;
    private Call mCall;
    private CoreListenerStub mListener;
    private boolean mAlreadyAcceptedOrDeniedCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        Compatibility.setShowWhenLocked(this, true);
//        Compatibility.setTurnScreenOn(this, true);

        setContentView(R.layout.call_incoming);

        mName = findViewById(R.id.contact_name);
        mNumber = findViewById(R.id.contact_number);

        Button mAccept = findViewById(R.id.answer_button);
        Button mDecline = findViewById(R.id.decline_button);

        lookupCurrentCall();

        // do_not_use_sliders_to_answer_hangup_call_if_phone_unlocked
        mAccept.setOnClickListener((v -> {
            answer();
        }));
        mDecline.setOnClickListener((v -> {
            decline();
        }));

        mListener = new CoreListenerStub() {
                    @Override
                    public void onCallStateChanged(
                            Core core, Call call, State state, String message) {
                        if (state == State.End || state == State.Released) {
                            mCall = null;
                            finish();
                        }
                    }
                };
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestCallPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core core = LinphoneService.getCore();
        if (core != null) {
            core.addListener(mListener);
        }

        mAlreadyAcceptedOrDeniedCall = false;
        mCall = null;

        // Only one call ringing at a time is allowed
        lookupCurrentCall();
        if (mCall == null) {
            // The incoming call no longer exists.
            Log.d("Couldn't find incoming call");
            finish();
            return;
        }

        Address address = mCall.getRemoteAddress();

        // Tim kiem trong Contact de hien thi thong tin
        String displayName = LinphoneUtils.getAddressDisplayName(address);
        mName.setText(displayName);
        mNumber.setText(address.asStringUriOnly());


    }

    @Override
    protected void onPause() {
        Core core = LinphoneService.getCore();
        if (core != null) {
            core.removeListener(mListener);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mName = null;
        mNumber = null;
        mCall = null;
        mListener = null;

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LinphoneService.isReady()
                && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
                && mCall != null) {
            mCall.terminate();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void lookupCurrentCall() {
        if (LinphoneService.getCore() != null) {
            for (Call call : LinphoneService.getCore().getCalls()) {
                if (State.IncomingReceived == call.getState()
                        || State.IncomingEarlyMedia == call.getState()) {
                    mCall = call;
                    break;
                }
            }
        }
    }

    private void decline() {
        if (mAlreadyAcceptedOrDeniedCall) {
            return;
        }
        mAlreadyAcceptedOrDeniedCall = true;

        if (mCall != null) mCall.terminate();
        finish();
    }

    private void answer() {
        if (mAlreadyAcceptedOrDeniedCall) {
            return;
        }
        mAlreadyAcceptedOrDeniedCall = true;

        if (!LinphoneUtils.acceptCall(mCall)) {
            // the above method takes care of Samsung Galaxy S
            Toast.makeText(this, "An error occurred while accepting the call", Toast.LENGTH_LONG).show();
        }
    }

    private void checkAndRequestCallPermissions() {
        ArrayList<String> permissionsList = new ArrayList<>();

        int readPhoneState =
                getPackageManager()
                        .checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName());

        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            Log.i("[Permission] Asking for read phone state");
            permissionsList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            Log.i(
                    "[Permission] "
                            + permissions[i]
                            + " is "
                            + (grantResults[i] == PackageManager.PERMISSION_GRANTED
                                    ? "granted"
                                    : "denied"));
        }
    }
}
