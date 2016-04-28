package com.arialyy.frame.core;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by lyy on 2016/4/28.
 */
final class TempViewPopupWindow extends PopupWindow {
    protected View mView;

    public TempViewPopupWindow(@NonNull View view) {
        mView = view;
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void changeView(@NonNull View view) {
        mView = view;
        setContentView(mView);
    }
}
