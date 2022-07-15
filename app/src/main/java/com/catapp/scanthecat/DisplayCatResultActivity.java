package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
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
        TextView txtCatFamilyFriendly = findViewById(R.id.txtCatFamilyFriendly);
        TextView txtCatChildFriendly = findViewById(R.id.txtCatChildFriendly);

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
        txtCatFamilyFriendly.setText(String.valueOf(cats.getFamily_friendly()));
        txtCatChildFriendly.setText(String.valueOf(cats.getChildren_friendly()));

    }
}