package com.dylanbui.android_library.mvp_structure.conductor;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.bluelinelabs.conductor.Controller;
import com.dylanbui.android_library.mvp_structure.conductor.delegate.DbMvpConductorDelegateCallback;
import com.dylanbui.android_library.mvp_structure.conductor.delegate.DbMvpConductorLifecycleListener;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;


/**
 * Base class you can use to extend from to have a Mosby MVP based controller.
 *
 * @author Hannes Dorfmann
 * @since 1.0
 */
public abstract class DbMvpController<V extends MvpView, P extends MvpPresenter<V>> extends Controller
        implements MvpView, DbMvpConductorDelegateCallback<V, P> {

    protected P presenter;

    // Initializer block
    {
        addLifecycleListener(getMosbyLifecycleListener());
    }

    public DbMvpController() {
    }

    public DbMvpController(Bundle args) {
        super(args);
    }

    /**
     * This method is for internal purpose only.
     * <p><b>Do not override this until you have a very good reason</b></p>
     * @return Mosby's lifecycle listener so that
     */
    protected LifecycleListener getMosbyLifecycleListener() {
        return new DbMvpConductorLifecycleListener<V, P>(this);
    }

    @NonNull
    @Override public P getPresenter() {
        return presenter;
    }

    @Override public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

    @NonNull @Override public V getMvpView() {
        return (V) this;
    }
}