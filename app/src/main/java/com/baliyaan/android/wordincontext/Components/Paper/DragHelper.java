package com.baliyaan.android.wordincontext.Components.Paper;

import android.support.v4.widget.ViewDragHelper;
import android.view.View;

/**
 * Created by Pulkit Singh on 11/18/2017.
 */

class DragHelper extends ViewDragHelper.Callback{
    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return false;
    }
}
