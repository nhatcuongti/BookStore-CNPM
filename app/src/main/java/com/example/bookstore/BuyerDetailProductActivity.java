package com.example.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.models.ProductModel;
import com.example.bookstore.utils.ManageLogCart;

/**
 * Created by reiko-lhnhat on 12/3/2021.
 */
public class BuyerDetailProductActivity extends AppCompatActivity {
    private ImageView imageProduct;
    private TextView  nameProduct;
    private TextView  priceProduct;
    private TextView  quantityProduct;
    private TextView  descriptionProduct;
    private ImageButton backward;
    private ImageButton cartBtn;
    private Button addToCartBtn;

    private ProductModel product = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_buyer);

        imageProduct = findViewById(R.id.imageProductatDetail);
        nameProduct = findViewById(R.id.nameProductatDetail);
        priceProduct = findViewById(R.id.priceProductatDetail);
//        quantityProduct = findViewById(R.id.quantityatDetail);
        descriptionProduct = findViewById(R.id.descriptionProduct);
        backward = findViewById(R.id.backward_from_detailproduct_buyer);
        addToCartBtn = findViewById(R.id.addtocart_detail_buyer);
        cartBtn = findViewById(R.id.button_cart_detail_buyer);


        Intent intent = getIntent();
        product = (ProductModel) intent.getSerializableExtra("clickProduct_ProductDetail");
        imageProduct.setImageResource(product.getImg());
        nameProduct.setText(product.getName());
        priceProduct.setText("Price: " + product.getPrice() +"vnđ");
//        quantityProduct.setText("Quanity: " + Integer.toString(product.getQuantity()));
        descriptionProduct.setText(product.getDescription());

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductModel productModel = new ProductModel(product);

                //Add database
                //----------------------------------------------
                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                String userID = sharedPreferences.getString("MY_ID","");
//                String userID = "haobui";
                productModel.addToCart(userID, productModel.getID());
                //----------------------------------------------

                ManageLogCart.writeProductToFile(productModel, getApplicationContext());
                Toast toast = Toast.makeText(BuyerDetailProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BuyerDetailProductActivity.this, BuyerCartActivity.class);
                BuyerDetailProductActivity.this.startActivity(intent1);
            }
        });
    }

}