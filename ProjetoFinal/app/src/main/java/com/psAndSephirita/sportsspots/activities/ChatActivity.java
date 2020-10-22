package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.adapter.MensagensAdapter;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.helper.Base64Custom;
import com.psAndSephirita.sportsspots.helper.UsuarioFirebase;
import com.psAndSephirita.sportsspots.models.Mensagem;
import com.psAndSephirita.sportsspots.models.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    private CircleImageView circleImageViewFoto;
    private Usuario usuarioDestinatario;
    private FloatingActionButton actionButton;
    private EditText textChat;
    private ImageView buttonCamera;
    private RecyclerView recyclerView;
    private MensagensAdapter adapter;

    private String idContato;
    private String idUser;
    private ArrayList<Mensagem> mensagens = new ArrayList<>();
    private DatabaseReference databaseMsg;
    private ChildEventListener childEventListenerMsg;
    private StorageReference storage;

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Configurar toolbar

        //Configuracoes iniciais
        textViewNome = findViewById(R.id.textViewNomeChat);
        circleImageViewFoto = findViewById(R.id.circleImageViewFotoChat);
        actionButton = findViewById(R.id.fabSend);
        textChat = findViewById(R.id.editTextTextMensagemChat);
        buttonCamera = findViewById(R.id.imageViewCameraChat);
        recyclerView = findViewById(R.id.recyclerMensagens);

        //Recuperar dados
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");
            textViewNome.setText(usuarioDestinatario.getNome());
            if(usuarioDestinatario.getUrlFoto() != null && !usuarioDestinatario.getUrlFoto().equals("")){
                Uri uri = Uri.parse(usuarioDestinatario.getUrlFoto());
                Glide.with(getApplicationContext()).load(uri).into(circleImageViewFoto);
            }
        }

        idContato = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());
        idUser = UsuarioFirebase.getIdentificadorUsuario();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = textChat.getText().toString();
                if(!msg.isEmpty()){
                    Mensagem mensagem = new Mensagem(idUser,msg,"");
                    salvarMensagem(idUser,idContato, mensagem);
                }
                else{

                }
            }
        });


        //Configurar adapter
        adapter = new MensagensAdapter(mensagens,this);

        //Configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        databaseMsg = ConfiguracaoFirebase.getFireBaseDatabase()
                .child("mensagens")
                .child(idUser)
                .child(idContato);
        storage = ConfiguracaoFirebase.getFireBaseStorage();

        buttonCamera.setVisibility(View.GONE);

//        buttonCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if(intent.resolveActivity(getPackageManager()) != null){
//                    startActivityForResult(intent,SELECAO_CAMERA);
//                }
//            }
//        });
//        buttonCamera.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                if(intent.resolveActivity(getPackageManager()) != null){
//                    startActivityForResult(intent,SELECAO_GALERIA);
//                }
//                return true;
//            }
//        });

    }


    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        DatabaseReference database = ConfiguracaoFirebase.getFireBaseDatabase().child("mensagens");
        database.child(idRemetente).child(idDestinatario).push().setValue(mensagem);
        database.child(idDestinatario).child(idRemetente).push().setValue(mensagem);
        textChat.setText("");
    }

    private void recuperarMensagens(){
        childEventListenerMsg = databaseMsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagens.clear();
        databaseMsg.removeEventListener(childEventListenerMsg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                if (requestCode == SELECAO_CAMERA)
                    imagem = (Bitmap) data.getExtras().get("data");
                else {
                    Uri localImagemSelecionada = data.getData();
                    imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                }
                if (imagem != null) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //criar nome da imagem
                    final String nomeImagem = UUID.randomUUID().toString();

                    final StorageReference imagemRef = storage.child("imagens")
                            .child("fotos")
                            .child(idUser)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload");
                            Toast.makeText(getApplicationContext(),"Erro ao fazer upload de imagem",Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url =  task.getResult();
                                    Mensagem mensagem = new Mensagem(idUser,"imagem.jpeg",url.toString());
                                    salvarMensagem(idUser,idContato,mensagem);

                                }
                            });
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}