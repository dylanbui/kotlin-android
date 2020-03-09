package com.dylanbui.routerapp.typicode.user.login

import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dylanbui.android_library.date_time_picker.SlideDateTimeListener
import com.dylanbui.android_library.date_time_picker.SlideDateTimePicker
import com.dylanbui.android_library.dialog_plus.DialogPlus
import com.dylanbui.android_library.dialog_plus.DialogPlusBuilder
import com.dylanbui.android_library.dialog_plus.ViewHolder
import com.dylanbui.android_library.utils.DbDialogAbstract
import com.dylanbui.android_library.utils.DbEditTextUtils
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.android_library.utils.toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import com.dylanbui.routerapp.networking.AppNetworkServiceError
import java.text.SimpleDateFormat
import java.util.*


class LoginViewController : BaseMvpController<LoginActionView, LoginPresenter>(), LoginActionView {


    @BindView(R.id.edtUsername)
    lateinit var edtUsername: EditText
    @BindView(R.id.edtPassword)
    lateinit var edtPassword: EditText

    override fun setTitle(): String? = "Login"

    override fun createPresenter(): LoginPresenter = LoginPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_login, container, false)
    }

    override fun onViewBound(view: View) {
//        txtUsername = view.findViewById(R.id.txtUsername)
//        txtPassword = view.findViewById(R.id.txtPassword)

        edtUsername.setText("tien duc")
        edtPassword.setText("1234")

        // -- At here presenter == null --
//        var btnLogin = view.findViewById<Button>(R.id.btnLogin)
//        btnLogin.setOnClickListener {
//            router.defaultPushController(RegisterViewController())
//            //this.doLogin()
//        }

        // setHasOptionsMenu(true)

//        toolbar_login?.setNavigationIcon(R.drawable.ic_arrow_back)
        // need to set the icon here to have a navigation icon. You can simple create an vector image by "Vector Asset" and using here
//        toolbar_login?.setNavigationOnClickListener {
//            // do something when click navigation
//        }

        var menu = toolbar?.menu
        menu?.let {
            it.add(Menu.NONE, 11, 0, "View RSS")
                .setIcon(R.mipmap.ic_delete)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER) // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

            it.add(Menu.NONE, 22, 1, "Read RSS")
                .setIcon(R.mipmap.ic_error)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER) // .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }


        // toolbar_login?.inflateMenu(R.menu.menu_login)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                11 -> {
                    activity?.toast("View RSS")
                    true
                }
                22 -> {
                    activity?.toast("Read RSS")
                    true
                }
                R.id.action_cut -> {
                    activity?.toast("action_cut")
                    true
                }
                R.id.action_copy -> {
                    activity?.toast("action_copy")
                    true
                }
                R.id.action_add -> {
                    dLog("R.id.action_add")
                    // do something
                    true
                }
                R.id.action_update_room -> {
                    dLog("R.id.action_update_room")
                    // do something
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }



    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        activity?.let {
            val controllerContainer = view.findViewById<ViewGroup>(R.id.controllerContainer)
            DbEditTextUtils.setupEditTextUI(controllerContainer, it)
            DbEditTextUtils.requestFocus(edtUsername, it)
        }

    }


    @OnClick(R.id.btnLogin)
    internal fun doLogin() {
        if (TextUtils.isEmpty(edtUsername.text)) {
            // showWarningMessage(getStringResource(R.string.invalid_phone), R.string.close, null)
            return
        }
        if (TextUtils.isEmpty(edtPassword.text)) {
            // showWarningMessage(getStringResource(R.string.invalid_password), R.string.close, null)
            return
        }
        presenter.doLogin(edtUsername.text.toString(), edtPassword.text.toString())
    }

    @OnClick(R.id.btnRegister)
    internal fun doRegister() {
        // Toast.makeText(this.activity, "doRegister", Toast.LENGTH_SHORT).show()

        val dialog = BottomDialog(this.activity!!)
        dialog.build(object : BottomDialog.BottomDialogListener {
            override fun onLikeClick() {
                Toast.makeText(this@LoginViewController.activity, "onLikeClick", Toast.LENGTH_SHORT).show()
            }

            override fun onLoveClick() {
                Toast.makeText(this@LoginViewController.activity, "onLoveClick", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelClick() {
                Toast.makeText(this@LoginViewController.activity, "onCancelClick", Toast.LENGTH_SHORT).show()
            }
        })
        dialog.show()
    }

    @OnClick(R.id.btnForgotPassword)
    internal fun doForgotPassword() {
        // Toast.makeText(this.activity, "doForgotPassword", Toast.LENGTH_SHORT).show()
        val dialog = CustomBottomDialog(this.activity!!).create(object : CustomBottomDialog.CBottomDialogListener {
            override fun onLikeClick() {
                Toast.makeText(this@LoginViewController.activity, "Custom onLikeClick", Toast.LENGTH_SHORT).show()
            }

            override fun onLoveClick() {
                Toast.makeText(this@LoginViewController.activity, "Custom onLoveClick", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelClick() {
                Toast.makeText(this@LoginViewController.activity, "Custom onCancelClick", Toast.LENGTH_SHORT).show()
            }
        })
        dialog.show()
    }


    private val mFormatter = SimpleDateFormat("MMMM dd yyyy hh:mm aa")
    private val listener: SlideDateTimeListener = object : SlideDateTimeListener() {
        override fun onDateTimeSet(date: Date) {
            Toast.makeText(this@LoginViewController.activity,
                mFormatter.format(date).toString(), Toast.LENGTH_SHORT).show()
        }

        // Optional cancel listener
        override fun onDateTimeCancel() {
            Toast.makeText(this@LoginViewController.activity, "Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    @OnClick(R.id.btnDatetime)
    internal fun doDatetime() {

        val act = activity as AppCompatActivity

        SlideDateTimePicker.Builder(act.supportFragmentManager)
            .setListener(listener)
            // .setPickerType(SlideDateTimePicker.PickerType.ONLY_DATE)
            .setInitialDate(Date())
            //.setMinDate(minDate)
            //.setMaxDate(maxDate)
            //.setIs24HourTime(true)
            //.setTheme(SlideDateTimePicker.HOLO_DARK)
            //.setIndicatorColor(Color.parseColor("#990000"))
            .build()
            .show()
    }

    /**
     * LoginActionView interface
     */

    override fun loginComplete() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoginError(error: AppNetworkServiceError) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}



class CustomBottomDialog(private val context: Context,var listener: CBottomDialogListener? = null): DbDialogAbstract(context) {

    // private var listenerDig: CBottomDialogListener? = null
    @BindView(R.id.title)
    lateinit var tvTitle: TextView

    interface CBottomDialogListener {
        fun onLikeClick()
        fun onLoveClick()
        fun onCancelClick()
    }

    override fun inflateViewId(): Int = R.layout.dialog_content

    override fun configurationBuilder(dialogBuilder: DialogPlusBuilder) {

        dialogBuilder.setGravity(Gravity.TOP)

        dialogBuilder.setOnCancelListener { listener?.onCancelClick() }
    }

    override fun onViewBound(view: View) {
        ButterKnife.bind(this, view)


        tvTitle.text = "Bui van tien duc"
    }

    fun create(listenerDig: CBottomDialogListener?): CustomBottomDialog {
        super.create()
        listener = listenerDig
        return this
    }

    @OnClick(R.id.like_it_button)
    internal fun likeClick() {
        dismiss()
        listener?.onLikeClick()
    }

    @OnClick(R.id.love_it_button)
    internal fun loveClick() {
        dismiss()
        listener?.onLoveClick()
    }

}


class BottomDialog(val context: Context) {

    private val builder: DialogPlusBuilder = DialogPlus.newDialog(context)
    private lateinit var dialog: DialogPlus

    private var listenerDig: BottomDialogListener? = null

    interface BottomDialogListener {
        fun onLikeClick()
        fun onLoveClick()
        fun onCancelClick()
    }

    init {

        val holder = ViewHolder(R.layout.dialog_content)

        builder.setContentHolder(holder) //ViewHolder(R.layout.dialog_content))
        builder.isCancelable = true

//        Gravity.TOP
//        Gravity.CENTER
//        Gravity.BOTTOM

        builder.setGravity(Gravity.BOTTOM)
        builder.isExpanded = false
        builder.overlayBackgroundResource = android.R.color.transparent
    }


    fun build(listener: BottomDialogListener? = null) {

        listenerDig = listener

        builder.setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        //builder.setContentHeight(500)
        // builder.setContentWidth(800)
        

//        val btnLike: Button = builder.holder.inflatedView.findViewById(R.id.like_it_button)
//        btnLike.onClick {
//            dLog("Click vao day thu")
//        }

        // builder.setHeader(R.layout.header, fixedHeader)
        // builder.setFooter(R.layout.footer, fixedFooter)


        // builder.setAdapter(adapter)
//        builder.setOnClickListener { dialog, view ->
//            if (view is TextView) {
//                toast(view.text.toString())
//            }
//        }
//        setOnItemClickListener { dialog, item, view, position ->
//            val textView = view.findViewById<TextView>(R.id.text_view)
//            toast(textView.text.toString())
//        }
//        //        .setOnDismissListener(dismissListener)
//        setExpanded(expanded)
//
//        if (contentHeightInput.text.toString().toInt() != -1) {
//            setContentHeight(contentHeightInput.text.toString().toInt())
//        } else {
//            setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
//        }
//
//        if (contentWidthInput.text.toString().toInt() != -1) {
//            setContentWidth(800)
//        }
//
//        setOnCancelListener { dialog -> toast("cancelled") }
//        setOverlayBackgroundResource(android.R.color.transparent)
        //        .setContentBackgroundResource(R.drawable.corner_background)
        //                .setOutMostMargin(0, 100, 0, 0)


        builder.setOnCancelListener { listenerDig?.onCancelClick() }

        // -- Make dialog, bind action --
        dialog = builder.create()
        ButterKnife.bind(this, dialog.holderView)
    }

    fun show() {
        dialog.show()
    }

    @OnClick(R.id.like_it_button)
    internal fun likeClick() {
        dialog.dismiss()
        listenerDig?.onLikeClick()
    }

    @OnClick(R.id.love_it_button)
    internal fun loveClick() {
        dialog.dismiss()
        listenerDig?.onLoveClick()

    }

}