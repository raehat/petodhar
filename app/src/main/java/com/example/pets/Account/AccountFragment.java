package com.example.pets.Account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.pets.R;
import com.example.pets.beforeAuth.SaveSharedPreference;
import com.example.pets.beforeAuth.login;
import com.example.pets.beforeAuth.loginOrRegister;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFragment extends Fragment {
    private Button button2;
    private TextView textView2, textView3, textView4,textView5,textView6;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userID;
    FirebaseStorage storage;
    StorageReference storageReference;
    private List<String> list;
    private List<String > StringList= new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_account, container, false);
        button2= view.findViewById(R.id.button2);
        textView2= view.findViewById(R.id.testValue);
        textView3= view.findViewById(R.id.textView3);
        textView4= view.findViewById(R.id.textView4);
        textView5= view.findViewById(R.id.textView5);
        textView6= view.findViewById(R.id.textView6);
        Button button4= view.findViewById(R.id.button4);
        Button delete= (Button) view.findViewById(R.id.delete);
        TextView tran= (TextView) view.findViewById(R.id.tran);
        TextView tran2= (TextView) view.findViewById(R.id.tran2);
        storage= FirebaseStorage.getInstance();

        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.text_view_anim);

        fstore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        userID=mAuth.getCurrentUser().getUid();
        final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);
        tran.startAnimation(animation);
        tran2.startAnimation(animation);
        DocumentReference documentReference=fstore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override

            public void onSuccess(DocumentSnapshot documentSnapshot) {

                dialog.dismiss();
                textView2.setText(documentSnapshot.getString("fname"));
                textView3.setText(documentSnapshot.getString("address"));
                textView4.setText(documentSnapshot.getString("pin"));
                textView5.setText(documentSnapshot.getString("City"));
                textView6.setText(documentSnapshot.getString("number"));
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet= new BottomSheet();
                bottomSheet.show(getFragmentManager(), "bottomSheet");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Log out?")
                        .setMessage("Do you really want to logout from this account?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                SaveSharedPreference.setLoggedIn(getActivity(), false);
                                Intent intent=new Intent(getContext(), loginOrRegister.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you really want to delete this account. All the information" +
                                " and all the books assosiated with this account will be permanently deleted.")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userID=mAuth.getCurrentUser().getUid();
                                final DocumentReference documentReference=fstore.collection("users").document(userID);
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String booksValue= documentSnapshot.getString("books");
                                        final String phoneJi= documentSnapshot.getString("number");
                                        if (booksValue!=null)
                                        {
                                            final List<String> strings= Arrays.asList(booksValue.split(",", 0));
                                            for (String item: strings)
                                            {
                                                FirebaseFirestore rootRef= FirebaseFirestore.getInstance();
                                                rootRef.collection("books")
                                                        .document(item).delete();
                                                storageReference= storage.getReference().child("images/" + item);
                                                storageReference.delete();

                                            }

                                        }
                                        final DocumentReference documentReference1= fstore.collection("phone")
                                                .document("phone") ;
                                        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        String mobile= documentSnapshot.getString("phone");
                                                        list=  Arrays.asList(mobile.split(",",0));
                                                        Map<String, Object> userBe= new HashMap<>();

                                                        for (String s : list)
                                                        {
                                                            if (s.contains(phoneJi))
                                                            {}
                                                            else
                                                            {
                                                                StringList.add(s);
                                                            }
                                                        }
                                                        String listStringg= String.join(",", StringList);
                                                        Toast.makeText(getContext(), "" + StringList.size(), Toast.LENGTH_LONG).show();
                                                        userBe.put("phone", listStringg);
                                                        documentReference1.set(userBe);
                                                    }
                                                });
                                        fstore.collection("users").document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        mAuth.getCurrentUser().delete();
                                        SaveSharedPreference.setLoggedIn(getActivity(), false);
                                        Intent intent=new Intent(getContext(), login.class);
                                        startActivity(intent);

                                    }
                                });
                            }
                        })
                        .setNegativeButton("no", null)
                        .show();
            }
        });

        return view;
    }
}





