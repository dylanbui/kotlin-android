package com.dylanbui.routerapp.typicode

import android.content.Context
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.dylanbui.routerapp.StartApplication
import com.dylanbui.routerapp.demofragment.FirstViewController
import com.dylanbui.routerapp.typicode.bottom_sheet.DemoBottomSheetViewController
import com.dylanbui.routerapp.typicode.format_edittext.FormatEditTextController
import com.dylanbui.routerapp.typicode.google_map.GoogleMapViewController
import com.dylanbui.routerapp.typicode.google_map.PlaceAutoCompleteViewController
import com.dylanbui.routerapp.typicode.multi_upload_progress.UploadProgressController
import com.dylanbui.routerapp.typicode.numeric_keyboard.NumericKeyboardController
import com.dylanbui.routerapp.typicode.post.PostDetailViewController
import com.dylanbui.routerapp.typicode.post.PostListViewController
import com.dylanbui.routerapp.typicode.splash_intro.SplashViewController
import com.dylanbui.routerapp.typicode.tabbar.CustomTabbarController
import com.dylanbui.routerapp.typicode.tabbar.MainTabarController
import com.dylanbui.routerapp.typicode.user.login.LoginViewController
import com.dylanbui.routerapp.typicode.view_photo.ViewPhotoViewAction
import com.dylanbui.routerapp.typicode.view_photo.ViewPhotoViewController
import com.dylanbui.routerapp.ui_controller.HitSpinnerController
import com.dylanbui.routerapp.utils.*


//enum class ApplicationRoute : DbEnumRoute {
//    //DefaultError inherited plus
//    GotoPostDetail,
//    GotoPhUserDetail,
//    GotoAnyWhere
//}

// Define enum nhu la 1 data class
sealed class ApplicationRoute: DbEnumRoute {
    class SplashPageComplete() : ApplicationRoute()
    class GotoPostDetail(val post: TyPost) : ApplicationRoute()
    class GotoPhUserDetail(val url: String, val caption: String) : ApplicationRoute()
    class GotoAnyWhere() : ApplicationRoute()
}

class AppCoordinator(router: Router): BaseDbCoordinator(router), DbNavigation {

    // private var router = router

    override fun start() {
        // var vcl = PostListViewController()

//        var vcl = SplashViewController()
        // var vcl = PlaceAutoCompleteViewController()
        // var vcl = ViewPhotoViewController()
//         var vcl = GoogleMapViewController()
//        var vcl = MainTabarController()
        // var vcl = CustomTabbarController()
        // var vcl = DemoBottomSheetViewController()
        //var vcl = LoginViewController()
        //var vcl = UploadProgressController()
        // var vcl = FormatEditTextController()

        //var vcl = NumericKeyboardController()
        //router.setRoot(RouterTransaction.with(vcl))

         var vcl = FirstViewController()
        // var vcl = HitSpinnerController()

        router.setRoot(RouterTransaction.with(vcl)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler()))
    }

    private fun splashPageComplete() {

        var user = StartApplication.currentUser
        if (user.isLogin()) {
            // Da login roi, chuyen qua man hinh ds
        } else {
            // Chua login, chuyen qua man hinh login
            router.defaultSetRootController(LoginViewController())
        }
    }

    override fun navigate(toRoute: DbEnumRoute, context: Context?, parameters: Any?) {

//        var route = (toRoute as? ControllerRoute).guard {
//            Log.d("ERROR", "Eo cast duoc")
//            return
//        }

        when (toRoute) {

            is ApplicationRoute.SplashPageComplete -> {
                this.splashPageComplete()
            }

            is ApplicationRoute.GotoPostDetail -> {
                var vcl = PostDetailViewController()
                vcl.tyPost = toRoute.post //(parameters as TyPost)

                router.defaultPushController(vcl)

//                router.pushController(
//                    RouterTransaction.with(vcl)
//                        .pushChangeHandler(HorizontalChangeHandler())
//                        .popChangeHandler(HorizontalChangeHandler()))
            }

            is ApplicationRoute.GotoPhUserDetail -> {


            }

            is ApplicationRoute.GotoAnyWhere -> {

            }

            else -> {

            }
        }

    }
}