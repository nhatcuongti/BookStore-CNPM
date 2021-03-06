package com.example.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookstore.fragment.HomePageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Author : Hao
 * Describe : This is Main Activity for Admin
 */
public class infoActivity extends AppCompatActivity {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    FragmentManager fragmentManager = getSupportFragmentManager();
    String username;
    String password = "";
    TextView hello, address, role;
    EditText input_password, repassword;
    Button changePW, logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("MY_ID","");

        changePW = (Button)findViewById(R.id.info_btnChangePassword);
        logout = (Button) findViewById(R.id.info_logout);
        input_password = (EditText)findViewById(R.id.info_password);
        repassword = (EditText)findViewById(R.id.info_repassword);
        initData();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        changePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(input_password.getText().toString().equals("") || repassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "B???n c???n ph???i ??i???n m???t kh???u m???i", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println(input_password.getText().toString());
                    System.out.println(repassword.getText().toString());
                    if(input_password.getText().toString().equals(repassword.getText().toString())){
                        db.collection("users").document(username).update("password", input_password.getText().toString());
                        Toast.makeText(getApplicationContext(), "?????i m???t kh???u th??nh c??ng", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "M???t kh???u kh??ng tr??ng kh???p", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
    void initData(){

        hello = (TextView) findViewById(R.id.info_hello);
        address = (TextView) findViewById(R.id.info_address);
        role = (TextView)findViewById(R.id.info_role);


        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(username.equals(document.getId())){
                                    hello.setText(hello.getText()+document.get("firstname").toString() + " " + document.get("lastname").toString());
                                    address.setText(address.getText()+document.get("address").toString());
                                    role.setText(role.getText()+document.get("role").toString());
                                    password = document.get("password").toString();
                                }
                            }
                        }
                    }
                });
    }


}
