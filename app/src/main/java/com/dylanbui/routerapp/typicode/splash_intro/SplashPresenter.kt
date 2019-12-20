package com.dylanbui.routerapp.typicode.splash_intro

import android.os.Handler
import android.util.Log
import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.typicode.TyPhoto
import com.dylanbui.routerapp.typicode.TyPhotoApi
import com.dylanbui.routerapp.typicode.TyPost
import com.dylanbui.routerapp.typicode.TyPostApi
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation


interface SplashActionView : BaseMvpView
{
    fun allLoadingComplete()
    fun showPostError(error : AppNetworkServiceError)
}


class SplashPresenter : BaseMvpPresenter<SplashActionView>()
{
//    fun backgroundTask(param: Int, callback: Continuation<Int>): Int {
//        // long running operation
//        return 3
//    }

    fun getAllDataForApp(complete: () -> Unit) {

        Log.d("TAG", "==> getAllDataForApp Start ")

        var error = "Gia tri ban dau"

//        GlobalScope.launch {
//            delay(1000)
//            Log.d("TAG", " 00--- ${Thread.currentThread()} has run.")
//
//            delay(1000)
//            Log.d("TAG", " 11--- ${Thread.currentThread()} has run.")
//
//
//            TyPostApi.getPostSyn { list, appNetworkServiceError ->
//                appNetworkServiceError?.let {
//                    // activity?.toast(it.errorMessage)
//                    // have error
//                    return@getPostSyn
//                }
//
//                error = "Gia tri moi thay the tu : return@getPostSyn"
//
//                return@getPostSyn
//
//                Log.d("TAG", " 22---- ${Thread.currentThread()} has run.")
//
//                // Handler().postDelayed({ },  1000)
//                 Thread.sleep(10000)
//
//                // Cai nay se duoc goi tuan tu
//                Log.d("TAG", " 22 --- Da load xong Post : ${list.count()}")
//
//            }
//
//        }

        Log.d("TAG", " Master --- ${Thread.currentThread()} has run.")

        var job = GlobalScope.launch {
            delay(500)
            Log.d("TAG", " 0--- ${Thread.currentThread()} has run.")

            TyPostApi.getPostSyn { list, appNetworkServiceError ->
                appNetworkServiceError?.let {
                    // activity?.toast(it.errorMessage)
                    // have error
                    return@getPostSyn
                }

                // Log.d("TAG", " 2---- ${Thread.currentThread()} has run.")

                // Handler().postDelayed({ },  1000)
                Thread.sleep(500)

                // Cai nay se duoc goi tuan tu
                Log.d("TAG", "00000 --- Da load xong Post : ${list.count()}")

            }

             Log.d("TAG", " 1---- ${Thread.currentThread()} has run.")

            TyPhotoApi.getPhotoSyn() { list, appNetworkServiceError ->
                appNetworkServiceError?.let {
                    // activity?.toast(it.errorMessage)
                    // have error
                    return@getPhotoSyn
                }

                // Handler().postDelayed({ },  1000)
                Thread.sleep(500)

                Log.d("TAG", "11111 -- Da load xong Photo: ${list.count()}")
            }

            Log.d("TAG", "Da load xong GlobalScope.launch")
        }

        job.invokeOnCompletion {

            Log.d("TAG", " job.invokeOnCompletion --- ${Thread.currentThread()} has run.")
        }

        Log.d("TAG", "Da load PHOTO")

        TyPhotoApi.getPhoto { list, appNetworkServiceError ->
            appNetworkServiceError?.let {
                // activity?.toast(it.errorMessage)
                // have error
                return@getPhoto
            }

            // Handler().postDelayed({ },  1000)
            // Thread.sleep(500)

            Log.d("TAG", "Master getPhoto --- ${Thread.currentThread()} has run.")
            Log.d("TAG", "Da load xong Photo: ${list.count()}")
        }

        Log.d("TAG", "Error ERR: $error")

        complete()
    }

    fun getPostList(page: Int = 0) {
        TyPostApi.getPost { list, appNetworkServiceError ->
            appNetworkServiceError?.let {
                // activity?.toast(it.errorMessage)
                // have error
                return@getPost
            }
            // Reload data
            // ifViewAttached { it.updatePostList(page, list) }
        }
    }

    fun onPostRowClick(position: Int, post: TyPost) {
        // -- Code xu ly khi click vao row --
        // ifViewAttached { view -> view.onPostRowClick(position, post) }
    }


}