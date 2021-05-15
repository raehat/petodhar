package com.example.pets;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductsViewHolder extends RecyclerView.ViewHolder {
    public TextView mBook;
    public TextView mAmount;
    public ImageView mImageView;
    public TextView mLocation;
    public ProgressBar progressBar;

    public ProductsViewHolder(View itemView, final MyRecyclerViewAdapter.OnItemClickListener listener) {
        super(itemView);

        mBook = (TextView) itemView.findViewById(R.id.list_book);
        mAmount = (TextView) itemView.findViewById(R.id.list_amount);
        mImageView= (ImageView) itemView.findViewById(R.id.bookImage);
        mLocation= (TextView) itemView.findViewById(R.id.mLocation);
        progressBar= (ProgressBar) itemView.findViewById(R.id.progressBar);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    int position= getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            }
        });


    }

}
