package com.ps.trabalho2.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ps.trabalho2.R;
import com.ps.trabalho2.model.Moto;

public class MainActivity2 extends AppCompatActivity {

    private EditText editTextFabricante;
    private EditText editTextModelo;
    private EditText editTextCilindrada;
    private Button cancelar;
    private Button confirmar;
    private Moto motinha = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editTextCilindrada = findViewById(R.id.editTextCilindrada);
        editTextFabricante = findViewById(R.id.editTextFabricante);
        editTextModelo = findViewById(R.id.editTextModelo);
        cancelar = findViewById(R.id.buttonCancelar);
        confirmar = findViewById(R.id.buttonSalvar);

        Bundle dados = getIntent().getExtras();
        if(dados != null)
            motinha = (Moto) dados.getSerializable("motinha");
        if(motinha != null){
            editTextModelo.setText(motinha.getModelo());
            editTextFabricante.setText(motinha.getFabricante());
            editTextCilindrada.setText(Integer.toString(motinha.getCilindradas()));
        }

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextCilindrada.getText().toString().equals("") || editTextModelo.getText().toString().equals("") || editTextFabricante.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Informações inválidas", Toast.LENGTH_SHORT).show();
                else{
                    if(motinha != null){
                        motinha.setModelo(editTextModelo.getText().toString());
                        motinha.setFabricante(editTextFabricante.getText().toString());
                        motinha.setCilindradas(Integer.valueOf(editTextCilindrada.getText().toString()));
                    }
                    else
                        motinha = new Moto(editTextFabricante.getText().toString(),editTextModelo.getText().toString(),Integer.valueOf(editTextCilindrada.getText().toString()));
                    Intent result = new Intent();
                    result.putExtra("novaMoto",motinha);
                    setResult(RESULT_OK,result);
                    finish();
                }

            }
        });
    }

}