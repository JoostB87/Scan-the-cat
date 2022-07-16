package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class DisplayCatListActivity extends MenuActivity {

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
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adViewResults);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        CatResultListRecViewAdapter adapter = new CatResultListRecViewAdapter(cats);
        catResultRecView.setAdapter(adapter);
        catResultRecView.setLayoutManager(new GridLayoutManager(this, 2));

    }
}