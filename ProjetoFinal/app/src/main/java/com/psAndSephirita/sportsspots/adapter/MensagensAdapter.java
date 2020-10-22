package com.psAndSephirita.sportsspots.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.helper.UsuarioFirebase;
import com.psAndSephirita.sportsspots.models.Mensagem;
import com.psAndSephirita.sportsspots.models.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private ArrayList<Mensagem> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE    = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagensAdapter(ArrayList<Mensagem> mensagens, Context c) {
        this.mensagens = mensagens;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;
        if(viewType == TIPO_REMETENTE)
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente,parent,false);
        else if(viewType == TIPO_DESTINATARIO)
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String photo = mensagem.getImagem();
        if(photo != null && !photo.equals("")){
            Uri url = Uri.parse(photo);
            Glide.with(context).load(url).into(holder.imagem);
            holder.mensagem.setVisibility(View.GONE);
        }
        else{
            holder.imagem.setVisibility(View.GONE);
            holder.mensagem.setText(msg);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        String idUser = UsuarioFirebase.getIdentificadorUsuario();
        if(mensagem.getIdUsuario().equals(idUser)){
            return TIPO_REMETENTE;
        }
        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            imagem = itemView.findViewById(R.id.imageMensagemFoto);
        }
    }
}
