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
        TextView txtCatLocations = findViewById(R.id.txtCatLocations);
        TextView txtCatDistinctiveFeature = findViewById(R.id.txtCatDistinctiveFeature);
        TextView txtCatOtherNames = findViewById(R.id.txtCatOtherNames);
        TextView txtCatTemperament = findViewById(R.id.txtCatTemperament);
        TextView txtCatTraining = findViewById(R.id.txtCatTraining);
        TextView txtCatDiet = findViewById(R.id.txtCatDiet);
        TextView txtCatAverageLitterSize = findViewById(R.id.txtCatAverageLitterSize);
        TextView txtCatType = findViewById(R.id.txtCatType);
        TextView txtCatCommonName = findViewById(R.id.txtCatCommonName);
        TextView txtCatSlogan = findViewById(R.id.txtCatSlogan);
        TextView txtCatGroup = findViewById(R.id.txtCatGroup);
        TextView txtCatColor = findViewById(R.id.txtCatColor);
        TextView txtCatSkinType = findViewById(R.id.txtCatSkinType);
        TextView txtCatLifespan = findViewById(R.id.txtCatLifespan);
        TextView txtCatWeight = findViewById(R.id.txtCatWeight);
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
        if (catDetail.getLocations() != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String Locations = String.join(",", catDetail.getLocations());
                txtCatLocations.setText(Locations);
            }
        }
        if (catDetail.getCharacteristics().getDistinctive_features() != null) { txtCatDistinctiveFeature.setText(catDetail.getCharacteristics().getDistinctive_features()); }
        if (catDetail.getCharacteristics().getOther_names() != null) { txtCatOtherNames.setText(catDetail.getCharacteristics().getOther_names()); }
        if (catDetail.getCharacteristics().getTemperament() != null) { txtCatTemperament.setText(catDetail.getCharacteristics().getTemperament()); }
        if (catDetail.getCharacteristics().getTraining() != null) { txtCatTraining.setText(catDetail.getCharacteristics().getTraining()); }
        if (catDetail.getCharacteristics().getDiet() != null) { txtCatDiet.setText(catDetail.getCharacteristics().getDiet()); }
        if (catDetail.getCharacteristics().getAverage_litter_size() != null) { txtCatAverageLitterSize.setText(catDetail.getCharacteristics().getAverage_litter_size()); }
        if (catDetail.getCharacteristics().getType() != null) { txtCatType.setText(catDetail.getCharacteristics().getType()); }
        if (catDetail.getCharacteristics().getCommon_name() != null) { txtCatCommonName.setText(catDetail.getCharacteristics().getCommon_name()); }
        if (catDetail.getCharacteristics().getSlogan() != null) {txtCatSlogan.setText(catDetail.getCharacteristics().getSlogan());}
        if (catDetail.getCharacteristics().getGroup() != null) {txtCatGroup.setText(catDetail.getCharacteristics().getGroup());}
        if (catDetail.getCharacteristics().getColor() != null) { txtCatColor.setText(catDetail.getCharacteristics().getColor()); }
        if (catDetail.getCharacteristics().getSkin_type() != null) {txtCatSkinType.setText(catDetail.getCharacteristics().getSkin_type());}
        if (catDetail.getCharacteristics().getLifespan() != null) { txtCatLifespan.setText(catDetail.getCharacteristics().getLifespan()); }
        if (catDetail.getCharacteristics().getWeight() != null) { txtCatWeight.setText(catDetail.getCharacteristics().getWeight()); }
    }
}