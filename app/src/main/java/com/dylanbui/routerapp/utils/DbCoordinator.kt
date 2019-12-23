package com.dylanbui.routerapp.utils

import android.content.Context
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler

fun Router.defaultPushController(controller: Controller) {
    pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(HorizontalChangeHandler())
            .popChangeHandler(HorizontalChangeHandler()))
}

fun Router.defaultSetRootController(controller: Controller) {
    setRoot(
        RouterTransaction.with(controller)
        .pushChangeHandler(HorizontalChangeHandler())
        .popChangeHandler(HorizontalChangeHandler()))
}

interface DbEnumRoute {}

interface DbNavigation {
    fun navigate(toRoute: DbEnumRoute, context: Context? = null, parameters: Any? = null)
}

abstract class DbCoordinator {

    abstract var router: Router

    constructor(router: Router)

    abstract fun start()
    abstract fun start(option: Any?)
}


open class BaseDbCoordinator(router: Router) : DbCoordinator(router) {

    override var router = router

    override fun start() { }

    override fun start(option: Any?) { }
}

