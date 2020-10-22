package com.psAndSephirita.sportsspots.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.models.Spots;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.MyViewHolder> {

    private ArrayList<Spots> spots;
    private Context context;

    public SpotsAdapter(ArrayList<Spots> spots, Context context){
        this.spots = spots;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_spots,parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Spots spot = spots.get(position);
        holder.descricao.setText(spot.getDescricao());
        holder.longitude.setText(String.valueOf(spot.getLongitude()));
        holder.latitude.setText(String.valueOf(spot.getLatitude()));
        try{
            if(spot.getFoto() == null || spot.getFoto().equals(""))
                holder.circleImageView.setImageResource(R.drawable.spot_padrao);
            else{
                Uri uri = Uri.parse(spot.getFoto());
                Glide.with(context).load(uri).into(holder.circleImageView);
            }
        }catch (Exception e){
            e.printStackTrace();
            holder.circleImageView.setImageResource(R.drawable.spot_padrao);

        }
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView latitude;
        private TextView longitude;
        private TextView descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageSpot);
            latitude = itemView.findViewById(R.id.textLatitudeList);
            longitude = itemView.findViewById(R.id.textLongitudeList);
            descricao = itemView.findViewById(R.id.textDescricaoList);
        }
    }
}
