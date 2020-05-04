package com.dylanbui.routerapp.typicode.numeric_keyboard

import android.util.Log
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.routerapp.bgDispatcher
import com.dylanbui.routerapp.uiDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestCallService {


    fun doThreadOne(complete: ()->Unit) = GlobalScope.launch(bgDispatcher) {
        launch {

            Log.d("TAG", " Waiting delay 5000 --- ${Thread.currentThread()} has run.")

            delay(5000)

            withContext(uiDispatcher) {
                Log.d("TAG", " After delay 5000 --- ${Thread.currentThread()} has run.")
                complete()
            }
        }
    }


    fun doThreadTwo(content: String, delayTme: Long, complete: ()->Unit) = GlobalScope.launch(bgDispatcher) {
        launch {

            Log.d("TAG", "[$content] Waiting $delayTme --- ${Thread.currentThread()}.")

            delay(delayTme)

            withContext(uiDispatcher) {
                Log.d("TAG", "[$content] After $delayTme --- ${Thread.currentThread()}.")
                complete()
            }
        }
    }


}