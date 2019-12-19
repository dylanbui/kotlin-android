
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.controller.EndlessRecyclerViewScrollListener
import com.dylanbui.routerapp.controller.user.User
import com.dylanbui.routerapp.controller.user.UserActionView
import com.dylanbui.routerapp.controller.user.UserPresenter
import com.dylanbui.routerapp.controller.user.UsersAdapter
import com.dylanbui.routerapp.R


class UserViewController : BaseMvpController<UserActionView, UserPresenter>(), UserActionView, UsersAdapter.UserRowListener
{
    // @BindView(R.id.cycView)
    lateinit var recyclerView: RecyclerView

    // @BindView(R.id.refreshLayout)
    lateinit var layoutRefresh: SwipeRefreshLayout

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var usersAdapter: UsersAdapter

    override fun createPresenter(): UserPresenter = UserPresenter()

    override fun setTitle(): String? {
        return "UserView   Controller"
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewBound(view: View)
    {
        recyclerView = view.findViewById(R.id.cycView)
        layoutRefresh = view.findViewById(R.id.refreshLayout)

        usersAdapter = UsersAdapter(view.context, this)

        var layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = usersAdapter

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.getUserList(page, false)
            }
        }
        recyclerView.addOnScrollListener(scrollListener!!)

        layoutRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                //surveyAdapter.clearData()
                presenter.getUserList(1, false)
            }
        })

    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)
    }

//    override fun showLoading()
//    {
////        greetingTextView.visibility = View.GONE
////        loadingIndicator.visibility = View.VISIBLE
//    }
//
//    override fun hideLoading()
//    {
////        loadingIndicator.visibility = View.GONE
//
//    }
//override fun getStringResource(resourceString: Int): String
//{
//    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//}


    override fun onUserRowClick(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showUserList(list: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setData(list: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showContent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(error: Throwable, pullToRefresh: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateListSurvey(page: Int, userList: List<User>) {
        if (page == 1) {
            usersAdapter.clearData()
            // emptyData(surveyList.size == 0)
        } else {
            // emptyData(false)
        }
        usersAdapter.updateData(userList.toMutableList())
        layoutRefresh.isRefreshing = false
    }

    override fun onRowClick(position: Int, user: User)
    {
        Toast.makeText(applicationContext, "position ${position} - ${user.name}", Toast.LENGTH_LONG).show()
    }


    override fun showLoading()
    {
//        greetingTextView.visibility = View.GONE
//        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoading()
    {
//        loadingIndicator.visibility = View.GONE

    }

    override fun getStringResource(resourceString: Int): String
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPreAttach() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        presenter.getUserList(1, false)
    }


}