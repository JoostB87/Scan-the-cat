package com.catapp.scanthecat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayCatGeneralInfoActivity extends MenuActivity {

    private Cat cats;
    private CatDetail[] catDetail;
    private String myUrl = "https://api.api-ninjas.com/v1/animals?name=";
    private Button buttonGeneralInfo;
    private Button buttonDetails;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_general_info);

        buttonGeneralInfo = findViewById(R.id.buttonGeneralInfo);
        buttonDetails = findViewById(R.id.buttonDetails);
        ImageView imageCat = findViewById(R.id.imageCat);
        TextView txtCatName = findViewById(R.id.txtCatName);
        TextView txtCatOrigin = findViewById(R.id.txtCatOrigin);
        TextView txtCatLength = findViewById(R.id.txtCatLength);
        TextView txtCatWeight = findViewById(R.id.txtCatWeight);
        TextView txtCatLifeExpectancy = findViewById(R.id.txtCatLifeExpectancy);
        RatingBar ratingFamilyFriendly = findViewById(R.id.ratingFamilyFriendly);
        RatingBar ratingChildrenFriendly = findViewById(R.id.ratingChildrenFriendly);
        RatingBar ratingStrangerFriendly = findViewById(R.id.ratingStrangerFriendly);
        RatingBar ratingOtherPetsFriendly = findViewById(R.id.ratingOtherPetsFriendly);
        RatingBar ratingGeneralHealth = findViewById(R.id.ratingGeneralHealth);
        RatingBar ratingIntelligence = findViewById(R.id.ratingIntelligence);
        RatingBar ratingPlayfulness = findViewById(R.id.ratingPlayfulness);
        RatingBar ratingMeowing = findViewById(R.id.ratingMeowing);
        RatingBar ratingShedding = findViewById(R.id.ratingShedding);
        RatingBar ratingGrooming = findViewById(R.id.ratingGrooming);

        //Buttons are invisible at first. After async all for animals, when there is a result -> show buttons
        buttonGeneralInfo.setVisibility(View.GONE);
        buttonDetails.setVisibility(View.GONE);

        //ToDo doe een call naar animal endpoint. Als geen result is doe niks.
        //Als er een result is (waarbij het type is cat) -> laat de buttons zien (general info enabled = false, details enabled = true).
        //Bij on click ga je naar details toe, details nog maken.
        //Moeilijk met labels, checken wat er allemaal terug kan komen
        //Bij on click details ook interstitial add tonen
        //meegeven cats en catdetails in intent bundle (zie hieronder) - string catDetail

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cats = (Cat) extras.get("cats");
        }

        Glide.with(this)
                .asBitmap()
                .load(cats.getImage_link())
                .into(imageCat);

        //Plak deze achter de API call url en gooi daarna de titel weer leeg (anders problemen met heen en terugnavigeren), anders plakt ie meerdere titels achter elkaar
        myUrl = myUrl + cats.getName();

        // create object of GetResultsAsync class and execute it
        DisplayCatGeneralInfoActivity.GetResultsAsync getResultsAsync = new DisplayCatGeneralInfoActivity.GetResultsAsync();
        Thread thread = new Thread(getResultsAsync);
        thread.start();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adViewDetails);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (cats.getName() != null) { txtCatName.setText(cats.getName()); }
        if (cats.getOrigin() != null) { txtCatOrigin.setText(cats.getOrigin()); }
        if (cats.getLength() != null) { txtCatLength.setText(cats.getLength()); }
        if (cats.getMin_weight() != null) { txtCatWeight.setText(Math.round(cats.getMin_weight()) + " - " + Math.round(cats.getMax_weight())); }
        if (cats.getMin_life_expectancy() != null) { txtCatLifeExpectancy.setText(Math.round(cats.getMin_life_expectancy()) + " - " + Math.round(cats.getMax_life_expectancy())); }
        if (cats.getFamily_friendly() != null) { ratingFamilyFriendly.setRating(cats.getFamily_friendly()); }
        if (cats.getChildren_friendly() != null) { ratingChildrenFriendly.setRating(cats.getChildren_friendly()); }
        if (cats.getStranger_friendly() != null) { ratingStrangerFriendly.setRating(cats.getStranger_friendly()); }
        if (cats.getOther_pets_friendly() != null) { ratingOtherPetsFriendly.setRating(cats.getOther_pets_friendly()); }
        if (cats.getGeneral_health() != null) { ratingGeneralHealth.setRating(cats.getGeneral_health()); }
        if (cats.getIntelligence() != null) { ratingIntelligence.setRating(cats.getIntelligence()); }
        if (cats.getPlayfulness() != null) { ratingPlayfulness.setRating(cats.getPlayfulness()); }
        if (cats.getMeowing() != null) { ratingMeowing.setRating(cats.getMeowing()); }
        if (cats.getShedding() != null) { ratingShedding.setRating(cats.getShedding()); }
        if (cats.getGrooming() != null) { ratingGrooming.setRating(cats.getGrooming()); }

    }

    public class GetResultsAsync implements Runnable {

        @Override
        public void run() {

            // Fetch data from the API in the background.
            String result = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(myUrl);
                    //open a URL connection

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("X-Api-Key", "6P95jdveVV+eAo4s1ub3hw==N82UH1hbB5J1wkQw");

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();

                    }
                    // return the data to runOnUITrhead method

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            final String resultCatDetail = result;
            runOnUiThread(new Runnable() {
                public void run() {
                    // do onPostExecute stuff GUI
                    Gson gson = new Gson();
                    // Convert JSON File to Java Object
                    catDetail = gson.fromJson(resultCatDetail, CatDetail[].class);

                    //Resetten van de url omdat ie anders shit achter elkaar blijft plakken.
                    myUrl = "https://api.api-ninjas.com/v1/animals?name=";

                    AdRequest adRequest = new AdRequest.Builder().build();

                    InterstitialAd.load(DisplayCatGeneralInfoActivity.this,"ca-app-pub-4788000105337932/3780443581", adRequest,
                            new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    // The mInterstitialAd reference will be null until
                                    // an ad is loaded.
                                    mInterstitialAd = interstitialAd;

                                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Called when fullscreen content is dismissed.
                                            goToDetailsPage();
                                            Log.d("TAG", "The ad was dismissed.");
                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            // Called when fullscreen content failed to show.
                                            goToDetailsPage();
                                            Log.d("TAG", "The ad failed to show.");
                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            // Called when fullscreen content is shown.
                                            // Make sure to set your reference to null so you don't
                                            // show it a second time.
                                            mInterstitialAd = null;
                                            Log.d("TAG", "The ad was shown.");
                                        }
                                    });

                                    Log.i(TAG, "onAdLoaded");
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error
                                    Log.i(TAG, loadAdError.getMessage());
                                    mInterstitialAd = null;
                                }
                            });

                    if (catDetail.length != 0) {
                        if (catDetail[0].getTaxonomy().getGenus().equals("Felis")) {
                            buttonGeneralInfo.setVisibility(View.VISIBLE);
                            buttonGeneralInfo.setEnabled(false);
                            buttonDetails.setVisibility(View.VISIBLE);
                            buttonDetails.setEnabled(true);
                        }
                    }

                    buttonDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(DisplayCatGeneralInfoActivity.this);
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                                goToDetailsPage();
                            }
                        }
                    });
                }
            });
        }
    }

    public void goToDetailsPage() {
        Intent intentResult = new Intent(DisplayCatGeneralInfoActivity.this, DisplayCatDetailInfoActivity.class);
        intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentResult.putExtra("cats", cats);
        intentResult.putExtra("catDetail", catDetail[0]);
        getApplicationContext().startActivity(intentResult);
    }
}