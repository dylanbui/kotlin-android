package com.dylanbui.routerapp.typicode.splash_intro

import android.animation.Animator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.demofragment.SecondViewController
import com.dylanbui.routerapp.utils.DbNavigation
import com.dylanbui.routerapp.utils.toast

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


        prensenter.getAllDataForApp {
            Log.d("TAG", "Da chay choi xong, kiem chung thoi")
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
        activity?.toast("onAnimationEnd")
    }

    override fun onAnimationCancel(animation: Animator?) {
        Log.d("TAG", "onAnimationCancel")

    }


}