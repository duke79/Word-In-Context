package com.baliyaan.android.wordincontext.UI;

import android.os.Bundle;

/**
 * Created by Pulkit Singh on 7/2/2017.
 */

public interface MVPBasePortInterface {
    void onSaveState();
    void onResumeState();
    Bundle getState();
    void setState(Bundle state);
}
