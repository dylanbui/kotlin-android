package com.dylanbui.android_library.dialog_plus;

import androidx.annotation.NonNull;
import android.view.View;

public interface OnHolderListener {

  void onItemClick(@NonNull Object item, @NonNull View view, int position);

}
