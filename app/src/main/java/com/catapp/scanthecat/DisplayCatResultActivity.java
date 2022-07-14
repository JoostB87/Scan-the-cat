package com.catapp.scanthecat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DisplayCatResultActivity extends AppCompatActivity {

    private TextView txtCatName;
    private Cat cats;
    private String afkomst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cat_result);

        txtCatName = (TextView) findViewById(R.id.txtCatName);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            cats = (Cat) extras.get("result");
            afkomst = extras.getString("afkomst");
        }

        txtCatName.setText(cats.getName());

    }
}