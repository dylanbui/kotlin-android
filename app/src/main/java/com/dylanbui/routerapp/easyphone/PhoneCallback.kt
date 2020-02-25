package com.dylanbui.routerapp.easyphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.linphone.core.Call

abstract class PhoneCallback {
    /**
     * 来电状态
     * @param linphoneCall
     */
    fun incomingCall(linphoneCall: Call) {}

    /**
     * 呼叫初始化
     */
    fun outgoingInit() {}

    /**
     * 电话接通
     */
    fun callConnected() {}

    /**
     * 电话挂断
     */
    fun callEnd() {}

    /**
     * 释放通话
     */
    fun callReleased() {}

    /**
     * 连接失败
     */
    fun error() {}
}

abstract class RegistrationCallback {
    fun registrationNone() {}
    fun registrationProgress() {}
    fun registrationOk() {}
    fun registrationCleared() {}
    fun registrationFailed() {}
}

class KeepAliveHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        if (LinphoneManager.getLcIfManagerNotDestroyOrNull() != null) {
//            LinphoneManager.getLc().refreshRegisters()
//            //            SPUtils.save(context, "keepAlive", true);
//            try {
//                Thread.sleep(2000)
//            } catch (e: InterruptedException) {
//                Log.e(TAG, "Cannot sleep for 2s")
//            }
//        }
    }

    companion object {
        private const val TAG = "KeepAliveHandler"
    }
}

class PhoneBean {
    var displayName: String? = null
    var userName: String? = null
    var host: String? = null
    var password: String? = null

}