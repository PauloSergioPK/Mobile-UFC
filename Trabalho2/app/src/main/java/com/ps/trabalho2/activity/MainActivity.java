package com.ps.trabalho2.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ps.trabalho2.R;
import com.ps.trabalho2.adapter.Adapter;
import com.ps.trabalho2.model.Moto;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*

        Paulo Sergio - 422218
     */
    private Button cadastrar;
    private Button editar;
    private RecyclerView recyclerView;
    private EditText editText;
    private ArrayList<Moto> motos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadastrar = findViewById(R.id.buttonCadastrar);
        editar = findViewById(R.id.buttonEditar);
        recyclerView = findViewById(R.id.recycleView);
        editText = findViewById(R.id.editTextIdRecovery);
        motos = new ArrayList<>();

        //preparar o adapter
        Adapter adapter = new Adapter(motos);
        //preparar o layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayout.VERTICAL));
        recyclerView.setAdapter(adapter);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("motinha", (Bundle) null);
                startActivityForResult(intent,1);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validID()){
                    for(Moto m: motos){
                        if(m.getId() == Integer.valueOf(editText.getText().toString())){
                            Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                            intent.putExtra("motinha",m);
                            startActivityForResult(intent,1);
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Identificador inválido",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean validID(){
        if(editText.getText().toString().equals(""))
            return false;
        for(Moto m : motos) {
            if(m.getId() == Integer.valueOf(editText.getText().toString()))
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == -1){
                Bundle bundle = data.getExtras();
                Moto aux = (Moto) bundle.getSerializable("novaMoto");
                if(aux != null){
                    for(Moto m : motos){
                        if(aux.getId() == m.getId()) {
                            motos.set(motos.indexOf(m),aux);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            return;
                        }
                    }
                    motos.add(aux);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

            }
        }
    }
}