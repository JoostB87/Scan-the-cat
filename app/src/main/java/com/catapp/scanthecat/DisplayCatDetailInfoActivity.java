package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        TextView txtCatPhylum = findViewById(R.id.txtCatPhylum);
        TextView txtCatClass = findViewById(R.id.txtCatClass);
        TextView txtCatOrder = findViewById(R.id.txtCatOrder);
        TextView txtCatFamily = findViewById(R.id.txtCatFamily);
        TextView txtCatGenus = findViewById(R.id.txtCatGenus);
        TextView txtCatScientificName = findViewById(R.id.txtCatScientificName);
        Button buttonGeneralInfo = findViewById(R.id.buttonGeneralInfo);
        Button buttonDetails = findViewById(R.id.buttonDetails);

        buttonGeneralInfo.setVisibility(View.VISIBLE);
        buttonGeneralInfo.setEnabled(true);
        buttonDetails.setVisibility(View.VISIBLE);
        buttonDetails.setEnabled(false);

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

        buttonGeneralInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentResult = new Intent(DisplayCatDetailInfoActivity.this, DisplayCatGeneralInfoActivity.class);
                intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentResult.putExtra("cats", cats);
                getApplicationContext().startActivity(intentResult);
            }
        });

        if (catDetail.getName() != null) { txtCatName.setText(catDetail.getName()); }
        if (catDetail.getTaxonomy().getKingdom() != null) { txtCatKingdom.setText(catDetail.getTaxonomy().getKingdom());}
        if (catDetail.getTaxonomy().getPhylum() != null) { txtCatPhylum.setText(catDetail.getTaxonomy().getPhylum()); }
        if (catDetail.getTaxonomy().getClas() != null) { txtCatClass.setText(catDetail.getTaxonomy().getClas()); }
        if (catDetail.getTaxonomy().getOrder() != null) { txtCatOrder.setText(catDetail.getTaxonomy().getOrder()); }
        if (catDetail.getTaxonomy().getFamily() != null) { txtCatFamily.setText(catDetail.getTaxonomy().getFamily()); }
        if (catDetail.getTaxonomy().getGenus() != null) { txtCatGenus.setText(catDetail.getTaxonomy().getGenus()); }
        if (catDetail.getTaxonomy().getScientific_name() != null) { txtCatScientificName.setText(catDetail.getTaxonomy().getScientific_name()); }
        //ToDo nog rest toevoegen
    }
}