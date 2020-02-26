package com.dylanbui.routerapp.voip_call_manager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanbui.routerapp.R;

import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListener;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.tools.Log;

public class VoipCallActivity extends AppCompatActivity {

    // We use 2 TextureView, one for remote video and one for local camera preview

//    private RelativeLayout mButtons,
//            mActiveCalls,
//            mContactAvatar,
//            mActiveCallHeader,
//            mConferenceHeader;

    private ImageView mMicro, mSpeaker;
    //    private ImageView mPause, mSwitchCamera, mRecordingInProgress;
//    private ImageView mExtrasButtons, mAddCall, mTransferCall, mRecordCall, mConference;
//    private ImageView mAudioRoute, mRouteEarpiece, mRouteSpeaker, mRouteBluetooth;
    private TextView mContactName;
    //    private ProgressBar mVideoInviteInProgress;
    private Chronometer mCallTimer;


    private Core mCore;
    private CoreListener mListener;
//    private AndroidAudioManager mAudioManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voip_call);

        mContactName = findViewById(R.id.current_contact_name);
        mCallTimer = findViewById(R.id.current_call_timer);

        mMicro = findViewById(R.id.micro);
        mMicro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMic();
            }
        });

        mSpeaker = findViewById(R.id.speaker);
        mSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSpeaker();
            }
        });

        ImageView hangUp = findViewById(R.id.hang_up);
        hangUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.hangUp();
            }
        });


        // Core core = LinphoneService.getCore();
        // We need to tell the core in which to display what
        // Listen for call state changes
        mListener = new CoreListenerStub() {

            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                if (state == Call.State.End || state == Call.State.Released) {
                    if (core.getCallsNb() == 0) {
                        finish();
                    }
                } else if (state == Call.State.PausedByRemote) {
                    if (core.getCurrentCall() != null) {
                        finish();
                    }
                } else if (state == Call.State.Pausing || state == Call.State.Paused) {
                    if (core.getCurrentCall() != null) {

                    }
                } else if (state == Call.State.StreamsRunning) {

                    setCurrentCallContactInformation();

                }

                updateButtons();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This also must be done here in case of an outgoing call accepted
        // before user granted or denied permissions
        // or if an incoming call was answer from the notification

        mCore = LinphoneService.getCore();
        if (mCore != null) {
            mCore.addListener(mListener);
        }

        // LinphoneService.getCore().addListener(mCoreListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateButtons();

        if (mCore.getCallsNb() == 0) {
            Log.w("[Call Activity] Resuming but no call found...");
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Core core = LinphoneService.getCore();
        if (core != null) {
            core.removeListener(mListener);
            core.setNativeVideoWindowId(null);
            core.setNativePreviewWindowId(null);
        }

        mCallTimer.stop();
        mCallTimer = null;

        mListener = null;

        mMicro = null;
        mSpeaker = null;

        mContactName = null;

        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (mAudioManager.onKeyVolumeAdjust(keyCode)) return true;
//        return super.onKeyDown(keyCode, event);
//    }


    private void updateButtons() {
        // Call call = mCore.getCurrentCall();
        mMicro.setSelected(!mCore.micEnabled());
        // mSpeaker.setSelected(mAudioManager.isAudioRoutedToSpeaker());

//        updateAudioRouteButtons();
//
//        boolean isBluetoothAvailable = mAudioManager.isBluetoothHeadsetConnected();
//        mSpeaker.setVisibility(isBluetoothAvailable ? View.GONE : View.VISIBLE);
//        mAudioRoute.setVisibility(isBluetoothAvailable ? View.VISIBLE : View.GONE);
//        if (!isBluetoothAvailable) {
//            mRouteBluetooth.setVisibility(View.GONE);
//            mRouteSpeaker.setVisibility(View.GONE);
//            mRouteEarpiece.setVisibility(View.GONE);
//        }

//        mVideo.setEnabled(
//                LinphonePreferences.instance().isVideoEnabled()
//                        && call != null
//                        && !call.mediaInProgress());
//        mVideo.setSelected(call != null && call.getCurrentParams().videoEnabled());
//        mSwitchCamera.setVisibility(
//                call != null && call.getCurrentParams().videoEnabled()
//                        ? View.VISIBLE
//                        : View.INVISIBLE);
//
//        mPause.setEnabled(call != null && !call.mediaInProgress());

//        mRecordCall.setSelected(call != null && call.isRecording());
//        mRecordingInProgress.setVisibility(
//                call != null && call.isRecording() ? View.VISIBLE : View.GONE);
//
//        mConference.setEnabled(
//                mCore.getCallsNb() > 1
//                        && mCore.getCallsNb() > mCore.getConferenceSize()
//                        && !mCore.soundResourcesLocked());
    }

    private void toggleMic() {
        mCore.enableMic(!mCore.micEnabled());
        mMicro.setSelected(!mCore.micEnabled());
    }

    private void toggleSpeaker() {
        // co lop AndroidAudioManager trong Linphone su dung AudioManager de quan ly loa, loa ngoai, bluetooth
//        if (mAudioManager.isAudioRoutedToSpeaker()) {
//            mAudioManager.routeAudioToEarPiece();
//        } else {
//            mAudioManager.routeAudioToSpeaker();
//        }
//        mSpeaker.setSelected(mAudioManager.isAudioRoutedToSpeaker());
    }

    private void updateCurrentCallTimer() {
        Call call = mCore.getCurrentCall();
        if (call == null) return;

        mCallTimer.setBase(SystemClock.elapsedRealtime() - 1000 * call.getDuration());
        mCallTimer.start();
    }

    private void setCurrentCallContactInformation() {
        updateCurrentCallTimer();

        Call call = mCore.getCurrentCall();
        if (call == null) return;

        String displayName = LinphoneUtils.getAddressDisplayName(call.getRemoteAddress());
        mContactName.setText(displayName);

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
