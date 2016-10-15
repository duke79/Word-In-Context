package com.baliyaan.android.wordincontext;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static com.baliyaan.android.wordincontext.Scraper.GetExamples;

public class MainActivity extends AppCompatActivity {

    ArrayList<WordExample> _examples = new ArrayList<WordExample>();
    Context _context = null;
    RecyclerView _recyclerView;
    ExamplesAdapter _examplesAdapter;

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

        _recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        _examplesAdapter = new ExamplesAdapter(_examples);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(_context,LinearLayoutManager.HORIZONTAL,false);
        if(null != _recyclerView) {
            _recyclerView.setLayoutManager(mLayoutManager);
            _recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            _recyclerView.setAdapter(_examplesAdapter);
        }

        prepareExamplesList();

    }

    private void prepareExamplesList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetExamples("fly",_examples);
                    Handler handler = new Handler(_context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(_examplesAdapter != null) {
                                _examplesAdapter.notifyDataSetChanged();
                            }
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



    /*
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
    */
}
