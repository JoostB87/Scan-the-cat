package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayCatDetailInfoActivity extends MenuActivity {

    private Cat cats;
    private CatDetail catDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_detail_info);

        TextView txtCatName = findViewById(R.id.txtCatName);
        TextView txtCatKingdom = findViewById(R.id.txtCatKingdom);

        //Todo nog iets doen met de knoppen bovenin (ene grijs, andere niet)

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adViewDetails);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cats = (Cat) extras.get("cats");
            catDetail = (CatDetail) extras.get("catDetail");
        }

        if (catDetail.getName() != null) { txtCatName.setText(catDetail.getName()); }
        if (catDetail.getTaxonomy().getKingdom() != null) { txtCatKingdom.setText(catDetail.getTaxonomy().getKingdom());}
        //ToDo nog rest toevoegen
    }
}