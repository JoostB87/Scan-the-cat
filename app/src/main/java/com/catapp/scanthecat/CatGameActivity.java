package com.catapp.scanthecat;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CatGameActivity extends MenuActivity {

    private SharedPreferences prefGame;
    private String gameStartDateTime;
    private ImageView imageViewCat;
    private ImageView imageViewLightsOut;
    private TextView textViewValueAge;
    private ProgressBar progressBarValueWeight;
    private ImageView imageViewFoodButton;
    private ImageView imageViewLightButton;
    private ImageView imageViewGameButton;
    private ImageView imageViewMedicationButton;
    private ImageView imageViewBathroomButton;
    private RatingBar ratingHungry;
    private RatingBar ratingHappy;
    private Long age;
    private Integer weight;
    private Integer happy;
    private Integer hungry;
    private Boolean lightsOn;

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
        imageViewLightsOut = findViewById(R.id.imageViewLightsOut);
        textViewValueAge = findViewById(R.id.textViewValueAge);
        progressBarValueWeight = findViewById(R.id.progressBarValueWeight);
        ratingHungry = findViewById(R.id.ratingHungry);
        ratingHappy = findViewById(R.id.ratingHappy);
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
        /*
        Maaltijd:
            gezond: +10 voor weight
            snack: +20 voor weight, +10 voor happy
            teveel snacks = ziek?
            honger + 1 ster
            weiger eten bij volle honger?
        Spelen:
            weight: -10, maar kan niet onder de 50 komen
            happy: spel is altijd 5 rondes, als speler 3 of meer wint -> + 1 happy
            happy kan nooit meer worden dan 5
        Ziek:
            wil niet eten of spelen, moet eerst medicijnen krijgen (1 of 2)
        Badkamer:
            flusht alle poep weg
        Poep:
            te lang op het scherm -> ziek
            maximaal 3 stuks
            Honger -1 ster per poep
            Poept niet onder het slapen, maar poep van voor die tijd blijft wel liggen en telt door
            poep elke 3 a 4 uur?
         Licht:
            Doet het licht uit. (invisible zetten van imageViewLightsOut)
            wijzig de tint van de imageview van lichtknop (geel is aan, zwart is uit)
            sla op in sharedprefs of licht aan of uit is achtergelaten.
            Als licht niet uit is gedaan wordt kat op rare tijden wakker,
            als licht uit is wordt kat altijd rond dezelfde tijd wakker (ongeveer)
            Als licht niet uit is kan kat ziek worden of dood gaan zelfs
         */

        getFromSharedPreferences();

        if (gameStartDateTime.equals("")) {
            startNewGame();
        } else {
            gameExists();
        }

        imageViewLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lightsOn) {
                    //als de lampen aan waren gaan ze uit
                    lightsOn = false;
                } else {
                    //en anders gaan ze aan
                    lightsOn = true;
                }
                //setten de nieuwe waarde van lightsOn in sharedprefs zodat je dit kunt gaan gebruiken
                SharedPreferences.Editor editor = prefGame.edit();
                editor.putBoolean("lightsOn", lightsOn);
                editor.apply();

                //pas alles op het scherm aan volgens de nieuwe waarde
                showLightsBasedOnSharedPrefs();
            }
        });
    }

    public void getFromSharedPreferences() {
        prefGame = getApplicationContext().getSharedPreferences("prefGame", 0);
        gameStartDateTime = prefGame.getString("gameStartDateTime", "");
        weight = prefGame.getInt("weight", 50);
        hungry = prefGame.getInt("hungry", 5);
        happy = prefGame.getInt("happy", 5);
        lightsOn = prefGame.getBoolean("lightsOn", true);

        //ToDo hier toevoegen laatste datumtijd voeden bijv.
        //ToDo dan kun je op basis van verschil tussen die en currentdatetime bepalen hoeveel sterren er af moeten of hoeveel er gepoept is etc
    }

    public void gameExists() {
        //ToDo vul game dingen in, leeftijd, plaatje kitten, gewicht, happy, poep etc.
        calculateAge();
        showAgeAndProperCatImage();
        showWeight();
        showHungry();
        showHappy();
        sleeping();
        showLightsBasedOnSharedPrefs();
    }

    public void sleeping() {

    }

    public void showLightsBasedOnSharedPrefs() {
        //Huidige waarde ophalen uit sharedPrefs
        lightsOn = prefGame.getBoolean("lightsOn", true);

        if (lightsOn == true) {
            imageViewLightsOut.setVisibility(View.GONE);
            imageViewLightButton.setColorFilter(Color.parseColor("#FFEA00"),
                    PorterDuff.Mode.SRC_ATOP);
        } else {
            imageViewLightsOut.setVisibility(View.VISIBLE);
            imageViewLightButton.setColorFilter(Color.parseColor("#63666A"),
                    PorterDuff.Mode.SRC_ATOP);
        }

    }

    public void showHappy() {
        ratingHappy.setRating(happy);
        if (happy >= 4) {
            //Green
            ratingHappy.getProgressDrawable().setColorFilter(Color.parseColor("#008000"),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (happy.equals(2) || happy.equals(3)) {
            //Orange
            ratingHappy.getProgressDrawable().setColorFilter(Color.parseColor("#FFA500"),
                    PorterDuff.Mode.SRC_ATOP);
        } else {
            //Red
            ratingHappy.getProgressDrawable().setColorFilter(Color.parseColor("#EE4B2B"),
                    PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void showHungry() {
        ratingHungry.setRating(hungry);
        if (hungry >= 4) {
            //Green
            ratingHungry.getProgressDrawable().setColorFilter(Color.parseColor("#008000"),
                    PorterDuff.Mode.SRC_ATOP);
        } else if (hungry.equals(2) || hungry.equals(3)) {
            //Orange
            ratingHungry.getProgressDrawable().setColorFilter(Color.parseColor("#FFA500"),
                    PorterDuff.Mode.SRC_ATOP);
        } else {
            //Red
            ratingHungry.getProgressDrawable().setColorFilter(Color.parseColor("#EE4B2B"),
                    PorterDuff.Mode.SRC_ATOP);
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
            dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            LocalDate date1 = LocalDate.parse(gameStartDateTime, dtf);
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
                        if (!gameStartDateTime.equals("")) {
                            //clearen van alle sharedprefs want je gaat een nieuw game beginnen, dus alle variabelen van oude game mogen weg.
                            SharedPreferences.Editor editor = prefGame.edit();
                            editor.clear();
                            editor.apply();
                        }
                        //setten van de gameStartDate want je gaat een nieuwe game beginnen
                        SharedPreferences.Editor editor = prefGame.edit();
                        editor.putString("gameStartDateTime", getCurrentDate());
                        editor.apply();
                        gameStartDateTime = getCurrentDate();

                        dialog.dismiss();

                        //Hier gaat 'ie ook verder wanneer er al een bestaande game is
                        gameExists();
                    }
                });
        alertDialog.show();
    }

    public String getCurrentDate() {

        DateTimeFormatter date = null;
        String currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime now = null;
            now = LocalDateTime.now();
            currentDate = date.format(now);
            System.out.println("CURRENTDATE: " + currentDate);

        }
        return currentDate;
    }
}
//https://tamagotchi.fandom.com/wiki/Tamagotchi_(1996_Pet)#:~:text=Five%20minutes%20after%20the%20clock,by%20the%20age%20of%206.