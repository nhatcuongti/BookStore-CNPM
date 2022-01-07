package com.example.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.fragment.HomePageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    TextView register;
    EditText username, password;
    Button btn_login;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Cloud Firestore";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        //cài đặt textview đăng ký
        register = (TextView) findViewById(R.id.dangky);
        username = (EditText)findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        btn_login = (Button)findViewById(R.id.login_btn);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Username:" + username.getText().toString());
                Log.d(TAG, "Password: " + password.getText().toString());
                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Bạn cần phải điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    db.collection("users")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean have = false;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(username.getText().toString().equals(document.getId())){
                                                if(password.getText().toString().equals(document.get("password").toString())){
                                                    have = true;
                                                    if(document.get("role").toString().equals("Người mua hàng")){
                                                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("MY_ID", document.getId());
                                                        editor.commit();
                                                        Intent intent = new Intent(getApplicationContext(), MainBuyerHomepageActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("MY_ID", document.getId());
                                                        editor.commit();
                                                        Intent intent = new Intent(getApplicationContext(), MainSellerActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    break;
                                                }
                                            }
//                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                        }
                                        if(!have)
                                            Toast.makeText(LoginActivity.this, "Sai mật khẩu hoặc tài khoản", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(LoginActivity.this, "Testing", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);


            }
        });
    }
}
