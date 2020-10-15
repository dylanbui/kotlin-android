package com.dylanbui.android_library.mvvm_structure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dylanbui.android_library.DbError

// https://androidcoban.com/example-mvvm-viewmodel-livedata-retrofit2-kotlin.html

// Cach su dung : setupViewModel()
// Cach su dung : setObserveLive()

open class DbEventViewModel<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

open class DbBaseViewModel : ViewModel() {
    val eventLoading = MutableLiveData<DbEventViewModel<Boolean>>()
    val eventError = MutableLiveData<DbEventViewModel<DbError>>()
    val eventFailure = MutableLiveData<DbEventViewModel<Throwable>>()

    fun showLoading(value: Boolean) {
        eventLoading.value = DbEventViewModel(value)
    }

    fun showError(error: DbError) {
        eventError.value = DbEventViewModel(error)
    }

    fun showFailure(throwable: Throwable) {
        eventFailure.value = DbEventViewModel(throwable)
    }
}

/*

fun setObserveLive(viewModel: DbBaseViewModel) {
        viewModel.eventLoading.observe(this, Observer {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    if (it.peekContent()) {
                        showLoadingDialog()
                    } else {
                        hideLoadingDialog()
                    }
                }
            }
        })
        viewModel.eventError.observe(this, Observer {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    showRequestError(it.peekContent())
                }
            }
        })
        viewModel.eventFailure.observe(this, Observer {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    showQuestFailure(it.peekContent())
                }
            }
        })
    }

* */