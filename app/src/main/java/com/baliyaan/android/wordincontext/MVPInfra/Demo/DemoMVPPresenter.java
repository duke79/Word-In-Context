package com.baliyaan.android.wordincontext.MVPInfra.Demo;

import android.app.Activity;

import com.baliyaan.android.wordincontext.MVPInfra.Adapters.MVPPresenterAdapter;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class DemoMVPPresenter
        extends MVPPresenterAdapter<DemoMVPContract.View>
        implements DemoMVPContract.Presenter {

    protected DemoMVPPresenter(Activity activity, DemoMVPContract.View view) {
        super(activity, view);

        view().makeViewSomething();
    }

    @Override
    public void makePresenterSomething() {
    }
}
