package com.ps.trabalho1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        logo.setVisibility(View.INVISIBLE);
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        String[] motos = getResources().getStringArray(R.array.motos);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,motos));
        RadioButton radioButtonDark = findViewById(R.id.radioButtonDark);
        radioButtonDark.setChecked(true);
        radioButtonDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    mudarParaEscuro();
                }
                else{
                    mudarParaClaro();
                }
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    logo.setVisibility(View.VISIBLE);
                }
                else{
                    logo.setVisibility(View.INVISIBLE);
                }
            }
        });
        Spinner spinner = findViewById(R.id.spinner);
        String[] carros = getResources().getStringArray(R.array.carros);
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,carros));
        Button confirmar = findViewById(R.id.buttonConfirmar);
        confirmar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(MainActivity.this,"Confirmado meu barão",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void mudarParaEscuro(){
        Drawable fundo = getDrawable(R.drawable.gradient_background1);
        findViewById(R.id.constraintLayout).setBackground(fundo);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.colorPrimary));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void mudarParaClaro(){
        Drawable fundo = getDrawable(R.drawable.gradient_background2);
        findViewById(R.id.constraintLayout).setBackground(fundo);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.colorSecondary));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opcoes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(intent);
        return true;
    }
}



/*

tocar um som ao passar a tela
ter um options para mudar o tema do app
primeira tela
	toogle button V
	EditText com autocomplete V
	dropDownList V
	RadioButtons V
	botao para usar clique longo e aparecer um toast com o edit text V
	options para ir pra activity com multiplas tabs V
segunda tela - multiplas tabs
	primeira tab
		list view V
	segunda tab
		grid view V
	terceira tab
		letra de boate azul com a musica V
 */