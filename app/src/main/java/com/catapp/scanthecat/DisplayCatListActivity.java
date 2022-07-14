package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class DisplayCatListActivity extends AppCompatActivity {

    private String afkomst;
    private TextView txtHeaderResults;
    private RecyclerView catResultRecView;
    private Cat[] cats = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_list);

        catResultRecView = findViewById(R.id.catResultRecView);
        txtHeaderResults = findViewById(R.id.txtHeaderResults);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cats = (Cat[]) extras.get("result");
            afkomst = extras.getString("afkomst");
        }

        CatResultListRecViewAdapter adapter = new CatResultListRecViewAdapter(cats);
        catResultRecView.setAdapter(adapter);
        catResultRecView.setLayoutManager(new GridLayoutManager(this, 2));

    }
}