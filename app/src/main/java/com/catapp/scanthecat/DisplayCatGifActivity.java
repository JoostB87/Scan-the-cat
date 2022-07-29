package com.catapp.scanthecat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

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

    private CatGifs[] catGif;
    private ImageView catGifImageview;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = null;
    private Integer advertentieTeller = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_gif);

        catGifImageview = findViewById(R.id.catGifImageview);
        Button btnNextGif = findViewById(R.id.btnNextGif);

        // create object of GetResultsAsync class and execute it
        DisplayCatGifActivity.GetResultsAsync getResultsAsync = new DisplayCatGifActivity.GetResultsAsync();
        Thread thread = new Thread(getResultsAsync);
        thread.start();

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewCatGifBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnNextGif.setOnClickListener(view -> {
            if (advertentieTeller == 10) {
                advertentieTeller = 0;

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
                                        requestNewGif();
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        requestNewGif();
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
                                requestNewGif();
                                // Handle the error
                                Log.i(TAG, loadAdError.getMessage());
                                mInterstitialAd = null;
                            }
                        });
            } else {
                requestNewGif();
            }
        });
    }

    public void requestNewGif() {
        GetResultsAsync getResultsAsync1 = new GetResultsAsync();
        Thread thread1 = new Thread(getResultsAsync1);
        thread1.start();
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
                    String catGifUrl = "https://api.thecatapi.com/v1/images/search?mime_types=gif";
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

                Glide.with(DisplayCatGifActivity.this)
                        .asGif()
                        .load(catGif[0].getUrl())
                        .into(catGifImageview);

                advertentieTeller ++;
                System.out.println("De teller staat nu op: " + advertentieTeller);
            });
        }
    }
}