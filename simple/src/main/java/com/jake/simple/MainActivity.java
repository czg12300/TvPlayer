package com.jake.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("主页");
        ListView listView = new ListView(this);
        setContentView(listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mAdapter.add("电视");
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        TvListActivity.start(MainActivity.this);
                        break;
                }
            }
        });
    }

}
