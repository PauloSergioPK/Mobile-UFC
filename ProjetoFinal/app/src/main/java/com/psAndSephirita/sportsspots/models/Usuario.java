package com.psAndSephirita.sportsspots.models;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.helper.UsuarioFirebase;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private ArrayList<String> amigos = new ArrayList<>();
    private String  urlFoto;
    private int idade;

    public Usuario(){

    }

    public Usuario(String nome,String email, String senha, int idade) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idade = idade;
        this.amigos = new ArrayList<>();
        this.urlFoto = "";
    }

    public void salvar(){
        DatabaseReference reference = ConfiguracaoFirebase.getFireBaseDatabase();
        DatabaseReference usuario = reference.child("usuarios").child(getId());
        usuario.setValue(this);
    }

    public void atualizar(){
        String identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        DatabaseReference firebaseDatabase = ConfiguracaoFirebase.getFireBaseDatabase();
        DatabaseReference usuariosRef = firebaseDatabase.child("usuarios").child(identificadorUsuario);
        Map<String,Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren(valoresUsuario);
    }

    @Exclude
    public Map<String,Object> converterParaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email",getEmail());
        usuarioMap.put("nome",getNome());
        usuarioMap.put("urlFoto",getUrlFoto());
        usuarioMap.put("idade",getIdade());
        //usuarioMap.put("amigos",getAmigos());
        return usuarioMap;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
