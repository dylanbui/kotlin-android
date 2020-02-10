package com.dylanbui.routerapp.typicode.bottom_sheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.dylanbui.android_library.utils.DbDeviceUtils
import com.dylanbui.routerapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback


class MySheetDialogFragment : SuperBottomSheetFragment(), DbOptionListAdapter.OptionRowListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_demo_sheet, container, false)
    }

    override fun getCornerRadius() = 16.0f //context!!.resources.getDimension(R.dimen.demo_sheet_rounded_corner)

    override fun getStatusBarColor() = Color.TRANSPARENT

    override fun getPeekHeight() = DbDeviceUtils.getHeightScreen(activity!!) / 2

    // override fun isSheetAlwaysExpanded() = true

    lateinit var rvOptions: RecyclerView
    lateinit var adapter: DbOptionListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(view?.parent as View)
//        behavior.peekHeight = 500 //getPeekHeight()
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    override fun onStart() {
        super.onStart()

//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(view?.parent as View)
//        // behavior.isHideable = false
//        behavior.setBottomSheetCallback(object : BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    dialog?.cancel()
//                }
//
//                if (newState === BottomSheetBehavior.STATE_DRAGGING) {
//                    // behavior.peekHeight = getPeekHeight()
//                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                    // behavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//
//            }
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                // behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//        })
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        behavior.skipCollapsed = true

        // rvOptions = dialog?.findViewById(R.id.rvOptions)!!
        rvOptions = view?.findViewById(R.id.rvOptions)!!

        // data to populate the RecyclerView with
        // data to populate the RecyclerView with
        val animalNames: ArrayList<IOptionItem> = ArrayList()
        animalNames.add(DbOption(1, "Horse"))
        animalNames.add(DbOption(2, "Cow"))
        animalNames.add(DbOption(3, "Camel"))
        animalNames.add(DbOption(4, "Sheep"))
        animalNames.add(DbOption(5, "Goat"))

        animalNames.add(DbOption(6, "1 Horse"))
        animalNames.add(DbOption(7, "1 Cow"))
        animalNames.add(DbOption(8, "1 Camel"))
        animalNames.add(DbOption(9, "1 Sheep"))
        animalNames.add(DbOption(10, "1 Goat"))

        animalNames.add(DbOption(11, "1 Horse"))
        animalNames.add(DbOption(12, "1 Cow"))
        animalNames.add(DbOption(13, "1 Camel"))
        animalNames.add(DbOption(14, "1 Sheep"))
        animalNames.add(DbOption(15, "1 Goat"))

        adapter = DbOptionListAdapter(context!!, this)
        adapter.dataSet = animalNames

        // set up the RecyclerView
        rvOptions.layoutManager = LinearLayoutManager(context)
        rvOptions.adapter = adapter
        rvOptions.setHasFixedSize(true)
        // rvOptions.layoutManager = layoutManager
        rvOptions.adapter = adapter


    }


    override fun onRowClick(position: Int, user: IOptionItem) {


    }

}