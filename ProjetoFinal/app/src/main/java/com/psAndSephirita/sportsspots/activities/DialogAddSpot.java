package com.psAndSephirita.sportsspots.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.psAndSephirita.sportsspots.R;
import com.psAndSephirita.sportsspots.config.ConfiguracaoFirebase;

import java.io.ByteArrayOutputStream;

public class DialogAddSpot extends AppCompatDialogFragment {

    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private EditText editTextDescricao;
    private Button buttonCamera;
    private DialogAddSpotListener listener;
    private Bitmap fotoSpot;

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_add_spot,null);
        builder.setView(view)
                .setTitle("Adicionar Spot")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                if(fotoSpot == null){
//                    Toast.makeText(getContext(), "foto padrao", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getContext(), "foto show", Toast.LENGTH_SHORT).show();
//                }
                listener.adicionarSpot(editTextLatitude.getText().toString(),editTextLongitude.getText().toString(),editTextDescricao.getText().toString(),fotoSpot);
            }
        });
        fotoSpot = null;
        editTextLatitude = view.findViewById(R.id.editTextLatitude);
        editTextLongitude = view.findViewById(R.id.editTextLongitude);
        editTextDescricao = view.findViewById(R.id.editTextDescricaoSpotAdd);
        buttonCamera = view.findViewById(R.id.buttonAddSpot);
        editTextLatitude.setText(String.valueOf(MapsActivity.latitudeAtualDoClique));
        editTextLongitude.setText(String.valueOf(MapsActivity.longitudeAtualDoClique));

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "teste", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_CAMERA);
                }

            }
        });

        buttonCamera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(intent,SELECAO_GALERIA);
                }
                return true;
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (DialogAddSpotListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogAddFriendListener");
        }
    }

    public interface DialogAddSpotListener{
        void adicionarSpot(String latitude, String longitude, String descricao,Bitmap bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            Bitmap imagem = null;
            try{
                if(requestCode == SELECAO_CAMERA)
                    imagem = (Bitmap) data.getExtras().get("data");
                else{
                    Uri localImagemSelecionada = data.getData();
                    imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),localImagemSelecionada);
                }
                if(imagem != null) {
                    fotoSpot = imagem;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
