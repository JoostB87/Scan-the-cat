package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class DisplayCatResultActivity extends AppCompatActivity {

    private Cat cats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_result);

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

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cats = (Cat) extras.get("result");
        }

        Glide.with(this)
                .asBitmap()
                .load(cats.getImage_link())
                .into(imageCat);

        txtCatName.setText(cats.getName());
        txtCatOrigin.setText(cats.getOrigin());
        txtCatLength.setText(cats.getLength());
        txtCatWeight.setText(cats.getMin_weight() + " - " + cats.getMax_weight());
        txtCatLifeExpectancy.setText(cats.getMin_life_expectancy() + " - " + cats.getMax_life_expectancy());
        ratingFamilyFriendly.setRating(cats.getFamily_friendly());
        ratingChildrenFriendly.setRating(cats.getChildren_friendly());
        ratingStrangerFriendly.setRating(cats.getStranger_friendly());
        ratingOtherPetsFriendly.setRating(cats.getOther_pets_friendly());
        ratingGeneralHealth.setRating(cats.getGeneral_health());
        ratingIntelligence.setRating(cats.getIntelligence());
        ratingPlayfulness.setRating(cats.getPlayfulness());
        ratingMeowing.setRating(cats.getMeowing());
        ratingShedding.setRating(cats.getShedding());
        ratingGrooming.setRating(cats.getGrooming());

    }
}