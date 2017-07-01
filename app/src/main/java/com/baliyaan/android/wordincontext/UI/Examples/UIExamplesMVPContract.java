package com.baliyaan.android.wordincontext.UI.Examples;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UIExamplesMVPContract {
    interface View{

    }

    interface Presenter {

    }

    interface Navigator {

    }

    interface Port{
        void onQueryTextSubmit(String query);
    }
}
