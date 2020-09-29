package com.ps.trabalho2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Button remover;
    private RecyclerView recyclerView;
    private EditText editText;
    private ArrayList<Moto> motos;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("motos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadastrar = findViewById(R.id.buttonCadastrar);
        editar = findViewById(R.id.buttonEditar);
        remover = findViewById(R.id.buttonRemover);
        recyclerView = findViewById(R.id.recycleView);
        editText = findViewById(R.id.editTextIdRecovery);
        motos = new ArrayList<>();

        //recuperar dados do FIREBASE

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                motos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //String result = snapshot.getValue().toString();
                    Moto m = snapshot.getValue(Moto.class);
                    //Log.i("Teste", result);
                    motos.add(m);
                }
                if(motos.size() > 0)
                    Moto.setCont(motos.get(motos.size() - 1).getId() + 1);
                else
                    Moto.setCont(1);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

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

        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validID()){
                    int id = 0;
                    for(Moto m: motos){
                        if(m.getId() == Integer.valueOf(editText.getText().toString())){
                            id = motos.indexOf(m);
                            break;
                        }
                    }
                    motos.remove(id);
                    DatabaseReference removido = reference.child(editText.getText().toString());
                    removido.removeValue();
                    recyclerView.getAdapter().notifyDataSetChanged();
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
                            reference.child(Integer.toString(aux.getId())).setValue(aux);
                            return;
                        }
                    }
                    motos.add(aux);
                    reference.child(Integer.toString(aux.getId())).setValue(aux);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

            }
        }
    }
}