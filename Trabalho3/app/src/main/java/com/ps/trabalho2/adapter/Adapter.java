package com.ps.trabalho2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ps.trabalho2.R;
import com.ps.trabalho2.model.Moto;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    ArrayList<Moto> motos;
    public Adapter(ArrayList<Moto> lista) {
        motos = lista;
    }

    public void setData(ArrayList<Moto> lista){
        motos= lista;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_lista,
                parent,false
        );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Moto moto = motos.get(position);
        holder.identificador.setText(String.valueOf(moto.getId()));
        holder.modelo.setText(moto.getModelo());
        holder.fabricante.setText(moto.getFabricante());
        holder.cilindrada.setText(String.valueOf(moto.getCilindradas()) + "cc");
    }

    @Override
    public int getItemCount() {
        return motos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView fabricante;
        private TextView modelo;
        private TextView cilindrada;
        private TextView identificador;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fabricante= itemView.findViewById(R.id.textViewFabricante);
            modelo = itemView.findViewById(R.id.textViewModelo);
            cilindrada = itemView.findViewById(R.id.textViewCilindrada);
            identificador = itemView.findViewById(R.id.textViewIdentificador);
        }
    }
}
