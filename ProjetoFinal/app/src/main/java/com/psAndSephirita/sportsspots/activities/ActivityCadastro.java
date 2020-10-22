package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.helper.Base64Custom;
import com.psAndSephirita.sportsspots.helper.UsuarioFirebase;
import com.psAndSephirita.sportsspots.models.Usuario;

import java.util.ArrayList;

public class ActivityCadastro extends AppCompatActivity {

    private Button buttonCadastro;
    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private EditText editTextIdade;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");
    private DatabaseReference referenceUsuario;
    private FirebaseAuth autenticacao;
    private Boolean jaCadastrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        buttonCadastro = findViewById(R.id.buttonCadastrar);
        editTextNome = findViewById(R.id.editTextNomeCadastro);
        editTextEmail = findViewById(R.id.editTextEmailCadastro);
        editTextSenha = findViewById(R.id.editTextSenhaCadastro);
        editTextIdade = findViewById(R.id.editTextIdadeCadastro);
        referenceUsuario = reference;
        jaCadastrado = false;
        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar(){
        String textoNome = editTextNome.getText().toString();
        final String textoEmail = editTextEmail.getText().toString();
        String textoSenha = editTextSenha.getText().toString();
        String textoIdade = editTextIdade.getText().toString();
        if(textoEmail.isEmpty() || textoIdade.isEmpty() || textoNome.isEmpty() || textoSenha.isEmpty())
            Toast.makeText(this,"Dados invalidos",Toast.LENGTH_LONG).show();
        else{
            final Usuario usuario = new Usuario(textoNome,textoEmail,textoSenha,Integer.valueOf(textoIdade));
            autenticacao = ConfiguracaoFirebase.getFireBaseAuth();
            autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Sucesso ao cadastrar usuário!",Toast.LENGTH_SHORT).show();
                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                        finish();
                        try{
                            String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                            usuario.setId(idUsuario);
                            usuario.salvar();


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        String excecao = "";
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthWeakPasswordException e){
                            excecao = "Digite uma senha mais forte!";
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            excecao = "Por favor, digite um e-mail válido";
                        }catch (FirebaseAuthUserCollisionException e){
                            excecao = "Esta conta já foi cadastrada";
                        }catch (Exception e){
                            excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),excecao,Toast.LENGTH_LONG).show();
                    }

                }
            });
//            referenceUsuario = reference.child(editTextUser.getText().toString());
//            referenceUsuario.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.getValue() != null && !jaCadastrado)
//                        Toast.makeText(getApplicationContext(), "Usuario ja em uso", Toast.LENGTH_LONG).show();
//                    else if(snapshot.getValue() == null){
//                        if(referenceUsuario != null) {
//                            referenceUsuario.setValue(new Usuario(editTextNome.getText().toString(), editTextUser.getText().toString(), editTextEmail.getText().toString(),
//                                    editTextSenha.getText().toString(), Integer.valueOf(editTextIdade.getText().toString()), new ArrayList<Usuario>()));
//                        }
//                        referenceUsuario = null;
//                        jaCadastrado = true;
//                        finish();
//                    }
//
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }
    }

}