package com.dylanbui.routerapp.typicode.multi_upload_progress

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dylanbui.android_library.ui_control.progress_dialog.DbCustomDialog
import com.dylanbui.android_library.ui_control.progress_dialog.DbProgressDialog
import com.dylanbui.android_library.ui_control.progress_dialog.DbUploadProgressDialog
import com.dylanbui.android_library.utils_extentions.doAsync
import com.dylanbui.android_library.utils_extentions.uiThread
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R
import kotlinx.android.synthetic.main.controller_upload_progress.view.*

class UploadProgressController : BaseMvpController<UploadProgressViewAction, UploadProgressPresenter>(), UploadProgressViewAction {

    /* ProgressDialog object declared globally */
    private lateinit var progressUploadDialog: DbProgressDialog

    private lateinit var uploadDialog: DbUploadProgressDialog
    private lateinit var customDialog: DbCustomDialog

    /* List of colors */
    private val colors = arrayListOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.GRAY,
        Color.CYAN,
        Color.MAGENTA,
        Color.DKGRAY,
        Color.BLACK
    )

    /* color selector variable */
    private var currentColor = 0

    /* progress style selector */
    private var progressStyle: DbProgressDialog.ProgressStyle = DbProgressDialog.ProgressStyle.SpinnerStyle

    /* indeterminate mode selector */
    private var indeterminate = false


    override fun setTitle(): String? = "UploadProgress"



    override fun createPresenter(): UploadProgressPresenter = UploadProgressPresenter()

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_upload_progress, container, false)
    }

    override fun onViewBound(view: View) {

        // Initialize progressDialog
        progressUploadDialog = DbProgressDialog(activity)

        /*progressDialog.apply {
            setMessage("Loading")
            setMax(5)
            setProgress(4)
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        }.show()*/

        // Set message
        progressUploadDialog.setMessage("Pretending to do some long task...")

        // Set maximum progress
        progressUploadDialog.setMax(5)

        // Set Current Progress
        progressUploadDialog.setProgress(0)

        // Set cancelable
        progressUploadDialog.setCancelable(true)

        /* showProgressDialog Click Listener */
        view.showProgressDialogButton?.setOnClickListener {

            // Show dialog
            progressUploadDialog.show()

            /* Faking a long task */
            doAsync {
                Thread(Runnable {
                    var i = 0
                    while (++i <= 5) {
                        uiThread {

                            Log.e("TAG", "--- progress = ${progressUploadDialog.getProgress()}")

                            // Update Progress
                            // progressUploadDialog.setProgress(progressUploadDialog.getProgress() + 1)
                            progressUploadDialog.setProgress(i)

                            // Set message
                            progressUploadDialog.setMessage("Pretending to do some long task...${5 - i}")

                        }
                        Thread.sleep(2000)
                    }
                }).run()
                uiThread {
                    // Dismiss dialog
                    progressUploadDialog.dismiss()

                }
            }

        }

        /* toggleCancelableButton Click Listener */
        view.toggleCancelableButton?.setOnClickListener {

            // Set cancelable
            progressUploadDialog.setCancelable(!progressUploadDialog.isCancelable())

            (it as Button).text = "Cancelable: ${progressUploadDialog.isCancelable()}"
        }

        /* changeProgressColorButton Click Listener */
        view.changeProgressColorButton?.setOnClickListener {
            if (currentColor == colors.size)
                currentColor = 0
            // Cycle through colors list
            progressUploadDialog.setColor(colors[currentColor++])
        }

        /* toggleProgressStyleButton Click Listener */
        view.toggleProgressStyleButton?.setOnClickListener {

            if (progressStyle == DbProgressDialog.ProgressStyle.SpinnerStyle) {

                // Set Progress Style
                progressUploadDialog.setProgressStyle(DbProgressDialog.ProgressStyle.HorizontalStyle)

                (it as Button).text = "Toggle ProgressStyle: STYLE_HORIZONTAL"

                progressStyle = DbProgressDialog.ProgressStyle.HorizontalStyle

            } else {

                // Set Progress Style
                progressUploadDialog.setProgressStyle(DbProgressDialog.ProgressStyle.SpinnerStyle)

                (it as Button).text = "Toggle ProgressStyle: STYLE_SPINNER"

                progressStyle = DbProgressDialog.ProgressStyle.SpinnerStyle

            }
        }

        view.setCustomViewButton?.setOnClickListener { it ->
            progressUploadDialog.setProgressStyle(DbProgressDialog.ProgressStyle.CustomStyle {
                LayoutInflater.from(it).inflate(R.layout.upload_progress_custom_view, null, false)
            })
        }

        /* toggleIndeterminateButton Click Listener*/
        view.toggleIndeterminateButton?.setOnClickListener {

            // Toggle indeterminate
            indeterminate = !indeterminate

            // Set indeterminate mode
            progressUploadDialog.setIndeterminate(indeterminate)

            // Set value in TextView
            (it as Button).text = "Toggle Indeterminate: $indeterminate"

        }




        uploadDialog = DbUploadProgressDialog(activity)

        /*progressDialog.apply {
            setMessage("Loading")
            setMax(5)
            setProgress(4)
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        }.show()*/

        // Set message
        uploadDialog.setMessage("Upload file to server...")
        // Set maximum progress
        uploadDialog.setMaxFiles(1)
        // Set Current Progress
        uploadDialog.setProgress(0, 0)
        /* showProgressDialog Click Listener */
        view.showUploadDialogButton?.setOnClickListener {

            // Show dialog
            uploadDialog.show()

            /* Faking a long task */
            doAsync {
                Thread(Runnable {
                    var i = 0
                    while (++i <= uploadDialog.getMaxFiles()) {

                        for (j: Int in 0..100) {
                            uiThread {

                                Log.e("TAG", "--- progress = ${uploadDialog.getProgress()}")

                                // Update Progress
                                uploadDialog.setProgress(j, i)

                                // Set message
                                uploadDialog.setMessage("Pretending to do some long task...${uploadDialog.getMaxFiles() - i}")

                            }
                            Thread.sleep(100)
                        }

                    }
                }).run()
                uiThread {
                    // Dismiss dialog
                    uploadDialog.dismiss()

                }
            }

        }


        showCustom(view)
    }

    override fun onPreAttach() {
        // -- At here presenter is existed --
        // -- Get action in row => presenter --
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }


    private fun showCustom(view: View) {
        // Initialize progressDialog
        customDialog = DbCustomDialog(activity as AppCompatActivity, DbCustomDialog.ProgressStyle.HorizontalStyle)

        /*progressDialog.apply {
            setMessage("Loading")
            setMax(5)
            setProgress(4)
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        }.show()*/

        // Set message
        customDialog.setMessage("Cho xu ly long task...")

        // Set maximum progress
        customDialog.setMax(5)

        // Set Current Progress
        customDialog.setProgress(0)

        // Set cancelable
        // customDialog.setCancelable(true)

        /* showProgressDialog Click Listener */
        view.showCustomDialogButton?.setOnClickListener {

            // Show dialog
            customDialog.show()

            /* Faking a long task */
            doAsync {
                Thread(Runnable {
                    var i = 0
                    while (++i <= 5) {
                        uiThread {

                            Log.e("TAG", "--- progress = ${progressUploadDialog.getProgress()}")

                            // Update Progress
                            // progressUploadDialog.setProgress(progressUploadDialog.getProgress() + 1)
                            customDialog.setProgress(i)

                            // Set message
                            customDialog.setMessage("Cho xu ly long task...${5 - i}")

                        }
                        Thread.sleep(2000)
                    }
                }).run()
                uiThread {
                    // Dismiss dialog
                    customDialog.dismiss()

                }
            }

        }


    }

}

