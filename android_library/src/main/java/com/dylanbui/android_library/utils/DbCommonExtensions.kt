package com.dylanbui.android_library.utils

import android.app.Activity
import android.app.Dialog
import android.app.NotificationManager
import android.content.*
import android.content.res.ColorStateList
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// https://github.com/Sub6Resources/Utilities/blob/master/app/src/main/java/com/sub6resources/utilities/Extensions.kt

/**
 * Converts from long to short time
 */
fun Long.toShortTime(): String {
    val hr = TimeUnit.MILLISECONDS.toHours(this)
    val min = TimeUnit.MILLISECONDS.toMinutes(this)
    val sec = TimeUnit.MILLISECONDS.toSeconds(this)

    return "${if (hr != 0L) "$hr:" else ""}${if (min < 10L) "0$min" else min}:${if (sec < 10L) "0$sec" else sec}"
}

/**
 * Returns a random number between `min` and `max`
 */
@Throws(IndexOutOfBoundsException::class)
fun random(min: Int, max: Int): Int {
    if(max < min) throw IndexOutOfBoundsException("Max must be greater than min")
    val range = max - min + 1
    return (Math.random() * range).toInt() + min
}

/**
 * Returns the screen's width
 */
val Activity.screenWidth: Int get() {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

/**
 * Returns the screen's height
 */
val Activity.screenHeight: Int get() {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

/**
 * Sets the onClickListener of a view.
 */
fun View.onClick(listener: View.OnClickListener) {
    setOnClickListener(listener)
}

/**
 * Calls the callback when the view is clicked.
 */
fun View.onClick(onClick: (v: View) -> Unit) {
    setOnClickListener(onClick)
}

/**
 * A simple and pretty [EditText] textChanged listener.
 */
fun EditText.onTextChanged(onTextChanged: (editable: Editable) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            editable?.let {
                onTextChanged(editable)
            }
        }
    })
}

/**
 * Default [SharedPreferences]
 */
val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences(packageName, 0)

/**
 * Performs all edits to [SharedPreferences] then applies them.
 */
fun SharedPreferences.edit(editor: SharedPreferences.Editor.() -> Unit) {
    this.edit().apply {
        editor()
    }.apply()
}

/**
 * Gets a string from string resources
 */
fun Context.getStringRes(@StringRes resId: Int) = resources.getString(resId)

/**
 * Gets a plural from string resources
 */
fun Context.getPlural(@PluralsRes resId: Int, value: Int) = resources.getQuantityString(resId, value, value)

/**
 * Formats a string to be like money
 * Requires a string set up like '$ %.2f' (using whatever currency symbol is desired) in the String Resource file.
 */
fun Context.getMoneyString(@StringRes resId: Int, vararg amount: Int) = resources.getString(resId, *amount.map{it.toDouble()}.toTypedArray())

/**
 * Inflates a layout
 */
fun Context.inflateLayout(layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

/**
 * The system input method manager
 */
val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

/**
 * The system clipboard manager
 */
val Context.clipboardManager: ClipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

/**
 * The system layout inflater
 */
val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * The system notification manager
 */
val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

/**
 * The context wifiManager
 */
val Context.wifiManager: WifiManager
    get() = applicationContext.wifiManager

private val version: Int
    get() = Build.VERSION.SDK_INT

/**
 * Returns the external storage directory
 */
val externalStoragePath: String
    get() = Environment.getExternalStorageDirectory().path

/**
 * Returns true if the device is charging
 */
val Context.charging: Boolean
    get() {
        val intent = this.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
    }

/**
 * Calls action_greater if the android version is higher than the version, action_lower if it is lower, and both action_greater and action_lower if inclusive is true and the android version is equal to version.
 */
inline fun apiOr(version: Int, action_greater: () -> Unit, action_lower: () -> Unit, inclusive: Boolean = false) {
    fromApi(version, inclusive, action_greater)
    toApi(version, inclusive, action_lower)
}

@Deprecated("To make things more pretty, action and inclusive have been switched, so if you are calling this old version, you can make it more pretty by switching them.", ReplaceWith("toApi(toVersion, inclusive, action)"))
inline fun toApi(toVersion: Int, action: () -> Unit, inclusive: Boolean = false) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

/**
 * Calls the callback if the android version is lower than the toVersion or equal to toVersion if inclusive is true (it is not by default)
 */
inline fun toApi(toVersion: Int, inclusive: Boolean = false, action: () -> Unit) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

@Deprecated("To make things more pretty, action and inclusive have been switched, so if you are calling this old version, you can make it more pretty by switching them.", ReplaceWith("fromApi(fromVersion, inclusive, action)"))
inline fun fromApi(fromVersion: Int, action: () -> Unit, inclusive: Boolean = true) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}

