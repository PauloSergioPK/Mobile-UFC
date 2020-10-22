package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.adapter.SpotsAdapter;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.models.Spots;

import java.util.ArrayList;

public class SpotsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SpotsAdapter adapter;
    private ArrayList<Spots> spots = new ArrayList<>();
    private DatabaseReference referenceSpots;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots);
        recyclerView = findViewById(R.id.recyclerViewSpots);
        referenceSpots = ConfiguracaoFirebase.getFireBaseDatabase().child("spots");


        //Configurar adapter
        adapter = new SpotsAdapter(spots,this);

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceSpots.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarSpots();
    }

    public void recuperarSpots(){
        valueEventListener = referenceSpots.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    spots.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Log.i("teste",snap.toString());
                        Spots spot = snap.getValue(Spots.class);
                        spots.add(spot);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}