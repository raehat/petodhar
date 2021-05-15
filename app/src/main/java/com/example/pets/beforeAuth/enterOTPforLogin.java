package com.example.pets.beforeAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.pets.R;
import com.example.pets.screen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class enterOTPforLogin extends AppCompatActivity {

    String codeSent;
    String phone1;
    PinView kk;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_pfor_login);

        getSupportActionBar().hide();
        phone1= getIntent().getExtras().getString("number");
        kk= (PinView) findViewById(R.id.kk);
        mAuth= FirebaseAuth.getInstance();

        sendVerificationCode();;


        new AlertDialog.Builder(enterOTPforLogin.this)
                .setTitle("Enter the OTP")
                .setMessage("You will recieve OTP on the" + phone1 + " in a moment. Please enter that OTP here" +
                        "to confirm your phone number").
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
        final String code = kk.getText().toString();
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


                            Toast.makeText(enterOTPforLogin.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "" + SaveSharedPreference.getLoggedStatus(getApplicationContext()), Toast.LENGTH_LONG).show();
                            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                            Toast.makeText(getApplicationContext(), "" + SaveSharedPreference.getLoggedStatus(getApplicationContext()), Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(enterOTPforLogin.this, screen.class);
                            dialog.dismiss();
                            startActivity(intent);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(enterOTPforLogin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void sendVerificationCode() {


        String phone = phone1;

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
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(enterOTPforLogin.this, "OTP on it's way", Toast.LENGTH_SHORT).show();
            codeSent = s;

        }
    };
}