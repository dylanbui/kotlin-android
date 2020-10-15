package com.dylanbui.android_library.livedatabus

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dylanbui.android_library.DbMessageEvent

// 16/10/2020
// Khong su dung dc trong Conductor, phai cau hinh them Lifecycle moi dung dc
// Sau khi tim hieu EventBus van con dung dc trong thooi gian dai

/**
 *  A consumable event, make sure event can run only one time
 *  If you don't consume it, an event can be run multiple times
 */
data class DbConsumableEvent(var isConsumed: Boolean = false, var value: DbMessageEvent? = null) {
    /**
     *  run task & consume event after that
     */
    fun runAndConsume(runnable: () -> Unit) {
        if (!isConsumed) {
            runnable()
            isConsumed = true
        }
    }
}

/**
 * A custom LiveData which can unregister when there is no observer
 */
class DbEventLiveData( private val mSubject: String) : LiveData<DbConsumableEvent>() {

    fun update(obj: DbConsumableEvent) {
        postValue(obj)
    }

    override fun removeObserver(observer: Observer<in DbConsumableEvent>) {
        super.removeObserver(observer)
        if (!hasObservers()) {
            DbLiveDataBus.unregister(mSubject)
        }
    }
}