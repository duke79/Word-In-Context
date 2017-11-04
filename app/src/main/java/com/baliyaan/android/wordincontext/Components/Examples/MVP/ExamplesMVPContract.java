package com.baliyaan.android.wordincontext.Components.Examples.MVP;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.MVPInfra.MVPBasePortInterface;
import com.baliyaan.android.wordincontext.MVPInfra.MVPBasePresenterInterface;
import com.baliyaan.android.wordincontext.MVPInfra.MVPBaseViewInterface;

import java.util.List;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface ExamplesMVPContract {
    interface View extends MVPBaseViewInterface<Presenter> {
        void displayResult();
        void displayError();
    }

    interface Presenter extends MVPBasePresenterInterface {
        void onQueryTextSubmit(final String query);
        List<WordExample> getExamples();
    }

    interface Navigator {
        void onTryAgain();
    }

    interface Port extends MVPBasePortInterface {
        void onQueryTextSubmit(String query);
    }
}
