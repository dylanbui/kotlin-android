package com.dylanbui.routerapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.dylanbui.routerapp.demofragment.FirstViewController
import com.dylanbui.routerapp.networking.UploadViewController
import com.dylanbui.routerapp.retrofit.PostViewController
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//import com.google.android.material.animation.AnimationUtils

//class Post {
//    @SerializedName("title")
//    @Expose
//    var title: String? = null
//
//    @SerializedName("body")
//    @Expose
//    var body: String? = null
//
//    @SerializedName("userId")
//    @Expose
//    var userId: Int? = null
//
//    @SerializedName("id")
//    @Expose
//    var id: Int? = null
//
//    override fun toString(): String {
//        return "Post{" +
//                "title='" + title + '\'' +
//                ", body='" + body + '\'' +
//                ", userId=" + userId +
//                ", id=" + id +
//                '}'
//    }
//}

// Extension function to show toast message
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

class MainActivity : AppCompatActivity()
{

    // @BindView(R.id.controller_container)
    lateinit var container: ViewGroup
    lateinit var toolbar: Toolbar

    private lateinit var router: Router

    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ButterKnife.bind(this)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Get action bar
        //supportActionBar?.hide()



        container = findViewById(R.id.controller_container)
        router = Conductor.attachRouter(this, container, savedInstanceState)
        if (!router.hasRootController()) {
             //router.setRoot(RouterTransaction.with(GreetingViewController()))
             //router.setRoot(RouterTransaction.with(UserViewController()))
            // router.setRoot(RouterTransaction.with(PostViewController()))
            router.setRoot(RouterTransaction.with(UploadViewController()))
//            router.setRoot(RouterTransaction.with(FirstViewController())
//                .pushChangeHandler(FadeChangeHandler())
//                .popChangeHandler(FadeChangeHandler()))
        }

        // Convert hashmap to JSONObject
//        val postData = HashMap<String, String?>()
//        postData["Google"] = "San Jose"
//        postData["Facebook"] = "Mountain View"
//        postData["Datamake"] = "NYC"
//        postData["Twitter"] = "SFO"
//        postData["Microsoft"] = null
//        println("Raw Map ===> ${postData}")
//
//        // Construct a JSONObject from a Map.
//        val crunchifyObject = JSONObject(postData)
//        println("Method-2: Using new JSONObject() ==> ${crunchifyObject}")

        // Initialize a list of required permissions to request runtime
        val list = listOf<String>(
            Manifest.permission.CAMERA,
//            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
//            Manifest.permission.SEND_SMS,
//            Manifest.permission.READ_CALENDAR
        )

        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(this,list,PermissionsRequestCode)

    }

    override fun onStart()
    {
        super.onStart()

        managePermissions.checkPermissions()
    }

    override fun onBackPressed()
    {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    fun setToolBarTitle(title: String?) {
        // toolbar.title = title

        var txtTitle = getToolbarTitle() as? TextView
        txtTitle?.let {
            it.text = title
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
            it.startAnimation(animation)

        }

    }

    fun enableUpArrow(enabled: Boolean) {
        if (enabled) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener { _ ->
                if (router.backstackSize > 1) {
                    router.popCurrentController()
                    // Toast.makeText(this, "setNavigationOnClickListener", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            toolbar.navigationIcon = null
        }
    }

    fun enableToolBar(enabled: Boolean) {
        if (enabled) {
            toolbar.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            toolbar.startAnimation(animation)
        } else {
            toolbar.visibility = View.GONE
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            toolbar.startAnimation(animation)

        }
    }

    private fun getToolbarTitle(): View? {
        val childCount = toolbar.childCount
        for (i in 0 until childCount) {
            val child: View = toolbar.getChildAt(i)
            if (child is TextView) {
                return child
            }
        }
        return null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            PermissionsRequestCode ->{
                val isPermissionsGranted = managePermissions.processPermissionsResult(requestCode, permissions, grantResults)

                if(isPermissionsGranted){
                    // Do the task now
                    toast("Permissions granted.")
                }else{
                    toast("Permissions denied.")
                }
                return
            }
        }

    }


}


class ManagePermissions(val activity: Activity, val list: List<String>, val code:Int) {

    // Check permissions at runtime
    fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        } else {
            activity.toast("Permissions already granted.")
        }
    }


    // Check permissions status
    private fun isPermissionsGranted(): Int {
        // PERMISSION_GRANTED : Constant Value: 0
        // PERMISSION_DENIED : Constant Value: -1
        var counter = 0;
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }


    // Find the first denied permission
    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) return permission
        }
        return ""
    }


    // Show alert dialog to request permissions
    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Need permission(s)")
        builder.setMessage("Some permissions are required to do the task.")
        builder.setPositiveButton("OK", { dialog, which -> requestPermissions() })
        builder.setNeutralButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }


    // Request the permissions at run time
    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Show an explanation asynchronously
            activity.toast("Should show an explanation.")
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
    }


    // Process permissions result
    fun processPermissionsResult(requestCode: Int, permissions: Array<String>,
                                 grantResults: IntArray): Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }
}