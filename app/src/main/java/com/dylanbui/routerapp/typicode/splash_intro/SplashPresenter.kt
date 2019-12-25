package com.dylanbui.routerapp.typicode.splash_intro

import android.os.Handler
import android.util.Log
import com.dylanbui.routerapp.BaseMvpPresenter
import com.dylanbui.routerapp.BaseMvpView
import com.dylanbui.routerapp.bgDispatcher
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import com.dylanbui.routerapp.typicode.TyPhoto
import com.dylanbui.routerapp.typicode.TyPhotoApi
import com.dylanbui.routerapp.typicode.TyPost
import com.dylanbui.routerapp.typicode.TyPostApi
import com.dylanbui.routerapp.uiDispatcher
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext


interface SplashActionView : BaseMvpView
{
    fun allLoadingComplete()
    fun showPostError(error : AppNetworkServiceError)
}


class SplashPresenter : BaseMvpPresenter<SplashActionView>(), CoroutineScope
{
    // This launch uses the coroutineContext defined
    // by the coroutine presenter.
    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    // launch + async + async (execute two tasks parallel)
    // Thuc thi song song 2 nhiem vu
    fun executeTwoTasksParallel(complete: () -> Unit) {

        var jobUpdate = launch {
            delay(500)
            Log.d("TAG", " 0--- ${Thread.currentThread()} has run.")

            var result_1: String = "none_1"
            val task1 = async(bgDispatcher) {
                delay(500)
                Log.d("TAG", " ===> Start task 1")
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
                    result_1 = "result_1 : 00000 --- Da load xong Post : ${list.count()}"
                }


                // Variable cuoi cung luu tru la bien tra ve cua task1.await()
                result_1
            }

            Log.d("TAG", " 1---- ${Thread.currentThread()} has run.")

            var result_2: String = "none_2"
            val task2 = async(bgDispatcher) {
                delay(250)
                Log.d("TAG", " ===> Start task 2")
                TyPhotoApi.getPhotoSyn() { list, appNetworkServiceError ->
                    appNetworkServiceError?.let {
                        // activity?.toast(it.errorMessage)
                        // have error
                        return@getPhotoSyn
                    }

                    // Handler().postDelayed({ },  1000)
                    Thread.sleep(500)

                    Log.d("TAG", "11111 -- Da load xong Photo: ${list.count()}")
                    result_2 = "result_2 : 11111 -- Da load xong Photo: ${list.count()}"
                }
                // Variable cuoi cung luu tru la bien tra ve cua task2.await()
                result_2
            }

            val result = task1.await() + task2.await()

            Log.d("TAG", "Da load xong: getAllUpdateDataForApp")

            withContext(uiDispatcher) {
                // Sau khi chay xong update tren main thread
                Log.d("TAG", "-- Chay update main thread: getAllUpdateDataForApp")
                Log.d("TAG", "-- result la gi : ${result}")
                Log.d("TAG", "-- jobUpdate --- ${Thread.currentThread()} has run.")
            }
        }

        jobUpdate.invokeOnCompletion {

            Log.d("TAG", " jobUpdate --- ${Thread.currentThread()} has run.")
        }

        // Log.d("TAG", "Da load PHOTO")
        complete()

    }

    // launch + withContext (execute two tasks sequentially)
    // Thuc thi tuan tu 2 task
    fun executeTwoTasksSequentially(complete: () -> Unit) {

        var jobUpdate = launch {
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

            Log.d("TAG", "Da load xong: getAllUpdateDataForApp")

            withContext(uiDispatcher) {
                // Sau khi chay xong update tren main thread
                Log.d("TAG", "-- Chay update main thread: getAllUpdateDataForApp")
                Log.d("TAG", "-- jobUpdate --- ${Thread.currentThread()} has run.")

            }
        }

        jobUpdate.invokeOnCompletion {

            Log.d("TAG", " jobUpdate --- ${Thread.currentThread()} has run.")
        }

        // Log.d("TAG", "Da load PHOTO")
        complete()

    }

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


    override fun detachView()
    {
        // By default, every coroutine initiated in this context
        // will use the job and dispatcher specified by the
        // coroutineContext.
        // The coroutines are scoped to their execution environment.
        job.cancel()

        super.detachView()
    }
}