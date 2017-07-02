package com.baliyaan.android.wordincontext.UI.Examples;

import com.baliyaan.android.wordincontext.Model.WordExample;
import com.baliyaan.android.wordincontext.UI.MVPBasePortInterface;
import com.baliyaan.android.wordincontext.UI.MVPBasePresenterInterface;
import com.baliyaan.android.wordincontext.UI.MVPBaseViewInterface;

import java.util.List;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UIExamplesMVPContract {
    interface View extends MVPBaseViewInterface {
        void displayResult();
        void displayError();
    }

    interface Presenter extends MVPBasePresenterInterface {
        void onQueryTextSubmit(final String query);
        List<WordExample> getExamples();
    }

    interface Navigator {

    }

    interface Port extends MVPBasePortInterface {
        void onQueryTextSubmit(String query);
    }
}
