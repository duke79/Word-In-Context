package com.baliyaan.android.wordincontext.MVPInfra;

import android.os.Bundle;

/**
 * Created by Pulkit Singh on 7/2/2017.
 */

public interface MVPBasePresenterInterface {
    void onSaveState();
    void onResumeState();
    Bundle getState();
    void setState(Bundle state);
}
