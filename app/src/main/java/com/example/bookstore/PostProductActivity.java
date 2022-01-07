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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PostProductActivity extends AppCompatActivity {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Cloud Firestore";
    private static EditText productNameET, priceET, descriptionET;
    private static String productName, price, description;
    private static Button addProduct;
    private String userID;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_seller);

        productNameET = (EditText) findViewById(R.id.productNameInput);
        priceET = (EditText) findViewById(R.id.priceInput);
        descriptionET = (EditText) findViewById(R.id.descField);
        addProduct = (Button) findViewById(R.id.addProductBtn);
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("MY_ID","");
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productName = productNameET.getText().toString();
                price = priceET.getText().toString();
                description = descriptionET.getText().toString();

                Log.d(TAG, "Product name:" + productName);
                Log.d(TAG, "Price: " + price);
                Log.d(TAG, "Description: " + description);

                if (productName.equals("") || price.equals("")) {
                    Toast.makeText(PostProductActivity.this, "Bạn cần phải điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> newProduct = new HashMap<String, Object>();
                    newProduct.put("name", productName);
                    newProduct.put("price", price);
                    newProduct.put("owner", userID);
                    newProduct.put("img", "String");
                    newProduct.put("description", description);
                    db.collection("products")
                            .add(newProduct)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Đăng sản phẩm thành công", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Intent intent = new Intent(getApplicationContext(), MainSellerActivity.class);
                                    startActivity(intent);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostProductActivity.this, "Xảy ra lỗi. Xin vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });

                }
            }
        });
    }
}
