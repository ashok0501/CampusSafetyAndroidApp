package com.sriher.campussafetyapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    private List<CardModel> cardList;
    private Context context;

    public CardAdapter(List<CardModel> cardList,Context context) {
        this.cardList = cardList;
        this.context=context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardModel card = cardList.get(position);
        holder.name.setText( card.getName());
        holder.phone.setText(card.getPhone());
        if(position%3==0)
        {
            holder.itemView.setBackgroundColor(Color.rgb(255,165,0));
            holder.name.setTextColor(Color.BLACK);
            holder.phone.setTextColor(Color.BLACK);
        }
        else if(position%3==1)
        {
            holder.itemView.setBackgroundColor(Color.rgb(0,0,255));
            holder.name.setTextColor(Color.WHITE);
            holder.phone.setTextColor(Color.WHITE);
        }
        else {
            holder.itemView.setBackgroundColor(Color.rgb(199, 21, 133));
            holder.name.setTextColor(Color.WHITE);
            holder.phone.setTextColor(Color.WHITE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber =card.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;


        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);

        }
    }
}
