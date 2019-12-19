package com.dylanbui.routerapp.typicode

import android.content.Context
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.dylanbui.routerapp.typicode.post.PostDetailViewController
import com.dylanbui.routerapp.typicode.post.PostListViewController
import com.dylanbui.routerapp.utils.BaseDbCoordinator
import com.dylanbui.routerapp.utils.DbEnumRoute
import com.dylanbui.routerapp.utils.DbNavigation
import com.dylanbui.routerapp.utils.defaultPushController


enum class ApplicationRoute : DbEnumRoute {
    //DefaultError inherited plus
    GotoPostDetail,
    GotoPhUserDetail,
    GotoAnyWhere
}

class AppCoordinator(router: Router): BaseDbCoordinator(router), DbNavigation {

    // private var router = router

    override fun start() {
        var vcl = PostListViewController()
        vcl.nav = this
        router.setRoot(RouterTransaction.with(vcl))

//        router.setRoot(RouterTransaction.with(FirstViewController())
//            .pushChangeHandler(FadeChangeHandler())
//            .popChangeHandler(FadeChangeHandler()))
    }

    override fun navigate(toRoute: DbEnumRoute, context: Context?, parameters: Any?) {

//        var route = (toRoute as? ControllerRoute).guard {
//            Log.d("ERROR", "Eo cast duoc")
//            return
//        }

        when (toRoute) {

            ApplicationRoute.GotoPostDetail -> {
                var vcl = PostDetailViewController()
                vcl.tyPost = (parameters as TyPost)

                router.defaultPushController(vcl)

//                router.pushController(
//                    RouterTransaction.with(vcl)
//                        .pushChangeHandler(HorizontalChangeHandler())
//                        .popChangeHandler(HorizontalChangeHandler()))
            }

            ApplicationRoute.GotoPhUserDetail -> {

            }

            ApplicationRoute.GotoAnyWhere -> {


            }

            else -> {

            }
        }

    }
}