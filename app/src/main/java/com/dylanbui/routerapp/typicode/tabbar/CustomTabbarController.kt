package com.dylanbui.routerapp.typicode.tabbar

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.viewpager.widget.ViewPager
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import com.dylanbui.android_library.utils.dLog
import com.dylanbui.routerapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView





class CustomTabbarController : Controller(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {


//    lateinit var pagerAdapter: MyRouterPagerAdapter
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var viewPager: ViewPager

    lateinit var pagerAdapter: RouterPagerAdapter

    private var prevMenuItem: MenuItem? = null

    private val PAGE_COLORS = intArrayOf(
        R.color.green_300,
        R.color.cyan_300,
        R.color.deep_purple_300,
        R.color.lime_300
    )


    /**
     * This is the current selected item id from the [BottomNavigationView]
     */
    @IdRes
    private var currentSelectedItemId: Int = -1

    init {
        pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {

                    var page: Controller? = null
                    if (position == 0) {
                        page = ChildController("Child (Swipe to see more)", PAGE_COLORS[position])
                    } else if (position == 1) {
                        page = ChildController("Child (Swipe to see more)", PAGE_COLORS[position])
                    } else if (position == 2) {
                        page = ChildController("Child (Swipe to see more)", PAGE_COLORS[position])
                    } else if (position == 3) {
                        page = ChildController("Child (Swipe to see more)", PAGE_COLORS[position])
                    }
                    // val page: Controller = ChildController("Child (Swipe to see more)", PAGE_COLORS[position])
                    router.setRoot(RouterTransaction.with(page!!))
                }
            }

            override fun getCount(): Int {
                return PAGE_COLORS.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return "Page $position"
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

        retainViewMode = RetainViewMode.RETAIN_DETACH

        val view = inflater.inflate(R.layout.controller_custom_tabbar, container, false)

        this.viewPager = view.findViewById(R.id.viewPager)
        this.viewPager.addOnPageChangeListener(this)

        this.bottomNavigationView = view.findViewById(R.id.navigation)
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this)

        this.viewPager.adapter = this.pagerAdapter

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        dLog("onAttach")
    }

    override fun onDestroyView(view: View) {
        if (!activity!!.isChangingConfigurations) {
            viewPager.adapter = null
        }
        super.onDestroyView(view)
    }


    /**
     * Listener which get called if a item from the [BottomNavigationView] is selected
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // There is no state (hierarchy/backstack etc.) saved in the [routerBundles].
        // We have to create a new [Controller] and set as root
        when (item.itemId) {
            R.id.tabBeach -> {
                viewPager.setCurrentItem(0, true)
                return true
            }
            R.id.tabGas -> {
                viewPager.setCurrentItem(1, true)
                // childRouter.setRoot(RouterTransaction.with(ConductorController("Gas")))
                return true
            }
            R.id.tabOverview -> {
                viewPager.setCurrentItem(2, true)
                // childRouter.setRoot(RouterTransaction.with(ConductorController("Overview")))
                return true
            }
            R.id.tabPerson -> {
                viewPager.setCurrentItem(3, true)
                // childRouter.setRoot(RouterTransaction.with(ConductorController("Person")))
                return true
            }
            else -> return false
        }
    }

    /**
     * Listener OnPagerChanger
     */
    override fun onPageScrollStateChanged(state: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageSelected(position: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        if (prevMenuItem != null) {
            prevMenuItem?.isChecked = false
        } else {
            bottomNavigationView.menu.getItem(0).isChecked = false
        }

        bottomNavigationView.menu.getItem(position).isChecked = false
        prevMenuItem = bottomNavigationView.menu.getItem(position)
    }


}

