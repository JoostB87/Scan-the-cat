package com.catapp.scanthecat;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;

public class CatFullListActivity extends MenuActivity {

    private final ArrayList<String> catsLimited = new ArrayList<>(
            Arrays.asList("Abyssinian",
                    "Aegean",
                    "American Bobtail",
                    "American Curl",
                    "American Shorthair",
                    "American Wirehair",
                    "Aphrodite Giant",
                    "Arabian Mau",
                    "Asian",
                    "Australian Mist",
                    "Balinese",
                    "Bambino",
                    "Bengal Cats",
                    "Birman",
                    "Bombay",
                    "Brazilian Shorthair",
                    "British Longhair",
                    "British Shorthai",
                    "Burmilla",
                    "California Spangled",
                    "Chantilly-Tiffany",
                    "Chartreux",
                    "Chausie",
                    "Chinese Li Hua",
                    "Colorpoint Shorthair",
                    "Cornish Rex",
                    "Cymric",
                    "Cyprus",
                    "Desert Lynx",
                    "Devon Rex",
                    "Donskoy",
                    "Egyptian Mau",
                    "European Burmese",
                    "European Shorthair",
                    "Exotic",
                    "Foldex",
                    "German Rex",
                    "Havana Brown",
                    "Highlander",
                    "Himalayan",
                    "Japanese Bobtail",
                    "Javanese",
                    "Khao Manee",
                    "Korat",
                    "Kurilian Bobtail",
                    "LaPerm",
                    "Lykoi",
                    "Maine Coon",
                    "Manx",
                    "Mekong Bobtail",
                    "Napoleon",
                    "Nebelung",
                    "Norwegian Forest",
                    "Ocicat",
                    "Oriental",
                    "Oriental Bicolor",
                    "Persian",
                    "Peterbald",
                    "Pixie-Bob",
                    "Ragamuffin",
                    "Ragdoll Cats",
                    "Russian Blue",
                    "Savannah",
                    "Scottish Fold",
                    "Selkirk Rex",
                    "Serengeti",
                    "Siamese Cat",
                    "Siberian",
                    "Singapura",
                    "Snowshoe",
                    "Sokoke",
                    "Somali",
                    "Sphynx",
                    "Thai",
                    "Thai Lilac",
                    "Tonkinese",
                    "Toyger",
                    "Turkish Angora",
                    "Turkish Van",
                    "Ukrainian Levkoy",
                    "York Chocolate"
            )
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_full_list);

        RecyclerView catFullListRecView = findViewById(R.id.catFullListRecView);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adViewResults);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        CatFullListRecViewAdapter adapter = new CatFullListRecViewAdapter(catsLimited);
        catFullListRecView.setAdapter(adapter);
        catFullListRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}