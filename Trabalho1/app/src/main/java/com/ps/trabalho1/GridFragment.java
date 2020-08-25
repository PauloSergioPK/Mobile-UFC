package com.ps.trabalho1;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance(String param1, String param2) {
        GridFragment fragment = new GridFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        final ArrayList<Moto> motos = criarMotos();

        // Inflate the layout for this fragment
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        //configurar adapter
        Adapter adapter = new Adapter(motos);
        //configurar layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),GridLayout.HORIZONTAL));
        recyclerView.setAdapter(adapter);

        //evento de click
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(
                        getContext(),
                        motos.get(position).getModelo(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }
        ));
        return view;
    }

    public ArrayList<Moto> criarMotos() {
        ArrayList<Moto> motos = new ArrayList<>();
        Moto moto1 = new Moto("Kawasaki", "Ninja 300", "299");
        Moto moto2 = new Moto("Kawasaki", "Ninja 250", "250");
        Moto moto3 = new Moto("Kawasaki", "Ninja 400", "400");
        Moto moto4 = new Moto("Kawasaki", "Ninja ZX6R", "600");
        Moto moto5 = new Moto("Kawasaki", "Ninja 636", "636");
        Moto moto6 = new Moto("Kawasaki", "Ninja ZX10", "998");
        Moto moto7 = new Moto("Honda", "CBR 600F", "600");
        Moto moto8 = new Moto("Honda", "CB 600", "600");
        Moto moto9 = new Moto("Honda", "CBR 600RR", "600");
        Moto moto10 = new Moto("Honda", "CBR 650F", "650");
        Moto moto11 = new Moto("Honda", "CBR 650R", "650");
        motos.add(moto1);
        motos.add(moto2);
        motos.add(moto3);
        motos.add(moto4);
        motos.add(moto5);
        motos.add(moto6);
        motos.add(moto7);
        motos.add(moto8);
        motos.add(moto9);
        motos.add(moto10);
        motos.add(moto11);
        return motos;
    }
}