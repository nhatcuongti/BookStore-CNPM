package com.example.bookstore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.adapters.ItemAdapter;
import com.example.bookstore.models.OrderModel;
import com.example.bookstore.models.ProductModel;
import com.example.bookstore.utils.CustomLinearLayoutManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.StructuredQuery;
import com.google.firestore.v1.WriteResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements ItemAdapter.AdapterUpdate, ItemAdapter.OnProductListener, View.OnClickListener {

    ArrayList<ProductModel> listItems = null;
    RecyclerView rv;
    ItemAdapter ia;
    ImageButton backward = null;
    Button purchaseBtn;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_buyer);

        initData();
        View Payment_toolbar = findViewById(R.id.payment_toolbar_buyer);
        if (listItems == null || listItems.size() == 0)
            Payment_toolbar.setVisibility(View.GONE);
        else{
            Payment_toolbar.setVisibility(View.VISIBLE);

            ia = new ItemAdapter(listItems, this, this, this);

            rv.setAdapter(ia);
            CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(this);
            linearLayoutManager.setScrollEnabled(false);
            rv.setLayoutManager(linearLayoutManager);
        }


        backward = findViewById(R.id.backward_from_payment_buyer);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //TextView v = findViewById(R.id.totalCostProduct);
    }

    private void initData() {
        rv = findViewById(R.id.view);
        Intent intent = getIntent();
        listItems = (ArrayList<ProductModel>) intent.getSerializableExtra("listProduct_cart");

        purchaseBtn = findViewById(R.id.purchaseBtn);
        purchaseBtn.setOnClickListener(this);

        Log.i("testInit", "initData: " + listItems);
//        listItems.add(new ProductModel(R.drawable.dac_nhan_tam, "Đắc nhân tâm", 350000, 0,""));
//        listItems.add(new ProductModel(R.drawable.nha_gia_kim, "Nhà giả kim", 150000, 0, ""));
//        listItems.add(new ProductModel(R.drawable.tu_duy_phan_bien, "Tư duy phản biện", 500000, 0, ""));


    }

    public void UpdateTotalCostAndProduct(Boolean isPlus, int money) {
        //TextView totalProduct = findViewById(R.id.totalNumberProductOfCart);
        //TextView totalCost = findViewById(R.id.totalCostProduct);

        //int totalProductValue = Integer.valueOf(totalProduct.getText().toString()).intValue(); //Lấy tổng số lượng của sản phẩm
        //totalProductValue = (isPlus) ? totalProductValue + 1 : totalProductValue - 1; // Thay đổi tùy vào isPlus
        //totalProduct.setText(String.valueOf(totalProductValue)); // Set lại giá trị khi đã tính toán xong

        //int totalCostValue = ProcessCurrency.convertStringToNumber(totalCost.getText().toString());// Lấy tổng chi phí của toàn bộ sản phẩm trong đơn hàng
        //totalCostValue = (isPlus) ? totalCostValue + money : totalCostValue - money; // Thay đổi tùy vào isPlus
        //totalCost.setText(ProcessCurrency.convertNumberToString(totalCostValue)); // Set lại giá trị khi đã tính toán xong

    }

    @Override
    public void onProductClick(int positionProduct) {

    }

    @Override
    public void onClick(View view) {
        //         public orderModel(String address, String buyer, String phoneNumber, String price, String shipCost) {
        //            this.address = address;
        //            this.buyer = buyer;
        //            this.phoneNumber = phoneNumber;
        //            this.price = price;
        //            this.shipCost = shipCost;
        //            this.productList = productList;
        //        }

        EditText address = findViewById(R.id.vitri);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        TextView price = findViewById(R.id.hao);

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("MY_ID", "");
        String buyer = userID;
        if (address.getText().toString().equals("") || phoneNumber.getText().toString().equals("")) {
            Toast.makeText(PaymentActivity.this, "Bạn cần phải điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            OrderModel orderModel = new OrderModel(
                    address.getText().toString(),
                    buyer,
                    phoneNumber.getText().toString(),
                    price.getText().toString(),
                    "50000");

            orderModel.addOrderToDatabase();
            for (int i = listItems.size() - 1; i >= 0; i--) {
                listItems.remove(i);
            }
            ArrayList<String> list = new ArrayList<>();
            DocumentReference user = db.collection("users").document(userID);
            user.update("cart", list)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                        Log.d(TAG, );
                            System.out.println("DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error updating document", e);
                        }
                    });

            Intent intent = new Intent(PaymentActivity.this, MainBuyerHomepageActivity.class);
            PaymentActivity.this.startActivity(intent);
        }
    }
}