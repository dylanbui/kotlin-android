package com.dylanbui.android_library.ui_control.hint_spinner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SpinnerAdapter
import androidx.core.content.ContextCompat
import com.dylanbui.android_library.IDbItem
import com.dylanbui.android_library.R
import com.dylanbui.android_library.utils.tintCurrentDrawable
import kotlinx.android.synthetic.main.db_material_spinner.view.*


/**
 * Created by IntelliJ IDEA.
 * User: Dylan Bui
 * Email: duc@propzy.com
 * Date: 11/10/20
 * To change this template use File | Settings | File and Code Templates.
 */

class DbMaterialSpinner : LinearLayout {

    private var hintText: String? = null
    private var hintColor: Int? = null
    private var lineColor: Int? = null
    private var arrowColor: Int? = null
    // private var textSize: Int? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.db_material_spinner, this, true)

        if (attrs == null) throw Exception("Provide hint for the DbSpinner")

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DbMaterialSpinner)

        hintText = typedArray.getString(R.styleable.DbMaterialSpinner_dbms_hint_text)
        hintColor = typedArray.getColor(R.styleable.DbMaterialSpinner_dbms_hint_color, ContextCompat.getColor(context, R.color.hintText))
        lineColor = typedArray.getColor(R.styleable.DbMaterialSpinner_dbms_line_color, ContextCompat.getColor(context, R.color.hintText))
        arrowColor = typedArray.getColor(R.styleable.DbMaterialSpinner_dbms_arrow_color, ContextCompat.getColor(context, R.color.hintText))
        // textSize = typedArray.getDimensionPixelSize(R.styleable.DbMaterialSpinner_dbms_text_size, context.resources.getDimensionPixelSize(R.dimen.dbms_text_size))
        typedArray.recycle()

        initData()
    }

    private fun initData() {
        dbms_line.setBackgroundColor(lineColor!!)
        // dbms_arrow.tintCurrentDrawable(arrowColor!!)
        dbms_arrow.setColorFilter(arrowColor!!)
        dbms_spinner.setHintText(hintText!!)
    }

    /**
     * Set hint text
     *
     * @param hint String
     **/
    fun setHint(hint: String) {
        this@DbMaterialSpinner.hintText = hint
        initData()
    }

    /**
     * Set Spinner adapter
     *
     * @param adapter A SpinnerAdapter
     **/
    fun setAdapter(adapter: SpinnerAdapter) {
        dbms_spinner.adapter = adapter
    }

    /**
     * Set Spinner items. Only if items are String Array. For custom items, use setAdapter(...)
     *
     * @param items String Array
     **/
    fun setItems(items: List<String>) {
        dbms_spinner.addAllDropdownList(items)
//        val adapter = StringArrayAdapter(context, items, textSize!!)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        setAdapter(adapter)
    }

    /**
     * Set Spinner items. Only if items are String Array. For custom items, use setAdapter(...)
     *
     * @param items String Array
     **/
    fun <T: IDbItem> setItemList(items: List<T>) {
        dbms_spinner.addAllDropdownItemList(items)
//        dbms_spinner.addAllDropdownList(items)
//        val adapter = StringArrayAdapter(context, items, textSize!!)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        setAdapter(adapter)
    }

    /**
     * Set hint text color
     *
     * @param hintColor A Color
     **/
    fun setHintColor(hintColor: Int) {
        this@DbMaterialSpinner.hintColor = hintColor
        initData()
    }

    /**
     * Set the color of the line below the text
     *
     * @param arrowColor A Color
     **/
    fun setArrowColor(arrowColor: Int) {
        this@DbMaterialSpinner.arrowColor = arrowColor
        initData()
    }

    /**
     * Set the color of the line below the text
     *
     * @param lineColor A Color
     **/
    fun setLineColor(lineColor: Int) {
        this@DbMaterialSpinner.lineColor = lineColor
        initData()
    }

    /**
     * Returns Spinner object. This can be used to set listeners etc.
     *
     * @return Spinner
     **/
    fun getSpinner(): DbSpinner = dbms_spinner

    /**
     * Returns current selected item in the Spinner
     *
     * @return Object
     **/
    fun getSelectedItem() = dbms_spinner.selectedItem

//    private class StringArrayAdapter(context: Context, items: Array<String>, val textSize: Int) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val view = super.getView(position, convertView, parent)
//            view.setPadding(context.resources.getDimensionPixelSize(R.dimen.ms_text_margin), 0, 0, 0)
//            (view as TextView).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//            return view
//        }
//    }
}