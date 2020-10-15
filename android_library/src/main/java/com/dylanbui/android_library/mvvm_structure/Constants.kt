package vn.propzy.android_core_kit.mvvm_structure

import com.dylanbui.android_library.DbError

data class DbResource<out T>(val status: Status, val data: T?, val error: DbError?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): DbResource<T> {
            return DbResource(Status.SUCCESS, data, null)
        }

        fun <T> error(error: DbError, data: T? = null): DbResource<T> {
            return DbResource(Status.ERROR, data, error)
        }

        fun <T> loading(data: T? = null): DbResource<T> {
            return DbResource(Status.LOADING, data, null)
        }
    }
}