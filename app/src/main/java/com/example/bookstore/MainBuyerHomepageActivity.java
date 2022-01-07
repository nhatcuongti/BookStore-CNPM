package com.example.bookstore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.adapters.ProductListAdapter;
import com.example.bookstore.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
/**
 * Created by reiko-lhnhat on 12/3/2021.
 */
public class MainBuyerHomepageActivity extends AppCompatActivity implements ProductListAdapter.OnProductListener {
    DrawerLayout drawerLayout;
    BottomNavigationView navigationView;
    Toolbar toolBar;
    EditText searchView;

    private ArrayList<ProductModel> listProduct  = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductListAdapter productListAdapter;
    private ImageButton goToCartBtn;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Cloud Firestore";


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_buyer);




        initTopAndBottomBar();
        initData();

        searchView = findViewById(R.id.search_bar_view);
        //set recycle


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        goToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainBuyerHomepageActivity.this, BuyerCartActivity.class);
                MainBuyerHomepageActivity.this.startActivity(intent);
            }
        });
    }

    /**
     * Initialize Action Bar on top and bottom
     */
    void initTopAndBottomBar(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_bot);
        toolBar = findViewById(R.id.toolbar);
        goToCartBtn = findViewById(R.id.carBtn_homepageBuyer);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.bottom_nav_buyer_profile:
                        Intent intent = new Intent(getApplicationContext(), infoActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
//        setSupportActionBar(toolBar);
//
//        ToggleButton toggle = new ToggleButton(MainBuyerHomepageActivity.this);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.();
    }

    public void initData(){
//        listProduct = new ArrayList<>();
//        listProduct.add(new ProductModel("haobu01", "20000", 20, "abc"));
//        listProduct.add(new ProductModel("hiếu 10 mái", "20000", 19,"cdcd"));
//        listProduct.add(new ProductModel("hê lô bé Lê Dzan Đạt", "20000", 17,"Bé Tên Lê Dăn Đạt, quê quán TP HCM" +
//                " hiện đang sống và làm việc tại tp Hồ Chí Minh, độc thân single boy, có chị và " +
//                "Lê Dăn Ngọc sống tại cam quýt. Bạn của bé tên Quách Triết, anh của bé tên Hào buỏi "));
//        listProduct.add(new ProductModel("Triết ông trùm đái đường", "20000", 20, "abc"));
//        listProduct.add(new ProductModel("lthieu", "20000", 19,"cdcd"));
//        listProduct.add(new ProductModel("nhatks14", "20000", 18,"cdc"));
//        listProduct.add(new ProductModel("DatAKC", "20000", 17,"bcd"));
//        listProduct.add(new ProductModel("DatAKC", "20000", 17,"bcd"));

        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel = new ProductModel(document.getId(), document.get("owner").toString(),
                                        R.drawable.nha_gia_kim, document.get("price").toString(),
                                        document.get("name").toString(), document.get("description").toString());
                                System.out.println(productModel.getName());
                                listProduct.add(productModel);
                            }
                            recyclerView = findViewById(R.id.list_product);
                            productListAdapter = new ProductListAdapter(listProduct, MainBuyerHomepageActivity.this);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                            recyclerView.setLayoutManager(gridLayoutManager);
                            recyclerView.setAdapter(productListAdapter);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }

    @Override
    public void onProductClick(int positionProduct) {
        ProductModel product = listProduct.get(positionProduct);
        Intent intent = new Intent(this, BuyerDetailProductActivity.class);
        intent.putExtra("clickProduct_ProductDetail", product);
        this.startActivity(intent);
    }
}
