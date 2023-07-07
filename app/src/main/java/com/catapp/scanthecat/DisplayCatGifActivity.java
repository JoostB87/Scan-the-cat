package com.catapp.scanthecat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayCatGifActivity extends MenuActivity {

    private Context context;
    private CatGifs[] catGif;
    private ImageView catGifImageview;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = null;
    private Integer counter = 0;
    private Button btnNextGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_gif);

        catGifImageview = findViewById(R.id.catGifImageview);
        btnNextGif = findViewById(R.id.btnNextGif);
        btnNextGif.setEnabled(false);

        //Haal nieuwe gifs op en de 1e wordt direct getoond in de async task
        requestNewGifs();

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewCatGifBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Zorg dat er een interstitial klaarstaat
        loadInterstitial(adRequest);

        btnNextGif.setOnClickListener(view -> {

            if (counter >= 4) {
                counter = 0;

                loadInterstitial(adRequest);

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(DisplayCatGifActivity.this);
                } else {
                    requestNewGifs();
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            } else {
                counter++;
                loadGifOnPage();
            }
        });
    }

    public void requestNewGifs() {

        ConnectivityManager cm = (ConnectivityManager) DisplayCatGifActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            GetResultsAsync getResultsAsync = new GetResultsAsync();
            Thread thread = new Thread(getResultsAsync);
            thread.start();
        } else {
            Toast.makeText(DisplayCatGifActivity.this, "You have no active internet connection, try again later!", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadGifOnPage() {

        ConnectivityManager cm = (ConnectivityManager) DisplayCatGifActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            Glide.with(DisplayCatGifActivity.this)
                    .asGif()
                    .load(catGif[counter].getUrl())
                    .into(catGifImageview);
        } else {
            Toast.makeText(DisplayCatGifActivity.this, "You have no active internet connection, try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadInterstitial(AdRequest adRequest) {
        InterstitialAd.load(DisplayCatGifActivity.this,"ca-app-pub-4788000105337932/1134292462", adRequest,
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
                                requestNewGifs();
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                requestNewGifs();
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
                        requestNewGifs();
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
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
                    String catGifUrl = "https://api.thecatapi.com/v1/images/search?mime_types=gif&limit=5";
                    url = new URL(catGifUrl);
                    //open a URL connection

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("X-Api-Key", "c1995466-7245-4f45-bb99-3a6e2584c96e");

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

            final String resultCatImage = result;
            runOnUiThread(() -> {
                // do onPostExecute stuff GUI
                Gson gson = new Gson();
                // Convert JSON File to Java Object
                catGif = gson.fromJson(resultCatImage, CatGifs[].class);

                if (catGif != null) {
                    if (catGif.length > 0) {
                        //Toon de 1e gif op het scherm als ophalen is gelukt
                        Glide.with(getApplicationContext())
                                .asGif()
                                .load(catGif[0].getUrl())
                                .into(catGifImageview);

                        btnNextGif.setEnabled(true);
                    } else {
                        //Probeer opnieuw gifs op te halen, want blijkbaar ging het niet goed vorig keer
                        GetResultsAsync getResultsAsync = new GetResultsAsync();
                        Thread thread = new Thread(getResultsAsync);
                        thread.start();
                    }
                } else {
                    //Probeer opnieuw gifs op te halen, want blijkbaar ging het niet goed vorig keer
                    GetResultsAsync getResultsAsync = new GetResultsAsync();
                    Thread thread = new Thread(getResultsAsync);
                    thread.start();
                }
            });
        }
    }
}