package com.example.pets.GetPets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.pets.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class ViewPagerFragment extends Fragment {
    int currentPage = 0;
    Timer timer;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private List<String> bookUrls;
    TextView textViewUrl;

    public String bookUrl;
    private boolean flag= false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_pager_fragment, container, false);

        final ViewPager viewPager= view.findViewById(R.id.view_pager11);
        String string= this.getArguments().getString("key");
        bookUrls= Arrays.asList(string.split(",", 0));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(), bookUrls);
        viewPager.setAdapter(adapter);


        return view;
    }

}
