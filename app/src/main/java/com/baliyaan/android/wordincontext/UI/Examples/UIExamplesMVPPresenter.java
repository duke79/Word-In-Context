package com.baliyaan.android.wordincontext.UI.Examples;

import android.app.Activity;

import com.baliyaan.android.wordincontext.UI.MVPPresenterAdapter;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

class UIExamplesMVPPresenter extends MVPPresenterAdapter<UIExamplesMVPContract.View> implements UIExamplesMVPContract.Presenter {
    protected UIExamplesMVPPresenter(Activity activity, UIExamplesMVPContract.View view) {
        super(activity, view);
    }
}
