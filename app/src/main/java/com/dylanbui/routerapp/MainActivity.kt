package com.dylanbui.routerapp

import android.Manifest
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.dylanbui.routerapp.typicode.AppCoordinator
import com.dylanbui.routerapp.typicode.post.PostListViewController
import com.dylanbui.routerapp.utils.ManagePermissions
import com.dylanbui.routerapp.utils.toast


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

class MainActivity : AppCompatActivity()
{

    // @BindView(R.id.controller_container)
    lateinit var container: ViewGroup
    lateinit var toolbar: Toolbar

    private lateinit var router: Router

    private val permissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions


    var appCoordinator: AppCoordinator? = null

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

        appCoordinator = AppCoordinator(router)
        appCoordinator?.start()


//        if (!router.hasRootController()) {
//             //router.setRoot(RouterTransaction.with(GreetingViewController()))
//             //router.setRoot(RouterTransaction.with(UserViewController()))
//            // router.setRoot(RouterTransaction.with(PostViewController()))
//            // router.setRoot(RouterTransaction.with(UploadViewController()))
//            // router.setRoot(RouterTransaction.with(ListPhotoViewController()))
//            router.setRoot(RouterTransaction.with(PostListViewController()))
////            router.setRoot(RouterTransaction.with(FirstViewController())
////                .pushChangeHandler(FadeChangeHandler())
////                .popChangeHandler(FadeChangeHandler()))
//        }

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
        managePermissions = ManagePermissions(this, list, permissionsRequestCode)

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

        when (requestCode) { permissionsRequestCode -> {
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
