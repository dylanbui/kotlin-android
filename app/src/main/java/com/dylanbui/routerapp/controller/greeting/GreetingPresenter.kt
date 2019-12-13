import java.util.concurrent.TimeUnit
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface GreetingContract {
    interface View : MvpView {
        fun onGreetingButtonClicked()
        fun showLoading()
        fun hideLoading()
        fun hideGreeting()
        fun showGreeting(greetingText: String)
        fun showError()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadGreeting()
    }
}

class GreetingPresenter : MvpBasePresenter<GreetingContract.View>(), GreetingContract.Presenter
{
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun loadGreeting() {
        disposables.add(getHelloGreeting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { ifViewAttached { view -> view.showLoading() } }
                .doFinally { ifViewAttached { view -> view.hideLoading() } }
                .subscribe({ ifViewAttached { view -> view.showGreeting(it) } }, { ifViewAttached { view -> view.showError() } }))
    }

    override fun destroy() {
        super.destroy()
        disposables.clear()
    }

    private fun getHelloGreeting(): Single<String>
    {
        return Single.just("hi there!").delay(2, TimeUnit.SECONDS)
    }
}