package com.example.pets.beforeAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.pets.R;

public class getLocationBS extends AppCompatDialogFragment {

    @NonNull

    private  EditText city, pin;
    private getLocationListener listener;

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        LayoutInflater inflater= getActivity().getLayoutInflater();

        View view= inflater.inflate(R.layout.activity_get_location_b_s, null);

        builder.setView(view)
                .setTitle("Information")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String CITY= city.getText().toString();
                        String PIN= pin.getText().toString();
                        listener.applyTexts(CITY, PIN);
                    }
                });

        city= view.findViewById(R.id.city);
        pin= view.findViewById(R.id.pin);



        return builder.create();
    }
    public void onAttach(Context context) {

        super.onAttach(context);

        listener= (getLocationListener) context;
    }

    public interface getLocationListener
    {
        void applyTexts(String CITY, String PIN);
    }

}