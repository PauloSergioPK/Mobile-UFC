package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.models.Usuario;

public class MainActivity extends AppCompatActivity {

    private Button logar;
    private EditText email;
    private EditText password;
    private TextView cadastrar;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logar = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmailLogin);
        password = findViewById(R.id.editTextSenhaLogin);
        cadastrar = findViewById(R.id.textViewCriarConta);
        autenticacao = ConfiguracaoFirebase.getFireBaseAuth();
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityCadastro.class);
                startActivity(intent);
            }
        });
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fazerLogin();
            }
        });
    }

    public void fazerLogin(){
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this,"Dados invalidos", Toast.LENGTH_SHORT).show();
        }
        else{
            autenticacao.signInWithEmailAndPassword(
                    email.getText().toString(),password.getText().toString()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                    }
                    else{
                        String excecao = "";
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthInvalidUserException e){
                            excecao = "Usuario não cadastrado!";
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            excecao = "E-mail e senha não correspondem à um usuário";
                        }catch (Exception e){
                            excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),excecao,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
    }
}