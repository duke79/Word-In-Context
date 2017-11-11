package com.baliyaan.android.wordincontext.Components.Examples.MVP;


import com.baliyaan.android.mvp.Interfaces.BaseMVPNavigator;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPort;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPresenter;
import com.baliyaan.android.mvp.Interfaces.BaseMVPView;
import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;

import java.util.List;


/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface Contract {
    interface Presenter extends BaseMVPPresenter {
        void onQueryTextSubmit(final String query);
        List<Example> getExamples();
    }

    interface View extends BaseMVPView<Presenter> {
        void displayResult();
        void displayError();
    }

    interface Navigator extends BaseMVPNavigator {
        String getQuery();
        void onTryAgain();
    }

    interface Port extends BaseMVPPort {
        void onQueryTextSubmit(String query);
    }
}
