package com.dylanbui.routerapp.typicode.numeric_keyboard

import android.util.Log
import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.bgDispatcher
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface NumericKeyboardViewAction : BaseMvpView {

    fun updateDisplay(content: String)

}


class NumericKeyboardPresenter : BaseMvpPresenter<NumericKeyboardViewAction>(), CoroutineScope {

    // This launch uses the coroutineContext defined
    // by the coroutine presenter.
    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun testCoroutines(completeTest: ()->Unit) = launch(bgDispatcher) {

        Log.d("TAG", " Start --- ${Thread.currentThread()} has run.")

        val test = TestCallService()

        test.doThreadOne {
            Log.d("TAG", " Callback completeTest --- ${Thread.currentThread()} has run.")

            completeTest()
        }

    }


    fun testCoroutinesType2(completeTest: ()->Unit) = launch(bgDispatcher) {

        val test = TestCallService()

        test.doThreadTwo("Thread 1", 2000) {
            ifViewAttached { it.updateDisplay("Thread 1") }
        }

        test.doThreadTwo("Thread 2", 500) {
            ifViewAttached { it.updateDisplay("Thread 2") }
        }

        test.doThreadTwo("Thread 3", 1500) {
            ifViewAttached { it.updateDisplay("Thread 3") }
        }

        test.doThreadTwo("Thread 4", 1000) {
            ifViewAttached { it.updateDisplay("Thread 4") }
        }

        completeTest()
    }


    fun testCoroutinesType3(completeTest: ()->Unit)  {

        val test = TestCallService()

        test.doThreadTwo("Thread 1", 2000) {
            ifViewAttached { it.updateDisplay("Thread 1") }
        }

        test.doThreadTwo("Thread 2", 500) {
            ifViewAttached { it.updateDisplay("Thread 2") }
        }

        test.doThreadTwo("Thread 3", 1500) {
            ifViewAttached { it.updateDisplay("Thread 3") }
        }

        test.doThreadTwo("Thread 4", 1000) {
            ifViewAttached { it.updateDisplay("Thread 4") }
        }

        completeTest()
    }

    fun testCoroutinesType4(completeTest: ()->Unit) = launch {

        val test = TestCallService()

        val str: String = suspendCoroutine {
            test.doThreadTwo("Thread 1", 3000) {
                Log.d("TAG", " [suspendCoroutine] ---- ${Thread.currentThread()}.")
                it.resume("Thread 1")
            }
        }

        Log.d("TAG", " [$str] ---- ${Thread.currentThread()}.")

        completeTest()
    }
}