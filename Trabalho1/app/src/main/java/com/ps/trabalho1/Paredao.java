package com.ps.trabalho1;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Paredao#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Paredao extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MediaPlayer mediaPlayer;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;


    public Paredao() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Paredao.
     */
    // TODO: Rename and change types and number of parameters
    public static Paredao newInstance(String param1, String param2) {
        Paredao fragment = new Paredao();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paredao, container, false);
        b1 = view.findViewById(R.id.buttonMusica1);
        b2 = view.findViewById(R.id.buttonMusica2);
        b3 = view.findViewById(R.id.buttonMusica3);
        b4 = view.findViewById(R.id.buttonMusica4);
        b5 = view.findViewById(R.id.buttonMusica5);
        b6 = view.findViewById(R.id.buttonMusica6);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tocando = executarSom(view,R.raw.pressaonenem);
                if(tocando) {
                    b1.setText("PARAR");
                    b6.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
                else {
                    b1.setText("TOCAR");
                    b6.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tocando = executarSom(view,R.raw.boateazul);
                if(tocando) {
                    b2.setText("PARAR");
                    b1.setText("TOCAR");
                    b6.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
                else {
                    b2.setText("TOCAR");
                    b1.setText("TOCAR");
                    b6.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tocando = executarSom(view,R.raw.takeonme);
                if(tocando) {
                    b3.setText("PARAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b6.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
                else {
                    b3.setText("TOCAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b6.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tocando = executarSom(view,R.raw.careless);
                if(tocando) {
                    b4.setText("PARAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b6.setText("TOCAR");
                    b5.setText("TOCAR");
                }
                else {
                    b4.setText("TOCAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b6.setText("TOCAR");
                    b5.setText("TOCAR");
                }
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tocando = executarSom(view,R.raw.chaves);
                if(tocando) {
                    b5.setText("PARAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b6.setText("TOCAR");
                }
                else {
                    b5.setText("TOCAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b6.setText("TOCAR");
                }
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean tocando = executarSom(view,R.raw.mcpoze);
                if(tocando) {
                    b6.setText("PARAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
                else {
                    b6.setText("TOCAR");
                    b1.setText("TOCAR");
                    b2.setText("TOCAR");
                    b3.setText("TOCAR");
                    b4.setText("TOCAR");
                    b5.setText("TOCAR");
                }
            }
        });
        return view;
    }

    public boolean executarSom(View view,int m){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getActivity(),m);
            mediaPlayer.start();
            return true;
        }
        else{
            mediaPlayer.release();
            mediaPlayer = null;
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}