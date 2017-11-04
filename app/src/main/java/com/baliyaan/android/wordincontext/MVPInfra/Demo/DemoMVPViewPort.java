package com.baliyaan.android.wordincontext.MVPInfra.Demo;

import android.app.Activity;

import com.baliyaan.android.wordincontext.MVPInfra.Adapters.MVPViewPortAdapter;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class DemoMVPViewPort
        extends MVPViewPortAdapter<DemoMVPContract.Navigator,DemoMVPContract.Presenter>
        implements DemoMVPContract.View, DemoMVPContract.Port {

    protected DemoMVPViewPort(Activity activity, DemoMVPContract.Navigator navigator) {
        super(activity, navigator);
        super.bindPresenter(new DemoMVPPresenter(activity(),this));

        presenter().makePresenterSomething();
        navigator().makeNavigatorSomething();
    }

    @Override
    public void makeViewSomething() {
    }

    @Override
    public void makePortSomething() {
    }
}
