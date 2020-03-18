package com.dylanbui.routerapp.typicode.splash_intro

import android.animation.Animator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.bluelinelabs.conductor.Controller
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.typicode.ApplicationRoute
import com.dylanbui.routerapp.utils.DbNavigation
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.networking.ApiServiceError
import com.dylanbui.routerapp.networking.CoroutinesNetwork

//internal class AnimatorListenerAdapter(
//    val onStart: ((Animator) -> Unit)? = null,
//    val onRepeat: ((Animator) -> Unit)? = null,
//    val onEnd: ((Animator) -> Unit)? = null,
//    val onCancel: ((Animator) -> Unit)? = null
//): Animator.AnimatorListener {
//
//    override fun onAnimationStart(animation: Animator) = onStart?.invoke(animation) ?: Unit
//    override fun onAnimationRepeat(animation: Animator) = onRepeat?.invoke(animation) ?: Unit
//    override fun onAnimationEnd(animation: Animator) = onEnd?.invoke(animation) ?: Unit
//    override fun onAnimationCancel(animation: Animator) = onCancel?.invoke(animation) ?: Unit
//}
// Example: animationView.addAnimatorListener(AnimatorListenerAdapter(onEnd = { _ ->  }))

class DistrictUnit {

    var districtId: Int? = null

    var countryId: Int? = null
    var regionId: Int? = null
    var cityId: Int? = null
    var orders: Int? = null

    var districtName: String? = null
    var districtShortName: String? = null
    var districtNameEn: String? = null
    var districtNameEnLower:String?=null
    var districtNameLower: String? = null
    var districtShortNameLower: String? = null

}




class SplashViewController: Controller(), Animator.AnimatorListener
{
    open var nav: DbNavigation? = null

    lateinit var animationView: LottieAnimationView

    var prensenter = SplashPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        var view: View = inflater.inflate(R.layout.controller_splash, container, false)
        onViewBound(view)
        return view
    }

    private fun setTitle(): String = "Title first"

    private var updateDone: Boolean = false

    private fun onViewBound(view: View)
    {
        animationView = view.findViewById(R.id.lottieAnimationView)
        animationView.addAnimatorListener(this)


//        btnFirst = view.findViewById(R.id.btnFirst)
//        btnFirst.setOnClickListener { _ ->
//            router.pushController(
//                RouterTransaction.with(SecondViewController())
//                    .pushChangeHandler(HorizontalChangeHandler())
//                    .popChangeHandler(HorizontalChangeHandler()))
//
////            router.pushController(RouterTransaction.with(SecondViewController())
////                .pushChangeHandler(FadeChangeHandler())
////                .popChangeHandler(FadeChangeHandler()))
//
//        }
//
//        btnNext = view.findViewById(R.id.btnNextControl)
//        btnNext.setOnClickListener { _ ->
//            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
//            Toast.makeText(activity, "show ra loi", Toast.LENGTH_LONG).show()
//        }


        CoroutinesNetwork.doRequest("districts/1", CoroutinesNetwork.Method.GET) {
                arrDistrict: List<DistrictUnit>?, error: ApiServiceError? ->

            Log.d("TAG", " 1 ---- ${Thread.currentThread()} has run.")

            if (error != null) {
                dLog("Loi cai me gi" + error.errorCode + " -- " + error.errorMessage)
                return@doRequest
            }

            arrDistrict?.let {

                for (district: DistrictUnit in it) {
                    dLog("  --- dictrict.districtName = " + district.districtName)
                }

            }


        }











    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

//        var mainActivity = activity as? MainActivity
//        mainActivity?.let {
//            it.setToolBarTitle(setTitle())
//            it.enableUpArrow(router.backstackSize > 1)
//        }

        // -- Begin play LottieAnimationView --
        animationView.playAnimation()

//        dLog("Chuyen vao thread")
//        Utils.delayFunc(1500) {
//            dLog("-- jobUpdate --- ${Thread.currentThread()} has run.")
//        }

//        prensenter.executeTwoTasksParallel {
//            Log.d("TAG", "Chay dong bo 1 luc 2 Task")
//        }

//        prensenter.executeTwoTasksSequentially {
//            Log.d("TAG", "Chay tuan tu tung task trong 2 Task")
//        }

        prensenter.getAllDataForApp {
            Log.d("TAG", "Da chay choi xong, kiem chung thoi")
            this.updateDone = true
        }
    }

    override fun onAnimationStart(animation: Animator?) {
        Log.d("TAG", "onAnimationStart")
    }

    override fun onAnimationRepeat(animation: Animator?) {
        Log.d("TAG", "onAnimationRepeat")
    }

    override fun onAnimationEnd(animation: Animator?) {
        Log.d("TAG", "onAnimationEnd")
        // activity?.toast("onAnimationEnd")

        nav?.navigate(ApplicationRoute.SplashPageComplete())
    }

    override fun onAnimationCancel(animation: Animator?) {
        Log.d("TAG", "onAnimationCancel")

    }


}