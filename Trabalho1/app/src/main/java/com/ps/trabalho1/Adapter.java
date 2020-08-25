package com.ps.trabalho1;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<Moto> lista;
    public Adapter(ArrayList<Moto> motos) {
        lista = motos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_lista,parent,false
        );
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Moto moto = lista.get(position);
        holder.marca.setText(moto.getMarca());
        holder.modelo.setText(moto.getModelo());
        holder.cc.setText(moto.getCilindrada());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView marca,modelo,cc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            marca = itemView.findViewById(R.id.textViewMarca);
            modelo = itemView.findViewById(R.id.textViewModelo);
            cc = itemView.findViewById(R.id.textViewCC);
        }
    }
}
