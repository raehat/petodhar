package com.example.pets.beforeAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.pets.R;
import com.example.pets.screen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class enterOTP extends AppCompatActivity {

    PinView editTextCode;
    String phoneTemp;
    String name;
    String address;
    String city;
    String pinn;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    private String userID;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_p);

        getSupportActionBar().hide();
        fstore= FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        phoneTemp= getIntent().getExtras().getString("temp");
        name= getIntent().getExtras().getString("name");
        address= getIntent().getExtras().getString("email");
        city= getIntent().getExtras().getString("city");
        pinn= getIntent().getExtras().getString("pinn");

        editTextCode= findViewById(R.id.kk);

        sendVerificationCode();


        new AlertDialog.Builder(enterOTP.this)
                .setTitle("Enter the OTP")
                .setMessage("You will recieve OTP on the" + phoneTemp + " in a moment. Please enter that OTP here" +
                        "to confirm your phone number (Also, browser may open before OTP is sent. This" +
                        "is because you have to fill a captcha to prove you're a human)").
                setNeutralButton("OK", null).
                show();

        Button button2= (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
        overridePendingTransition(R.anim.slide_down, R.anim.slide_down1);
    }

    private void verifySignInCode() {
        final String code = editTextCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Loading. Please wait...", true);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Getting values to store
                            //here you can open new activity
                            Toast.makeText(getApplicationContext(),
                                    "Login Successful", Toast.LENGTH_LONG).show();

                            userID= mAuth.getCurrentUser().getUid();
                            final DocumentReference documentReference= fstore.collection("users").document(userID);
                            Map<String, Object> user= new HashMap<>();
                            user.put("fname", name);
                            user.put("City", city);
                            user.put("number", phoneTemp);
                            user.put("pin", pinn);
                            user.put("address", address);
                            dialog.dismiss();
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(enterOTP.this, "User profile created", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                            final DocumentReference documentReference1= fstore.collection("phone")
                                    .document("phone");
                            documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String kk= documentSnapshot.getString("phone");
                                    Map<String, Object> user1= new HashMap<>();
                                    user1.put("phone", kk + "," + phoneTemp);
                                    documentReference1.set(user1);
                                }
                            });



                            Toast.makeText(enterOTP.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                            Intent intent=new Intent(enterOTP.this, screen.class);
                            startActivity(intent);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
    public void sendVerificationCode() {


        String phone = phoneTemp;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
            Toast.makeText(enterOTP.this, "OTP on it's way", Toast.LENGTH_SHORT).show();
        }
    };
}

