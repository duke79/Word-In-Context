package com.baliyaan.android.wordincontext.UI.Examples;

import io.reactivex.Observable;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UIExamplesMVPContract {
    interface View{

    }

    interface Presenter {
        Observable onQueryTextSubmit(final String query);
    }

    interface Navigator {

    }

    interface Port{
        void onQueryTextSubmit(String query);
    }
}
