package com.example.pets.Account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pets.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BottomSheet extends BottomSheetDialogFragment {

    EditText name;
    EditText phone;
    EditText address;
    EditText city;
    EditText pin;
    String userID;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    Button button5;
    String pets, cart;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottomsheet, container, false);

        mAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();

        name= (EditText) view.findViewById(R.id.name);
        phone= (EditText) view.findViewById(R.id.phone);
        address = (EditText) view.findViewById(R.id.address);
        city= (EditText) view.findViewById(R.id.city);
        pin= (EditText) view.findViewById(R.id.pin);
        button5= (Button) view.findViewById(R.id.button5);

        userID=mAuth.getCurrentUser().getUid();
        final DocumentReference documentReference=fstore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {

                /*User user = documentSnapshot.toObject(User.class);*/
                name.setText(documentSnapshot.getString("fname"));
                phone.setText(documentSnapshot.getString("address"));
                address.setText(documentSnapshot.getString("pin"));
                city.setText(documentSnapshot.getString("City"));
                pin.setText(documentSnapshot.getString("number"));
                pets = documentSnapshot.getString("pets");
                cart= documentSnapshot.getString("cart");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user= new HashMap<>();
                user.put("breed", name.getText().toString());
                user.put("number", phone.getText().toString());
                user.put("pin", pin.getText().toString());
                user.put("address", address.getText().toString());
                user.put("city", city.getText().toString());
                user.put("pets", pets);
                user.put("cart", cart);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "SUBMITTED", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}
