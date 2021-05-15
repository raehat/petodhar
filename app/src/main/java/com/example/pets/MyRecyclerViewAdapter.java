package com.example.pets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<ProductsViewHolder> implements Filterable {
    public FirestoreRecyclerAdapter<ProductModel, ProductsViewHolder> adapter;
    public OnItemClickListener onItemClickListener;

    List<ProductModel> fullList;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    public Filter getFilter() {
        return FilterUser;
    }

    public Filter FilterUser = new Filter() {
        @Override
        public FilterResults performFiltering(CharSequence charSequence) {
            String searchText = charSequence.toString().toLowerCase();
            List<ProductModel> tempList = new ArrayList<>();
            if (searchText.length() == 0 || searchText.isEmpty()) {
                tempList.addAll(fullList);
            } else {
                for (ProductModel item : fullList) {
                    if (item.getBook().toLowerCase().contains(searchText)) {
                        tempList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=tempList;
            return filterResults;
        }

        @Override
        public void publishResults(CharSequence constraint, FilterResults filterResults) {
            userArrayList.clear();
            userArrayList.addAll((Collection<? extends ProductModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener= listener;
    }

    public MyRecyclerViewAdapter(Context buyFragment, ArrayList<ProductModel> userArrayList) {
        this.buyFragment= buyFragment;
        this.userArrayList = userArrayList;
        fullList= new ArrayList<>(userArrayList);
    }

    Context buyFragment;
    ArrayList<ProductModel> userArrayList;



    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(buyFragment.getApplicationContext());



        View view= layoutInflater.inflate(R.layout.list_amount_single, parent, false);
        return new ProductsViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder holder, int position) {

        /*ProductModel productModel = new ProductModel();
        holder.mBook.setText(productModel.getBook());
        holder.mAmount.setText(productModel.getAmount());*/
        holder.mBook.setText(userArrayList.get(position).getBook());
        holder.mAmount.setText("₹" + userArrayList.get(position).getAmount());
        holder.mLocation.setText(" ಠ_ಠ" + userArrayList.get(position).getLocation());
        holder.progressBar.setVisibility(View.VISIBLE);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("images/" + userArrayList.get(position).getUserIDBook());
        File localFile = null;
        try {
            localFile = File.createTempFile(userArrayList.get(position).getUserIDBook(), "jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        storageReference.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap= BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        holder.progressBar.setVisibility(View.GONE);
                        holder.mImageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

}
