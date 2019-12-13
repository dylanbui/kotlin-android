package com.dylanbui.routerapp.demofragment

import android.view.*
import android.widget.Button
import android.widget.Toast
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.*
import com.dylanbui.routerapp.MainActivity
import com.dylanbui.routerapp.R

class SecondViewController : BaseController()
{
    lateinit var btnFirst: Button
    lateinit var btnNext: Button

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_first, container, false)
    }

    override fun hasOptionsMenu(): Boolean = true

    override fun getTitle(): String? = "Title Second"

    override fun onViewBound(view: View)
    {
        super.onViewBound(view)

        btnFirst = view.findViewById(R.id.btnFirst)
        btnFirst.setOnClickListener { _ ->
            //Toast.makeText(activity, "Day la man hinh thu 2", Toast.LENGTH_LONG).show()

            router.pushController(
//                RouterTransaction.with(ThirdViewController())
//                    .pushChangeHandler(HorizontalChangeHandler())
//                    .popChangeHandler(HorizontalChangeHandler()))

                RouterTransaction.with(ThirdViewController())
                .pushChangeHandler(VerticalChangeHandler())
                .popChangeHandler(VerticalChangeHandler()))

//            router.pushController(
//                RouterTransaction.with(ThirdViewController())
//                    .pushChangeHandler(FadeChangeHandler())
//                    .popChangeHandler(FadeChangeHandler()))

        }
        btnFirst.text = "${btnFirst.text} 222"

        btnNext = view.findViewById(R.id.btnNextControl)
        btnNext.setOnClickListener { _ ->
            // Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
            Toast.makeText(activity, "Day la man hinh thu 2", Toast.LENGTH_LONG).show()
        }
        btnNext.text = "${btnNext.text} 2 Next"
    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

        println("Dylan: onAttach")
//        var mainActivity = activity as? MainActivity
//        mainActivity?.let {
//            it.setToolBarTitle(setTitle())
//            it.enableUpArrow(router.backstackSize > 1)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.about -> Toast.makeText(activity, "You clicked about", Toast.LENGTH_LONG).show()
        }

        return true
    }

}