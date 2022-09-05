package com.catapp.scanthecat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CatFullListRecViewAdapter extends RecyclerView.Adapter<CatFullListRecViewAdapter.ViewHolder> {

    private Context context;
    private String myUrl = "https://api.api-ninjas.com/v1/cats?name=";
    private ProgressDialog progressDialog;
    private final ArrayList<String> catsLimited;

    public CatFullListRecViewAdapter(ArrayList<String> catsLimited) {
        this.catsLimited = catsLimited;
    }

    @NonNull
    @Override
    public CatFullListRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item, parent, false);
        CatFullListRecViewAdapter.ViewHolder holder = new CatFullListRecViewAdapter.ViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CatFullListRecViewAdapter.ViewHolder holder, int position) {
        //Hier kun je de teksten wijzigen in de recyclerview

        String catName = catsLimited.get(position);
        holder.txtCatName.setText(catName);

        String catImage = "https://api-ninjas.com/images/cats/" + catName.toLowerCase().replace(" ", "_") + ".jpg";

        Glide.with(context)
                .asBitmap()
                .load(catImage)
                .into(holder.imageCat);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAbsoluteAdapterPosition();

                myUrl = myUrl + catsLimited.get(pos);

                CatFullListRecViewAdapter.GetResultsAsync getResultsAsync = new CatFullListRecViewAdapter.GetResultsAsync();
                Thread thread = new Thread(getResultsAsync);
                thread.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        //number of items in object zodat recyclerview weet hoeveel blokjes
        return catsLimited.size();
    }

    //deze class is verantwoordelijk voor de items in de recyclerview, dus hier definieer je de velden.
    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView parent;
        private ImageView imageCat;
        private TextView txtCatName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCat = itemView.findViewById(R.id.imageCat);
            txtCatName = itemView.findViewById(R.id.txtCatName);
            parent = itemView.findViewById(R.id.parent);
        }
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
                    url = new URL(myUrl);
                    //open a URL connection

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("X-Api-Key", "6P95jdveVV+eAo4s1ub3hw==N82UH1hbB5J1wkQw");

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

            final String resultCats = result;
            ((CatFullListActivity)context).runOnUiThread(new Runnable() {
                public void run() {
                    // do onPostExecute stuff GUI
                    Gson gson = new Gson();
                    // Convert JSON File to Java Object
                    Cat[] cats = gson.fromJson(resultCats, Cat[].class);

                    //Resetten van de url omdat ie anders shit achter elkaar blijft plakken.
                    myUrl = "https://api.api-ninjas.com/v1/cats?name=";

                    if (!resultCats.isEmpty()) {
                        //Ga direct naar displaycatresult, geen lijst nodig
                        Intent intentResult = new Intent(context, DisplayCatGeneralInfoActivity.class);
                        intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentResult.putExtra("cats", cats[0]);
                        context.startActivity(intentResult);
                    } else {
                        Intent catListIntent = new Intent(context, CatFullListActivity.class);
                        catListIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(catListIntent);
                    }
                }
            });
        }
    }
}
