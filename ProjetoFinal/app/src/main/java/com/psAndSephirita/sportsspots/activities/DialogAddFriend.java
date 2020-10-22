package com.psAndSephirita.sportsspots.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.psAndSephirita.sportsspots.R;

public class DialogAddFriend extends AppCompatDialogFragment {

    private EditText editTextEmail;
    private DialogAddFriendListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        builder.setView(view)
                .setTitle("Adicionar amigo")
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = editTextEmail.getText().toString();
                listener.adicionarAmigo(email);
            }
        });

        editTextEmail = view.findViewById(R.id.editTextEmailAddContato);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (DialogAddFriendListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogAddFriendListener");
        }
    }

    public interface DialogAddFriendListener{
        void adicionarAmigo(String email);
    }
}
