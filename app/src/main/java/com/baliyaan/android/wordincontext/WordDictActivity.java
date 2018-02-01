package com.baliyaan.android.wordincontext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.baliyaan.android.mvp.Adapters.MVPNavigatorAdapter;
import com.baliyaan.android.wordincontext.Components.Definition.MVP.Contract;
import com.baliyaan.android.wordincontext.Components.Examples.MVP.ViewPort;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

/**
 * Created by Pulkit Singh on 1/10/2018.
 */

public class WordDictActivity
        extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Navigator _navigator;
    private FirebaseAnalytics _firebaseAnalytics;
    private int ACTIVITY_REQ_CODE_TTS_CHECK = 0;
    private TextToSpeech _ttsEngine = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Firebase */
        // Obtain the FirebaseAnalytics instance.
        _firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*Set content view*/
        setContentView(R.layout.activity_word_dict);

        /*Initialize TTS Engine*/
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, ACTIVITY_REQ_CODE_TTS_CHECK);

        /* Fab Button Callback */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tts_btn);
        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toolbar toolbar = (Toolbar) findViewById(R.id.word_dict_toolbar);
                    String title = (String) toolbar.getTitle();
                    if (title != "") {

                        //TODO: Voice chooser in settings?
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Set<Voice> voices = _ttsEngine.getVoices();
                            int toggle = 1;
                            for (Voice voice : voices) {
                                Locale locale = voice.getLocale();
                                if (Objects.equals(Locale.US.getCountry(), locale.getCountry()) && toggle < 3) {
                                    _ttsEngine.setVoice(voice);
                                    toggle++;
                                }
                            }
                        }*/

                        _ttsEngine.setSpeechRate(0.5f);
                        Bundle params = new Bundle();
                        params.putString(TextToSpeech.Engine.KEY_PARAM_PAN, "-1");
                        _ttsEngine.speak(title, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
        }

        /*Initialize MVP Navigator*/
        _navigator = new Navigator(this);

        /*
        * Search by intent (if any)
        */
        Intent intent = getIntent();
        onNewIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*Handle search intent - exmaples & definition views*/
        String query = intent.getStringExtra("query");
        _navigator.onSearchIntent(query);
        /*Log query in firebase*/
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
        if (null != _firebaseAnalytics)
            _firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _navigator.onSaveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        _navigator.onRestoreState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQ_CODE_TTS_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                _ttsEngine = new TextToSpeech(this, this);
            } else {
                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(intent);
            }
        }
    }

    /* TextToSpeech.OnInitListener implementations*/
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (_ttsEngine.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                _ttsEngine.setLanguage(Locale.US);
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, R.string.tts_fail, Toast.LENGTH_LONG).show();
        }
    }

    public class Navigator
            extends
            MVPNavigatorAdapter
            implements
            com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Navigator,
            com.baliyaan.android.wordincontext.Components.Definition.MVP.Contract.Navigator {

        /*
        * Member Variables
         */
        //MVP-Ports
        private com.baliyaan.android.wordincontext.Components.Examples.MVP.Contract.Port _examplesPort = null;
        private Contract.Port _definitionPort = null;
        //Local-Use-Vars
        private String _query = "";

        /*
        * Initialization
         */
        Navigator(Activity activity) {
            super(activity);
            View examplesView = findViewById(R.id.list_view);
            _examplesPort = new ViewPort(this, examplesView);

            View defView = findViewById(R.id.definitions_list);
            _definitionPort = new com.baliyaan.android.wordincontext.Components.Definition.MVP.ViewPort(this, defView);
        }

        /*
        * Methods to be called from parent activity
         */
        void onSaveState(Bundle state) {
            state.putString("query", _query);
            _examplesPort.onSaveState(state);
            _definitionPort.onSaveState(state);
        }

        void onRestoreState(Bundle state) {
            _query = (String) state.get("query");
            _examplesPort.onRestoreState(state);
            _definitionPort.onRestoreState(state);
        }

        /**
         * Simulates search operation.
         * To be called from parent activity, specifically, to handle search intent from other activities.
         *
         * @param query word string to search
         */
        void onSearchIntent(String query) {
            _query = query;
            _examplesPort.onQueryTextSubmit(query);
            _definitionPort.onQueryTextSubmit(query);

            /*Update toolbar*/
            String firstChar = query.substring(0, 1);
            firstChar = firstChar.toUpperCase();
            query = firstChar + query.substring(1, query.length()).toLowerCase();

            Toolbar toolbar = (Toolbar) findViewById(R.id.word_dict_toolbar);
            if (null != toolbar)
                toolbar.setTitle(query);
        }

    /*
    * Navigator implementations
    */

        @Override //Contract.Navigator
        public String getQuery() {
            return _query;
        }

        /**
         * Re-load the contents for last query.
         * Mainly in cases when content couldn't be loaded due to some reason (NoInternet etc.).
         */
        @Override //Contract.Navigator
        public void onTryAgain() {
            onSearchIntent(_query);
        }
    }
}
