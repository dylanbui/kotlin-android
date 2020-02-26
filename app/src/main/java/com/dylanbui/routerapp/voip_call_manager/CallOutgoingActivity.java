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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Chronometer;
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
import org.linphone.core.Reason;
import org.linphone.core.tools.Log;

import java.util.ArrayList;

public class CallOutgoingActivity extends AppCompatActivity implements OnClickListener {
    private TextView mName, mNumber;
    private ImageView mMicro;
    private ImageView mSpeaker;
    private Call mCall;
    private CoreListenerStub mListener;
    private boolean mIsMicMuted, mIsSpeakerEnabled;

    private Chronometer mCallTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.call_outgoing);

        mName = findViewById(R.id.contact_name);
        mNumber = findViewById(R.id.contact_number);
        mCallTimer = findViewById(R.id.current_call_timer);

        mIsMicMuted = false;
        mIsSpeakerEnabled = false;

        mMicro = findViewById(R.id.micro);
        mMicro.setOnClickListener(this);
        mSpeaker = findViewById(R.id.speaker);
        mSpeaker.setOnClickListener(this);

        ImageView hangUp = findViewById(R.id.outgoing_hang_up);
        hangUp.setOnClickListener(this);

        mListener = new CoreListenerStub() {
                    @Override
                    public void onCallStateChanged(
                            Core core, Call call, Call.State state, String message) {
                        if (state == State.Error) {
                            // Convert Core message for internalization
                            if (call.getErrorInfo().getReason() == Reason.Declined) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_call_declined),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (call.getErrorInfo().getReason() == Reason.NotFound) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_user_not_found),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (call.getErrorInfo().getReason() == Reason.NotAcceptable) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_incompatible_media),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (call.getErrorInfo().getReason() == Reason.Busy) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_user_busy),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            } else if (message != null) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_unknown) + " - " + message,
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else if (state == State.End) {
                            // Convert Core message for internalization
                            if (call.getErrorInfo().getReason() == Reason.Declined) {
                                Toast.makeText(
                                                CallOutgoingActivity.this,
                                                getString(R.string.error_call_declined),
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else if (state == State.Connected) {
                            // This is done by the LinphoneContext listener now
                            // startActivity(new Intent(CallOutgoingActivity.this,
                            // CallActivity.class));
                        } else if (state == Call.State.StreamsRunning) {
                            // Da ket noi thanh cong cuoc goi, tinh gio goi
                            setCurrentCallContactInformation();

                        }

                        if (state == State.End || state == State.Released) {
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

        mCall = null;

        // Only one call ringing at a time is allowed
        if (LinphoneService.getCore() != null) {
            for (Call call : LinphoneService.getCore().getCalls()) {
                State cstate = call.getState();
                if (State.OutgoingInit == cstate
                        || State.OutgoingProgress == cstate
                        || State.OutgoingRinging == cstate
                        || State.OutgoingEarlyMedia == cstate) {
                    mCall = call;
                    break;
                }
            }
        }
        if (mCall == null) {
            Log.e("[Call Outgoing Activity] Couldn't find outgoing call");
            finish();
            return;
        }

        Address address = mCall.getRemoteAddress();
        // Tim kiem ve show contact so can goi
        // Tim trong danh ba cua minh hay truyen vao activity
        String displayName = LinphoneUtils.getAddressDisplayName(address);
        mName.setText(displayName);
        mNumber.setText(LinphoneUtils.getDisplayableAddress(address));

        boolean recordAudioPermissionGranted = checkPermission(Manifest.permission.RECORD_AUDIO);
        if (!recordAudioPermissionGranted) {
            Log.w("[Call Outgoing Activity] RECORD_AUDIO permission denied, muting microphone");
            core.enableMic(false);
            mMicro.setSelected(true);
        }
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
        mMicro = null;
        mSpeaker = null;
        mCall = null;
        mListener = null;

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.micro) {
            mIsMicMuted = !mIsMicMuted;
            mMicro.setSelected(mIsMicMuted);
            LinphoneService.getCore().enableMic(!mIsMicMuted);
        }
        if (id == R.id.speaker) {
            mIsSpeakerEnabled = !mIsSpeakerEnabled;
            mSpeaker.setSelected(mIsSpeakerEnabled);
            if (mIsSpeakerEnabled) {
                // LinphoneService.getAudioManager().routeAudioToSpeaker();
            } else {
                // LinphoneService.getAudioManager().routeAudioToEarPiece();
            }
        }
        if (id == R.id.outgoing_hang_up) {
            decline();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (LinphoneService.isReady()
                && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
            mCall.terminate();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decline() {
        mCall.terminate();
        finish();
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

    private boolean checkPermission(String permission) {
        int granted = getPackageManager().checkPermission(permission, getPackageName());
        Log.i(
                "[Permission] "
                        + permission
                        + " permission is "
                        + (granted == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    private void updateCurrentCallTimer() {

        if (mCall == null) return;

        mCallTimer.setBase(SystemClock.elapsedRealtime() - 1000 * mCall.getDuration());
        mCallTimer.start();
    }

    private void setCurrentCallContactInformation() {
        updateCurrentCallTimer();

//        Call call = mCore.getCurrentCall();
//        if (call == null) return;
//
//        String displayName = LinphoneUtils.getAddressDisplayName(call.getRemoteAddress());
//        mContactName.setText(displayName);

//        LinphoneContact contact =
//                ContactsManager.getInstance().findContactFromAddress(call.getRemoteAddress());
//        if (contact != null) {
//            ContactAvatar.displayAvatar(contact, mContactAvatar, true);
//            mContactName.setText(contact.getFullName());
//        } else {
//            String displayName = LinphoneUtils.getAddressDisplayName(call.getRemoteAddress());
//            ContactAvatar.displayAvatar(displayName, mContactAvatar, true);
//            mContactName.setText(displayName);
//        }
    }
}
