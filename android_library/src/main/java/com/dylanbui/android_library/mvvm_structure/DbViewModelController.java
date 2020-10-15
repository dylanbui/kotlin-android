package com.dylanbui.android_library.mvvm_structure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.bluelinelabs.conductor.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: Dylan Bui
 * Email: duc@propzy.com
 * Date: 10/1/20
 * To change this template use File | Settings | File and Code Templates.
 */


public abstract class DbViewModelController extends Controller implements LifecycleOwner {

    private final ViewModelStore viewModelStore = new ViewModelStore();
    private final DbControllerLifecycleOwner lifecycleOwner = new DbControllerLifecycleOwner(this);

    public DbViewModelController() {
        super();
    }

    public DbViewModelController(Bundle bundle) {
        super(bundle);
    }

    public ViewModelProvider viewModelProvider() {
        return viewModelProvider(new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()));
    }

    public ViewModelProvider viewModelProvider(ViewModelProvider.Factory factory) {
        return new ViewModelProvider(viewModelStore, factory);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelStore.clear();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleOwner.getLifecycle();
    }
}