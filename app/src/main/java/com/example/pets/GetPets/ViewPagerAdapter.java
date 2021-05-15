package com.example.pets.GetPets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.pets.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageViewUrls;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore fstore;

    public ViewPagerAdapter(Context context, List<String> imageViewUrls) {
        this.context= context;
        this.imageViewUrls = imageViewUrls;
    }

    @Override
    public int getCount() {
        return imageViewUrls.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {


        View view= LayoutInflater.from(context).inflate(R.layout.cart_item, container, false);
        final ImageView imageView5= view.findViewById(R.id.imageView5);
        final TextView textView5= view.findViewById(R.id.textView5);
        final TextView textView6= view.findViewById(R.id.textView6);

        fstore= FirebaseFirestore.getInstance();
        DocumentReference documentReference= fstore.collection("books")
                .document(imageViewUrls.get(position));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textView5.setText(documentSnapshot.getString("book"));
                textView6.setText(documentSnapshot.getString("description"));
            }
        });

        /*final ImageView imageView= new ImageView(context);*/
        File localFile = null;
        try {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference().child("images/" + imageViewUrls.get(position));
            localFile = File.createTempFile(imageViewUrls.get(position), "png");
            final File finalLocalFile = localFile;
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                            /*imageView.setImageBitmap(bitmap);*/
                            /*container.addView(imageView);*/
                            imageView5.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        container.addView(view, position);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== object;
    }
}
