import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dylanbui.routerapp.BaseMvpController
import com.dylanbui.routerapp.R


class GreetingViewController : BaseMvpController<GreetingContract.View, GreetingContract.Presenter>(), GreetingContract.View
{

    // @BindView(R.id.greeting_textview)
    lateinit var greetingTextView: TextView

    // @BindView(R.id.loading_indicator)
    lateinit var loadingIndicator: ProgressBar

    // @BindView(R.id.hello_greeting_button)
    lateinit var btn: Button

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View
    {
        return inflater.inflate(R.layout.greeting_view, container, false)
    }

    override fun onViewBound(view: View)
    {
        greetingTextView = view.findViewById(R.id.greeting_textview)
        greetingTextView.text = "test choi thoi 12121"

        btn = view.findViewById(R.id.hello_greeting_button)
        btn.setOnClickListener { _ ->
            this.onGreetingButtonClicked()
        }
    }

//    override fun onDestroyView(view: View)
//    {
//        super.onDestroyView(view)
//    }

    override fun createPresenter(): GreetingPresenter = GreetingPresenter()

//    @OnClick(R.id.hello_greeting_button)
//    public fun onClick(view: View)
//    {
//        Toast.makeText(applicationContext, applicationContext?.getString(R.string.greeting_loading_error), Toast.LENGTH_LONG).show()
//        presenter.loadGreeting()
//    }

    // @OnClick(R.id.hello_greeting_button)
    override fun onGreetingButtonClicked() {
        Toast.makeText(applicationContext, "tim dai di", Toast.LENGTH_LONG).show()
        presenter.loadGreeting()
    }

    override fun showGreeting(greetingText: String) {
        greetingTextView.visibility = View.VISIBLE
        greetingTextView.text = greetingText
    }

    override fun showLoading() {
        greetingTextView.visibility = View.GONE
        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingIndicator.visibility = View.GONE
    }

    override fun showError() {
        Toast.makeText(applicationContext, "show ra loi", Toast.LENGTH_LONG).show()
    }

    override fun hideGreeting() {
        greetingTextView.visibility = View.GONE
    }

    override fun setTitle(): String? {
        return "Greeting"
    }

    override fun onPreAttach() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}