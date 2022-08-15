package com.catapp.scanthecat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CatGameActivity extends MenuActivity {

    private SharedPreferences prefGame;
    private String gameStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_game);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewCatGame);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //ToDo nadenken over interstitial, op welk moment in game? Misschien altijd als er medicatie wordt gegeven ofzo
        //Todo 1e week kitten, 2e week normaal, 3e week oude kat

        //ToDo voeden, spelen, slapen, medicijnen, poep ruimen (evt later straffen voor slecht gedrag)
        //ToDo leeftijd, gewicht, honger, geluk

        /*
            Schrijf bij start spel de begindatum weg naar sharedpreferences
            Laat de leeftijd zien in jaren. 1 jaar = 1 dag
        */

        prefGame = getApplicationContext().getSharedPreferences("prefGame", 0);
        gameStartDate = prefGame.getString("gameStartDate", "");

        if (gameStartDate.equals("")) {
            startNewGame();
        }
    }

    public void startNewGame() {
        AlertDialog alertDialog = new AlertDialog.Builder(CatGameActivity.this).create();
        alertDialog.setTitle("New Game");
        alertDialog.setMessage("Start a new game");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!gameStartDate.equals("")) {
                            //clearen van alle sharedprefs want je gaat een nieuw game beginnen, dus alle variabelen van oude game mogen weg.
                            SharedPreferences.Editor editor = prefGame.edit();
                            editor.clear();
                            editor.apply();
                        }
                        //setten van de gameStartDate want je gaat een nieuwe game beginnen
                        SharedPreferences.Editor editor = prefGame.edit();
                        editor.putString("gameStartDate", getCurrentDate());
                        editor.apply();

                        dialog.dismiss();

                        /*ToDo van alles gaan tonen op het scherm
                            - Denk aan plaatje kitten
                            - Leeftijd
                            - Knoppen voor van alles en nog wat

                         */
                    }
                });
        alertDialog.show();
    }

    public String getCurrentDate() {

        DateTimeFormatter date = null;
        String currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = null;
            now = LocalDateTime.now();
            currentDate = date.format(now);
            System.out.println("CURRENTDATE: " + currentDate);

        }
        return currentDate;
    }
}
//https://tamagotchi.fandom.com/wiki/Tamagotchi_(1996_Pet)#:~:text=Five%20minutes%20after%20the%20clock,by%20the%20age%20of%206.