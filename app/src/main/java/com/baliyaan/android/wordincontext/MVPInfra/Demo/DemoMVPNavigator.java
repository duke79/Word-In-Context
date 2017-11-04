package com.baliyaan.android.wordincontext.MVPInfra.Demo;

import android.app.Activity;

/**
 * Created by Pulkit Singh on 11/4/2017.
 */

public class DemoMVPNavigator
        implements DemoMVPContract.Navigator {

    private Activity _context = null;
    private DemoMVPContract.Port _port = null;

    DemoMVPNavigator(Activity activity){
        _context = activity;

        _port = new DemoMVPViewPort(_context,this);
        _port.makePortSomething();
    }

    public void makeNavigatorSomething() {

    }
}
