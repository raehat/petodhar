package com.example.pets.beforeAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Arrays;
import java.util.List;

public class register extends AppCompatActivity {

    EditText editTextPhone;
    CountryCodePicker ccp;
    String number;
    EditText name, address;
    TextView city, pinn;
    public String codeSent;
    String CITY, PIN;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    List<String> phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("REGISTER");
        fstore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        CITY= getIntent().getExtras().getString("city");
        PIN= getIntent().getExtras().getString("pin");

        Button button3 = findViewById(R.id.button4);
        name= findViewById(R.id.name);
        address= findViewById(R.id.address);
        city= findViewById(R.id.city);
        pinn= findViewById(R.id.pinn);
        editTextPhone = findViewById(R.id.no);
        ccp = findViewById(R.id.ccp);

        city.setText(CITY);
        pinn.setText(PIN);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp();
            }
        });
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void getOtp() {
        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Just a moment");
        progressDialog.show();
        number = "+" + ccp.getFullNumber() + editTextPhone.getText().toString();

        final DocumentReference documentReference= fstore.collection("phone").document("phone");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String mobile=documentSnapshot.getString("phone");
                phones= Arrays.asList(mobile.split(",",0));
                if (phones.contains(number))
                {
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), enterOTP.class);
                    intent.putExtra("temp", number);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("address", address.getText().toString());
                    intent.putExtra("pinn", PIN);
                    intent.putExtra("city", CITY);
                    progressDialog.dismiss();
                    startActivity(intent);
                }
            }
        });




    }


}

