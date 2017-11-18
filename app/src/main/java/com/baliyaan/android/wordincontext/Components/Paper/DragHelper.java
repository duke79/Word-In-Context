package com.baliyaan.android.wordincontext.Components.Paper;

import android.support.v4.widget.ViewDragHelper;
import android.view.View;

/**
 * Created by Pulkit Singh on 11/18/2017.
 */

class DragHelper extends ViewDragHelper.Callback{
    PaperView _paperView;

    public DragHelper(PaperView paperView) {
        _paperView = paperView;
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        boolean bTryCapture = false;
        if(child.equals(_paperView._bottomView))
            bTryCapture = true;
        return bTryCapture;
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        return top+dy;
    }
}
