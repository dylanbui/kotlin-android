package com.dylanbui.android_library.mvvm_structure;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.bluelinelabs.conductor.Controller;

/**
 * Created by IntelliJ IDEA.
 * User: Dylan Bui
 * Email: duc@propzy.com
 * Date: 10/1/20
 * To change this template use File | Settings | File and Code Templates.
 */


class DbControllerLifecycleOwner implements LifecycleOwner, Controller.LifecycleListener {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    DbControllerLifecycleOwner(Controller controller) {
        controller.addLifecycleListener(new Controller.LifecycleListener() {

            @Override
            public void preCreateView(@NonNull Controller controller) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            }

            @Override
            public void postAttach(@NonNull Controller controller, @NonNull View view) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            }

            @Override
            public void preDetach(@NonNull Controller controller, @NonNull View view) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            }

            @Override
            public void postDestroyView(@NonNull Controller controller) {
                lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
            }

        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}