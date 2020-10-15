package com.dylanbui.android_library.mvp_structure

import android.content.Context
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.bluelinelabs.conductor.changehandler.VerticalChangeHandler

fun Router.defaultPushController(controller: Controller, tag: String? = null) {
    pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(HorizontalChangeHandler())
            .popChangeHandler(HorizontalChangeHandler())
            .tag(tag)
    )
}

fun Router.defaultPushModalController(controller: Controller, tag: String? = null) {
    pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(VerticalChangeHandler())
            .popChangeHandler(VerticalChangeHandler())
            .tag(tag)
    )
}

fun Router.defaultReplaceTopController(controller: Controller, tag: String? = null) {
    replaceTopController(
        RouterTransaction.with(controller)
            .pushChangeHandler(HorizontalChangeHandler())
            .popChangeHandler(HorizontalChangeHandler())
            .tag(tag)
    )
}

fun Router.defaultSetRootController(controller: Controller,
                                    animatorChangeHandler: AnimatorChangeHandler? = null,
                                    tag: String? = null) {
    val animator = animatorChangeHandler ?: HorizontalChangeHandler()
    setRoot(
        RouterTransaction.with(controller)
            .pushChangeHandler(animator)
            .popChangeHandler(animator)
            .tag(tag)
    )
}

fun Router.defaultSetDialogController(controller: Controller) {
    pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(FadeChangeHandler(false))
            .popChangeHandler(FadeChangeHandler())
    )
}

interface DbEnumRoute {}

interface DbNavigation {
    fun navigate(toRoute: DbEnumRoute, context: Context? = null, parameters: Any? = null)

    fun popCurrentController(router: Router) {
        if (router.backstackSize > 1) router.popCurrentController()
    }

    fun getParentRouter() : Router? = null
}

abstract class DbCoordinator {

    abstract var router: Router

    abstract var rootController: Controller

    constructor(router: Router)

    abstract fun start()
    abstract fun start(option: Any?)
}


abstract class BaseDbCoordinator(router: Router) : DbCoordinator(router) {

    override var router = router

    override fun start() {}

    override fun start(option: Any?) {}
}

