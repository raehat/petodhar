package com.example.pets.beforeAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pets.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;
import java.util.List;

public class login extends AppCompatActivity {
    Button button;
    EditText editText2;
    CountryCodePicker ccp;
    String phone;
    private FirebaseFirestore fstore;
    private List<String> ListM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fstore= FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        editText2= (EditText) findViewById(R.id.editText);
        ccp= (CountryCodePicker) findViewById(R.id.countryCodePicker);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("LOGIN");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                final ProgressDialog progressDialog= new ProgressDialog(login.this);
                progressDialog.setMessage("just a sec");
                progressDialog.show();
                final String number= "+" + ccp.getFullNumber() + editText2.getText().toString();
                DocumentReference documentReference= fstore.collection("phone").document("phone");
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String mobiles= documentSnapshot.getString("phone");
                                ListM= Arrays.asList(mobiles.split(",",0));
                                if (ListM.contains(number))
                                {
                                    Intent intent= new Intent(getApplicationContext(), enterOTPforLogin.class);
                                    intent.putExtra("number", number);
                                    startActivity(intent);
                                    progressDialog.dismiss();;
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });}
                catch (Exception e)
                {
                    Toast.makeText(login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}