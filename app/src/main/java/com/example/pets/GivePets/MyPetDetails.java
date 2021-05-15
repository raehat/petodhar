package com.example.pets.GivePets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.BottomSheetPets;
import com.example.pets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class MyPetDetails extends AppCompatActivity {

    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userID;
    String cart;
    FloatingActionButton floatingActionButton2;
    private ImageView imageView3;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri imageUri;
    private ImageView imageView;
    String passedArg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypet_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MY BOOK DETAILS");
        fstore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        final TextView book= (TextView) findViewById(R.id.pet);
        final TextView amount= (TextView) findViewById(R.id.amount);
        final TextView locality= (TextView) findViewById(R.id.locality);
        final TextView description= (TextView) findViewById(R.id.description);
        final TextView city= (TextView) findViewById(R.id.city);
        final TextView mobile= (TextView) findViewById(R.id.mobile);
        imageView3 = findViewById(R.id.imageView3);
        FloatingActionButton floatingActionButton= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton2= (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        storage = FirebaseStorage.getInstance();

        final String passedArg = getIntent().getExtras().getString("temp");


        DocumentReference documentReference=fstore.collection("books").document(passedArg);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {


                /*User user = documentSnapshot.toObject(User.class);*/
                book.setText(documentSnapshot.getString("book"));
                amount.setText(documentSnapshot.getString("amount"));
                city.setText(documentSnapshot.getString("city"));
                description.setText(documentSnapshot.getString("description"));
                locality.setText(documentSnapshot.getString("locality"));
                mobile.setText(documentSnapshot.getString("mobile"));
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(MyPetDetails.this).setTitle("Delete book from My Books")
                        .setMessage("Do you want to delete this book from My Books. If you delete, interested buyers" +
                                "won't be able to buy your book :/ ")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DocumentReference documentRef= fstore.collection("books").document(passedArg);
                                documentRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Book Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    }

                                });
                                storageReference=storage.getReference().child("images/" + passedArg);
                                storageReference.delete();
                            }
                        })
                        .setNegativeButton("no", null).show();
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetPets bottomSheet= new BottomSheetPets();
                Bundle bundle= new Bundle();
                bundle.putString("key", passedArg);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "bottomSheet");
            }
        });

        final ProgressDialog pd= new ProgressDialog(this);

        File localFile = null;
        try {
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

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void choosePicture() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri= data.getData();
            imageView3.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd= new ProgressDialog(this);
        pd.setTitle("Image is being uploaded!");
        pd.show();

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(MyPetDetails.this, "Upload Succesful", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(MyPetDetails.this, "fuck you!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent= (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }
}