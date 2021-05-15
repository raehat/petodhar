package com.example.pets.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pets.MyRecyclerViewAdapter;
import com.example.pets.ProductModel;
import com.example.pets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartFragment extends Fragment {
    private Button button2;
    private TextView textView2, textView3, textView4,textView5,textView6;
    RecyclerView recyclerView;
    ArrayList<ProductModel> userArrayList;
    String petValue;
    List<String> strings;
    MyRecyclerViewAdapter adapter;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userID;
    TextView tt;
    TextView tt2;
    ProgressBar progressBar;
    TextView add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_cart, container, false);

        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        tt= (TextView) view.findViewById(R.id.tt);
        tt2= (TextView) view.findViewById(R.id.tt2);
        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.text_view_anim);
        tt.startAnimation(animation);
        tt2.startAnimation(animation);
        TextView testValue= view.findViewById(R.id.testValue);
        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        add= view.findViewById(R.id.add);

        progressBar.setVisibility(View.VISIBLE);
        userArrayList= new ArrayList<>();
        userID=mAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fstore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {

                petValue =documentSnapshot.getString("cart");
                if (petValue !=null){
                strings= Arrays.asList(petValue.split(",", 0));
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                rootRef.collection("pets")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot querySnapshot: task.getResult()){
                                    ProductModel user= new ProductModel(querySnapshot.getString("breed"),querySnapshot.getString("amount"), querySnapshot.getId(),
                                            querySnapshot.getString("description"));

                                    if (strings.contains(user.getUserIDBook())){
                                        userArrayList.add(user);}

                                }
                                if (userArrayList.size()==0)
                                {
                                    Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
                                }
                                adapter = new MyRecyclerViewAdapter(getContext(), userArrayList);
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent= new Intent(getContext(), PetDetailsCart.class);
                                        intent.putExtra("position", position);
                                        intent.putExtra("temp", userArrayList.get(position).getUserIDBook());
                                        startActivity(intent);
                                    }
                                });

                            }
                        });}
                else if (petValue ==null|| petValue.isEmpty())
                {
                    add.setText("CART IS EMPTY!!");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
}
