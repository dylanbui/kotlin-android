package com.dylanbui.android_library.mvp_structure.conductor.delegate;

import android.view.View;

import androidx.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Controller.LifecycleListener;
import com.dylanbui.android_library.mvp_structure.conductor.DbMvpController;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;



/**
 * This {@link LifecycleListener} has to added to your Controller to integrate Mosby.
 * This class uses {@link DbMvpConductorDelegateCallback} to call Mosby's methods like {@code
 * createPresenter()} according to the {@link Controller} lifecycle events.
 *
 * <p>
 * {@link DbMvpController} already implements {@link DbMvpConductorDelegateCallback} and registers this
 * class as listener.
 * </p>
 *
 * @author Hannes Dorfmann
 * @see DbMvpConductorDelegateCallback
 * @see DbMvpController
 * @since 1.0
 */
public class DbMvpConductorLifecycleListener<V extends MvpView, P extends MvpPresenter<V>>
        implements LifecycleListener {

    protected final DbMvpConductorDelegateCallback<V, P> callback;

    /**
     * Instantiate a new Mosby MVP Listener
     *
     * @param callback {@link DbMvpConductorDelegateCallback} to set presenter. Typically the
     * controller
     * himself.
     */
    public DbMvpConductorLifecycleListener(DbMvpConductorDelegateCallback<V, P> callback) {
        this.callback = callback;
    }

    protected DbMvpConductorDelegateCallback<V, P> getCallback() {
        return callback;
    }

    @Override public void postCreateView(@NonNull Controller controller, @NonNull View view) {

        DbMvpConductorDelegateCallback<V, P> callback = getCallback();

        P presenter = callback.getPresenter();
        if (presenter == null) {
            presenter = callback.createPresenter();
            if (presenter == null) {
                throw new NullPointerException(
                        "Presenter returned from createPresenter() is null in " + callback);
            }
            callback.setPresenter(presenter);
        }

        V mvpView = callback.getMvpView();
        if (mvpView == null) {
            throw new NullPointerException("MVP View returned from getMvpView() is null in " + callback);
        }
        presenter.attachView(mvpView);
    }

    @Override public void preDestroyView(@NonNull Controller controller, @NonNull View view) {
        P presenter = getCallback().getPresenter();
        if (presenter == null) {
            throw new NullPointerException(
                    "Presenter returned from getPresenter() is null in " + callback);
        }
        presenter.detachView();
    }

    @Override
    public void postDestroy(@NonNull Controller controller) {
        // super.postDestroy(controller);

        P presenter = getCallback().getPresenter();
        if (presenter == null) {
            throw new NullPointerException(
                    "Presenter returned from getPresenter() is null in " + callback);
        }
        presenter.destroy();
    }
}