/**
 * Calls the callback if the android version is higher than the fromVersion or equal to fromVersion if inclusive is true (default).
 */
inline fun fromApi(fromVersion: Int, inclusive: Boolean = true, action: () -> Unit) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}

/**
 * Tints a drawable of an [ImageView]
 */
fun ImageView.tintCurrentDrawable(color: Int) {
    DrawableCompat.wrap(drawable!!).let {
        it.mutate()
        DrawableCompat.setTintList(it, ColorStateList.valueOf(color))
        setImageDrawable(it)
    }
}

/**
 * Checks if a string is worthless (null or just whitespace)
 */
fun String?.isNothing(): Boolean {
    this?.let { string ->
        if(string.isEmpty()) return true
        string.forEach {
            if(!it.isWhitespace()) return false
        }
    }
    return true
}

/**
 * Unescapes a string
 */
fun String.unescape(): String = this.replace("""\/""", "/")

/**
 * Removes a file extension from a string if it exists
 */
fun String.removeFileExtension(): String = if(this.contains(".") && this.lastIndexOf(".") > 0) { this.substring(0, this.lastIndexOf("."))} else {this}

/**
 * Removes a file name from a string if it exists
 */
fun String.removeFileName(): String = if(this.contains("/") && this.lastIndexOf("/") > 0) { this.substring(0, this.lastIndexOf("/")+1)} else {this}

/**
 * Calls a callback if any of the views is clicked
 */
fun View.bulkClick(ids: Array<Int>, _onClick: (View) -> Unit) {
    ids.forEach { findViewById<View>(it).apply { onClick { v -> _onClick.invoke(v) } } }
}

/**
 * Calls a callback if any of the views is clicked
 */
fun Array<View>.bulkClick(_onClick: (View) -> Unit) {
    forEach { it.apply { onClick { v -> _onClick.invoke(v) } } }
}

/**
 * Gets the string of an [EditText]. No more [Editable]!
 */
fun EditText.getString(): String = text.toString()

/**
 * Invokes a callback if null
 */
inline infix fun Any?.isNull(if_true: () -> Unit) {
    if (this == null) if_true.invoke()
}

/**
 * Invokes a callback if not null
 */
inline infix fun Any?.isNotNull(if_true: (Any) -> Unit) {
    if (this != null) if_true.invoke(this)
}

/**
 * Shows a view
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Makes a view become gone
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Makes a view invisible
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Disables a view
 */
fun View.disable() {
    isEnabled = false
}

/**
 * Enables a view
 */
fun View.enable() {
    isEnabled = true
}

/**
 * Checks if an [EditText] is blank.
 */
fun EditText.isBlank(): Boolean = getString().isBlank()

/**
 * Adds min and max to a [SeekBar] and adds a pretty looking callback for it.
 */
fun SeekBar.minMaxProgressListener(min: Int, max: Int, onValueChanged: (value: Int, fromUser: Boolean) -> Unit) {
    this.max = max - min
    this.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onValueChanged(progress + min, fromUser)
        }
    })
}

/**
 * Pretty looking callback for [SeekBar] progress changes
 */
fun SeekBar.onProgressChanged(function: (progress: Int, fromUser: Boolean) -> Unit) {
    this.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            function(progress, fromUser)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}

/**
 * Find a view.  Not lazy.
 */
inline fun <reified V : View> View.find(id: Int): V = findViewById(id)

