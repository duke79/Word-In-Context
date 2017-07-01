package com.baliyaan.android.wordincontext.UI.Examples;

import com.baliyaan.android.wordincontext.Model.WordExample;

import java.util.List;

/**
 * Created by Pulkit Singh on 7/1/2017.
 */

public interface UIExamplesMVPContract {
    interface View{
        void displayResult();
        void displayError();
    }

    interface Presenter {
        void onQueryTextSubmit(final String query);
        List<WordExample> getExamples();
    }

    interface Navigator {

    }

    interface Port{
        void onQueryTextSubmit(String query);
    }
}
