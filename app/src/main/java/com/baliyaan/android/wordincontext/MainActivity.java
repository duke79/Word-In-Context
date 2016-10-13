package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static com.baliyaan.android.wordincontext.Scraper.GetExamples;

public class MainActivity extends AppCompatActivity {

    List<WordExample> _examples = null;
    Context _context = null;
    ListView _listView ;

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

        _listView = (ListView)findViewById(R.id.view_examples);
        ((SingleScrollListView)_listView).setSingleScroll(true);
        _listView.setRotation(-90);

        ViewTreeObserver viewTreeObserver = _listView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int go =1;
                Adapter adapter = _listView.getAdapter();
                if(null != adapter) {
                    int listSize = adapter.getCount();
                    for (int i = 0; i < listSize; i++) {
                        View view = _listView.getChildAt(i);
                        if (null != view) {
                            view.setRotation(90);
                            view.getLayoutParams().height = 500;
                            view.getLayoutParams().width = 500;
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        StartScraping();

    }

    private void StartScraping() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _examples = GetExamples("fly");

                    Handler handler = new Handler(_context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            PopulateList();
                        }
                    });

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
    }

    private void PopulateList()
    {
        if(_examples != null) {
            String[] values = new String[_examples.size()];
            int i=0;
            for(WordExample example : _examples)
            {
                values[i] = example.get_content();
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,values);
            _listView.setAdapter(adapter);

            _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int itemPosition = i;
                    String itemValue = (String)_listView.getItemAtPosition(itemPosition);
                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                            .show();
                }
            });

            int size = _examples.size();
        }
    }
}
