package com.psAndSephirita.sportsspots.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;
import com.psAndSephirita.sportsspots.models.Mensagem;
import com.psAndSephirita.sportsspots.models.Spots;
import com.psAndSephirita.sportsspots.models.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DialogAddSpot.DialogAddSpotListener {

    private GoogleMap mMap;
    private Button buttonPerfil;
    private Button buttonAmigos;
    private Button buttonSpots;
    private FirebaseAuth autenticacao;
    private String [] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private FloatingActionButton addSpot;
    private ArrayList<Spots> spots = new ArrayList<>();
    private DatabaseReference databaseSpots;
    private StorageReference storageSpots;
    private ValueEventListener valueEventListener;

    private Uri urlGambiarra = null;

    public static Double latitudeAtualDoClique = 0.0;
    public static Double longitudeAtualDoClique = 0.0;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        buttonAmigos = findViewById(R.id.buttonAmigos);
        buttonPerfil = findViewById(R.id.buttonMeuPerfil);
        buttonSpots = findViewById(R.id.buttonLocalizacoes);
        autenticacao = ConfiguracaoFirebase.getFireBaseAuth();
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setLogo(getDrawable(R.drawable.nome_app));
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menuOptions){
                    deslogarUsuario();
                    finish();
                }
                return true;
            }
        });

        addSpot = findViewById(R.id.fabAddSpot);

        //configurar butoes
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });
        buttonAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FriendsActivity.class);
                startActivity(intent);
            }
        });
        buttonSpots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SpotsActivity.class);
                startActivity(intent);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        databaseSpots = ConfiguracaoFirebase.getFireBaseDatabase().child("spots");
        storageSpots = ConfiguracaoFirebase.getFireBaseStorage().child("imagens").child("spots");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e("erro", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("erro", "Can't find style. Error: ", e);
        }

        // Add a marker in Sydney and move the camera
        LatLng quixada = new LatLng(-4.971874, -39.014097);
        //mMap.addMarker(new MarkerOptions().position(quixada).title("Marker in Sydney"));
//        for(Spots spot : spots){
//            mMap.addMarker(new MarkerOptions().position(new LatLng(spot.getLatitude(),spot.getLongitude())));
//        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                Toast.makeText(getApplicationContext(),"Latitude : " + latitude + " \nLongitude : " + longitude,Toast.LENGTH_LONG).show();
                latitudeAtualDoClique = latitude;
                longitudeAtualDoClique = longitude;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(quixada,16));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(quixada));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void deslogarUsuario(){
        try{
            autenticacao.signOut();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void openDialog(){
        DialogAddSpot dialogAddSpot = new DialogAddSpot();
        dialogAddSpot.show(getSupportFragmentManager(),"example dialog");
        dialogAddSpot.setCancelable(false);
    }

    @Override
    public void adicionarSpot(String latitude, String longitude, String descricao, Bitmap bitmap) {
        if(latitude != null && !latitude.equals("") && longitude != null && !longitude.equals("")){
            Double la = Double.valueOf(latitude);
            Double lo = Double.valueOf(longitude);
            Spots spot = new Spots();
            spot.setLatitude(la);
            spot.setLongitude(lo);
            String foto = "";
            if(descricao != null){
                if(!descricao.equals(""))
                    spot.setDescricao(descricao);
                else
                    spot.setDescricao("spot");
            }
            if(bitmap != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,baos);
                byte [] dadosImagem = baos.toByteArray();

                final String nomeImagem = UUID.randomUUID().toString();
                final StorageReference imagemRef = storageSpots.child(nomeImagem);
                UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Erro", "Erro ao fazer upload");
                        Toast.makeText(getApplicationContext(),"Erro ao fazer upload de imagem",Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri url =  task.getResult();
                                urlGambiarra = url;

                            }
                        });
                    }
                });
            }
            spot.setFoto(foto);
            if(urlGambiarra != null){
                spot.setFoto(urlGambiarra.toString());
                urlGambiarra = null;
            }

            databaseSpots.push().setValue(spot);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarSpots();
        atualizarMapa();
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseSpots.removeEventListener(valueEventListener);
    }

    public void recuperarSpots(){
        valueEventListener = databaseSpots.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    spots.clear();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Spots spot = snap.getValue(Spots.class);
                        Log.i("teste",snap.toString());
                        spots.add(spot);
                    }
                    atualizarMapa();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarMapa(){
        if(mMap != null){
            mMap.clear();
            for(Spots spot : spots){
                mMap.addMarker(new MarkerOptions().
                        position(new LatLng(spot.getLatitude(),spot.getLongitude())).
                        title(spot.getDescricao())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            }
        }
    }
}