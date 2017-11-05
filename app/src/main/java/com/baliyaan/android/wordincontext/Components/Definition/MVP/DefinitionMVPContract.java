package com.baliyaan.android.wordincontext.Components.Definition.MVP;

import com.baliyaan.android.mvp.Interfaces.BaseMVPNavigator;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPort;
import com.baliyaan.android.mvp.Interfaces.BaseMVPPresenter;
import com.baliyaan.android.mvp.Interfaces.BaseMVPView;

/**
 * Created by Pulkit Singh on 11/5/2017.
 */

public interface DefinitionMVPContract {
    interface Presenter extends BaseMVPPresenter{
        void onQueryTextSubmit(final String query);
    }

    interface View extends BaseMVPView<Presenter>{
        void setDefinition(String definition);
    }

    interface Port extends BaseMVPPort{
        void onQueryTextSubmit(final String query);
    }

    interface Navigator extends BaseMVPNavigator{
    }
}
