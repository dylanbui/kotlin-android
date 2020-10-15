package com.dylanbui.android_library.livedatabus

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * Singleton object to manage bus events.
 */
object DbLiveDataBus {

    private val subjectMap = HashMap<String, DbEventLiveData>()

    /**
     * Get the live data or create it if it's not already in memory.
     */
    @NonNull
    private fun getLiveData(subjectCode: String): DbEventLiveData {
        var liveData: DbEventLiveData? = subjectMap[subjectCode]
        if (liveData == null) {
            liveData = DbEventLiveData(subjectCode)
            subjectMap[subjectCode] = liveData
        }

        return liveData
    }

    /**
     * Subscribe to the specified subject and listen for updates on that subject.
     */
    fun subscribe(subject: String, @NonNull lifecycle: LifecycleOwner, @NonNull action: Observer<DbConsumableEvent>) {
        try {
            // avoid register same instance
            getLiveData(subject).observe(lifecycle, action)
        } catch (throwable: IllegalArgumentException) {
            throwable.printStackTrace()
        }
    }

    /**
     * Removes this subject when it has no observers.
     */
    fun unregister(subject: String) {
        subjectMap.remove(subject)
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    fun publish(subject: String, message: DbConsumableEvent = DbConsumableEvent()) {
        getLiveData(subject).update(message)
    }
}