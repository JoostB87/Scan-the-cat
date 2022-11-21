package com.catapp.scanthecat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Random;

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
    private String sleepingStartTime;
    private String sleepingEndTime;
    private ImageView imageViewZzzz;
    private Boolean sleepingBool;
    private String laatsteDatumTijdPoep;
    private Integer aantalPoepOpScherm;
    private ImageView imageViewPoep1;
    private ImageView imageViewPoep2;
    private ImageView imageViewPoep3;
    private ImageView imageViewPoep4;
    private ImageView imageViewMedication;
    private ImageView imageViewDead;
    private String isZiekDateTime;
    private ImageView foodChoiceHealthy;
    private ImageView foodChoiceSnack;
    private TextView textViewNummerGetoond;
    private ImageView imageViewHigher;
    private ImageView imageViewLower;
    private TextView higherLowerResult;
    private Button stopGameButton;
    private Integer aantalGespeeld;
    private Integer scoreGebruiker;
    private Integer nummerGetoond;
    private Integer nummerHogerLager;
    private Dialog gameDialog;
    private Button buttonStartNewGame;
    private Integer ageOfDeath;
    private String catIsDeadDate;
    private String lastLightsChangedDateTime;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = null;

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
        imageViewZzzz = findViewById(R.id.imageViewZzzz);
        imageViewPoep1 = findViewById(R.id.imageViewPoep1);
        imageViewPoep2 = findViewById(R.id.imageViewPoep2);
        imageViewPoep3 = findViewById(R.id.imageViewPoep3);
        imageViewPoep4 = findViewById(R.id.imageViewPoep4);
        imageViewMedication = findViewById(R.id.imageViewMedication);
        imageViewDead = findViewById(R.id.imageViewDead);
        buttonStartNewGame = findViewById(R.id.buttonStartNewGame);

        //ToDo geen kat ziek lengte tellen gedurende de nacht
        //ToDo geen poep meetellen gedurende de nacht

        getFromSharedPreferences();

        if (gameStartDateTime.equals("")) {
            startNewGame();
        } else {
            gameExists();
        }

        AdRequest interstitialAdRequest = new AdRequest.Builder().build();

        InterstitialAd.load(CatGameActivity.this,"ca-app-pub-4788000105337932/6703764016", interstitialAdRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                cleanupPoopIfPresentOrShowToast();
                                stuffThatNeedsRefreshing();
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                cleanupPoopIfPresentOrShowToast();
                                stuffThatNeedsRefreshing();
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

        imageViewLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lightsOn) {
                    //als de lampen aan waren gaan ze uit
                    setLightsOn(false);
                } else {
                    //en anders gaan ze aan
                    setLightsOn(true);
                }
                //pas alles op het scherm aan volgens de nieuwe waarde
                showLightsBasedOnSharedPrefs();
                stuffThatNeedsRefreshing();
            }
        });

        imageViewBathroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(CatGameActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    cleanupPoopIfPresentOrShowToast();
                }
            }
        });

        imageViewMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isZiekDateTime.equals("")) {
                    feedMedication();
                } else {
                    Toast.makeText(CatGameActivity.this, "Cat is not sick, no need for medication!", Toast.LENGTH_SHORT).show();
                }
                stuffThatNeedsRefreshing();
            }
        });

        imageViewFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isZiekDateTime = prefGame.getString("isZiekDateTime", "");
                if (isZiekDateTime.equals("")) {
                    final Dialog foodDialog = new Dialog(CatGameActivity.this, R.style.Dialog);
                    foodDialog.setContentView(R.layout.foodchoice);
                    foodDialog.setTitle("Feed your cat");
                    foodDialog.setCanceledOnTouchOutside(true);
                    foodDialog.show();

                    foodChoiceHealthy = foodDialog.findViewById(R.id.foodChoiceHealthy);
                    foodChoiceSnack = foodDialog.findViewById(R.id.foodChoiceSnack);

                    foodChoiceHealthy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            foodDialog.dismiss();
                            adjustWeightMeter(10);
                            adjustHungryMeter(1);
                        }
                    });

                    foodChoiceSnack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("SNACK");
                            foodDialog.dismiss();

                            //ToDo teveel snacks is ziek worden
                            adjustWeightMeter(20);
                            adjustHungryMeter(1);
                            adjustHappyMeter(1);
                        }
                    });
                } else {
                    Toast.makeText(CatGameActivity.this, "Cat is sick, does not feel like eating", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isZiekDateTime = prefGame.getString("isZiekDateTime", "");
                if (isZiekDateTime.equals("")) {
                    numbersGame();
                } else {
                    Toast.makeText(CatGameActivity.this, "Cat is sick, does not feel like playing", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonStartNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });
    }

    public void getFromSharedPreferences() {
        prefGame = getApplicationContext().getSharedPreferences("prefGame", 0);
        gameStartDateTime = prefGame.getString("gameStartDateTime", "");
        weight = prefGame.getInt("weight", 50);
        hungry = prefGame.getInt("hungry", 4);
        happy = prefGame.getInt("happy", 4);
        lastLightsChangedDateTime = prefGame.getString("lastLightsChangedDateTime", "01-01-1970 00:00");
        lightsOn = prefGame.getBoolean("lightsOn", true);
        sleepingStartTime = prefGame.getString("sleepingStartTime", "22:43");
        sleepingEndTime = prefGame.getString("sleepingEndTime", "07:34");
        laatsteDatumTijdPoep = prefGame.getString("laatsteDatumTijdPoep", "");
        aantalPoepOpScherm = prefGame.getInt("aantalPoepOpScherm", 0);
        ageOfDeath = prefGame.getInt("ageOfDeath", 22);
        isZiekDateTime = prefGame.getString("isZiekDateTime", "");
        catIsDeadDate = prefGame.getString("catIsDeadDate", "");
    }

    public void gameExists() {
        hideImageViews();
        calculateAge();
        showAgeAndProperCatImage();
        stuffThatNeedsRefreshing();
        showLightsBasedOnSharedPrefs();
    }

    public void stuffThatNeedsRefreshing() {
        sleeping();
        showPoopFunction();
        showCatSickBecauseOfPoop();
        checkCatIsSickLength();
        showWeightBasedOnSharedPrefs();
        showHungryBasedOnSharedPrefs();
        showHappyBasedOnSharedPrefs();
        if (Objects.equals(catIsDeadDate, "")) {
            checkAgeOfDeath();
        } else {
            catIsDead();
        }
    }

    public void cleanupPoopIfPresentOrShowToast() {
        if (aantalPoepOpScherm > 0) {
            cleanUpPoop();
        } else {
            Toast.makeText(CatGameActivity.this, "No catpoop to clean up!", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkAgeOfDeath() {
        //haal uit shared prefs
        ageOfDeath = prefGame.getInt("ageOfDeath", 22);

        if (String.valueOf(ageOfDeath).equals(String.valueOf(age))) {
            catIsDead();
        }
    }

    public void numbersGame() {

        gameDialog = new Dialog(CatGameActivity.this, R.style.Dialog);
        gameDialog.setContentView(R.layout.gamehigherlower);
        gameDialog.setTitle("Gametime!");
        gameDialog.show();

        textViewNummerGetoond = gameDialog.findViewById(R.id.textViewNummerGetoond);
        imageViewHigher = gameDialog.findViewById(R.id.imageViewHigher);
        imageViewLower = gameDialog.findViewById(R.id.imageViewLower);
        higherLowerResult = gameDialog.findViewById(R.id.higherLowerResult);
        stopGameButton = gameDialog.findViewById(R.id.stopGameButton);

        Random rn = new Random();
        scoreGebruiker = 0;
        nummerGetoond = 0;
        nummerHogerLager = 0;
        aantalGespeeld = 0;

        nummerGetoond = rn.nextInt(10) + 1;
        nummerHogerLager = rn.nextInt(10) + 1;

        textViewNummerGetoond.setText(String.valueOf(nummerGetoond));

        imageViewHigher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nummerHogerLager > nummerGetoond) {
                    scoreGebruiker++;
                    higherLowerResult.setText("You won!");
                    newRoundAndCheckForWin();
                } else if (nummerHogerLager < nummerGetoond){
                    scoreGebruiker--;
                    higherLowerResult.setText("You lost!");
                    newRoundAndCheckForWin();
                } else {
                    newRoundAndCheckForWin();
                }
            }
        });

        imageViewLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nummerHogerLager < nummerGetoond) {
                    scoreGebruiker++;
                    higherLowerResult.setText("You won!");
                    newRoundAndCheckForWin();
                } else if (nummerHogerLager > nummerGetoond){
                    scoreGebruiker--;
                    higherLowerResult.setText("You lost!");
                    newRoundAndCheckForWin();
                } else {
                    higherLowerResult.setText("It's a draw!");
                    newRoundAndCheckForWin();
                }
            }
        });

        stopGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameDialog.dismiss();
            }
        });
    }

    private void newRoundAndCheckForWin() {
        aantalGespeeld++;
        nummerGetoond = nummerHogerLager;
        textViewNummerGetoond.setText(String.valueOf(nummerGetoond));
        Random rn = new Random();
        nummerHogerLager = rn.nextInt(10) + 1;

        if (aantalGespeeld == 5) {
            adjustWeightMeter(-10);
            String textToast = "Gametime is over, you did not win. Try Again!";
            if(scoreGebruiker >= 3) {
                adjustHappyMeter(1);
                textToast = "Gametime is over, you won. Cat is happy!";
            }
            gameDialog.dismiss();
            Toast.makeText(CatGameActivity.this, textToast, Toast.LENGTH_LONG).show();
        }
    }

    public void cleanUpPoop() {
        aantalPoepOpScherm = 0;
        //setten van de aantalPoep in sharedprefs
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putInt("aantalPoepOpScherm", 0);
        editor.apply();

        //zet alle imageviews van poep op niet visible
        imageViewPoep1.setVisibility(View.GONE);
        imageViewPoep2.setVisibility(View.GONE);
        imageViewPoep3.setVisibility(View.GONE);
        imageViewPoep4.setVisibility(View.GONE);
        imageViewBathroomButton.setColorFilter(Color.parseColor("#63666A"), PorterDuff.Mode.SRC_ATOP);
    }

    public void adjustWeightMeter(Integer aanpassing) {
        //haal uit shared prefs weight score
        weight = prefGame.getInt("weight", 50);

        Integer nieuweWeightScore = weight + aanpassing;

        if (nieuweWeightScore <= 20) {
            nieuweWeightScore = 20;
            //zet score terug in shared prefs en doe update
            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("weight", nieuweWeightScore);
            editor.apply();

            showWeightBasedOnSharedPrefs();
            // Bij zo'n laag gewicht is poesje minder blij
            adjustHappyMeter(-1);
        } else if (nieuweWeightScore >=80 && nieuweWeightScore <= 100) {

            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("weight", nieuweWeightScore);
            editor.apply();

            adjustHappyMeter(-1);
            showWeightBasedOnSharedPrefs();

        } else if (nieuweWeightScore > 100) {
            catIsDead();
        } else {
            //zet score terug in shared prefs en doe update
            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("weight", nieuweWeightScore);
            editor.apply();

            showWeightBasedOnSharedPrefs();
        }
    }

    public void adjustHappyMeter(Integer aanpassing) {
        //haal uit shared prefs happy score
        happy = prefGame.getInt("happy", 4);

        Integer nieuweHappyScore = happy + aanpassing;

        if (nieuweHappyScore <= 0) {
            catIsDead();
        } else if (nieuweHappyScore > 5) {
            nieuweHappyScore = 5;
            //zet score terug in shared prefs en doe update meters
            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("happy", nieuweHappyScore);
            editor.apply();

            showHappyBasedOnSharedPrefs();
        } else {
            //zet score terug in shared prefs en doe update meters
            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("happy", nieuweHappyScore);
            editor.apply();

            showHappyBasedOnSharedPrefs();
        }
    }

    public void adjustHungryMeter(Integer aanpassing) {
        //haal uit shared prefs hungry score
        hungry = prefGame.getInt("hungry", 4);

        Integer nieuweHungryScore = hungry + aanpassing;

        if (nieuweHungryScore <= 0) {
            catIsDead();
        } else if (nieuweHungryScore > 5) {
            nieuweHungryScore = 5;
            //zet score terug in shared prefs en doe update meters
            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("hungry", nieuweHungryScore);
            editor.apply();

            showHungryBasedOnSharedPrefs();
        } else {
            //zet score terug in shared prefs en doe update meters
            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("hungry", nieuweHungryScore);
            editor.apply();

            showHungryBasedOnSharedPrefs();
        }
    }

    public Integer generateRandomNumber(Integer upperbound) {
        Random rand = new Random(); //instance of random class
        //generate random values from 0-upperbound
        int randomNumber = rand.nextInt(upperbound);
        return randomNumber;
    }

    public void showCatSickBecauseOfPoop() {
        //haal aantalPoep uit shared prefs
        aantalPoepOpScherm = prefGame.getInt("aantalPoepOpScherm", 0);

        if (aantalPoepOpScherm == 1) {
            if (generateRandomNumber(10).equals(5)) {
                setCatIsSick();
            }
        } else if (aantalPoepOpScherm == 2) {
            if (generateRandomNumber(5).equals(3)) {
                setCatIsSick();
            }
        } else if (aantalPoepOpScherm == 3) {
            if (generateRandomNumber(3).equals(1)) {
                setCatIsSick();
            }
        } else if (aantalPoepOpScherm == 4) {
            if (generateRandomNumber(2).equals(1)) {
                setCatIsSick();
            }
        }
    }

    public void setCatIsSick() {
        //setten van de isZiekDateTime in sharedprefs
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putString("isZiekDateTime", getCurrentDateTime());
        editor.apply();

        imageViewMedication.setVisibility(View.VISIBLE);
        imageViewMedicationButton.setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP);

        //haal 1 van de happymeter af, want ziek
        adjustHappyMeter(-1);
    }

    public void checkCatIsSickLength() {
        //haal uit shared prefs
        isZiekDateTime = prefGame.getString("isZiekDateTime", "");

        if (!isZiekDateTime.equals("")) {
            DateTimeFormatter dtf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                LocalDateTime ziekSinds = LocalDateTime.parse(isZiekDateTime, dtf);
                LocalDateTime nu = LocalDateTime.parse(getCurrentDateTime(), dtf);
                //ToDo if ziekSinds is tussen slaapstart en slaapeindtijd en nu is tussen slaapstart en slaapeindtijd, doe dan hoursbetween -8
                //ToDo wat als zieksinds is voor slaapstart of na slaapeind
                Long duurZiekte = ChronoUnit.HOURS.between(ziekSinds, nu);
                if(duurZiekte > 8) {
                    catIsDead();
                }
            }
        }
    }

    public void feedMedication() {
        //setten van de isZiekDateTime in sharedprefs
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putString("isZiekDateTime", "");
        editor.apply();

        isZiekDateTime = "";

        imageViewMedication.setVisibility(View.GONE);
        imageViewMedicationButton.setColorFilter(Color.parseColor("#63666A"), PorterDuff.Mode.SRC_ATOP);

        stuffThatNeedsRefreshing();
    }

    public void catIsDead() {
        imageViewCat.setVisibility(View.GONE);
        imageViewPoep1.setVisibility(View.GONE);
        imageViewPoep2.setVisibility(View.GONE);
        imageViewPoep3.setVisibility(View.GONE);
        imageViewPoep4.setVisibility(View.GONE);
        imageViewLightsOut.setVisibility(View.GONE);
        imageViewMedication.setVisibility(View.GONE);
        imageViewFoodButton.setVisibility(View.GONE);
        imageViewLightButton.setVisibility(View.GONE);
        imageViewGameButton.setVisibility(View.GONE);
        imageViewMedicationButton.setVisibility(View.GONE);
        imageViewBathroomButton.setVisibility(View.GONE);
        imageViewDead.setVisibility(View.VISIBLE);
        buttonStartNewGame.setVisibility(View.VISIBLE);

        catIsDeadDate = getCurrentDateTime();
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putString("catIsDeadDate", getCurrentDateTime());
        editor.apply();
    }

    public void showPoopFunction() {
        System.out.println("laatsteDatumTijdVanPoep: " + laatsteDatumTijdPoep);
        System.out.println("currentDateTime: " + getCurrentDateTime());

        //Huidige waarde ophalen uit sharedPrefs
        aantalPoepOpScherm = prefGame.getInt("aantalPoepOpScherm", 0);
        laatsteDatumTijdPoep = prefGame.getString("laatsteDatumTijdPoep", "");

        if (laatsteDatumTijdPoep.equals("")) {
            aantalPoepOpScherm = 1;

            SharedPreferences.Editor editor = prefGame.edit();
            editor.putInt("aantalPoepOpScherm", 1);
            editor.putString("laatsteDatumTijdPoep", getCurrentDateTime());
            editor.apply();

            setPoepOpScherm(1);

        } else {
            DateTimeFormatter dtf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                LocalDateTime laatstePoep = LocalDateTime.parse(laatsteDatumTijdPoep, dtf);
                LocalDateTime nu = LocalDateTime.parse(getCurrentDateTime(), dtf);
            /*
            //ToDo hier doen we nog even niks mee. Niet poepen tijdens slaap komt nog.
            //ToDo als getcurrentdate ligt na currentdate withhour 23, dan gebruik currentdate withhour 23
            LocalDateTime slaapStartTijd = nu.withHour(22).withMinute(30);
            LocalDateTime slaapEindTijd = nu.withHour(7).withMinute(30);
            if (laatstePoep.isBefore(nu)) {
            }
             */
                Long verschilLaatstePoepEnNu = ChronoUnit.HOURS.between(laatstePoep, nu);
                Integer aantalNieuwePoep = (int) (verschilLaatstePoepEnNu / 3);

                Integer totaalPoep = aantalPoepOpScherm + aantalNieuwePoep;

                if (totaalPoep > 4) {
                    //dan ben je dood, hide alles op het scherm en toon de grafheuvel
                    catIsDead();
                } else {
                    setPoepOpScherm(totaalPoep);

                    //setten van de aantalPoep in sharedprefs
                    SharedPreferences.Editor editor = prefGame.edit();
                    editor.putInt("aantalPoepOpScherm", totaalPoep);
                    if (aantalNieuwePoep > 0) {
                        editor.putString("laatsteDatumTijdPoep", getCurrentDateTime());
                    }
                    editor.apply();
                }
                adjustHappyMeter(-aantalNieuwePoep);
                adjustHungryMeter(-aantalNieuwePoep);
            }
        }
    }

    public void setPoepOpScherm(Integer totaalPoep) {
        if (totaalPoep == 0) {
            imageViewPoep1.setVisibility(View.GONE);
            imageViewPoep2.setVisibility(View.GONE);
            imageViewPoep3.setVisibility(View.GONE);
            imageViewPoep4.setVisibility(View.GONE);
        } else if (totaalPoep == 1) {
            imageViewPoep1.setVisibility(View.VISIBLE);
            imageViewBathroomButton.setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP);
        } else if (totaalPoep == 2) {
            imageViewPoep1.setVisibility(View.VISIBLE);
            imageViewPoep2.setVisibility(View.VISIBLE);
            imageViewBathroomButton.setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP);
        } else if (totaalPoep == 3) {
            imageViewPoep1.setVisibility(View.VISIBLE);
            imageViewPoep2.setVisibility(View.VISIBLE);
            imageViewPoep3.setVisibility(View.VISIBLE);
            imageViewBathroomButton.setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP);
        } else if (totaalPoep == 4) {
            imageViewPoep1.setVisibility(View.VISIBLE);
            imageViewPoep2.setVisibility(View.VISIBLE);
            imageViewPoep3.setVisibility(View.VISIBLE);
            imageViewPoep4.setVisibility(View.VISIBLE);
            imageViewBathroomButton.setColorFilter(Color.parseColor("#FFA500"), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void setSleepingStartTime() {
        Random r = new Random();
        int low = 30;
        int high = 60;
        int randomSleepingMinutes = r.nextInt(high-low) + low;
        sleepingStartTime = "22:" + randomSleepingMinutes;

        //setten van de sleepingStartTime, eenmalig bij start game
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putString("sleepingStartTime", sleepingStartTime);
        editor.apply();
    }

    public void setSleepingEndTime() {
        Random r = new Random();
        int low = 30;
        int high = 60;
        int randomSleepingMinutes = r.nextInt(high-low) + low;
        sleepingEndTime = "07:" + randomSleepingMinutes;

        //setten van de sleepingEndTime, eenmalig bij start game
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putString("sleepingEndTime", sleepingEndTime);
        editor.apply();
    }

    public Boolean sleeping() {
        System.out.println("SleepingStartTime: " + sleepingStartTime);
        System.out.println("SleepingEndTime: " + sleepingEndTime);
        DateTimeFormatter dtf = null;
        DateTimeFormatter date = null;
        lastLightsChangedDateTime = prefGame.getString("lastLightsChangedDateTime", "01-01-1970 00:00");
        lightsOn = prefGame.getBoolean("lightsOn", true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("HH:mm");
            date = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalTime starttijdSlapen = LocalTime.parse(sleepingStartTime, dtf);
            LocalTime eindtijdSlapen = LocalTime.parse(sleepingEndTime, dtf);
            LocalTime currentTime = LocalTime.parse(getCurrentTime(), dtf);
            //convert lastLightsChangedDateTime string naar DateTimeFormat
            LocalDateTime lastLightsChanged = LocalDateTime.parse(lastLightsChangedDateTime, date);
            //convert van alles samen tot de huidige datum met de slaapeindtijd
            LocalDateTime nu = LocalDateTime.parse(getCurrentDateTime(), date);
            int sleepEndTimeMinutes = Integer.parseInt(sleepingEndTime.substring(sleepingEndTime.length() - 2));
            int sleepStartTimeMinutes = Integer.parseInt(sleepingStartTime.substring((sleepingStartTime.length() -2)));
            int numberOfDay = Integer.parseInt(getCurrentDateTime().substring(0,2));
            int yesterdayNumberOfDay = numberOfDay-1;
            LocalDateTime slaapEindDatumTijd = nu.withHour(7).withMinute(sleepEndTimeMinutes);
            LocalDateTime slaapStartDatumTijd = nu.withDayOfMonth(yesterdayNumberOfDay).withHour(22).withMinute(sleepStartTimeMinutes);
            if (currentTime.isBefore(starttijdSlapen) && eindtijdSlapen.isBefore(currentTime)) {
                imageViewZzzz.setVisibility(View.GONE);
                sleepingBool = false;
                if (lastLightsChanged.isAfter(LocalDateTime.of(1971, 1, 1, 12, 00)) && lastLightsChanged.isBefore(slaapEindDatumTijd) && lightsOn == false) {
                    //verlichting aanzetten wanneer je inlogt en het is dag (1x omdat daarna lastlightschangeddatetime weer wordt geupdatet)
                    setLightsOn(true);
                } else if (lastLightsChanged.isAfter(LocalDateTime.of(1971, 1, 1, 12, 00)) && lastLightsChanged.isBefore(slaapStartDatumTijd) && lightsOn == true) {
                    //licht is nooit uitgezet voor deze nacht, dus happy -1
                    adjustHappyMeter(-1);
                    setLightsOn(true);
                } else if (lastLightsChanged.isBefore(LocalDateTime.of(1971, 1, 1, 12, 00)) && age >= 1) {
                    //licht is nog nooit uitgezet, dus happy -1
                    adjustHappyMeter(-1);
                    setLightsOn(true);
                }
            } else {
                imageViewZzzz.setVisibility(View.VISIBLE);
                sleepingBool = true;
            }
        }
        return sleepingBool;
    }

    public void setLightsOn(Boolean lightsOn) {
        //setten de nieuwe waarde van lightsOn in sharedprefs zodat je dit kunt gaan gebruiken
        SharedPreferences.Editor editor = prefGame.edit();
        this.lightsOn = lightsOn;
        editor.putBoolean("lightsOn", lightsOn);
        editor.putString("lastLightsChangedDateTime", getCurrentDateTime());
        editor.apply();
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

    public void showHappyBasedOnSharedPrefs() {
        //Ophalen uit shared prefs
        happy = prefGame.getInt("happy", 4);

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

    public void showHungryBasedOnSharedPrefs() {
        //Ophalen uit shared prefs
        hungry = prefGame.getInt("hungry", 4);

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

    public void showWeightBasedOnSharedPrefs() {
        //Ophalen uit shared prefs
        weight = prefGame.getInt("weight", 50);

        progressBarValueWeight.setProgress(weight);
        if (weight >= 40 && weight <=60) {
            //Green
            Drawable progressDrawable = progressBarValueWeight.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.parseColor("#008000"), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBarValueWeight.setProgressDrawable(progressDrawable);
        } else if ((weight > 60 && weight <= 80) || (weight < 40 && weight >= 20)) {
            //Orange
            Drawable progressDrawable = progressBarValueWeight.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.parseColor("#FFA500"), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBarValueWeight.setProgressDrawable(progressDrawable);
        } else {
            //Red
            Drawable progressDrawable = progressBarValueWeight.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.parseColor("#EE4B2B"), android.graphics.PorterDuff.Mode.SRC_IN);
            progressBarValueWeight.setProgressDrawable(progressDrawable);
        }
    }

    public void showAgeAndProperCatImage() {
        textViewValueAge.setText(String.valueOf(age));
        imageViewCat.setVisibility(View.VISIBLE);
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

    public void hideImageViews() {
        imageViewCat.setVisibility(View.GONE);
        imageViewPoep1.setVisibility(View.GONE);
        imageViewPoep2.setVisibility(View.GONE);
        imageViewPoep3.setVisibility(View.GONE);
        imageViewPoep4.setVisibility(View.GONE);
        imageViewLightsOut.setVisibility(View.GONE);
        imageViewMedication.setVisibility(View.GONE);
        imageViewDead.setVisibility(View.GONE);
        buttonStartNewGame.setVisibility(View.GONE);
    }

    public Long calculateAge() {

        catIsDeadDate = prefGame.getString("catIsDeadDate", "");
        String date = null;
        //Als cat dood is dan wil je vergelijken met de datum waarop de kat is doodgegaan, niet met de huidige datum.
        if (Objects.equals(catIsDeadDate, "")) {
            date = getCurrentDateTime();
        } else {
            date = catIsDeadDate;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = null;
            dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            LocalDate date1 = LocalDate.parse(gameStartDateTime, dtf);
            //ToDo hier kun je een beetje mee spelen om wat verschillen te zien
            LocalDate date2 = LocalDate.parse(date, dtf);
            age = ChronoUnit.DAYS.between(date1, date2);
            System.out.println ("Days: " + age);
        }
        return age;
    }

    public void setAgeOfDeath() {

        Random rn = new Random();
        ageOfDeath = rn.nextInt(24) + 19;

        //setten van de age of death
        SharedPreferences.Editor editor = prefGame.edit();
        editor.putInt("ageOfDeath", ageOfDeath);
        editor.apply();
    }

    public void startNewGame() {
        AlertDialog alertDialog = new AlertDialog.Builder(CatGameActivity.this).create();
        alertDialog.setTitle("New Game");
        alertDialog.setMessage("Start a new game. \n\n" +
                "Feed your cat when it's hungry, play with it, " +
                "clean up after it, feed it medication whenever it is sick. " +
                "Dim the lights when it's sleeping. \n\n" +
                "You know the drill. Have fun! \n\n" +
                "Game takes multiple days, come back and continue whenever you feel like it.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //clearen van alle sharedprefs want je gaat een nieuw game beginnen, dus alle variabelen van oude game mogen weg.
                        SharedPreferences.Editor editor = prefGame.edit();
                        editor.clear();
                        editor.apply();

                        editor.putString("gameStartDateTime", getCurrentDateTime());
                        editor.apply();
                        gameStartDateTime = getCurrentDateTime();

                        setSleepingStartTime();
                        setSleepingEndTime();

                        hideImageViews();

                        imageViewFoodButton.setVisibility(View.VISIBLE);
                        imageViewLightButton.setVisibility(View.VISIBLE);
                        imageViewGameButton.setVisibility(View.VISIBLE);
                        imageViewMedicationButton.setVisibility(View.VISIBLE);
                        imageViewBathroomButton.setVisibility(View.VISIBLE);

                        setAgeOfDeath();

                        dialog.dismiss();

                        //Hier gaat 'ie ook verder wanneer er al een bestaande game is
                        gameExists();
                    }
                });
        alertDialog.show();
    }

    public String getCurrentDateTime() {

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

    public String getCurrentTime() {
        DateTimeFormatter date = null;
        String currentTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = null;
            now = LocalDateTime.now();
            currentTime = date.format(now);
            System.out.println("CURRENTTime: " + currentTime);

        }
        return currentTime;
    }
}
//https://tamagotchi.fandom.com/wiki/Tamagotchi_(1996_Pet)#:~:text=Five%20minutes%20after%20the%20clock,by%20the%20age%20of%206.