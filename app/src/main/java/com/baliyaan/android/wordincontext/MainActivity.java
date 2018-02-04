package com.baliyaan.android.wordincontext;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.baliyaan.android.library.ads.Interstitial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Interstitial _interstitial = null;
    private FirebaseAnalytics _firebaseAnalytics;
    private FirebaseAuth _firebaseAuth;
    private int ACTIVITY_REQ_CODE_VOICE_SEARCH = 1;
    private int SCREEN_HEIGHT = -1;
    private int SCREEN_WIDTH = -1;
    private FirebaseUser _user;
    private boolean _FLAG_FIRST_ONSTART = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0); //To prevent animation on returning from SearchActivity

        if(_FLAG_FIRST_ONSTART) {
            _FLAG_FIRST_ONSTART = false;
        /*Firebase*/
            // Obtain the FirebaseAnalytics instance.
            _firebaseAnalytics = FirebaseAnalytics.getInstance(this);
            // Obtain FirebaseAuth instance
            _firebaseAuth = FirebaseAuth.getInstance();
            _firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            _user = _firebaseAuth.getCurrentUser();
                            String name = _user.getDisplayName();
                        }
                    });

        /*
        * Display ads
        */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (null == _interstitial) {
                        _interstitial = new Interstitial(MyApplication.getAppContext(), getString(R.string.adId));
                        _interstitial.AllowOnlyAfter((long) 3.6e+5);// 6 minutes
                        _interstitial.AllowInDebug(false);
                        _interstitial.showEvery(1000000, true); // every 16.66 minutes
                    }
                }
            });

        /*Calculate Screen Size*/
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            SCREEN_HEIGHT = dm.heightPixels;
            SCREEN_WIDTH = dm.widthPixels;

        /*Back-ground image*/
            ImageView bgImage = (ImageView) findViewById(R.id.background_blur);
            if (null != bgImage) {
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.background_mage);
                bgImage.setImageBitmap(Bitmap.createBitmap(b));
            }

        /*SearchBox callback & size*/
            SearchView searchView = (SearchView) findViewById(R.id.search_view);
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Show the word definition activity*/
                    Intent intentSearch = new Intent(MyApplication.getAppContext(), SearchActivity.class);
                    intentSearch.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intentSearch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(intentSearch);
                    overridePendingTransition(0, 0);
                }
            });

        /*Navigation Drawer*/
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

        /*Navigation View*/
            ViewStub nav_stub = (ViewStub) findViewById(R.id.nav_view_stub);
        /*nav_stub.inflate();*/
            nav_stub.setVisibility(View.VISIBLE);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        /*Search by intent (if any)*/
            Intent intent = getIntent();
            onNewIntent(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        ClipData clipData = intent.getClipData();
        if (clipData == null)
            return;
        ClipData.Item item = clipData.getItemAt(0);
        if (item == null)
            return;
        String intentQuery = item.getText().toString();
        if (!(intentQuery.length() > 0))
            return;

            /*Show the word definition activity*/
        Intent intentWordDict = new Intent(this, WordDictActivity.class);
        intentWordDict.putExtra("query", intentQuery);
        this.startActivity(intentWordDict);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_REQ_CODE_VOICE_SEARCH && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String query = results.get(0);
            /*Toast.makeText(this,query,Toast.LENGTH_SHORT);*/
            Intent intentWordDict = new Intent(this, WordDictActivity.class);
            intentWordDict.putExtra("query", query);
            this.startActivity(intentWordDict);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (null != drawer) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_voice_search) {
            try {
                Intent intent = new Intent();
                intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(intent, ACTIVITY_REQ_CODE_VOICE_SEARCH);
            } catch (ActivityNotFoundException e) {
                Log.e(MainActivity.class.getName(), e.toString());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dictionary) {
            // Handle the dictionary action
        } else if (id == R.id.nav_recent) {

        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {
            final String appPackage = getPackageName();
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.putExtra(Intent.EXTRA_TEXT,
                        getString(R.string.action_share_pre)
                                + " " + getString(R.string.app_name) + " "
                                + getString(R.string.action_share_post)
                                + "https://play.google.com/store/apps/details?id=" + appPackage);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e(this.getClass().getName(), e.toString());
            }
        } else if (id == R.id.nav_rate) {
            final String appPackage = getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage)));
            } catch (ActivityNotFoundException e1) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackage)));
                } catch (ActivityNotFoundException e2) {
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


/*
Todo: Word List
Todo: Meaning and Examples
Todo: Could also gamify for better engagement and ease of learning
Todo: Example: https://play.google.com/store/apps/details?id=com.praveenj.gre
Todo: Translations
Todo: Antonyms-Synonyms
Todo: CamScanner
Todo: History
Todo: Save offline
Todo: MORE LANGUAGES - same app or more apps
Todo: Voice Search (from any screen, maybe with a keyword first like OK Google, short results preferably on overlay screen)
Todo: Text-to-Speech
Todo: More than 10 results
*/