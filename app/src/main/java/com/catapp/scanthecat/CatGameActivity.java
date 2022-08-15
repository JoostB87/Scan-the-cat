package com.catapp.scanthecat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CatGameActivity extends MenuActivity {

    private SharedPreferences prefGame;
    private String gameStartDate;
    private ImageView imageViewCat;
    private TextView textViewValueAge;
    private ProgressBar progressBarValueWeight;
    private ImageView imageViewFoodButton;
    private ImageView imageViewLightButton;
    private ImageView imageViewGameButton;
    private ImageView imageViewMedicationButton;
    private ImageView imageViewBathroomButton;
    private Long age;
    private Integer weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_game);

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adViewCatGame);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        imageViewCat = findViewById(R.id.imageViewCat);
        textViewValueAge = findViewById(R.id.textViewValueAge);
        progressBarValueWeight = findViewById(R.id.progressBarValueWeight);
        imageViewFoodButton = findViewById(R.id.imageViewFoodButton);
        imageViewLightButton = findViewById(R.id.imageViewLightButton);
        imageViewGameButton = findViewById(R.id.imageViewGameButton);
        imageViewMedicationButton = findViewById(R.id.imageViewMedicationButton);
        imageViewBathroomButton = findViewById(R.id.imageViewBathroomButton);

        /*ToDo van alles gaan tonen op het scherm. Schermpje, met alle knoppen bijv.
        Tonen van plaatje kitten etc, dat doe je dan later. De invulling.
        - ImageView voor kat
        - ImageView voor poep (2) en ziek zijn teken (1)
        - Links knopjes voor: voedsel, licht, spelletje, medicijnen, badkamer
        - Nog open: discipline en attention (geen knop)
        - TextLabel en view voor leeftijd, gewicht (balkje?), honger, happy
        */

        //ToDo nadenken over interstitial, op welk moment in game? Misschien altijd als er medicatie wordt gegeven ofzo

        //ToDo voeden, spelen, slapen, medicijnen, poep ruimen (evt later straffen voor slecht gedrag)
        //ToDo leeftijd, gewicht, honger, geluk

        prefGame = getApplicationContext().getSharedPreferences("prefGame", 0);
        gameStartDate = prefGame.getString("gameStartDate", "");
        weight = prefGame.getInt("weight", 50);

        if (gameStartDate.equals("")) {
            startNewGame();
        } else {
            //ToDo vul game dingen in, leeftijd, plaatje kitten, gewicht, happy, poep etc.
            calculateAge();
            showAgeAndProperCatImage();
            showWeight();
        }
    }

    public void showWeight() {
        progressBarValueWeight.setProgress(weight);
        if (weight >= 40 && weight <=60) {
            //Green
            progressBarValueWeight.getIndeterminateDrawable().setColorFilter(Color.parseColor("#008000"),
                    PorterDuff.Mode.MULTIPLY);
        } else if ((weight > 60 && weight <= 80) || (weight < 40 && weight >= 20)) {
            //Orange
            progressBarValueWeight.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFA500"),
                    PorterDuff.Mode.MULTIPLY);
        } else {
            //Red
            progressBarValueWeight.getIndeterminateDrawable().setColorFilter(Color.parseColor("#EE4B2B"),
                    PorterDuff.Mode.MULTIPLY);
        }

    }

    public void showAgeAndProperCatImage() {
        textViewValueAge.setText(String.valueOf(age));

        if (age <= 7) {
            System.out.println("SHOW KITTEN");
            imageViewCat.setImageResource(R.mipmap.kitten_image);
        } else if (age > 7 && age <= 14) {
            System.out.println("SHOW MEDIUM CAT");
            imageViewCat.setImageResource(R.mipmap.medium_cat_image);
        } else {
            System.out.println("SHOW OLD CAT");
            imageViewCat.setImageResource(R.mipmap.old_cat_image);
        }
    }

    public Long calculateAge() {

        DateTimeFormatter dtf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate date1 = LocalDate.parse(gameStartDate, dtf);
            //ToDo hier kun je een beetje mee spelen om wat verschillen te zien
            LocalDate date2 = LocalDate.parse(getCurrentDate(), dtf);
            age = ChronoUnit.DAYS.between(date1, date2);
            System.out.println ("Days: " + age);
        }
        return age;
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
                        gameStartDate = getCurrentDate();

                        dialog.dismiss();

                        /*ToDo van alles gaan tonen op het scherm
                            - Denk aan plaatje kitten
                            - Leeftijd etc invullen
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