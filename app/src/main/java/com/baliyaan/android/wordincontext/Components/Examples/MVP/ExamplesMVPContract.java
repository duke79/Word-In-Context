package com.baliyaan.android.wordincontext.Components.Examples.MVP;


import android.content.Context;

import com.baliyaan.android.mvp.Interfaces.BaseMVPNavigator;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPort;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPresenter;
import com.baliyaan.android.mvp.Interfaces.BaseMVPView;
import com.baliyaan.android.wordincontext.Components.Examples.Model.Example;

import java.util.List;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface ExamplesMVPContract {
    interface MVPView extends BaseMVPView<MVPPresenter> {
        void displayResult();
        void displayError();
    }

    interface MVPPresenter extends BaseMVPPresenter {
        void onQueryTextSubmit(final String query);
        List<Example> getExamples();
    }

    interface Navigator extends BaseMVPNavigator {
        String getQuery();
        Context getContext();
        void onTryAgain();
    }

    interface MVPPort extends BaseMVPPort {
        void onQueryTextSubmit(String query);
    }
}
