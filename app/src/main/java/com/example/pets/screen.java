package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pets.Account.AccountFragment;
import com.example.pets.Cart.CartFragment;
import com.example.pets.GetPets.GetPetsFragment;
import com.example.pets.GivePets.GivePetsFragment;
import com.example.pets.MyPets.MyPetsFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class screen extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView nameScreen;
    TextView numberScreen;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    private String userID;
    Fragment active;
    private Fragment buyFragment;
    private Fragment accountFragment;
    private Fragment sellFragment;
    private Fragment cartFragment;
    private Fragment myBooksFragment;
    public static String number;
    public static String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fstore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        nameScreen = (TextView) header.findViewById(R.id.nameScreen);
        numberScreen= (TextView) header.findViewById(R.id.numberScreen);

        userID=mAuth.getCurrentUser().getUid();
        final DocumentReference documentReference= fstore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nameScreen.setText(documentSnapshot.getString("fname"));
                numberScreen.setText(documentSnapshot.getString("number"));
                number= documentSnapshot.getString("number");
                city= documentSnapshot.getString("City");
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        BottomNavigationView bottomNav= findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        buyFragment= new GetPetsFragment();
        accountFragment= new AccountFragment();
        sellFragment= new GivePetsFragment();
        cartFragment= new CartFragment();
        myBooksFragment= new MyPetsFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, cartFragment).hide(cartFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, myBooksFragment).hide(myBooksFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, accountFragment).hide(accountFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, sellFragment).hide(sellFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, buyFragment).commit();
        active= buyFragment;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.nav_buy:
                    getSupportFragmentManager().beginTransaction().hide(active).show(buyFragment).commit();
                    active= buyFragment;
                    break;

                case R.id.nav_sell:
                    getSupportFragmentManager().beginTransaction().hide(active).show(sellFragment).commit();
                    active= sellFragment;
                    break;

                case R.id.nav_account:
                    getSupportFragmentManager().beginTransaction().hide(active).show(accountFragment).commit();
                    active= accountFragment;
                    break;

                case R.id.nav_cart:
                    getSupportFragmentManager().beginTransaction().hide(active).show(cartFragment).commit();
                    active= cartFragment;
                    break;

                case R.id.nav_myPets:
                    getSupportFragmentManager().beginTransaction().hide(active).show(myBooksFragment).commit();
                    active= myBooksFragment;
                    break;



            }


            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Do you want to quit? Hope you had a great time!")
                .setMessage("Maybe you have some assignments left to turn in lol, who knows")
                .setPositiveButton("YES, I WANT TO QUIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(screen.this);
                        finish();
                    }
                }).setNegativeButton("NOPE, I WON'T LEAVE", null).show();
    }

}