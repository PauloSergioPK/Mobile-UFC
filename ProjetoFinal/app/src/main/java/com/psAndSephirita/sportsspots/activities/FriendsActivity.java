package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.adapter.ContatosAdapter;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.helper.Base64Custom;
import com.psAndSephirita.sportsspots.helper.RecyclerItemClickListener;
import com.psAndSephirita.sportsspots.helper.UsuarioFirebase;
import com.psAndSephirita.sportsspots.models.Usuario;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity implements DialogAddFriend.DialogAddFriendListener {

    private RecyclerView listaAmigos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuariosRef;
    private DatabaseReference amigosRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser userAtual;
    private FloatingActionButton buttonAdd;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        listaAmigos = findViewById(R.id.recyclerViewContatos);
        //usuariosRef = ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios").child(UsuarioFirebase.getIdentificadorUsuario()).child("amigos");
        usuariosRef = ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios").child(UsuarioFirebase.getIdentificadorUsuario());
        amigosRef = usuariosRef.child("amigos");
        userAtual = UsuarioFirebase.getUsuarioAtual();
        buttonAdd = findViewById(R.id.fabAdd);

        //configurar adapter
        adapter = new ContatosAdapter(listaContatos,this);


        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listaAmigos.setLayoutManager(layoutManager);
        listaAmigos.setHasFixedSize(true);
        listaAmigos.setAdapter(adapter);
        listaAmigos.addItemDecoration(new DividerItemDecoration(listaAmigos.getContext(), DividerItemDecoration.VERTICAL));


        //configurar evento de clique
        listaAmigos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        listaAmigos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                                intent.putExtra("chatContato",listaContatos.get(position));
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void recuperarContatos(){
        valueEventListenerContatos = amigosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaContatos.clear();
                if(snapshot.exists()) {
                    for(final DataSnapshot snap : snapshot.getChildren()){
                        String email = snap.getValue(String.class);

                        String idAmigo = Base64Custom.codificarBase64(email);
                        DatabaseReference findFriend = ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios").child(idAmigo);
                        findFriend.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot snap : snapshot.getChildren()){
                                        String email = snapshot.child("email").getValue(String.class);
                                        String nome = snapshot.child("nome").getValue(String.class);
                                        String foto = snapshot.child("urlFoto").getValue(String.class);
                                        //int idade = snapshot.child("idade").getValue(Integer.class);
//                                        ArrayList<String> amigos = new ArrayList<>();
//                                        for(DataSnapshot snaps : snap.child("amigos").getChildren()){
//                                            String emailAmigo = snaps.getValue(String.class);
//                                            amigos.add(emailAmigo);
//                                        }
                                        Usuario amigo = new Usuario();
                                        //amigo.setAmigos(amigos);
                                        amigo.setEmail(email);
                                        amigo.setNome(nome);
                                        amigo.setUrlFoto(foto);
                                        if(!amigoContido(amigo.getEmail())) {
                                            listaContatos.add(amigo);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.i("teste",email);
                    }
                    Log.i("teste", snapshot.toString());
                }

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        amigosRef.removeEventListener(valueEventListenerContatos);
    }

    public void openDialog(){
        DialogAddFriend dialogAddFriend = new DialogAddFriend();
        dialogAddFriend.show(getSupportFragmentManager(),"example dialog");
        dialogAddFriend.setCancelable(false);

    }

    @Override
    public void adicionarAmigo(String email) { //recupera o email digitado
        if(email != null && !email.equals("")) {
            String id = Base64Custom.codificarBase64(email);
            Log.i("teste", id);
//            Query query =  ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios")
//                    .child(id);
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
////                        String email = snapshot.child("email").getValue(String.class);
////                        int idade = snapshot.child("idade").getValue(Integer.class);
////                        String nome = snapshot.child("nome").getValue(String.class);
////                        String foto = snapshot.child("urlFoto").getValue()
//                        Usuario aux = snapshot.getValue(Usuario.class);
//                        if(aux != null){
//                            if(!amigoContido(aux)){ //amigo novo
////                                listaContatos.add(aux);
////                                adapter.notifyDataSetChanged();
//
//                                usuariosRef.child(Base64Custom.codificarBase64(aux.getEmail())).setValue(aux.getEmail());
//                            }
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(),"Usuario nao encontrado",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(),"Usuario nao encontrado",Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            if (!id.equals(UsuarioFirebase.getIdentificadorUsuario())) {
                Query query = ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios")
                        .child(id);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String email = snapshot.child("email").getValue(String.class);
                            if (email != null && !email.equals("")) {
                                if (!amigoContido(email)) { //amigo novo
                                    usuariosRef.child("amigos").push().setValue(email);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Você já é amigo desta pessoa", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Usuario nao encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Usuario nao encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else{
                Toast.makeText(getApplicationContext(), "Você não pode se adicionar :c ", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Dados invalidos",Toast.LENGTH_SHORT).show();

        }

    }

    public boolean amigoContido(String email){
        for(Usuario u : listaContatos){
            if(u.getEmail().equals(email))
                return true;
        }
        return false;
    }
}