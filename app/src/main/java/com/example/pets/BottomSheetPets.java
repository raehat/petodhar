package com.example.pets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BottomSheetPets extends BottomSheetDialogFragment {

    EditText book;
    EditText amount;
    EditText description;
    EditText city;
    EditText locality;
    String bookID;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    Button button5;
    String books, cart;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottomsheet_pets, container, false);

        mAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();

        book= (EditText) view.findViewById(R.id.name);
        amount= (EditText) view.findViewById(R.id.phone);
        description= (EditText) view.findViewById(R.id.address);
        city= (EditText) view.findViewById(R.id.city);
        locality= (EditText) view.findViewById(R.id.pin);
        button5= (Button) view.findViewById(R.id.button5);

        bookID=this.getArguments().getString("key");
        final DocumentReference documentReference=fstore.collection("books").document(bookID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {

                /*User user = documentSnapshot.toObject(User.class);*/
                book.setText(documentSnapshot.getString("book"));
                amount.setText(documentSnapshot.getString("amount"));
                locality.setText(documentSnapshot.getString("locality"));
                city.setText(documentSnapshot.getString("city"));
                description.setText(documentSnapshot.getString("description"));
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user= new HashMap<>();
                user.put("book", book.getText().toString());
                user.put("amount", amount.getText().toString());
                user.put("locality", locality.getText().toString());
                user.put("city", city.getText().toString());
                user.put("description", description.getText().toString());
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
