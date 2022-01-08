package com.example.bookstore.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookstore.Firebase;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProductModel implements Serializable {
    private static final long serialVersionUID = 1L; //
    private String name;
    private String price;
    private String owner;
    private int priceTmp;
    private Integer quantity;
    private String description;
    private String id = "SP1";
    private int Img;

    private static final String TAG = "Cloud Firestore";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference myRef = database.child("users");


    public ProductModel(){
        id = "";
    }

    public ProductModel(ProductModel productModel){
        name = productModel.name;
        price = productModel.price;
        priceTmp = productModel.priceTmp;
        quantity = 1;
        description = productModel.description;
        Img = productModel.Img;
        this.id = productModel.id;
    }

    public ProductModel(String name, String price, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.priceTmp = Integer.valueOf(price);
    }

    public ProductModel(String owner, int img, String price, String name, String description) {
        this.owner = owner;
        this.Img = img;
        this.price = price;
        this.priceTmp = Integer.valueOf(price);
        this.name = name;
        this.description = description;
    }

    public ProductModel(String id ,String owner, int img, String price, String name, String description) {
        this.id = id;
        this.owner = owner;
        this.Img = img;
        this.price = price;
        this.priceTmp = Integer.valueOf(price);
        this.name = name;
        this.description = description;
    }



    public ProductModel(int img, String name, int priceTmp, Integer quantity, String description) {
        this.name = name;
        this.priceTmp = priceTmp;
        this.price = String.valueOf(priceTmp);
        this.quantity = quantity;
        this.description = description;
        Img = img;
    }

    public String getID() {
        return id;
    }

    public int getImg() {
        return Img;
    }

    public String getName() {
        return name;
    }

    public int getPriceTmp(){return priceTmp;}

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
        this.priceTmp = Integer.valueOf(price);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(int img) {
        Img = img;
    }

    //------------------------------------------------------------------------------------
    //Backend
    public static ProductModel getProductWithID(String productID, IQuery iQuery){
        ProductModel productModel = new ProductModel();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(productID)){
                                    Log.i(TAG, document.getId() + " -> " + document.getData());

                                    String owner = (String) document.get("owner");

                                    long img = (long) document.get("img");
                                    String price = (String) document.get("price");
                                    String name = (String) document.get("name");
                                    String description = (String) document.get("description");

                                    productModel.setDescription(description);
                                    productModel.setName(name);
                                    productModel.setPrice(price);
                                    productModel.setImg((int) img);
                                    productModel.setOwner(owner);
                                    productModel.setId(productID);
                                    productModel.setQuantity(1);
                                }
                            }
                            ArrayList<ProductModel> productModels = new ArrayList<>();
                            productModels.add(productModel);
                            iQuery.onSuccess(productModels);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return productModel;
    }

    public static void getAllProduct(IQuery iQuery){
        ArrayList<ProductModel> productList = new ArrayList<>();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.i(TAG, document.getId() + " -> " + document.getData());

                                String owner = (String) document.get("owner");

                                int img = (int) R.drawable.nha_gia_kim;
                                String price = (String) document.get("price");
                                String name = (String) document.get("name");
                                String description = (String) document.get("description");

                                ProductModel productModel = new ProductModel();
                                productModel.setDescription(description);
                                productModel.setName(name);
                                productModel.setPrice(price);
                                productModel.setImg((int) img);
                                productModel.setOwner(owner);
                                Log.i("IDCheck", "ID : " + document.getId());
                                productModel.setId(document.getId());

                                productList.add(productModel);
                            }
                            iQuery.onSuccess(productList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    public static ArrayList<ProductModel> getCart(String userID, IQuery iQuery){
        ArrayList<ProductModel> productsOnCart = new ArrayList<>();
        getAllProduct(new IQuery() {
            @Override
            public void onSuccess(ArrayList<ProductModel> productModels) {
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getId().equals(userID)){
                                            ArrayList<String> cart = (ArrayList<String>) document.get("cart");

                                            for (String idProductOnCart : cart){
                                                Log.i("PROCESSCART", "ID : " +  idProductOnCart);
                                                String idProduct = idProductOnCart.split("-")[1];
                                                int numberProduct = Integer.valueOf(idProductOnCart.split("-")[0]);

                                                for (ProductModel product : productModels)
                                                    if (product.getID().equals(idProduct)){
                                                        product.setQuantity(numberProduct);
                                                        productsOnCart.add(product);
                                                        break;
                                                    }


                                            }
                                        }
                                    }

                                    iQuery.onSuccess(productsOnCart);
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });


        Log.i("PROCESSCART", "product : " + productsOnCart);
        return productsOnCart;
    }

    public interface IQuery{
        void onSuccess(ArrayList<ProductModel> productModels);
    }

    public void addToCart(String userID, String productID) {
//        Log.i("hello", "" +  myRef.child(userID).child("password").setValue("123456"));
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(userID)){
//                                    String[] cartRaw = (String[]) document.get("cart");
                                    ArrayList<String> cart = (ArrayList<String>) document.get("cart");
//                                    if (cart != null)
//                                        cart = new ArrayList<>();

                                    int count = 0;
                                    for (String productIdCart : cart){
                                        String[] rawData = productIdCart.split("-");
                                        int numberProduct = Integer.valueOf(rawData[0]);
                                        String productIDOnCart = rawData[1];

                                        if (productIDOnCart.equals(productID)){
                                            numberProduct++;
                                            cart.set(count, String.valueOf(numberProduct) + "-" + productIDOnCart);

                                            Map<String, Object> addCart = new HashMap<String, Object>();
                                            addCart.put("cart", cart);

                                            db.collection("users")
                                                    .document(userID)
                                                    .update(addCart);
                                            return;
                                        }
                                        count++;

                                    }

                                    cart.add("1-" + productID);
                                    Map<String, Object> addCart = new HashMap<String, Object>();
                                    addCart.put("cart", cart);

                                    db.collection("users")
                                            .document(userID)
                                            .update(addCart);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void incDecProductOnCart(String userID, String productID, boolean isIncrease ) {
//        Log.i("hello", "" +  myRef.child(userID).child("password").setValue("123456"));
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> changeCart = new HashMap<String, Object>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(userID)){
//                                    String[] cartRaw = (String[]) document.get("cart");
                                    ArrayList<String> cart = (ArrayList<String>) document.get("cart");
                                    int amountNumber = (isIncrease) ? 1 : -1;

                                    int count = 0;
                                    int indexRemove = -1;
                                    for (String productIdCart : cart){
                                        String[] rawData = productIdCart.split("-");
                                        int numberProduct = Integer.valueOf(rawData[0]);
                                        String productIDOnCart = rawData[1];

                                        if (productIDOnCart.equals(productID)){
                                            numberProduct = numberProduct + amountNumber;

                                            if (numberProduct == 0) {
                                                indexRemove = count;
                                            }
                                            else {
                                                cart.set(count, String.valueOf(numberProduct) + "-" + productIDOnCart);
                                                changeCart.put("cart", cart);
                                            }
                                            break;
                                        }
                                        count++;

                                    }

                                    Log.i("CARTPROCESS", "" + changeCart.toString());


                                    if (indexRemove != -1) {
                                        cart.remove(indexRemove);
                                        changeCart.put("cart", cart);
                                    }

                                    db.collection("users")
                                            .document(userID)
                                            .update(changeCart);
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