package com.example.pets.SwipeFeature;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pets.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public CardStackAdapter(Context context, List<ItemModel> items) {
        this.context= context;
        this.items = items;
    }

    private List<ItemModel> items;
    private Context context;

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.breed.setText(items.get(position).getBreed());
        holder.city.setText(items.get(position).getCity());
        holder.description.setText(items.get(position).getDescription());
        holder.amount.setText(items.get(position).getAmount());
        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + items.get(position).getMobile()));
                context.startActivity(intent);
                Toast.makeText(context, "" + items.get(position).getMobile(), Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(context, "" + items.get(position).getImageID(), Toast.LENGTH_SHORT).show();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("images/" + items.get(position).getImageID());
        File localFile = null;
        try {
            localFile = File.createTempFile(items.get(position).getImageID(), "jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        storageReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap= BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        holder.imageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                /*Toast.makeText(context, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "" + imageAdd, Toast.LENGTH_SHORT).show();
                holder.progressBar.setVisibility(View.GONE);*/
            }
        });


        Picasso.get()
                .load(items.get(position).getImageID())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button contact;
        TextView breed, amount, description, city;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            breed= itemView.findViewById(R.id.item_breed);
            amount= itemView.findViewById(R.id.item_amount);
            description= itemView.findViewById(R.id.item_description);
            city= itemView.findViewById(R.id.item_city);
            contact= itemView.findViewById(R.id.info_button);
            imageView= itemView.findViewById(R.id.item_image);
        }
    }
}
