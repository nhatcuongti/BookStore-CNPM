package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.adapters.ItemAdapter;
import com.example.bookstore.models.ProductModel;
import com.example.bookstore.utils.CustomLinearLayoutManager;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity implements ItemAdapter.AdapterUpdate, ItemAdapter.OnProductListener {

    ArrayList<ProductModel> listItems = null;
    RecyclerView rv;
    ItemAdapter ia;
    ImageButton backward = null;

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

}