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
import com.psAndSephirita.sportsspots.models.Usuario;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    private ArrayList<Usuario> contatos;
    private Context context;

    public ContatosAdapter(ArrayList<Usuario> usuarios, Context c){
        this.contatos = usuarios;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = contatos.get(position);
        holder.email.setText(usuario.getEmail());
        holder.nome.setText(usuario.getNome());
        if(usuario.getUrlFoto() != null || !usuario.getUrlFoto().equals("")){
            Uri uri = Uri.parse(usuario.getUrlFoto());
            Glide.with(context).load(uri).into(holder.foto);
        }
        else{
            holder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView foto;
        private TextView nome;
        private TextView email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imageViewContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            email = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
