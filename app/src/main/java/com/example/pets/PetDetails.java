package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PetDetails extends AppCompatActivity {

    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userID;
    String cart;
    String mobile;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView imageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("BOOK DETAILS");
        fstore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        final TextView book= (TextView) findViewById(R.id.pet);
        final TextView amount= (TextView) findViewById(R.id.amount);
        final TextView locality= (TextView) findViewById(R.id.locality);
        final TextView description= (TextView) findViewById(R.id.description);
        final TextView city= (TextView) findViewById(R.id.city);
        final Button call= (Button) findViewById(R.id.mobile);
        FloatingActionButton floatingActionButton= (FloatingActionButton) findViewById(R.id.floatingActionButton);

        final String passedArg = getIntent().getExtras().getString("temp");


        DocumentReference documentReference=fstore.collection("books").document(passedArg);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {


                /*User user = documentSnapshot.toObject(User.class);*/
                book.setText(documentSnapshot.getString("book"));
                amount.setText("₹" + documentSnapshot.getString("amount"));
                city.setText(documentSnapshot.getString("city"));
                description.setText(documentSnapshot.getString("description"));
                locality.setText(documentSnapshot.getString("locality"));
                mobile= documentSnapshot.getString("mobile");
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(PetDetails.this).setTitle("Add to Cart?")
                        .setMessage("Do you want to add this book to your cart?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userID=mAuth.getCurrentUser().getUid();
                                final DocumentReference documentRef= fstore.collection("users").document(userID);
                                documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override

                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        cart= documentSnapshot.getString("cart");
                                        Map<String, Object> userJi= new HashMap<>();
                                        String temp= cart + "," + passedArg;
                                        if (cart==null)
                                        {
                                            userJi.put("cart" , passedArg);
                                        }
                                        else
                                        {
                                            userJi.put("cart", temp);
                                        }
                                        documentRef.update(userJi);
                                        Toast.makeText(getApplicationContext(), "Book Added Succesfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("no", null).show();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        });

        final ProgressDialog pd= new ProgressDialog(this);

        imageView3 = findViewById(R.id.imageView3);

        File localFile = null;
        try {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference().child("images/" + passedArg);
            localFile = File.createTempFile(passedArg, "jpeg");
            final File finalLocalFile = localFile;
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "l", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                            Bitmap bitmap= BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                            imageView3.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "dd" + exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    float percent= (float) (100.00 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pd.setMessage("Percentage: " + percent + "%" );
                    pd.show();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}