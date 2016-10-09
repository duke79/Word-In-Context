package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static com.baliyaan.android.wordincontext.Scraper.GetExamples;

public class MainActivity extends AppCompatActivity {

    List<WordExample> _examples = null;
    Context _context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context = this;

        ActionBar actionBar = getActionBar();
        if(null != actionBar)
        {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _examples = GetExamples("fly");
                } catch (IOException e) {
                    e.printStackTrace();

                    ((MainActivity)_context).runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(_context, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

        if(_examples != null) {
            int size = _examples.size();
        }
    }
}
