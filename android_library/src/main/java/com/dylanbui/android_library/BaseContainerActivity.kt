package com.dylanbui.android_library

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.dylanbui.android_library.permission_manager.DbPermissionManager
import com.dylanbui.android_library.permission_manager.DbPermissionManagerImpl
import com.dylanbui.android_library.permission_manager.DbPermissionRequestExplanation
import com.dylanbui.android_library.permission_manager.DbPermissionResult

abstract class BaseContainerActivity : AppCompatActivity() {

    abstract fun getActivityLayoutId(): Int

    fun getToolBarLayoutId(): Int? = null

    val managePermissions: DbPermissionManager = DbPermissionManagerImpl

    var toolbar: Toolbar? = null
    var alertDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // In Activity's onCreate() for instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var w = getWindow()
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(getActivityLayoutId())

        // Allow toolbar null
        getToolBarLayoutId()?.let {
            toolbar = findViewById(it)
            toolbar?.let { tool ->
                setSupportActionBar(tool)
            }
        }

        onViewBound()
    }

//    fun getStringResource(resourceString: Int): String = resources.getString(resourceString)
//    fun getContext(): Context = this

    open fun onViewBound() {
    }

    fun setToolBarTitle(title: String?) {
        // toolbar_custom.title = title

        var txtTitle = getToolbarTitle() as? TextView
        txtTitle?.let {
            it.text = title
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
            it.startAnimation(animation)

        }

    }

    fun enableUpArrow(enabled: Boolean) {
        toolbar?.let {
            if (enabled) {
                it.setNavigationIcon(R.drawable.ic_arrow_back)
                it.setNavigationOnClickListener { _ ->
                    onBackPressed()
//                    if (router.backstackSize > 1) {
//                        router.popCurrentController()
//                        // Toast.makeText(this, "setNavigationOnClickListener", Toast.LENGTH_LONG).show()
//                    }
                }
            } else {
                it.navigationIcon = null
            }
        }

    }

    fun enableToolBar(enabled: Boolean) {
        if (enabled) {
            toolbar?.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            toolbar?.startAnimation(animation)
        } else {
            toolbar?.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            toolbar?.startAnimation(animation)

        }
    }

    private fun getToolbarTitle(): View? {
        toolbar?.let {
            val childCount = it.childCount
            for (i in 0 until childCount) {
                val child: View = it.getChildAt(i)
                if (child is TextView) {
                    return child
                }
            }
        }
        return null
    }



    /**
     * check permission
     *
     */
    fun checkPermissions(permissions: Array<out String>,
                         onPermissionResult: ((DbPermissionResult) -> Unit)?,
                         permissionRequestPreExecuteExplanation: DbPermissionRequestExplanation? = null,
                         permissionRequestRetryExplanation: DbPermissionRequestExplanation? = null,
                         requestCode: Int? = null): Boolean {
        return managePermissions.checkPermissions(this,
            permissions,
            onPermissionResult,
            permissionRequestPreExecuteExplanation,
            permissionRequestRetryExplanation,
            requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        managePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
