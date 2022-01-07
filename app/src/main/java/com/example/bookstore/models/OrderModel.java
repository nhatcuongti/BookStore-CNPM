package com.example.bookstore.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderModel {
    private static final long serialVersionUID = 1L; //
    private String address;
    private String buyer;
    private String seller;
    private String phoneNumber;
    private String price;
    private String shipCost;
    private ArrayList<String> productList;


    private static final String TAG = "Cloud Firestore";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//    private static DatabaseReference myRef = database.child("users");


    public OrderModel(String address, String buyer, String phoneNumber, String price, String shipCost) {
        this.address = address;
        this.buyer = buyer;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.shipCost = shipCost;
        this.productList = null;
    }
    //------------------------------------------------------------------------------------
    //Backend

    public void addOrderToDatabase(){

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(buyer)){
                                    ArrayList<String> cart = (ArrayList<String>) document.get("cart");
                                    productList = cart;

                                    Map<String, Object> order = new HashMap<String, Object>();
                                    order.put("address", address);
                                    order.put("buyer", buyer);
                                    order.put("phoneNumber", phoneNumber);
                                    order.put("price", price);
                                    order.put("seller", seller);
                                    order.put("shipcost", shipCost);
                                    order.put("productList", cart);


                                    db.collection("orders")
                                            .add(order)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });




    }

    //------------------------------------------------------------------------------------

}
