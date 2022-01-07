package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookstore.fragment.HomePageFragment;

/**
 * Author : Hao
 * Describe : This is Main Activity for Admin
 */
public class MainSellerActivity extends AppCompatActivity implements View.OnClickListener {
    Button productButton;
    Button profileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_seller);
        productButton = findViewById(R.id.addPro);
        productButton.setOnClickListener(this);
        //initData();
        profileButton = findViewById(R.id.profile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), infoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), PostProductActivity.class);
        startActivity(intent);
    }

    /**
     * Author : Hao
     * Describe : Using to taking first data when activity is onCreate()
     */
//    void initData(){
//
//    }
}
