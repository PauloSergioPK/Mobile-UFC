package com.ps.trabalho1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    private ListViewFragment fragment1;
    private GridFragment fragment2;
    private Paredao fragment3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button buttonL = findViewById(R.id.buttonListView);
        Button buttonG = findViewById(R.id.buttonGridView);
        Button buttonP = findViewById(R.id.buttonParedao);
        fragment1 = new ListViewFragment();
        fragment2 = new GridFragment();
        fragment3 = new Paredao();



        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo,fragment1);
                transaction.commit();
            }
        });
        buttonG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo,fragment2);
                transaction.commit();
            }
        });
        buttonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameConteudo,fragment3);
                transaction.commit();
            }
        });
    }
}