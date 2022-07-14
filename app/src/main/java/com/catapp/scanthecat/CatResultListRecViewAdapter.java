package com.catapp.scanthecat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class CatResultListRecViewAdapter extends RecyclerView.Adapter<CatResultListRecViewAdapter.ViewHolder> {

    private Context context;
    private Cat[] cats;

    public CatResultListRecViewAdapter(Cat[] cats) {
        this.cats = cats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Hier kun je de teksten wijzigen in de recyclerview

        String catImage = cats[position].getImage_link();
        String catName = cats[position].getName();

        holder.txtCatName.setText(catName);

        Glide.with(context)
                .asBitmap()
                .load(catImage)
                .into(holder.imageCat);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAbsoluteAdapterPosition();

                Intent intentResult = new Intent(context, DisplayCatResultActivity.class);
                intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentResult.putExtra("result", cats[pos]);
                context.startActivity(intentResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        //number of items in object zodat recyclerview weet hoeveel blokjes
        return cats.length;
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

}