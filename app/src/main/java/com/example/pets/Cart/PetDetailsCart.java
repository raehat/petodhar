package com.example.pets.Cart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.R;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetDetailsCart extends AppCompatActivity {

    private ImageView imageView3;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("CART BOOK DETAILS");
        final FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final TextView book= (TextView) findViewById(R.id.pet);
        final TextView amount= (TextView) findViewById(R.id.amount);
        final TextView locality= (TextView) findViewById(R.id.locality);
        final TextView description= (TextView) findViewById(R.id.description);
        final TextView city= (TextView) findViewById(R.id.city);
        final Button mobile= (Button) findViewById(R.id.mobile);
        final FloatingActionButton floatingActionButton= (FloatingActionButton) findViewById(R.id.floatingActionButton);

        final String passedArg = getIntent().getExtras().getString("temp");
        final Integer pos= getIntent().getExtras().getInt("position");


        DocumentReference documentReference=fstore.collection("books").document(passedArg);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book.setText(documentSnapshot.getString("book"));
                amount.setText(documentSnapshot.getString("amount"));
                city.setText(documentSnapshot.getString("city"));
                description.setText(documentSnapshot.getString("description"));
                locality.setText(documentSnapshot.getString("locality"));
                call=documentSnapshot.getString("mobile");
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + call));
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(PetDetailsCart.this).setTitle("Remove from Cart?")
                        .setMessage("Do you want to remove this book from the cart?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String userID = mAuth.getCurrentUser().getUid();
                                final DocumentReference documentRef= fstore.collection("users").document(userID);
                                documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override

                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        String cart = documentSnapshot.getString("cart");
                                        List<String> stringsBook = Arrays.asList(cart.split(",", 0));
                                        List<String> stringList= new ArrayList<>();
                                        for (String item: stringsBook) {
                                            if (item.contains(passedArg)) {

                                            }
                                            else
                                            {
                                                stringList.add(item);
                                            }
                                        }
                                        String listString = String.join(",", stringList);

                                        Map<String, Object> userJii= new HashMap<>();

                                        userJii.put("cart", listString);

                                        documentRef.update(userJii);

                                    }
                                });
                            }
                        }).setNegativeButton("no", null).show();
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