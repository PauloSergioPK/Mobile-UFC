package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.helper.Base64Custom;
import com.psAndSephirita.sportsspots.helper.Permissao;
import com.psAndSephirita.sportsspots.helper.UsuarioFirebase;
import com.psAndSephirita.sportsspots.models.Usuario;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewNome;
    private TextView textViewEmail;
    private TextView textViewIdade;
    private ImageButton buttonCamera;
    private CircleImageView circleImageView;
    private String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private StorageReference storageReference;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private String identificadorUsuario;
    private Usuario usuarioLogado;
    private Query query;


    private ValueEventListener valueEventListenerDadosUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textViewEmail = findViewById(R.id.textViewEmailPerfil);
        textViewIdade = findViewById(R.id.textViewIdadePerfil);
        textViewNome = findViewById(R.id.textViewNomePerfil);
        buttonCamera = findViewById(R.id.imageButtonCamera);
        circleImageView = findViewById(R.id.circleImageViewFotoPerfil);
        storageReference = ConfiguracaoFirebase.getFireBaseStorage();
        Permissao.validarPermissoes(permissoesNecessarias,this,1);
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_CAMERA);
                }

            }
        });

        buttonCamera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_GALERIA);
                }
                return true;
            }
        });

        //recuperar dados do usuario
        FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();
        if(url == null){
            circleImageView.setImageResource(R.drawable.padrao);
        }
        else{
            Glide.with(this).load(url).into(circleImageView);
        }


        //tentar pegar todos os dados de usuario
//        Query query = ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios")
//                .orderByChild("email").equalTo(usuarioLogado.getEmail());

        query = ConfiguracaoFirebase.getFireBaseDatabase().child("usuarios")
                .child(UsuarioFirebase.getIdentificadorUsuario());
        //query.addListenerForSingleValueEvent(valueEventListener);

        atualizarExibicao();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                if(requestCode == SELECAO_CAMERA)
                    imagem = (Bitmap) data.getExtras().get("data");
                else{
                    Uri localImagemSelecionada = data.getData();
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localImagemSelecionada);
                }
                if(imagem != null){
                    circleImageView.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,baos);
                    byte [] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference.child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario)
                            .child("perfil.jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Erro ao fazer upload de imagem",Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"Sucesso ao fazer upload de imagem",Toast.LENGTH_LONG).show();
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });
                        }
                    });

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    public void atualizarFotoUsuario(Uri url){
        boolean deuCerto = UsuarioFirebase.atualizarFotoUsuario(url);
        if(deuCerto) {
            usuarioLogado.setUrlFoto(url.toString());
            usuarioLogado.atualizar();
        }
    }

//    ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            if(snapshot.exists()){
////                for(DataSnapshot snap : snapshot.getChildren()){
////                    usuarioLogado = snap.getValue(Usuario.class);
////                    textViewIdade.setText(String.valueOf(usuarioLogado.getIdade()));
////                    Log.i("teste",String.valueOf(usuarioLogado.getIdade()));
////                }
//                usuarioLogado.setIdade(Integer.valueOf(snapshot.child("idade").getValue().toString()));
//                textViewIdade.setText(String.valueOf(usuarioLogado.getIdade()));
//                Log.i("teste",String.valueOf(usuarioLogado.getIdade()));
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    };

    public void atualizarExibicao(){
        textViewNome.setText(usuarioLogado.getNome());
        textViewEmail.setText(usuarioLogado.getEmail());
//        try {
//            textViewIdade.setText(String.valueOf(usuarioLogado.getIdade()));
//        } catch (Exception e){
//            textViewIdade.setText("deu erro");
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosUsuario();
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(valueEventListenerDadosUsuario);
    }

    public void recuperarDadosUsuario(){
        valueEventListenerDadosUsuario = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    usuarioLogado.setIdade(Integer.valueOf(snapshot.child("idade").getValue().toString()));
                    //String url = snapshot.child("urlFoto").getValue().toString();
                    //circleImageView.setImageURI(Uri.parse(url));
                    //Glide.with(getApplicationContext()).load(url).into(circleImageView);
                    textViewIdade.setText(String.valueOf(usuarioLogado.getIdade()));
                    Log.i("teste",String.valueOf(usuarioLogado.getIdade()));
                    //Log.i("teste",url);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}