inline fun <reified V : View> Activity.find(id: Int): V = findViewById(id)
inline fun <reified V : View> Dialog.find(id: Int): V = findViewById(id)
inline fun <reified V : View> Fragment.find(id: Int): V = view!!.findViewById(id)
inline fun <reified V : View> android.app.Fragment.find(id: Int): V = view!!.findViewById(id)
inline fun <reified V : View> RecyclerView.ViewHolder.find(id: Int): V = itemView.findViewById(id)

fun SpannableStringBuilder.appendWithSpan(str: String, ss: Any) {
    val start = this.length
    this.append(str)
    this.setSpan(ss, start, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}


// Bind a view.
fun <V : View> View.bind(id: Int): ReadOnlyProperty<View, V> = required(id, { find(it) })
fun <V : View> Activity.bind(id: Int): ReadOnlyProperty<Activity, V> = required(id, { find(it) })
fun <V : View> Dialog.bind(id: Int): ReadOnlyProperty<Dialog, V> = required(id, { find(it) })
fun <V : View> android.app.Fragment.bind(id: Int): ReadOnlyProperty<android.app.Fragment, V> = required(id, { find(it) })
fun <V : View> Fragment.bind(id: Int): ReadOnlyProperty<Fragment, V> = required(id, { find(it) })
fun <V : View> RecyclerView.ViewHolder.bind(id: Int): ReadOnlyProperty<RecyclerView.ViewHolder, V> = required(id, { find(it) })
private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing = throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) = ViewLazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

private class ViewLazy<in T, out V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}

/**
 * Observe a nullable [LiveData] as if it is not nullable. All null data returned will simply be ignored.
 */
inline fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, crossinline callback: (data: T) -> Unit) {
    this.observe(lifecycleOwner, Observer {
        it?.let{
            callback(it)
        }
    })
}

/**
 * A shortened version of Transformations.switchMap(trigger: LiveData): LiveData
 */
inline fun <T, R: Any> LiveData<T>.switchMap(crossinline switchMap: (data: T) -> LiveData<R>): LiveData<R> {
    return Transformations.switchMap(this) { switchMap(it) }
}

/**
 * A shortened version of Transformations.map
 */
inline fun <T, R> LiveData<T>.map(crossinline map: (data: T) -> R): LiveData<R> {
    return Transformations.map(this) { map(it) }
}

/**
 * Adds a [RecyclerView.OnScrollListener] to show or hide the FloatingActionButton when the RecyclerView scrolls up
 * or down respectively
 */
fun RecyclerView.bindFloatingActionButton(fab: FloatingActionButton) = this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0 && fab.isShown) {
            fab.hide()
        } else if (dy < 0 && !fab.isShown) {
            fab.show()
        }
    }
})

/**
 * Loads an image from a url or path into an [ImageView] using [Picasso]
 */
fun ImageView.load(urlOrPath: String?) {
    Picasso.get()
        .load(urlOrPath)
        .into(this)
}

/**
 * Loads an image from a Drawable resource into an [ImageView] using [Picasso]
 */
fun ImageView.load(@DrawableRes res: Int) {
    Picasso.get()
        .load(res)
        .into(this)
}

/**
 * Loads an image from a [Uri] into an [ImageView] using [Picasso]
 */
fun ImageView.load(uri: Uri) {
    Picasso.get()
        .load(uri)
        .into(this)
}

/**
 * Loads an image from a [File] into an [ImageView] using [Picasso]
 */
fun ImageView.load(file: File) {
    Picasso.get()
        .load(file)
        .into(this)
}

/**
 * Starts an activity with no extras.
 */
inline fun <reified T: Activity> Context.startActivity() {
    this.startActivity(Intent(this, T::class.java))
}

/**
 * Creates a [Snackbar] on a view and allows more operations to be completed on it.
 */
inline fun View.snackbar(message: String, length: Int = Snackbar.LENGTH_LONG, operations: Snackbar.() -> Unit) {
    Snackbar.make(this, message, length).apply {
        operations()
        show()
    }
}

/**
 * A listener for a [Snackbar] action
 */
fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}
