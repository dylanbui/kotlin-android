package com.dylanbui.routerapp.typicode.tabbar

import android.graphics.Color
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.dylanbui.routerapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// -- Save status controller --
private const val ROUTER_STATES_KEY = "STATE"

class MainTabarController : Controller(), BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * This will hold all the information about the tabs.
     *
     * This needs to be a var because we have to reassign it in [onRestoreInstanceState]
     */
    private var routerStates = SparseArray<Bundle>()

    private lateinit var childRouter: Router

    /**
     * This is the current selected item id from the [BottomNavigationView]
     */
    @IdRes
    private var currentSelectedItemId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main_tabbar, container, false)

        val childContainer = view.findViewById<ViewGroup>(R.id.tabbar_changeHandlerFrameLayout)
        childRouter = getChildRouter(childContainer)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)


        // We have not a single bundle/state saved.
        // Looks like this [HomeController] was created for the first time
        if (routerStates.size() == 0) {
            // Select the first item
            currentSelectedItemId = R.id.tabBeach
            childRouter.setRoot(RouterTransaction.with(ConductorController("Beach")))
        } else {
            // We have something in the back stack. Maybe an orientation change happen?
            // We can just rebind the current router
            childRouter.rebindIfNeeded()
        }

        return view
    }

    /**
     * Listener which get called if a item from the [BottomNavigationView] is selected
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Save the state from the current tab so that we can restore it later - if needed
        saveStateFromCurrentTab(currentSelectedItemId)
        currentSelectedItemId = item.itemId
        // Clear all the hierarchy and backstack from the router. We have saved it already in the [routerStates]
        clearStateFromChildRouter()
        // Try to restore the state from the new selected tab.
        val bundleState = tryToRestoreStateFromNewTab(currentSelectedItemId)

        if (bundleState is Bundle) {
            // We have found a state (hierarchy/backstack etc.) and can just restore it to the [childRouter]
            childRouter.restoreInstanceState(bundleState)
            childRouter.rebindIfNeeded()
            return true
        }

        // There is no state (hierarchy/backstack etc.) saved in the [routerBundles].
        // We have to create a new [Controller] and set as root
        when (item.itemId) {
            R.id.tabBeach -> {
                childRouter.setRoot(RouterTransaction.with(ConductorController("Beach")))
                return true
            }
            R.id.tabGas -> {
                childRouter.setRoot(RouterTransaction.with(ConductorController("Gas")))
                return true
            }
            R.id.tabOverview -> {
                childRouter.setRoot(RouterTransaction.with(ConductorController("Overview")))
                return true
            }
            R.id.tabPerson -> {
                childRouter.setRoot(RouterTransaction.with(ConductorController("Person")))
                return true
            }
            else -> return false
        }
    }

    /**
     * Try to restore the state (which was saved via [saveStateFromCurrentTab]) from the [routerStates].
     *
     * @return either a valid [Bundle] state or null if no state is available
     */
    private fun tryToRestoreStateFromNewTab(itemId: Int): Bundle? {
        return routerStates.get(itemId)
    }

    /**
     * This will clear the state (hierarchy/backstack etc.) from the [childRouter] and goes back to root.
     */
    private fun clearStateFromChildRouter() {
        childRouter.setPopsLastView(true); /* Ensure the last view can be removed while we do this */
        childRouter.popToRoot();
        childRouter.popCurrentController();
        childRouter.setPopsLastView(false);
    }

    /**
     * This will save the current state of the tab (hierarchy/backstack etc.) from the [childRouter] in a [Bundle]
     * and put it into the [routerStates] with the tab id as key
     */
    private fun saveStateFromCurrentTab(itemId: Int) {
        val routerBundle = Bundle()
        childRouter.saveInstanceState(routerBundle)
        routerStates.put(itemId, routerBundle)
    }

    /**
     * Save our [routerStates] into the instanceState so we don't loose them on orientation change
     */
    override fun onSaveInstanceState(outState: Bundle) {
        saveStateFromCurrentTab(currentSelectedItemId)
        outState.putSparseParcelableArray(ROUTER_STATES_KEY, routerStates)
        super.onSaveInstanceState(outState)
    }

    /**
     * Restore our [routerStates]
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        routerStates = savedInstanceState.getSparseParcelableArray(ROUTER_STATES_KEY)
    }
}


private const val CONDUCT_TEXT = "ARG"

class ConductorController(text: String = "") : Controller(Bundle().apply { putString(CONDUCT_TEXT, text) }) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main_tabbar_demo_content, container, false)
        view.setBackgroundColor(ContextCompat.getColor(view.context,R.color.blue_text_color))
        // view.setBackgroundColor(Color.BLUE)

        val txtView = view.findViewById<TextView>(R.id.txtContent)
        txtView.text = "${args.getString(CONDUCT_TEXT)} : title"
        val button = view.findViewById<TextView>(R.id.btnDemo)
        // button.text = args.getString(CONDUCT_TEXT + "Button")
        button.text = "${args.getString(CONDUCT_TEXT)} : Button"
        button.setOnClickListener {
            router.pushController(RouterTransaction.with(ConductorController(args.getString(CONDUCT_TEXT) + " child")))
        }
        return view
    }

}
